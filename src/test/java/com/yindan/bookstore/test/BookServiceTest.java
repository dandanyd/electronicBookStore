package com.yindan.bookstore.test;

import com.yindan.bookstore.dto.BookDto;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
public class BookServiceTest {

//    @Autowired
//    private BookService bookService;
//
//    //录入书籍
//    @Test
//    public void testBookOperationsInsert() {
//         //插入一条记录
//        BookEntity newBook = new BookEntity();
//        newBook.setTitle("是多福多寿");
//        newBook.setAuthor("钩子");
//        newBook.setIsbn("5780061947233");
//        newBook.setCategory("科学");
//        newBook.setPrice(new BigDecimal("15.99"));
//        newBook.setStock(10);
//        newBook.setCoverImage("1https://example.com/to-kill-a-mockingbird.jpg");
//
//        bookService.saveBooks(newBook);
//    }
//
//    @Test
//    public void testBookOperationsUpdate(){
//        // 更新记录
//        BookEntity newBook = new BookEntity();
//        newBook.setId(Long.valueOf(1));
//        newBook.setTitle("To Kill a Mockingbird - Updated");
//    }
//    @Test
//    public void testBookOperationsQueryAll(){
//        // 查询所有记录
//        List<BookDto> books = bookService.searchBooks(null,null,null);
//        System.out.println(books);
//    }
//
//    @Test
//    public void testBookOperationsQueryOne(){
//        // 查询所有单条记录
//        BookEntity fetchedBook = bookService.getBookById(Long.valueOf(1));
//        System.out.println(fetchedBook);
//    }
//    @Test
//    public void testBookOperationsDelById(){
//        // 删除记录
//        bookService.deleteBookById(Long.valueOf(1));
//    }
}
