package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.PurchaseMapper;
import com.tmqt.model.Purchase;
import com.tmqt.model.PurchaseExcelEntity;
import com.tmqt.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public int addPurchase(Purchase purchase) {
        purchase.setAddtime(new Date());
        return purchaseMapper.insertSelective(purchase);
    }

    @Override
    public int deletePurchase(Integer id) {
        return purchaseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deletePurchaseByBillId(String billid) {
        return purchaseMapper.deleteByBillId(billid);
    }

    @Override
    public int updatePurchase(Purchase purchase) {
        return purchaseMapper.updateByPrimaryKeySelective(purchase);
    }

    @Override
    public Purchase findPurchaseById(Integer id) {
        return purchaseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Purchase> findPurchaseByBillId(String billid) {
        return purchaseMapper.selectByBillId(billid);
    }

    @Override
    public List<PurchaseExcelEntity> findPurExcEntityByBillId(String billid) {
        return purchaseMapper.selectPurExcEntityByBillId(billid);
    }

    @Override
    public List<Purchase> findAllPurchases() {
        return purchaseMapper.selectAll();
    }


    /*
    * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
    * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
    * pageNum 开始页数
    * pageSize 每页显示的数据条数
    * */
    @Override
    public List<Purchase> findPagePurchases(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return purchaseMapper.selectAll();
    }

    @Override
    public List<Purchase> findPurchasesByIf(Map conditions) {
        return purchaseMapper.selectByIf(conditions);
    }

    @Override
    public List<Purchase> findPurchasesByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return purchaseMapper.selectByIf(conditions);
    }

    @Override
    public int countPurchase() {
        return purchaseMapper.countPurchase();
    }
}
