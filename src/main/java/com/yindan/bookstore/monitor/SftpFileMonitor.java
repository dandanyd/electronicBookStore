package com.yindan.bookstore.monitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.dao.LastCheckTimestampDao;
import com.yindan.bookstore.dao.ReportTitleDao;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.entity.ReportDetailsEntity;
import com.yindan.bookstore.entity.ReportEntity;
import com.yindan.bookstore.entity.ReportTitleEntity;
import com.yindan.bookstore.service.ReportBorrowSaleService;
import com.yindan.bookstore.stfp.manager.SftpConnectionManager;
import com.yindan.bookstore.utils.ExcelReaderUtils;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SftpFileMonitor {

    private final ChannelSftp  channel;
    private final String remoteDirectory;
    //private Set<String> knownFiles = new HashSet<>();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ReportBorrowSaleService reportBorrowSaleService;

    @Autowired
    private ReportTitleDao reportTitleDao;

    @Autowired
    private LastCheckTimestampDao lastCheckTimestampDao;

//    public SftpFileMonitor(ChannelSftp  channel, String remoteDirectory) {
//        this.channel = channel;
//        this.remoteDirectory = remoteDirectory;
//    }
    public SftpFileMonitor() throws JSchException{
        this.channel = SftpConnectionManager.getInstance().getSftpChannel();
        this.remoteDirectory = "/data/sftp/uftp01/upload/";
    }

    // 每2分钟检查一次
    public void startMonitoring() {
        executor.scheduleAtFixedRate(this::checkForNewFiles, 0, 2 * 60, TimeUnit.SECONDS);
    }

    private void checkForNewFiles() {
        try {
            List<ChannelSftp.LsEntry> files = channel.ls(remoteDirectory);
            List<ChannelSftp.LsEntry> fileList = new ArrayList<>();
            for (ChannelSftp.LsEntry file : files) {
                String fileName = file.getFilename();
                // 排除`.`和`..`条目以及目录等非文件类型
                if (".".equals(fileName) || "..".equals(fileName) || file.getAttrs().isDir()) {
                    continue;
                }
                fileList.add(file);
            }
            Long lastestTime = lastCheckTimestampDao.getLastestTime();
            // 使用stream API和filter来过滤出文件，并通过max方法找到最新的那个
            Optional<ChannelSftp.LsEntry> max = fileList.stream()
                    .max(Comparator.comparingLong(entry -> entry.getAttrs().getMTime()));
            //String fileName = max.map(ChannelSftp.LsEntry::getFilename);
            String fileName = max.map(ChannelSftp.LsEntry::getFilename).orElse(null);
            //  Optional<String> fileTime = max.map(lsEntry -> lsEntry.getAttrs().getMtimeString());

            Long fileTime = max.map(lsEntry -> {
                long mtimeSeconds = lsEntry.getAttrs().getMTime(); // 获取以秒为单位的时间戳
                return mtimeSeconds * 1000; // 转换为毫秒
            }).orElse(null);

            if (lastestTime == null){
                lastCheckTimestampDao.insert(fileTime);
                handleNewFile(fileName);
            }else {
                if (lastestTime < fileTime){
                    lastCheckTimestampDao.updateTime(fileTime);
                    handleNewFile(fileName);
                }
            }


//            Optional<String> s = fileList.stream()
//                    .max(Comparator.comparingLong(entry -> entry.getAttrs().getMTime()))
//                    .map(ChannelSftp.LsEntry::getFilename);// 提取文件名
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    /*private void checkForNewFiles() {
        try {
            List<ChannelSftp.LsEntry> files = channel.ls(remoteDirectory);
            for (ChannelSftp.LsEntry file : files) {
                String fileName = file.getFilename();
                // 排除`.`和`..`条目以及目录等非文件类型
                if (".".equals(fileName) || "..".equals(fileName) || file.getAttrs().isDir()) {
                    continue;
                }
                List<ReportTitleEntity> reportTitleEntities = reportTitleDao.selectAll();
                List<String> excelNames = reportTitleEntities.stream()
                        .map(ReportTitleEntity::getExcelname)
                        .collect(Collectors.toList());
                if (!excelNames.contains(fileName)) {
                    excelNames.add(fileName);
                    System.out.println("新文件：" + fileName);
                    // 在这里可以调用其他方法处理新文件
                    handleNewFile(fileName);
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }*/

    private void handleNewFile(String fileName) {
        // 在这里处理新文件
        // 例如：下载文件、解析文件等
        System.out.println("处理新文件：" + fileName);
        // 下载文件并解析
        downloadAndParseFile(fileName);
    }

    private void downloadAndParseFile(String fileName) {
        String remoteFilePath = remoteDirectory + "/" + fileName;

        try {
            // 创建临时文件
            File tempFile = File.createTempFile("tempExcel", ".xlsx");
            tempFile.deleteOnExit(); // 确保在JVM退出时删除临时文件

            // 下载文件到临时文件
            channel.get(remoteFilePath, new FileOutputStream(tempFile));
            System.out.println("文件已成功下载至临时路径：" + tempFile.getAbsolutePath());

            // 解析文件
            parseFile(tempFile,fileName);

            // 删除临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
                System.out.println("临时文件已删除：" + tempFile.getAbsolutePath());
            }
        } catch (IOException | SftpException e) {
            e.printStackTrace();
        }
    }

    private void parseFile(File file,String fileName) throws IOException {

        ExcelReaderUtils readerService = new ExcelReaderUtils();
        Pair<List<ReportEntity>, List<ReportDetailsEntity>> result = readerService.readExcel(file.getAbsolutePath());
        //reportTitleDao.insert(fileName);
        reportBorrowSaleService.addReport(result.getKey(),result.getValue());
        System.out.println("操作完成");

    }

    public void stopMonitoring() {
        executor.shutdownNow();
    }

}
