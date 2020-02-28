package com.tmqt.service;


import com.tmqt.model.ReturnExcelEntity;
import com.tmqt.model.Returns;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
public interface ReturnsService {
    int addReturns(Returns returns);

    int deleteReturns(Integer id);

    int deleteReturnsByBillId(String billid);

    int updateReturns(Returns returns);

    Returns findReturnsById(Integer id);

    //根据billid查找退货记录
    List<Returns> findReturnsByBillId(String billid);

    List<ReturnExcelEntity> findRetExcEntityByBillId(String billid);

    List<Returns> findAllReturns();

    List<Returns> findPageReturns(Integer pageNum, Integer pageSize);

    List<Returns> findReturnsByIf(Map conditions);

    List<Returns> findReturnsByIfPage(Map conditions, Integer pageNum, Integer pageSize);

    int countReturns();

}
