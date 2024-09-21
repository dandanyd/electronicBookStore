package com.yindan.bookstore.dto;

import com.yindan.bookstore.entity.BookEntity;

public class BookDto extends BookEntity {

    private Integer stockBorrowing;
    private Integer stockSum;

    public Integer getStockBorrowing() {
        return stockBorrowing;
    }

    public void setStockBorrowing(Integer stockBorrowing) {
        this.stockBorrowing = stockBorrowing;
    }

    public Integer getStockSum() {
        return stockSum;
    }

    public void setStockSum(Integer stockSum) {
        this.stockSum = stockSum;
    }
}
