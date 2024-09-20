package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.dao.BookDao;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public int addBook(BookEntity book) {
        return bookDao.insert(book);
    }

    @Override
    public int updateBook(BookEntity book) {
        return bookDao.updateByPrimaryKey(book);
    }

    @Override
    public int deleteBookById(Long id) {
        return bookDao.deleteByPrimaryKey(id);
    }

    @Override
    public BookEntity getBookById(Long id) {
        return bookDao.selectByPrimaryKey(id);
    }

    @Override
    public List<BookEntity> searchBooks(String title, String author, String category) {
        return bookDao.selectByCondition(title, author, category);
    }


//    @Override
//    public List<BookEntity> searchBooks(Map<String, Object> params) {
//        return bookDao.searchBooks(params);
//    }


}
