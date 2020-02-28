package com.tmqt.service;


import com.tmqt.model.Purchase;
import com.tmqt.model.PurchaseExcelEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
public interface PurchaseService {
    //增
    int addPurchase(Purchase purchase);
    //删
    int deletePurchase(Integer id);
    //根据billid删除
    int deletePurchaseByBillId(String billid);
    //更新
    int updatePurchase(Purchase purchase);

    Purchase findPurchaseById(Integer id);

    //根据billid查找进货记录
    List<Purchase> findPurchaseByBillId(String billid);

    List<PurchaseExcelEntity> findPurExcEntityByBillId(String billid);

    List<Purchase> findAllPurchases();

    List<Purchase> findPagePurchases(Integer pageNum, Integer pageSize);

    List<Purchase> findPurchasesByIf(Map conditions);

    List<Purchase> findPurchasesByIfPage(Map conditions, Integer pageNum, Integer pageSize);

    int countPurchase();
}
