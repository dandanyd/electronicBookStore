package com.yindan.bookstore.controller;

import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.constant.SftpConstant;
import com.yindan.bookstore.stfp.file.SftpUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ScheduledTask {

    @Autowired
    private SftpUploader sftpUploader;


    //每天晚上12定时执行
   // @Scheduled(cron = "0 0 0 * * ?")
   // @Scheduled(fixedDelay = 60000)
    public void runDailyReport() throws IOException, SftpException {
        //reportBorrowSaleService.reportDataAuto();

        sftpUploader.uploadExeclFile(SftpConstant.REMOTE_FILE_PATH,"simple");

    }

}
