package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/salesRecord")
public class SalesRecordController {

    @Autowired
    private SalesRecordService salesRecordService;

}
