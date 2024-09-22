package com.yindan.bookstore.stfp.strategy.strategyImpl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.strategy.Strategy;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class SimpleUploadStrategy implements Strategy<Void> {

  /*  @Override
    public void upload(ChannelSftp channel, File file, String remotePath) throws IOException, SftpException {
        // 尝试打开文件输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // 使用 SFTP 通道上传文件
            channel.put(fis, remotePath + "/" + file.getName());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getPath());
            e.printStackTrace();
        } catch (SftpException e) {
            System.err.println("Error uploading file to SFTP server.");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream.");
                    e.printStackTrace();
                }
            }
        }
    }
*/
    @Override
    public void execute(ChannelSftp channel, File file, String remoteFilePath) throws SftpException {
        // 尝试打开文件输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // 使用 SFTP 通道上传文件
            channel.put(fis, remoteFilePath + "/" + file.getName());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getPath());
            e.printStackTrace();
        } catch (SftpException e) {
            System.err.println("Error uploading file to SFTP server.");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream.");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException, IOException {
        generateExcelFile(localFilePath);
        try {
            channel.put(localFilePath, remoteFilePath);
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String remoteFilePath) throws SftpException, IOException {

    }

    @Override
    public void execute(ChannelSftp channel, String remoteFilePath) throws SftpException {

    }

    @Override
    public void execute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException {

    }

    //生成excel
    public void generateExcelFile(String filePath) throws IOException {
        // 创建一个新的Excel工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建一个新的工作表
        Sheet sheet = workbook.createSheet("Books");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Title");
        headerRow.createCell(2).setCellValue("Author");

        // 创建数据行
        String[][] bookData = {
                { "1", "Harry Potter and the Sorcerer's Stone", "J.K. Rowling" },
                { "2", "The Hobbit", "J.R.R. Tolkien" }
        };

        for (int i = 0; i < bookData.length; i++) {
            Row dataRow = sheet.createRow(i + 1); // 加1跳过标题行
            for (int j = 0; j < bookData[i].length; j++) {
                dataRow.createCell(j).setCellValue(bookData[i][j]);
            }
        }

        // 写入Excel文件
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭工作簿
        workbook.close();
    }
}
