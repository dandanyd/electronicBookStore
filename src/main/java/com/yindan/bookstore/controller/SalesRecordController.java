package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/salesRecord")
public class SalesRecordController {

    @Autowired
    private SalesRecordService salesRecordService;

    @PostMapping("/create")
    public String createOrder(String bookId,String title,BigDecimal price){
        String paymentUrl = "html/order.html?bookId="+bookId+"&title="+title+"&price="+price+"&flag=1";
        return paymentUrl;
    }

    @PostMapping("/salesBook")
    public int salesBook(String isbn, Long userId, BigDecimal price, int quantity, int discount){
        return salesRecordService.salesBook(isbn, userId, price, quantity, discount);
    }

}
