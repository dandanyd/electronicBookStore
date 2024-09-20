package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.ReportEntity;

public interface ReportDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReportEntity record);

    int insertSelective(ReportEntity record);

    ReportEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportEntity record);

    int updateByPrimaryKeyWithBLOBs(ReportEntity record);

    int updateByPrimaryKey(ReportEntity record);
}