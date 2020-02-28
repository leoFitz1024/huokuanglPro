package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.PaymentMapper;
import com.tmqt.model.Payment;
import com.tmqt.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public int addPayment(Payment payment) {
        Date date = new Date();
        payment.setAdddtime(date);
        return paymentMapper.insertSelective(payment);
    }

    @Override
    public int deletePayment(Integer id) {
        return paymentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updatePayment(Payment payment) {
        return paymentMapper.updateByPrimaryKeySelective(payment);
    }

    @Override
    public Payment findPaymentById(Integer id) {
        return paymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Payment> findAllPayment() {
        return paymentMapper.selectAll();
    }

    @Override
    public List<Payment> findPagePayment(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return paymentMapper.selectAll();
    }

    @Override
    public List<Payment> findPaymentsByFactoryName(String factoryName) {
        return paymentMapper.selectByFactoryName(factoryName);
    }

    @Override
    public List<Payment> findPaymentsByIf(Map conditions) {
        return paymentMapper.selectByIf(conditions);
    }

    @Override
    public List<Payment> findPaymentsByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return paymentMapper.selectByIf(conditions);
    }

    @Override
    public int countPayment() {
        return paymentMapper.countPayment();
    }


}
