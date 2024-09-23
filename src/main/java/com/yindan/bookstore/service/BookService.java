package com.yindan.bookstore.service;

import com.yindan.bookstore.dto.BookDto;
import com.yindan.bookstore.entity.BookEntity;

import java.util.List;
import java.util.Map;

public interface BookService {
    int addBook(BookEntity book);
    int updateBook(BookEntity book);
    int deleteBookById(Long id);
    BookEntity getBookById(Long id);
    List<BookDto> searchBooks(String title, String author, String category);
    int saveBooks(BookEntity bookEntity);
}
