package com.tmqt.service;

import com.tmqt.model.Newproduct;

import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
public interface NewProductService {
    int addNewProduct(Newproduct newproduct);

    int deleteNewProduct(Integer id);

    int updateNewProduct(Newproduct newproduct);

    List<Newproduct> findNewProductByCode(String code);

    Newproduct findNewProductById(Integer id);

    List<Newproduct> findAllNewProduct();

    List<Newproduct> findPageNewProduct(Integer pageNum, Integer pageSize);

    List<Newproduct> findNewproductByIf(Map conditions);

    int countNewProduct();
}
