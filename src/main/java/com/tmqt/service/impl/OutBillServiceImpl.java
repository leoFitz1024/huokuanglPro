package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.OutbillMapper;
import com.tmqt.model.Outbill;
import com.tmqt.service.OutBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/15.
 */
@Service
public class OutBillServiceImpl implements OutBillService {

    @Autowired
    private OutbillMapper outbillMapper;

    @Override
    public int addOutbill(Outbill outbill) {
        outbill.setAddtime(new Date());
        return outbillMapper.insertSelective(outbill);
    }

    @Override
    public int deleteOutbill(String id) {
        return outbillMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateOutbill(Outbill outbill) {
        return outbillMapper.updateByPrimaryKeySelective(outbill);
    }

    @Override
    public Outbill findOutbillById(String id) {
        return outbillMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Outbill> findAllOutbills() {
        return outbillMapper.selectAll();
    }


    /*
    * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
    * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
    * pageNum 开始页数
    * pageSize 每页显示的数据条数
    * */
    @Override
    public List<Outbill> findPageOutbills(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return outbillMapper.selectAll();
    }

    @Override
    public List<Outbill> findOutbillsByIf(Map conditions) {
        return outbillMapper.selectByIf(conditions);
    }

    @Override
    public List<Outbill> findOutbillsByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return outbillMapper.selectByIf(conditions);
    }

    @Override
    public int countOutbill() {
        return outbillMapper.countOutbill();
    }
}
