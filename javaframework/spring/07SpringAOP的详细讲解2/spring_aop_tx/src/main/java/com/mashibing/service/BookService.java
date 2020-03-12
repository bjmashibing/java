package com.mashibing.service;

import com.mashibing.dao.BookDao;
import com.mashibing.entity.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    /*
    * propagation:传播特性：表示不同的事务之间执行的关系
    * isolation:隔离级别：4种隔离级别，会引发不同的数据错乱问题
    * timeout：超时时间,
    * readonly：只读事务:如果配置了只读事务，那么在事务运行期间，不允许对数据进行修改，否则抛出异常
    *
    * 设置哪些异常不会回滚数据
    * noRollBackfor: noRollbackFor = {ArithmeticException.class}
    * noRollbackForClassName:
    *
    * 设置哪些异常回滚
    * rollBackfor:
    * rollbackForClassName
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void buyBook() throws FileNotFoundException {
        bookDao.getPrice(1);
        bookDao.updateBalance("zhangsan", 100);
        bookDao.updateStock(1);
//        int i = 1/0;
        new FileInputStream("aaa.txt");
    }
}
