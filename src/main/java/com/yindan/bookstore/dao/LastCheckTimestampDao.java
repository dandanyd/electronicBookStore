package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.LastCheckTimestampEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LastCheckTimestampDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Long enterTime);

    int insertSelective(LastCheckTimestampEntity record);

    LastCheckTimestampEntity selectByPrimaryKey(Integer id);

    Long getLastestTime();

    int updateTime(Long enterTime);

    int updateByPrimaryKey(LastCheckTimestampEntity record);
}