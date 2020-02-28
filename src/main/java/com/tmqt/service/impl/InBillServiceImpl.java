package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.InbillMapper;
import com.tmqt.model.Inbill;
import com.tmqt.model.InbillExcelEntity;
import com.tmqt.service.InBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/15.
 */
@Service
public class InBillServiceImpl implements InBillService {

    @Autowired
    private InbillMapper inbillMapper;

    @Override
    public int addInbill(Inbill inbill) {
        inbill.setAddtime(new Date());
        return inbillMapper.insertSelective(inbill);
    }

    @Override
    public int deleteInbill(String id) {
        return inbillMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateInbill(Inbill inbill) {
        return inbillMapper.updateByPrimaryKeySelective(inbill);
    }

    @Override
    public Inbill findInbillById(String id) {
        return inbillMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Inbill> findAllInbills() {
        return inbillMapper.selectAll();
    }


    /*
    * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
    * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
    * pageNum 开始页数
    * pageSize 每页显示的数据条数
    * */
    @Override
    public List<Inbill> findPageInbills(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return inbillMapper.selectAll();
    }

    @Override
    public List<Inbill> findInbillsByIf(Map conditions) {
        return inbillMapper.selectByIf(conditions);
    }

    @Override
    public List<Inbill> findInbillsByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        if(pageNum != -1&&pageSize != -1){
            //将参数传给这个方法就可以实现物理分页了，非常简单。
            PageHelper.startPage(pageNum, pageSize);
        }

        return inbillMapper.selectByIf(conditions);
    }

    @Override
    public int countInbill() {
        return inbillMapper.countInbill();
    }

}
