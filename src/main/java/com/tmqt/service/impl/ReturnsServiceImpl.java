package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.ReturnsMapper;
import com.tmqt.model.ReturnExcelEntity;
import com.tmqt.model.Returns;
import com.tmqt.service.ReturnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Service
public class ReturnsServiceImpl implements ReturnsService {

    @Autowired
    private ReturnsMapper returnsMapper;

    @Override
    public int addReturns(Returns returns) {
        returns.setAddtime(new Date());
        return returnsMapper.insertSelective(returns);
    }

    @Override
    public int deleteReturns(Integer id) {
        return returnsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteReturnsByBillId(String billid) {
        return returnsMapper.deleteByBillId(billid);
    }

    @Override
    public int updateReturns(Returns returns) {
        return returnsMapper.updateByPrimaryKeySelective(returns);
    }

    @Override
    public Returns findReturnsById(Integer id) {
        return returnsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Returns> findReturnsByBillId(String billid) {
        return returnsMapper.selectByBillId(billid);
    }

    @Override
    public List<ReturnExcelEntity> findRetExcEntityByBillId(String billid) {
        return returnsMapper.selectRetExcEntityByBillId(billid);
    }

    @Override
    public List<Returns> findAllReturns() {
        return returnsMapper.selectAll();
    }


    /*
    * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
    * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
    * pageNum 开始页数
    * pageSize 每页显示的数据条数
    * */
    @Override
    public List<Returns> findPageReturns(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return returnsMapper.selectAll();
    }

    @Override
    public List<Returns> findReturnsByIf(Map conditions) {
        return returnsMapper.selectByIf(conditions);
    }

    @Override
    public List<Returns> findReturnsByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return returnsMapper.selectByIf(conditions);
    }

    @Override
    public int countReturns() {
        return returnsMapper.countReturns();
    }
}
