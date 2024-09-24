package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.ReportDetailsEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDetailsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ReportDetailsEntity record);

    int insertSelective(ReportDetailsEntity record);

    ReportDetailsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReportDetailsEntity record);

    int updateByPrimaryKey(ReportDetailsEntity record);
}