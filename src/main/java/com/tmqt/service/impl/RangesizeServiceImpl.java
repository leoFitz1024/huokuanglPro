package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.RangesizeMapper;
import com.tmqt.model.Rangesize;
import com.tmqt.service.RangesizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chenw on 2018/12/19.
 */
@Service
public class RangesizeServiceImpl implements RangesizeService {

    @Autowired
    private RangesizeMapper rangesizeMapper;

    @Override
    public int addRangesize(Rangesize rangesize) {
        Date date = new Date();
        rangesize.setAddtime(date);
        return rangesizeMapper.insertSelective(rangesize);
    }

    @Override
    public int deleteRangesize(Integer id) {
        return rangesizeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRangesize(Rangesize rangesize) {
        return rangesizeMapper.updateByPrimaryKeySelective(rangesize);
    }

    @Override
    public Rangesize findRangesizeById(Integer id) {
        return null;
    }

    @Override
    public List<Rangesize> findRangesizeBySize(String rangesize) {
        return rangesizeMapper.selectBySize(rangesize);
    }

    @Override
    public List<Rangesize> findAllRangesize() {
        return rangesizeMapper.selectAll();
    }

    @Override
    public List<Rangesize> findPageRangesize(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return rangesizeMapper.selectAll();
    }

    @Override
    public int countRangesize() {
        return rangesizeMapper.countRangesize();
    }

    @Override
    public String selectRangesizeById(int rangesizeId) {
        return rangesizeMapper.selectRangesizeById(rangesizeId);
    }
}
