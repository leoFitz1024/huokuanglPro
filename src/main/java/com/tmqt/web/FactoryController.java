package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Factory;
import com.tmqt.service.FactoryService;
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
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

    /**
     * 添加厂家
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFactory", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addFactory(@RequestBody Factory factory){
        return factoryService.addFactory(factory);
    }

    /**
     * 删除厂家
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delFactory", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delFactory(int id){
        return factoryService.deleteFactory(id);
    }

    /**
     * 修改厂家
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateFactory", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateFactory(@RequestBody Factory factory){
        return factoryService.updateFactory(factory);
    }

    /**
     * 获取厂家列表分页
     * @return
     */
    @RequestMapping(value = "/getFactoryList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getFactoryList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Factory> dataList = factoryService.findPageFactory(pageNum,pageSize);
        int count = factoryService.countFactory();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(dataList)
                .build();
    }

    /**
     * 获取厂家列表不分页
     * @return
     */
    @RequestMapping(value = "/getAllFactory", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getFactoryListNoPage(){
        List<Factory> dataList = factoryService.findAllFactory();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(dataList)
                .build();
    }

    /**
     * 查找厂家
     * @return
     */
    @RequestMapping(value = "/findFactory", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findFactoryByName(@RequestParam("factoryname") String factoryname){
        List<Factory> dataList = factoryService.findFactoryByName(factoryname);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(dataList)
                .build();
    }

}
