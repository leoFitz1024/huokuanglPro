package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.MonthcheckMapper;
import com.tmqt.model.Monthcheck;
import com.tmqt.service.MonthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
@Service
public class MonthCheckServiceImpl implements MonthCheckService {

    @Autowired
    private MonthcheckMapper monthcheckMapper;

    @Override
    public int addMonthcheck(Monthcheck monthcheck) {
        Date date = new Date();
        monthcheck.setAddtime(date);
        return monthcheckMapper.insertSelective(monthcheck);
    }

    @Override
    public int deleteMonthcheck(Integer id) {
        return monthcheckMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateMonthcheck(Monthcheck monthcheck) {
        if(monthcheck.getAllmoney()==0&&monthcheck.getDiscountmoney()==0&&monthcheck.getAmountpaid()==0){
            return monthcheckMapper.deleteByPrimaryKey(monthcheck.getId());
        }else{
            monthcheck.setUpdatetime(new Date());
            return monthcheckMapper.updateByPrimaryKeySelective(monthcheck);
        }
    }

    @Override
    public int updateMonthcheckForDic(Monthcheck monthcheck) {
        monthcheck.setUpdatetime(new Date());
        return monthcheckMapper.updateByPrimaryKeySelective(monthcheck);
    }


    @Override
    public List<Monthcheck> findMonthcheckByIf(Map conditions) {
        return monthcheckMapper.selectByIf(conditions);
    }

    @Override
    public List<Monthcheck> findMonthcheckByIfPage(Map conditions, Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return monthcheckMapper.selectByIf(conditions);
    }

    @Override
    public List<Monthcheck> findMonthcheck(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return monthcheckMapper.selectAll();
    }

    @Override
    public int countMonthcheck() {
        return monthcheckMapper.countMonthcheck();
    }
}
