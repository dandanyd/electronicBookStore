package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.constant.BorrowingStatus;
import com.yindan.bookstore.dao.BookDao;
import com.yindan.bookstore.dao.BorrowingDao;
import com.yindan.bookstore.dao.BorrowingRecordDao;
import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.entity.BorrowingEntity;
import com.yindan.bookstore.entity.BorrowingRecordEntity;
import com.yindan.bookstore.service.BorrowingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    @Autowired
    private BorrowingDao borrowingDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BorrowingRecordDao borrowingRecordDao;


    @Transactional
    @Override
    public Map<String, Object> addBorrowing(Long bookId, Long userId, Date dueDate) {

        Map<String, Object> map = new HashMap<>();
        //查看当前用户是否有已借阅未还书籍
        int i = borrowingDao.selUserBorrowing(bookId, userId, BorrowingStatus.BORROWING.getDisplayName());
        //当前用户所选的书有未还的借阅
        if (i > 0){
            map.put("msg", "该用户此本书正在借阅中，无法借阅！");
            System.out.println(map);
            return map;
        }else {
            //查看借阅表中是否有可供借阅的书籍
            List<BorrowingEntity> borrowingEntities = borrowingDao.selBorrowingBooks(bookId,BorrowingStatus.RETURNED.getDisplayName());
            if (borrowingEntities.size() > 0){
                //获取借阅次数最多的书籍进行借阅
                BorrowingEntity borrowingEntity = borrowingEntities.get(0);
                borrowingEntity.setUserId(userId);
                borrowingEntity.setBorrowDate(new Date());
                borrowingEntity.setDueDate(dueDate);
                borrowingEntity.setReturnDate(null);
                borrowingEntity.setStatus(BorrowingStatus.BORROWING.getDisplayName());
                borrowingEntity.setUpdatedAt(new Date());
                borrowingEntity.setNumbers(borrowingEntity.getNumbers() + 1);

                //修改借阅数据
                borrowingDao.updateByPrimaryKey(borrowingEntity);

                //添加借阅记录行
                BorrowingRecordEntity borrowingRecordEntity = new BorrowingRecordEntity();
                BeanUtils.copyProperties(borrowingEntity,borrowingRecordEntity);
                borrowingRecordEntity.setId(null);
                borrowingRecordEntity.setBorrowingId(borrowingEntity.getId());
                borrowingRecordEntity.setCreatedAt(new Date());
                borrowingRecordDao.insert(borrowingRecordEntity);
            }else {
                //无，则开新书借阅
                //查询是否有库存
                BookEntity bookEntity = bookDao.selectByPrimaryKey(bookId);
                //有库存
                if (bookEntity.getStock() > 0){
                    //扣减书籍表库存
                    bookEntity.setStock(bookEntity.getStock() - 1);
                    bookDao.updateByPrimaryKey(bookEntity);

                    BorrowingEntity borrowingEntity = new BorrowingEntity();
                    //查询借阅表的总条数
                    int counts = borrowingDao.counts();
                    Long borrowingId = Long.valueOf(counts + 1);

                    borrowingEntity.setId(borrowingId);
                    borrowingEntity.setBookId(bookId);
                    borrowingEntity.setUserId(userId);
                    borrowingEntity.setBorrowDate(new Date());
                    borrowingEntity.setDueDate(dueDate);
                    borrowingEntity.setCreatedAt(new Date());
                    borrowingEntity.setUpdatedAt(new Date());
                    borrowingEntity.setNumbers(1);
                    borrowingEntity.setDamageLevel(9);
                    borrowingEntity.setStatus(BorrowingStatus.BORROWING.getDisplayName());

                    //借阅表中插入数据
                    borrowingDao.insert(borrowingEntity);

                    BorrowingRecordEntity borrowingRecordEntity = new BorrowingRecordEntity();
                    BeanUtils.copyProperties(borrowingEntity,borrowingRecordEntity);
                    borrowingRecordEntity.setId(null);
                    borrowingRecordEntity.setBorrowingId(borrowingId);

                    //借阅记录表中插入数据
                    borrowingRecordDao.insert(borrowingRecordEntity);
                }else {
                    map.put("msg", "该书籍现在无可借用库存！");
                }
            }
        }

        System.out.println(map);
        return map;


    }

    @Override
    public int updateBorrowing(Long bookId,Long userId,Integer damageLevel) {
        //1.找到当前用户,当前书本的借阅表数据
        BorrowingEntity borrowingEntity = borrowingDao.selectByBookIdAndUserId(bookId, userId);

        //借阅书籍破损程度只能比之前严重或者不变
        if (damageLevel > borrowingEntity.getDamageLevel()){
            return 0;
        }

        //破损程度不能小于1成新
        if (damageLevel < 1){
            return 0;
        }

        //2。修改借阅表状态为已归还，设置归还日期
        borrowingEntity.setStatus(BorrowingStatus.RETURNED.getDisplayName());
        borrowingEntity.setReturnDate(new Date());
        borrowingEntity.setUpdatedAt(new Date());
        borrowingEntity.setDamageLevel(damageLevel);
        borrowingDao.updateByPrimaryKey(borrowingEntity);

        //3.借阅记录表增加一行记录
        BorrowingRecordEntity borrowingRecordEntity = new BorrowingRecordEntity();
        BeanUtils.copyProperties(borrowingEntity,borrowingRecordEntity);
        borrowingRecordEntity.setId(null);
        borrowingRecordEntity.setBorrowingId(borrowingEntity.getId());
        borrowingRecordEntity.setCreatedAt(new Date());
        borrowingRecordDao.insert(borrowingRecordEntity);

        return 1;
    }

    @Override
    public int deleteBorrowingById(Long id) {
        return borrowingDao.deleteByPrimaryKey(id);
    }

    @Override
    public BorrowingEntity getBorrowingById(Long id) {
        return borrowingDao.selectByPrimaryKey(id);
    }
}
