package com.yindan.bookstore.controller;

import com.yindan.bookstore.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingRecordController {

    @Autowired
    private BorrowingService borrowingService;

    //借阅书籍
    @PostMapping("/borrowingBooks")
    public Map<String, Object> borrowingBooks(Long bookId, Long userId, String dueDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowingService.addBorrowing(bookId, userId,date);
    }

    //归还书籍
    @PostMapping("/returnBooks")
    public void returnBooks(Long bookId,Long userId,Integer damageLevel){
        borrowingService.updateBorrowing(bookId, userId, damageLevel);
    }



}
