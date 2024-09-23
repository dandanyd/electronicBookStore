package com.yindan.bookstore.dao;

import com.yindan.bookstore.dto.BookDto;
import com.yindan.bookstore.entity.BookEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao {
    int deleteByPrimaryKey(Long id);

    int insert(BookEntity record);

    BookEntity selectByPrimaryKey(Long id);

    BookEntity selectByIsbn(String isbn);

    int updateByPrimaryKey(BookEntity record);

    List<BookDto> selectByCondition(@Param("title") String title, @Param("author") String author, @Param("category") String category);
}