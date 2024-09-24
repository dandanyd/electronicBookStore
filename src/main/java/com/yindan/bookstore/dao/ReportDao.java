package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.ReportEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ReportEntity record);

    int insertSelective(ReportEntity record);

    ReportEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReportEntity record);

    int updateByPrimaryKey(ReportEntity record);
}