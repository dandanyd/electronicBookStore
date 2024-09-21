package com.yindan.bookstore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDetailsDto {

        private String type;  // 借阅或销售
        private Long bookId;  // 书籍ID
        private String title;  // 书名
        private String author;  // 作者
        private String isbn;  // ISBN号
        private Date borrowSaleDate;  // 借阅/销售日期
        private Date dueDate;  // 应还日期
        private String username;  // 用户名
        private String phoneNumber;  // 电话号码
        private Integer diffDate;
	    private Integer dateFlag;



}
