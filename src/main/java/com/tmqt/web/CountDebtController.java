package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Monthcheck;
import com.tmqt.model.Payment;
import com.tmqt.service.FactoryService;
import com.tmqt.service.MonthCheckService;
import com.tmqt.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tmqt.util.RemoveNullKeyValue.removeNullValue;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class CountDebtController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MonthCheckService monthCheckService;

    @Autowired
    private FactoryService factoryService;

    //获取所有记录
    @RequestMapping(value = "/getAllMonthCheck", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getAllMonthCheck(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Monthcheck> dataList = monthCheckService.findMonthcheck(pageNum,pageSize);
        List monthCheckViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map monthCheckView = new HashMap();
            monthCheckView.put("id",dataList.get(i).getId());
            monthCheckView.put("month",dataList.get(i).getMonth());
            monthCheckView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            monthCheckView.put("allmoney",dataList.get(i).getAllmoney());
            monthCheckView.put("discountmoney",dataList.get(i).getDiscountmoney());
            monthCheckView.put("amountpaid",dataList.get(i).getAmountpaid());
            monthCheckView.put("debt",dataList.get(i).getDiscountmoney()-dataList.get(i).getAmountpaid());

            monthCheckViews.add(monthCheckView);
        }
        int count = monthCheckService.countMonthcheck();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(monthCheckViews)
                .build();
    }

    //根据条件筛选月账单记录
    @RequestMapping(value = "/findDebtMonthCheck", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findDebtMonthCheck(@RequestParam Map conditions){
        List<Monthcheck> dataList = monthCheckService.findMonthcheckByIf(conditions);
        List monthCheckViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map monthCheckView = new HashMap();
            monthCheckView.put("id",dataList.get(i).getId());
            monthCheckView.put("month",dataList.get(i).getMonth());
            monthCheckView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            monthCheckView.put("allmoney",dataList.get(i).getAllmoney());
            monthCheckView.put("discountmoney",dataList.get(i).getDiscountmoney());
            monthCheckView.put("amountpaid",dataList.get(i).getAmountpaid());
            monthCheckView.put("debt",dataList.get(i).getDiscountmoney()-dataList.get(i).getAmountpaid());

            monthCheckViews.add(monthCheckView);
        }

        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(monthCheckViews)
                .build();
    }

    /**
     * 修改月账单记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateMonthcheck", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateMonthcheck(@RequestBody Monthcheck monthcheck){
        return monthCheckService.updateMonthcheckForDic(monthcheck);
    }

}
