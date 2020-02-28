package com.tmqt.service;

import com.tmqt.model.Rangesize;

import java.util.List;

/**
 * Created by chenw on 2018/12/19.
 */
public interface RangesizeService {
    int addRangesize(Rangesize rangesize);

    int deleteRangesize(Integer id);

    int updateRangesize(Rangesize rangesize);

    Rangesize findRangesizeById(Integer id);

    List<Rangesize> findRangesizeBySize(String rangesize);

    List<Rangesize> findAllRangesize();

    List<Rangesize> findPageRangesize(Integer pageNum, Integer pageSize);

    int countRangesize();

    String selectRangesizeById(int rangesizeId);
}
