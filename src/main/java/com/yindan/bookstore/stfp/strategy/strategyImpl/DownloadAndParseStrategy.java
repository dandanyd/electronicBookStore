package com.yindan.bookstore.stfp.strategy.strategyImpl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.strategy.Strategy;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;

public class DownloadAndParseStrategy implements Strategy {

    private void parseFileContent(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 打印每一行的内容
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file content.", e);
        }
    }

    private void parseExcelFileContent(File tempFile){
        try (FileInputStream fis = new FileInputStream(tempFile)) {
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

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // 匹配可选负号开头的整数或小数
    }

    @Override
    public void execute(ChannelSftp channel, File localFilePath, String remoteFilePath) throws SftpException {

    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException, IOException {

    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String remoteFilePath) throws SftpException, IOException {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp-", ".tmp");
            tempFile.deleteOnExit(); // 确保在JVM退出时删除临时文件
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create temporary file", e);
        }
        // 下载文件到临时文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            channel.get(remoteFilePath, fos);
            System.out.println("文件已成功下载至临时路径：" + tempFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find temporary file", e);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file from SFTP: " + remoteFilePath, e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception occurred", e);
        }
        parseExcelFileContent(tempFile);

        // 删除临时文件
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
            System.out.println("临时文件已删除：" + tempFile.getAbsolutePath());
        }
    }

    @Override
    public void execute(ChannelSftp channel, String remoteFilePath) throws SftpException {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp-", ".tmp");
            tempFile.deleteOnExit(); // 确保在JVM退出时删除临时文件
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create temporary file", e);
        }
        // 下载文件到临时文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            channel.get(remoteFilePath, fos);
            System.out.println("文件已成功下载至临时路径：" + tempFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find temporary file", e);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file from SFTP: " + remoteFilePath, e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception occurred", e);
        }

        parseFileContent(tempFile);

        // 删除临时文件
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
            System.out.println("临时文件已删除：" + tempFile.getAbsolutePath());
        }
    }

    @Override
    public void execute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException {
        try {
            File localFile = new File(localFilePath);
            channel.get(remoteFilePath, new FileOutputStream(localFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find local file: " + localFilePath, e);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file from SFTP: " + remoteFilePath, e);
        } finally {
            // 断开连接
            // 注意：如果连接管理器负责多个连接，这里可能不需要每次都断开连接
            // manager.closeConnection();
        }
    }


}
