package com.yindan.bookstore.service;

import com.yindan.bookstore.entity.BookEntity;

import java.util.List;

public interface BookService {
    int addBook(BookEntity book);
    int updateBook(BookEntity book);
    int deleteBookById(Long id);
    BookEntity getBookById(Long id);
    List<BookEntity> searchBooks(String title, String author, String category);
}
