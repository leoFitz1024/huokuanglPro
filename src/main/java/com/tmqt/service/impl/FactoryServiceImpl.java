package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.FactoryMapper;
import com.tmqt.model.Factory;
import com.tmqt.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;

    @Override
    public int addFactory(Factory factory) {
        Date date = new Date();
        factory.setAddtime(date);
        return factoryMapper.insertSelective(factory);
    }

    @Override
    public int deleteFactory(Integer id) {
        return factoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateFactory(Factory factory) {
        return factoryMapper.updateByPrimaryKeySelective(factory);
    }

    @Override
    public Factory findFactoryById(Integer id) {
        return factoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Factory> findFactoryByName(String factoryName) {
        return factoryMapper.selectByName(factoryName);
    }

    @Override
    public List<Factory> findAllFactory() {
        return factoryMapper.selectAll();
    }

    @Override
    public List<Factory> findPageFactory(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return factoryMapper.selectAll();
    }

    @Override
    public int countFactory() {
        return factoryMapper.countFactory();
    }

    @Override
    public String selectNameById(int factoryid) {
        return factoryMapper.selectNameById(factoryid);
    }


}
