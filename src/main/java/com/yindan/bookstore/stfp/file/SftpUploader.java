package com.yindan.bookstore.stfp.file;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.strategy.Strategy;
import com.yindan.bookstore.stfp.factory.UploadAndDownloadStrategyFactory;
import com.yindan.bookstore.stfp.manager.SftpConnectionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Component
public class SftpUploader {


    //上传sftp
    public void uploadFile(String localFilePath, String remoteFilePath, String strategyType) throws JSchException {
        File file = new File(localFilePath);
        if (!file.exists()) {
            System.err.println("File not found: " + localFilePath);
            return;
        }
        //sftp管理
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        ChannelSftp channel = manager.getSftpChannel();

        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);

        try {
            strategy.execute(channel, file, remoteFilePath);
            //strategy.upload(channel, file, remoteFilePath);
            System.out.println("File uploaded successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void uploadExeclFile(String remoteFilePath, String strategyType) throws JSchException, IOException {
        //临时文件
        File tempFile = File.createTempFile("tempExcel", ".xlsx");
        String filePath = tempFile.getAbsolutePath();

        //sftp管理
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        ChannelSftp channel = manager.getSftpChannel();

        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.excelSftpexecute(channel,filePath,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }

        tempFile.deleteOnExit(); // 文件将在JVM退出时删除
    }

    //下载并解析文件
    public void downloadFile(String remoteFilePath, String localFilePath,String strategyType) throws JSchException, IOException, SftpException {

        //sftp管理
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        ChannelSftp channel = manager.getSftpChannel();

        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.execute(channel,localFilePath,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    //下载并解析文件
    public void downloadAndParseFile(String remoteFilePath, String localFilePath,String strategyType) throws JSchException, IOException, SftpException {

        //sftp管理
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        ChannelSftp channel = manager.getSftpChannel();

        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.execute(channel,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }


    public void downloadExcelAndParseFile(String remoteFilePath, String strategyType) throws JSchException {
        //sftp管理
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        ChannelSftp channel = manager.getSftpChannel();
        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.excelSftpexecute(channel,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException | IOException e) {
            e.printStackTrace();
        }
    }
}
