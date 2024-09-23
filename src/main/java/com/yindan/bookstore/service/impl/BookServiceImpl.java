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
        //根据isbn查询该书是否已经存在
        BookEntity bookEntity = bookDao.selectByIsbn(book.getIsbn());
        if (null != bookEntity){
            //存在该书籍，修改新书库存
            bookEntity.setStock(book.getStock() + bookEntity.getStock());
            bookDao.updateByPrimaryKey(bookEntity);
        }else {
            //不存在，新增书籍
            bookDao.insert(book);
        }
        logger.info("添加书籍其中isbn:"+book.getIsbn());
        return 1;
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
    public List<BookDto> searchBooks(String title, String author, String category) {
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
