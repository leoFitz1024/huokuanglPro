package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Monthcheck;
import com.tmqt.model.Purchase;
import com.tmqt.service.FactoryService;
import com.tmqt.service.MonthCheckService;
import com.tmqt.service.PurchaseService;
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
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private MonthCheckService monthCheckService;

    @Autowired
    private FactoryService factoryService;

    /**
     * 增加进货记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addPurchase", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addPurchase(@RequestBody Purchase purchase){

        int addRes = purchaseService.addPurchase(purchase);
        if(addRes > 0){
            Map conditions = new HashMap();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String monthStr = format.format(purchase.getTime());
//            Date monthDate = new Date(monthStr);
            conditions.put("month",monthStr);
            conditions.put("factoryid",purchase.getFactoryid());
            List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
            System.out.print(MonthcheckList);
            if(MonthcheckList.isEmpty()){
                //为该月该厂家第一条进货记录，添加Monthcheck
                Monthcheck monthcheck = new Monthcheck();
                monthcheck.setMonth(purchase.getTime());
                monthcheck.setFactoryid(purchase.getFactoryid());
                monthcheck.setAmountpaid((float)0);
                monthcheck.setAllmoney(purchase.getAllprice());
                monthcheck.setDiscountmoney(purchase.getAllprice());

                return monthCheckService.addMonthcheck(monthcheck);
            }else{
                //前面已有纪录
                Monthcheck monthcheck = MonthcheckList.get(0);
                monthcheck.setAllmoney(monthcheck.getAllmoney()+purchase.getAllprice());
                monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()+purchase.getAllprice());
                return monthCheckService.updateMonthcheck(monthcheck);
            }

        }else{
            return 0;
        }

    }

    /**
     * 删除进货记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delPurchase", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delPurchase(int id){
        Purchase purchase = purchaseService.findPurchaseById(id);
        Map conditions = new HashMap();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String monthStr = format.format(purchase.getTime());
        conditions.put("month",monthStr);
        conditions.put("factoryid",purchase.getFactoryid());
        List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
        Monthcheck monthcheck = MonthcheckList.get(0);
        monthcheck.setAllmoney(monthcheck.getAllmoney()-purchase.getAllprice());
        monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()-purchase.getAllprice());
        Date monthCheckupdatetime = new Date();
        monthcheck.setUpdatetime(monthCheckupdatetime);
        if(monthCheckService.updateMonthcheck(monthcheck)>0){
            return purchaseService.deletePurchase(id);
        }else {
            return 0;
        }

    }

    /**
     * 批量删除
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delPurchaseList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int deleteShoeList(@RequestBody ArrayList<Purchase> idList){
        int i;
        for(i = 0 ; i < idList.size() ; i++) {
            Purchase purchase = purchaseService.findPurchaseById(idList.get(i).getId());
            Map conditions = new HashMap();
            conditions.put("month",purchase.getTime());
            conditions.put("factoryid",purchase.getFactoryid());
            List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
            Monthcheck monthcheck = MonthcheckList.get(0);
            monthcheck.setAllmoney(monthcheck.getAllmoney()-purchase.getAllprice());
            monthcheck.setDiscountmoney(monthcheck.getDiscountmoney()-purchase.getAllprice());
            monthCheckService.updateMonthcheck(monthcheck);
            purchaseService.deletePurchase(idList.get(i).getId());
        }

        return i;
    }

    /**
     * 获取进货记录列表
     * @return
     */
    @RequestMapping(value = "/getPurchaseList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getPurchaseList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Purchase> dataList = purchaseService.findPagePurchases(pageNum,pageSize);
        List purchaseViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map purchaseView = new HashMap();
            purchaseView.put("id",dataList.get(i).getId());
            purchaseView.put("time",dataList.get(i).getTime());
            purchaseView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            purchaseView.put("code",dataList.get(i).getCode());
            purchaseView.put("color",dataList.get(i).getColor());
            purchaseView.put("price",dataList.get(i).getPrice());
            purchaseView.put("number",dataList.get(i).getNumber());
            purchaseView.put("allprice",dataList.get(i).getAllprice());
            purchaseView.put("rangesize",dataList.get(i).getRangesize());
            purchaseView.put("remarks",dataList.get(i).getRemarks());
            purchaseView.put("addtime",dataList.get(i).getAddtime());

            purchaseViews.add(purchaseView);
        }
        int count = purchaseService.countPurchase();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(purchaseViews)
                .build();
    }


    /**
     * 筛选进货记录
     * @return
     */

    @RequestMapping(value = "/findPurchase", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findPurchase(@RequestParam Map conditions){
        List<Purchase> dataList = purchaseService.findPurchasesByIf(conditions);
        List purchaseViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map purchaseView = new HashMap();
            purchaseView.put("id",dataList.get(i).getId());
            purchaseView.put("time",dataList.get(i).getTime());
            purchaseView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            purchaseView.put("code",dataList.get(i).getCode());
            purchaseView.put("color",dataList.get(i).getColor());
            purchaseView.put("price",dataList.get(i).getPrice());
            purchaseView.put("number",dataList.get(i).getNumber());
            purchaseView.put("allprice",dataList.get(i).getAllprice());
            purchaseView.put("rangesize",dataList.get(i).getRangesize());
            purchaseView.put("remarks",dataList.get(i).getRemarks());
            purchaseView.put("addtime",dataList.get(i).getAddtime());

            purchaseViews.add(purchaseView);
        }
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(purchaseViews)
                .build();
    }
}
