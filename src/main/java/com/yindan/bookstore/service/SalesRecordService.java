package com.yindan.bookstore.service;

import java.math.BigDecimal;

public interface SalesRecordService {

    int salesBook(String isbn, Long userId, BigDecimal price, int quantity, int discount);

}
