package com.tmqt.service;


import com.tmqt.model.Payment;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
public interface PaymentService {

    int addPayment(Payment payment);

    int deletePayment(Integer id);

    int updatePayment(Payment payment);

    Payment findPaymentById(Integer id);

    List<Payment> findAllPayment();

    List<Payment> findPagePayment(Integer pageNum, Integer pageSize);

    List<Payment> findPaymentsByFactoryName(String factoryName);

    List<Payment> findPaymentsByIf(Map conditions);

    List<Payment> findPaymentsByIfPage(Map conditions, Integer pageNum, Integer pageSize);

    int countPayment();

}
