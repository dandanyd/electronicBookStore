package com.yindan.bookstore.dto;

import lombok.Data;

@Data
public class ReportDto {
    private String type;  // 借阅或销售
    private String category;  // 书籍类别
    private int quantity;  // 数量

}
