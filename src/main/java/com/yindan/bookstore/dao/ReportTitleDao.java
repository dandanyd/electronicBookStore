package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.ReportTitleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTitleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(String excelname);

    int insertSelective(ReportTitleEntity record);

    ReportTitleEntity selectByPrimaryKey(Integer id);

    ReportTitleEntity selectByExcelName(String excelName);

    List<ReportTitleEntity> selectAll();

    int updateByPrimaryKeySelective(ReportTitleEntity record);

    int updateByPrimaryKey(ReportTitleEntity record);
}