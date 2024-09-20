package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.SalesRecordEntity;

public interface SalesRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(SalesRecordEntity record);

    SalesRecordEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(SalesRecordEntity record);
}