package com.yindan.bookstore.test;

import com.yindan.bookstore.entity.BorrowingEntity;
import com.yindan.bookstore.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class BorrowingServiceTest {

    @Autowired
    private BorrowingService borrowingService;

    //借阅书籍
    @Test
    public void testBorrowingInsert() {

        Long bookId = Long.valueOf(4);
        Long userId = Long.valueOf(2);
        Date dueDate = java.sql.Date.valueOf("2024-10-02");

        borrowingService.addBorrowing(bookId,userId,dueDate);
    }

    //归还书籍
    @Test
    public void testBorrowingUpdate(){
        // 更新记录
        Long bookId = Long.valueOf(3);
        Long userId = Long.valueOf(1);
        Integer damageLevel = 9;
        borrowingService.updateBorrowing(bookId,userId,damageLevel);
    }



}
