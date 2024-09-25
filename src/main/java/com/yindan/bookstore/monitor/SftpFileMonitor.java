package com.yindan.bookstore.monitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private ReportBorrowSaleService reportBorrowSaleService;

    @Autowired
    private ReportTitleDao reportTitleDao;

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
    }

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
        reportTitleDao.insert(fileName);
        reportBorrowSaleService.addReport(result.getKey(),result.getValue());
        System.out.println("操作完成");

    }

    public void stopMonitoring() {
        executor.shutdownNow();
    }

}
