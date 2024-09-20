package com.yindan.bookstore.controller;

import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public int addBook(@RequestBody BookEntity book) {
        return bookService.addBook(book);
    }

    @RequestMapping("/updateBook")
    public int updateBook(@RequestBody BookEntity book) {
        return bookService.updateBook(book);
    }

    @RequestMapping("/deleteBook")
    public int deleteBook(Long id) {
        return bookService.deleteBookById(id);
    }

    @RequestMapping("/getBook")
    public BookEntity getBook(Long id) {
        return bookService.getBookById(id);
    }

    @RequestMapping("/queryBooks")
    public List<BookEntity> getAllBooks(@RequestParam(name = "title",defaultValue = "") String title,
                                        @RequestParam(name = "author",defaultValue = "") String author,
                                        @RequestParam(name = "category",defaultValue = "") String category) {
        return bookService.searchBooks(title, author, category);
    }

}
