package com.tmqt.service;

import com.tmqt.model.Outbill;

import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/15.
 */
public interface OutBillService {
    int addOutbill(Outbill outbill);

    int deleteOutbill(String id);

    int updateOutbill(Outbill outbill);

    Outbill findOutbillById(String id);

    List<Outbill> findAllOutbills();

    List<Outbill> findPageOutbills(Integer pageNum, Integer pageSize);

    List<Outbill> findOutbillsByIf(Map conditions);

    List<Outbill> findOutbillsByIfPage(Map conditions, Integer pageNum, Integer pageSize);

    int countOutbill();
}
