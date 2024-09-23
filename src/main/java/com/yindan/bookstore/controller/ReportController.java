package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.ReportBorrowSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportBorrowSaleService reportBorrowSaleService;

    @RequestMapping("/reportData")
    public void reportData(HttpServletResponse response){
        reportBorrowSaleService.reportData(response);
    }

}
