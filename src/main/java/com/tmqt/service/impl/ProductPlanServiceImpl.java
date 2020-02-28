package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.ProductplanMapper;
import com.tmqt.model.Productplan;
import com.tmqt.service.ProductPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
@Service
public class ProductPlanServiceImpl implements ProductPlanService {

    @Autowired
    private ProductplanMapper productplanMapper;

    @Override
    public int addProductPlan(Productplan productplan) {
        Date currentTime = new Date();
        productplan.setAddtime(currentTime);
        return productplanMapper.insertSelective(productplan);
    }

    @Override
    public int deleteProductPlan(Integer id) {
        return productplanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateProductPlan(Productplan productplan) {
        return productplanMapper.updateByPrimaryKeySelective(productplan);
    }

    @Override
    public List<Productplan> findProductPlanByCode(String code) {
        return productplanMapper.selectByCode(code);
    }

    @Override
    public Productplan findProductPlanById(Integer id) {
        return productplanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Productplan> findAllProductPlan() {
        return productplanMapper.selectAll();
    }

    @Override
    public List<Productplan> findPageProductPlan(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return productplanMapper.selectAll();
    }

    @Override
    public List<Productplan> findProductplanByIf(Map conditions) {
        return productplanMapper.selectByIf(conditions);
    }

    @Override
    public int countProductPlan() {
        return productplanMapper.countProductPlan();
    }
}
