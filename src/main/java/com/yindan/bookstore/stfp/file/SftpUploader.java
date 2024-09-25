package com.yindan.bookstore.stfp.file;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.stfp.strategy.Strategy;
import com.yindan.bookstore.stfp.factory.UploadAndDownloadStrategyFactory;
import com.yindan.bookstore.stfp.manager.SftpConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class SftpUploader {

    private final SftpConnectionManager sftpManager;
    private final UploadAndDownloadStrategyFactory strategyFactory;

    //构造器注入
    @Autowired
    public SftpUploader(SftpConnectionManager sftpManager, UploadAndDownloadStrategyFactory strategyFactory) {
        this.sftpManager = sftpManager;
        this.strategyFactory = strategyFactory;
    }

    //上传sftp
    public void uploadFile(String localFilePath, String remoteFilePath, String strategyType) throws SftpException, JSchException {
        File file = new File(localFilePath);
        if (!file.exists()) {
            System.err.println("File not found: " + localFilePath);
            return;
        }
        //sftp管理
//        SftpConnectionManager manager = SftpConnectionManager.getInstance();
//        ChannelSftp channel = manager.getSftpChannel();
//
//        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);

        ChannelSftp channel = sftpManager.getSftpChannel();
        Strategy strategy = strategyFactory.createUploadStrategy(strategyType);

        try {
            strategy.execute(channel, file, remoteFilePath);
            //strategy.upload(channel, file, remoteFilePath);
            System.out.println("File uploaded successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }finally {
            sftpManager.close();
        }
    }

    public void uploadExeclFile(String remoteFilePath, String strategyType, List<ReportDto> reports , List<ReportDetailsDto> reportDetails) throws IOException, SftpException, JSchException {
        //临时文件
        File tempFile = File.createTempFile("tempExcel", ".xlsx");
        String filePath = tempFile.getAbsolutePath();

        //sftp管理
//        SftpConnectionManager manager = SftpConnectionManager.getInstance();
//        ChannelSftp channel = manager.getSftpChannel();
//
//        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        ChannelSftp channel = sftpManager.getSftpChannel();
        Strategy strategy = strategyFactory.createUploadStrategy(strategyType);

        try {
            strategy.excelSftpexecute(channel,filePath,remoteFilePath,reports,reportDetails);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }finally {
            sftpManager.close();
            tempFile.deleteOnExit();
        }

    }


    //下载并解析文件
    public void downloadFile(String remoteFilePath, String localFilePath,String strategyType) throws SftpException, JSchException {

        //sftp管理
//        SftpConnectionManager manager = SftpConnectionManager.getInstance();
//        ChannelSftp channel = manager.getSftpChannel();
//
//        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);

        ChannelSftp channel = sftpManager.getSftpChannel();
        Strategy strategy = strategyFactory.createUploadStrategy(strategyType);

        try {
            strategy.execute(channel,localFilePath,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }finally {
            sftpManager.close();
        }
    }

    //下载并解析文件
    public void downloadAndParseFile(String remoteFilePath, String strategyType) throws JSchException, IOException, SftpException {

        //sftp管理
//        SftpConnectionManager manager = SftpConnectionManager.getInstance();
//        ChannelSftp channel = manager.getSftpChannel();
//
//        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);

        ChannelSftp channel = sftpManager.getSftpChannel();
        Strategy strategy = strategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.execute(channel,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException e) {
            e.printStackTrace();
        }finally {
            sftpManager.close();
        }
    }


    public void downloadExcelAndParseFile(String remoteFilePath, String strategyType) throws SftpException, JSchException {
        //sftp管理
//        SftpConnectionManager manager = SftpConnectionManager.getInstance();
//        ChannelSftp channel = manager.getSftpChannel();
//        Strategy strategy =  UploadAndDownloadStrategyFactory.createUploadStrategy(strategyType);
        ChannelSftp channel = sftpManager.getSftpChannel();
        Strategy strategy = strategyFactory.createUploadStrategy(strategyType);
        try {
            strategy.excelSftpexecute(channel,remoteFilePath);
            //downloadStrategy.downloadAndParse(channel, localFilePath, remoteFilePath);
            System.out.println("File download successfully.");
        } catch (SftpException | IOException e) {
            e.printStackTrace();
        }finally {
            sftpManager.close();
        }
    }
}
