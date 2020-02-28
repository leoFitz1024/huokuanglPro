package com.tmqt.service;

import com.tmqt.model.Monthcheck;

import java.util.List;
import java.util.Map;

/**
 * Created by chenw on 2018/12/19.
 */
public interface MonthCheckService  {
    int addMonthcheck(Monthcheck monthcheck);

    int deleteMonthcheck(Integer id);

    int updateMonthcheck(Monthcheck monthcheck);

    int updateMonthcheckForDic(Monthcheck monthcheck);

    List<Monthcheck> findMonthcheckByIf(Map conditions);

    List<Monthcheck> findMonthcheckByIfPage(Map conditions,Integer pageNum,Integer pageSize);

    List<Monthcheck> findMonthcheck(Integer pageNum,Integer pageSize);
    int countMonthcheck();
}
