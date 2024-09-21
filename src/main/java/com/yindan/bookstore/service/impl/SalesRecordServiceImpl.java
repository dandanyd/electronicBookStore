package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.constant.BorrowingStatus;
import com.yindan.bookstore.dao.BookDao;
import com.yindan.bookstore.dao.BorrowingDao;
import com.yindan.bookstore.dao.SalesRecordDao;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.entity.BorrowingEntity;
import com.yindan.bookstore.entity.SalesRecordEntity;
import com.yindan.bookstore.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesRecordServiceImpl implements SalesRecordService {

    @Autowired
    private SalesRecordDao salesRecordDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BorrowingDao borrowingDao;

    //销售书籍
    @Transactional
    @Override
    public int salesBook(Long bookId, Long userId, BigDecimal price, int quantity, int discount) {

        //扣减书籍库存
        //如果有折扣就是借阅过的书籍，没有就是购买的新书
        if (discount < 10 ){
            List<BorrowingEntity> borrowingEntities = borrowingDao.selBorrowingBooks(bookId, BorrowingStatus.RETURNED.getDisplayName());
            //将该本书符合折扣的借阅过的书籍收集起来
            List<BorrowingEntity> collect = borrowingEntities.stream().filter(item -> {
                return item.getDamageLevel().equals(discount);
            }).collect(Collectors.toList());

            //借阅表中是否有足够的库存
            if (quantity > collect.size()){
                return 0;
            }

            //更新借阅表中书籍的状态为已售出
            for (BorrowingEntity record : collect) {
                record.setStatus(BorrowingStatus.SELLOFF.getDisplayName());
                record.setUpdatedAt(new Date());
                borrowingDao.updateByPrimaryKey(record);
            }

        }else {
            BookEntity bookEntity = bookDao.selectByPrimaryKey(bookId);

            //书籍表中是否有足够的库存
            if (quantity > bookEntity.getStock()){
                return 0;
            }

            bookEntity.setStock(bookEntity.getStock() - quantity);
            bookEntity.setUpdatedAt(new Date());
            //扣减新书库存
            bookDao.updateByPrimaryKey(bookEntity);
        }

        //计算销售出去的总价
        BigDecimal totalPrice =  price.multiply(new BigDecimal(quantity)).multiply(new BigDecimal(discount/10));
        BigDecimal sumPrice = totalPrice.divide(BigDecimal.valueOf(10), 2, BigDecimal.ROUND_HALF_UP);

        SalesRecordEntity salesRecordEntity = new SalesRecordEntity();
        salesRecordEntity.setBookId(bookId);
        salesRecordEntity.setUserId(userId);
        salesRecordEntity.setQuantity(quantity);
        salesRecordEntity.setSaleDate(new Date());
        salesRecordEntity.setTotalPrice(sumPrice);
        salesRecordEntity.setCreatedAt(new Date());

        //记录销售数据
        salesRecordDao.insert(salesRecordEntity);


        return 1;
    }
}
