package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Rangesize;
import com.tmqt.service.RangesizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class RangesizeController {

    @Autowired
    private RangesizeService rangesizeService;

    /**
     * 添加码段
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addRangesize", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addRangesize(@RequestBody Rangesize rangesize){
        return rangesizeService.addRangesize(rangesize);
    }

    /**
     * 删除码段
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delRangesize", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delRangesize(int id){
        return rangesizeService.deleteRangesize(id);
    }

    /**
     * 修改码段
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateRangesize", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateRangesize(@RequestBody Rangesize rangesize){
        return rangesizeService.updateRangesize(rangesize);
    }

    /**
     * 获取码段列表分页
     * @return
     */
    @RequestMapping(value = "/getRangesizeList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getRangesizeList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Rangesize> dataList = rangesizeService.findPageRangesize(pageNum,pageSize);
        int count = rangesizeService.countRangesize();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(dataList)
                .build();
    }

    /**
     * 获取码段列表不分页
     * @return
     */
    @RequestMapping(value = "/getAllRangesize", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getRangesizeListNoPage(){
        List<Rangesize> dataList = rangesizeService.findAllRangesize();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(dataList)
                .build();
    }

    /**
     * 查找码段
     * @return
     */
    @RequestMapping(value = "/findRangesize", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findRangesizeBySize(@RequestParam("rangesize") String rangesize){
        List<Rangesize> dataList = rangesizeService.findRangesizeBySize(rangesize);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(dataList)
                .build();
    }
}
