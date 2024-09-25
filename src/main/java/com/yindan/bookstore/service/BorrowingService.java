package com.yindan.bookstore.service;

import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.entity.BorrowingEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BorrowingService {

    /**
     * 借阅
     * @param bookId
     * @param userId
     * @param dueDate
     * @return
     */
    Map<String, Object> addBorrowing(Long bookId, Long userId, Date dueDate);

    /**
     * 归还
     * @param bookId
     * @param userId
     * @return
     */
    int updateBorrowing(Long bookId,Long userId,Integer damageLevel);

    /**
     * 删除借阅记录
     * @param id
     * @return
     */
    int deleteBorrowingById(Long id);

    /**
     * 根据借阅记录ID获取借阅数据
     * @param id
     * @return
     */
    BorrowingEntity getBorrowingById(Long id);


    List<ReportDto> reports();

    List<ReportDetailsDto> reportDetails();



}
