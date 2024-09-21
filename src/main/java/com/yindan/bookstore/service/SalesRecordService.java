package com.yindan.bookstore.service;

import java.math.BigDecimal;

public interface SalesRecordService {

    int salesBook(Long bookId, Long userId, BigDecimal price, int quantity, int discount);

}
