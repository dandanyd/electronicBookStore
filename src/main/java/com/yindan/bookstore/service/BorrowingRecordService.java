package com.yindan.bookstore.service;

import com.yindan.bookstore.entity.BorrowingRecordEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BorrowingRecordService {

    int addBorrowingRecord(BorrowingRecordEntity book);
    int updateBorrowingRecord(BorrowingRecordEntity book);
    int deleteBorrowingRecordById(Long id);
    BorrowingRecordEntity getBorrowingRecordById(Long id);

}
