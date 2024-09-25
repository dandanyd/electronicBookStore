package com.yindan.bookstore.entity;

public class ReportTitleEntity {
    private Integer id;

    private String excelname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExcelname() {
        return excelname;
    }

    public void setExcelname(String excelname) {
        this.excelname = excelname == null ? null : excelname.trim();
    }
}