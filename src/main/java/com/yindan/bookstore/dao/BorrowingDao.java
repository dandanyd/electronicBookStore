package com.yindan.bookstore.dao;

import com.yindan.bookstore.entity.BorrowingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingDao {
    int deleteByPrimaryKey(Long id);

    int insert(BorrowingEntity record);

    BorrowingEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKey(BorrowingEntity record);

    int counts();

    BorrowingEntity selectByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    int selUserBorrowing(@Param("bookId") Long bookId, @Param("userId") Long userId, @Param("status") String status);

    List<BorrowingEntity> selBorrowingBooks(@Param("bookId") Long bookId, @Param("status") String status);


}