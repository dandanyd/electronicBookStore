package com.yindan.bookstore.monitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SftpFileMonitor {

    private final ChannelSftp  channel;
    private final String remoteDirectory;
    private Set<String> knownFiles = new HashSet<>();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public SftpFileMonitor(ChannelSftp  channel, String remoteDirectory) {
        this.channel = channel;
        this.remoteDirectory = remoteDirectory;
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
                if (!knownFiles.contains(fileName)) {
                    knownFiles.add(fileName);
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
        //String localFilePath = "C:\\Users\\45905\\Desktop\\" + fileName;
        String localFilePath = "C:\\Users\\45905\\Desktop\\1q.xlsx" ;
        try {
            // 创建临时文件
            File tempFile = new File(localFilePath);
            tempFile.deleteOnExit(); // 确保在JVM退出时删除临时文件

            // 下载文件到临时文件
            channel.get(remoteFilePath, new FileOutputStream(tempFile));
            System.out.println("文件已成功下载至临时路径：" + tempFile.getAbsolutePath());

            // 解析文件
            parseFile(tempFile);

            // 删除临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
                System.out.println("临时文件已删除：" + tempFile.getAbsolutePath());
            }
        } catch (IOException | SftpException e) {
            e.printStackTrace();
        }
    }

    private void parseFile(File file) {
        // 这里是解析文件的逻辑
        // 示例：打印文件内容
       /* try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 打印每一行的内容
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file content.", e);
        }*/
        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // 假设第一行为标题行
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();
            String[] headers = new String[lastCellNum];

            for (int i = 0; i < lastCellNum; i++) {
                headers[i] = headerRow.getCell(i).getStringCellValue();
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String[] values = new String[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        values[j] = "";
                    } else {
                        values[j] = cell.getStringCellValue();
                    }
                }

                // 打印每一行的内容
                System.out.println("Row: " + i);
                for (int j = 0; j < headers.length; j++) {
                    System.out.println(headers[j] + ": " + values[j]);
                }
                System.out.println("------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file content.", e);
        }
    }

    public void stopMonitoring() {
        executor.shutdownNow();
    }

}
