package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.BorrowingRecordEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(BorrowingRecordEntity record);

    BorrowingRecordEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(BorrowingRecordEntity record);
}