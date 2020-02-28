package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.NewproductMapper;
import com.tmqt.model.Newproduct;
import com.tmqt.service.NewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
@Service
public class NewProductServiceImpl implements NewProductService {
    @Autowired
    private NewproductMapper newproductMapper;

    @Override
    public int addNewProduct(Newproduct newproduct) {
        Date currentTime = new Date();
        newproduct.setAddtime(currentTime);
        return newproductMapper.insertSelective(newproduct);
    }

    @Override
    public int deleteNewProduct(Integer id) {
        return newproductMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateNewProduct(Newproduct newproduct) {
        return newproductMapper.updateByPrimaryKeySelective(newproduct);
    }

    @Override
    public List<Newproduct> findNewProductByCode(String code) {
        return newproductMapper.selectByCode(code);
    }

    @Override
    public Newproduct findNewProductById(Integer id) {
        return newproductMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Newproduct> findAllNewProduct() {
        return newproductMapper.selectAll();
    }

    @Override
    public List<Newproduct> findPageNewProduct(Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return newproductMapper.selectAll();
    }

    @Override
    public List<Newproduct> findNewproductByIf(Map conditions) {
        return newproductMapper.selectByIf(conditions);
    }

    @Override
    public int countNewProduct() {
        return newproductMapper.countNewProduct();
    }
}
