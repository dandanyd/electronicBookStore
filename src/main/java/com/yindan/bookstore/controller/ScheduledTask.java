package com.yindan.bookstore.controller;

import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.constant.SftpConstant;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.service.BorrowingService;
import com.yindan.bookstore.service.ReportBorrowSaleService;
import com.yindan.bookstore.stfp.file.SftpUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private SftpUploader sftpUploader;

    @Autowired
    private BorrowingService borrowingService;


    //每天晚上12定时执行
   // @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(fixedDelay = 60000)
    public void runDailyReport() throws IOException, SftpException {
        //reportBorrowSaleService.reportDataAuto();


        List<ReportDto> reports = borrowingService.reports();
        List<ReportDetailsDto> reportDetailsDtos = borrowingService.reportDetails();

        sftpUploader.uploadExeclFile(SftpConstant.REMOTE_FILE_PATH,"simple",reports,reportDetailsDtos);

    }

}
