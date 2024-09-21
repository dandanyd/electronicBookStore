package com.yindan.bookstore.test;

import com.yindan.bookstore.service.SalesRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
public class SalesRecordServiceTest {

    @Autowired
    private SalesRecordService salesRecordService;

    //销售书籍
    @Test
    public void testBorrowingInsert() {
        Long bookId = Long.valueOf(3);
        Long userId = Long.valueOf(1);
        BigDecimal price = new BigDecimal("10.99");
        int quantity = 1;
        int discount = 9;

        salesRecordService.salesBook(bookId,userId,price,quantity,discount);
    }

    @Test
    public void testCount(){
        BigDecimal price = new BigDecimal("1.99");
        int quantity = 1;
        int discount = 3;
        BigDecimal b = new BigDecimal(discount);
        BigDecimal totalPrice =  price.multiply(new BigDecimal(quantity)).multiply(b);
        BigDecimal divide = totalPrice.divide(BigDecimal.valueOf(10), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("总价："+divide);
    }


}
