package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.dao.BookDao;
import com.yindan.bookstore.dto.BookDto;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    private static final Logger logger = LoggerFactory.getLogger("FILE");

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
    public List<BookDto> selectAllBooks() {
        return bookDao.selectAllBooks();
    }

    @Override
    public List<BookEntity> searchBooks(String title, String author, String category) {
        return bookDao.selectByCondition(title, author, category);
    }

    @Override
    public int saveBooks(BookEntity bookEntity) {
        //录入书籍时，检查书籍表中是否含有该书籍
        BookEntity entity = bookDao.selectByIsbn(bookEntity.getIsbn());
        if (entity != null){
            //书籍表中存在该书籍,则直接添加库存即可
            entity.setStock(bookEntity.getStock() + entity.getStock());
            entity.setUpdatedAt(new Date());
            return bookDao.updateByPrimaryKey(entity);
        }else {
            //书籍表中未有过的新书
            bookEntity.setCreatedAt(new Date());
            bookEntity.setUpdatedAt(new Date());
            return bookDao.insert(bookEntity);
        }
    }


//    @Override
//    public List<BookEntity> searchBooks(Map<String, Object> params) {
//        return bookDao.searchBooks(params);
//    }


}
