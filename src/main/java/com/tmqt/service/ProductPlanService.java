package com.tmqt.service;

import com.tmqt.model.Productplan;

import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
public interface ProductPlanService {
    int addProductPlan(Productplan productplan);

    int deleteProductPlan(Integer id);

    int updateProductPlan(Productplan productplan);

    List<Productplan> findProductPlanByCode(String code);

    Productplan findProductPlanById(Integer id);

    List<Productplan> findAllProductPlan();

    List<Productplan> findPageProductPlan(Integer pageNum, Integer pageSize);

    List<Productplan> findProductplanByIf(Map conditions);

    int countProductPlan();
}
