package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.BookEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao {
    int deleteByPrimaryKey(Long id);

    int insert(BookEntity record);

    BookEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(BookEntity record);

    List<BookEntity> selectByCondition(@Param("title") String title, @Param("author") String author, @Param("category") String category);
}