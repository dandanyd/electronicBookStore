package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.ReportBorrowSaleService;
import com.yindan.bookstore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTask {

    @Autowired
    ReportBorrowSaleService reportBorrowSaleService;

    @Scheduled(cron = "0 0 0 * * ?")
   // @Scheduled(fixedDelay = 60000)
    public void runDailyReport() throws IOException {
        System.out.println("zhixing");
        // 例如，生成并发送报告
        //TODO 替换成调用工厂实现生产excel上传
        reportBorrowSaleService.reportDataAuto();
    }

}
