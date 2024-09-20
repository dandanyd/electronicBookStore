package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.DamageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/damageRecord")
public class DamageRecordController {

    @Autowired
    private DamageRecordService damageRecordService;


}
