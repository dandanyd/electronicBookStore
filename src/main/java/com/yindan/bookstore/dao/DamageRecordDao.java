package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.DamageRecordEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(DamageRecordEntity record);

    DamageRecordEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(DamageRecordEntity record);
}