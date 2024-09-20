package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrowingRecord")
public class BorrowingRecordController {

    @Autowired
    private BorrowingRecordService borrowingRecordService;



}
