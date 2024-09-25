package com.yindan.bookstore.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.file.SftpUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sftp")
public class SftpController {

    //private final SftpUploader sftpUploader = new SftpUploader();

    @Autowired
    private  SftpUploader sftpUploader;


    /**
     *
     * @param localFilePath 本地文件地址
     * @param remoteFilePath 服务器文件地址
     * @param strategyType 上传策略
     * @return
     * @throws JSchException
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("localFilePath") String localFilePath,
                             @RequestParam("remoteFilePath") String remoteFilePath,
                             @RequestParam(value = "strategyType", defaultValue = "simple") String strategyType) throws SftpException, JSchException {
        localFilePath = "/Users/yindandan/Desktop/renren/daily_report.xlsx";
        remoteFilePath = "/data/sftp/uftp01/upload/";
        strategyType = "simple";
        sftpUploader.uploadFile(localFilePath, remoteFilePath, strategyType);
        return "File upload request processed.";
    }
}
