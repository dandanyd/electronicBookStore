package com.yindan.bookstore.entity;

public class LastCheckTimestampEntity {
    private Integer id;

    private Long enterTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Long enterTime) {
        this.enterTime = enterTime;
    }
}