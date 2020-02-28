package com.tmqt.service;

import com.tmqt.model.Inbill;
import com.tmqt.model.InbillExcelEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/15.
 */
public interface InBillService {
    int addInbill(Inbill inbill);

    int deleteInbill(String id);

    int updateInbill(Inbill inbill);

    Inbill findInbillById(String id);

    List<Inbill> findAllInbills();

    List<Inbill> findPageInbills(Integer pageNum, Integer pageSize);

    List<Inbill> findInbillsByIf(Map conditions);

    List<Inbill> findInbillsByIfPage(Map conditions, Integer pageNum, Integer pageSize);

    int countInbill();
}
