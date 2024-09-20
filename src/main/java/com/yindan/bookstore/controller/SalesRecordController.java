package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salesRecord")
public class SalesRecordController {

    @Autowired
    private SalesRecordService salesRecordService;

}
