package com.yindan.bookstore.test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.file.SftpUploader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@SpringBootTest
public class SftpUploaderTest {

    @Autowired
    private SftpUploader sftpUploader;

    //上传本地文件至sftp
    @Test
    public void uploadFile() throws JSchException, SftpException {
        String localFilePath = "/Users/yindandan/Desktop/renren/myfile.txt";
        String remoteFilePath = "/data/sftp/uftp01/upload/";
        String strategyType = "simple";
        sftpUploader.uploadFile(localFilePath,remoteFilePath,strategyType);
        System.out.println("上传成功");
    }

    //生成excel并上传sftp
    @Test
    public void uploadExeclFile() throws IOException, JSchException, SftpException {
        String remoteFilePath = "/data/sftp/uftp01/upload/";
        String strategyType = "simple";
        sftpUploader.uploadExeclFile(remoteFilePath,strategyType);
        System.out.println("生成excel上传成功");
    }

    //下载文件
    @Test
    public void downloadFile() throws JSchException, SftpException, IOException {
        String localFilePath = "/Users/yindandan/Desktop/renren/myfile.txt";
        String remoteFilePath = "/data/sftp/uftp01/upload/111.txt";
        String strategyType = "download";
        sftpUploader.downloadFile(remoteFilePath,localFilePath,strategyType);
        System.out.println("下载成功");
    }


    //下载文件并解析
    @Test
    public void downloadAndParseFile() throws JSchException, SftpException, IOException {
        String remoteFilePath = "/data/sftp/uftp01/upload/myfile.txt";
        String strategyType = "download";
        sftpUploader.downloadAndParseFile(remoteFilePath,strategyType);
        System.out.println("解析打印成功");
    }

    //下载Excel文件并解析
    @Test
    public void downloadExcelAndParseFile() throws JSchException, SftpException, IOException {
        String remoteFilePath = "/data/sftp/uftp01/upload/tempExcel3697729047839621505.xlsx";
        String strategyType = "download";
        sftpUploader.downloadExcelAndParseFile(remoteFilePath,strategyType);
        System.out.println("解析打印成功");
    }


}