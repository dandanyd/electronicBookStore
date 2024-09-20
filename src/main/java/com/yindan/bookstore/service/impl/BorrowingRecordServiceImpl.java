package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.dao.BorrowingRecordDao;
import com.yindan.bookstore.entity.BorrowingRecordEntity;
import com.yindan.bookstore.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService{

    @Autowired
    private BorrowingRecordDao borrowingRecordDao;

    @Override
    public int addBorrowingRecord(BorrowingRecordEntity borrowingRecordEntity) {
        return borrowingRecordDao.insert(borrowingRecordEntity);
    }

    @Override
    public int updateBorrowingRecord(BorrowingRecordEntity borrowingRecordEntity) {
        return borrowingRecordDao.updateByPrimaryKey(borrowingRecordEntity);
    }

    @Override
    public int deleteBorrowingRecordById(Long id) {
        return borrowingRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public BorrowingRecordEntity getBorrowingRecordById(Long id) {
        return borrowingRecordDao.selectByPrimaryKey(id);
    }
}
