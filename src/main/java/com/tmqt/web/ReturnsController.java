package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Monthcheck;
import com.tmqt.model.Returns;
import com.tmqt.service.FactoryService;
import com.tmqt.service.MonthCheckService;
import com.tmqt.service.ReturnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class ReturnsController {

    @Autowired
    private ReturnsService returnsService;

    @Autowired
    private MonthCheckService monthCheckService;

    @Autowired
    private FactoryService factoryService;

    /**
     * 增加退货记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addReturns", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addReturns(@RequestBody Returns returns){

        int addRes = returnsService.addReturns(returns);
        if(addRes > 0){
            Map conditions = new HashMap();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String monthStr = format.format(returns.getAtmonth());
            conditions.put("month",monthStr);
            conditions.put("factoryid",returns.getFactoryid());
            List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
            if(MonthcheckList.isEmpty()){
                //为该月该厂家第一条退货记录，添加Monthcheck
                Monthcheck monthcheck = new Monthcheck();
                monthcheck.setMonth(returns.getAtmonth());
                monthcheck.setFactoryid(returns.getFactoryid());
                monthcheck.setAmountpaid((float)0);
                monthcheck.setAllmoney(0-returns.getAllprice());
                monthcheck.setDiscountmoney(0-returns.getAllprice());
                return monthCheckService.addMonthcheck(monthcheck);
            }else{
                //前面已有纪录
                Monthcheck monthcheck = MonthcheckList.get(0);
                monthcheck.setAllmoney(monthcheck.getAllmoney()-returns.getAllprice());
                monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()-returns.getAllprice());
                return monthCheckService.updateMonthcheck(monthcheck);
            }

        }else{
            return 0;
        }

    }

    /**
     * 删除退货记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delReturns", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delReturns(int id){
        Returns returns = returnsService.findReturnsById(id);
        Map conditions = new HashMap();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String monthStr = format.format(returns.getAtmonth());
        conditions.put("month",monthStr);
        conditions.put("factoryid",returns.getFactoryid());
        List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
        Monthcheck monthcheck = MonthcheckList.get(0);
        monthcheck.setAllmoney(monthcheck.getAllmoney()+returns.getAllprice());
        monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()+returns.getAllprice());
        Date monthCheckupdatetime = new Date();
        monthcheck.setUpdatetime(monthCheckupdatetime);
        if(monthCheckService.updateMonthcheck(monthcheck)>0){
            return returnsService.deleteReturns(id);
        }else {
            return 0;
        }

    }

    /**
     * 批量删除
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delReturnsList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int deleteShoeList(@RequestBody ArrayList<Returns> idList){
        int i;
        for(i = 0 ; i < idList.size() ; i++) {
            Returns returns = returnsService.findReturnsById(idList.get(i).getId());
            Map conditions = new HashMap();
            conditions.put("month",returns.getTime());
            conditions.put("factoryid",returns.getFactoryid());
            List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
            Monthcheck monthcheck = MonthcheckList.get(0);
            monthcheck.setAllmoney(monthcheck.getAllmoney()+returns.getAllprice());
            monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()+returns.getAllprice());
            monthCheckService.updateMonthcheck(monthcheck);
            returnsService.deleteReturns(idList.get(i).getId());
        }

        return i;
    }

    /**
     * 获取退货记录列表
     * @return
     */
    @RequestMapping(value = "/getReturnsList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getReturnsList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Returns> dataList = returnsService.findPageReturns(pageNum,pageSize);
        List ReturnsViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map ReturnsView = new HashMap();
            ReturnsView.put("id",dataList.get(i).getId());
            ReturnsView.put("time",dataList.get(i).getTime());
            ReturnsView.put("atmonth",dataList.get(i).getAtmonth());
            ReturnsView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            ReturnsView.put("code",dataList.get(i).getCode());
            ReturnsView.put("color",dataList.get(i).getColor());
            ReturnsView.put("price",dataList.get(i).getPrice());
            ReturnsView.put("number",dataList.get(i).getNumber());
            ReturnsView.put("allprice",dataList.get(i).getAllprice());
            ReturnsView.put("rangesize",dataList.get(i).getRangesize());
            ReturnsView.put("remarks",dataList.get(i).getRemarks());
            ReturnsView.put("addtime",dataList.get(i).getAddtime());

            ReturnsViews.add(ReturnsView);
        }
        int count = returnsService.countReturns();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(ReturnsViews)
                .build();
    }


    /**
     * 筛选退货记录
     * @return
     */

    @RequestMapping(value = "/findReturns", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findReturns(@RequestParam Map conditions){
        List<Returns> dataList = returnsService.findReturnsByIf(conditions);
        List ReturnsViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map ReturnsView = new HashMap();
            ReturnsView.put("id",dataList.get(i).getId());
            ReturnsView.put("time",dataList.get(i).getTime());
            ReturnsView.put("atmonth",dataList.get(i).getAtmonth());
            ReturnsView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            ReturnsView.put("code",dataList.get(i).getCode());
            ReturnsView.put("color",dataList.get(i).getColor());
            ReturnsView.put("price",dataList.get(i).getPrice());
            ReturnsView.put("number",dataList.get(i).getNumber());
            ReturnsView.put("allprice",dataList.get(i).getAllprice());
            ReturnsView.put("rangesize",dataList.get(i).getRangesize());
            ReturnsView.put("remarks",dataList.get(i).getRemarks());
            ReturnsView.put("addtime",dataList.get(i).getAddtime());

            ReturnsViews.add(ReturnsView);
        }
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(ReturnsViews)
                .build();
    }
}
