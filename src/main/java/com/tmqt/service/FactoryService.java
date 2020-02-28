package com.tmqt.service;


import com.tmqt.model.Factory;

import java.util.List;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
public interface FactoryService {
    int addFactory(Factory factory);

    int deleteFactory(Integer id);

    int updateFactory(Factory factory);

    Factory findFactoryById(Integer id);

    List<Factory> findFactoryByName(String factoryName);

    List<Factory> findAllFactory();

    List<Factory> findPageFactory(Integer pageNum, Integer pageSize);

    int countFactory();

    String selectNameById(int factoryid);
}
