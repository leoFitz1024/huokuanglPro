package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Inbill;
import com.tmqt.model.Purchase;
import com.tmqt.service.FactoryService;
import com.tmqt.service.InBillService;
import com.tmqt.service.MonthCheckService;
import com.tmqt.service.PurchaseService;
import com.tmqt.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenw on 2018/12/15.
 */
@Controller
@RequestMapping
public class InbillController {

    @Autowired
    private InBillService inBillService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private PurchaseController purchaseController;

    /**
     * 增加进货订单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addInbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addInbill(@RequestBody Map inbillMap) throws Exception{
        Inbill inbill = new Inbill();
        String newId = new IDUtils().createID();
        inbill.setId(newId);
        inbill.setFactoryid((int)inbillMap.get("factoryid"));
        SimpleDateFormat sdateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdateformat.parse((String) inbillMap.get("time"));
        inbill.setTime(time);
        inbill.setAllnumber((int)inbillMap.get("allnumber"));
        inbill.setAllprice(Float.parseFloat(inbillMap.get("allprice").toString()));
        inbill.setRemarks((String)inbillMap.get("remarks"));
        inbill.setAddtime(new Date());

        List<Map> purchases = new ArrayList();
        purchases = (List)inbillMap.get("purchases");
        int successNum = 0;
        for( int i = 0 ; i < purchases.size() ; i++) {
            Purchase purchase = new Purchase();
            purchase.setBillid(newId);
            purchase.setTime(time);
            purchase.setFactoryid((int)inbillMap.get("factoryid"));
            purchase.setCode(purchases.get(i).get("code").toString());
            purchase.setColor(purchases.get(i).get("color").toString());
            purchase.setRangesize(purchases.get(i).get("rangesize").toString());
            int number = (int)purchases.get(i).get("number");
            float price = Float.parseFloat(purchases.get(i).get("price").toString());
            purchase.setNumber(number);
            purchase.setPrice(price);
            purchase.setAllprice(number*price);
            int res = purchaseController.addPurchase(purchase);
            successNum += res;
        }

        try{
            if(successNum == purchases.size()){
                return inBillService.addInbill(inbill);
            }else{
                throw new Exception("进货记录添加出现异常");
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//使Transactional即使异常被捕获也回滚
            return 0;
        }

    }

    /**
     * 修改进货订单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateInbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateInbill(@RequestBody Map updateInbillMap) throws Exception{
        //先执行删除操作
        String updateId = updateInbillMap.get("inbillid").toString();
        delInbill(updateId);
        Inbill inbill = new Inbill();
        inbill.setId(updateId);
        inbill.setFactoryid((int)updateInbillMap.get("factoryid"));
        SimpleDateFormat sdateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdateformat.parse((String) updateInbillMap.get("time"));
        inbill.setTime(time);
        inbill.setAllnumber((int)updateInbillMap.get("allnumber"));
        inbill.setAllprice(Float.parseFloat(updateInbillMap.get("allprice").toString()));
        inbill.setRemarks((String)updateInbillMap.get("remarks"));
        inbill.setAddtime(new Date());

        List<Map> purchases = new ArrayList();
        purchases = (List)updateInbillMap.get("purchases");
        int successNum = 0;
        for( int i = 0 ; i < purchases.size() ; i++) {
            Purchase purchase = new Purchase();
            purchase.setBillid(updateId);
            purchase.setTime(time);
            purchase.setFactoryid((int)updateInbillMap.get("factoryid"));
            purchase.setCode(purchases.get(i).get("code").toString());
            purchase.setColor(purchases.get(i).get("color").toString());
            purchase.setRangesize(purchases.get(i).get("rangesize").toString());
            int number = (int)purchases.get(i).get("number");
            float price = Float.parseFloat(purchases.get(i).get("price").toString());
            purchase.setNumber(number);
            purchase.setPrice(price);
            purchase.setAllprice(number*price);
            int res = purchaseController.addPurchase(purchase);
            successNum += res;
        }

        try{
            if(successNum == purchases.size()){
                return inBillService.addInbill(inbill);
            }else{
                throw new Exception("进货修改添加出现异常");
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//使Transactional即使异常被捕获也回滚
            return 0;
        }

    }

    /**
     * 删除进货订单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delInbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delInbill(String id){
        List<Purchase> inBillPurchases = purchaseService.findPurchaseByBillId(id);
        int successNum = 0;
        for(int i=0;i<inBillPurchases.size();i++){
            int res = purchaseController.delPurchase(inBillPurchases.get(i).getId());
            successNum += res;
        }

        try{
            if(successNum == inBillPurchases.size()){
                return inBillService.deleteInbill(id);
            }else{
                throw new Exception("进货记录删除出现异常");
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//使Transactional即使异常被捕获也回滚
            return 0;
        }
    }

    /**
     * 获取进货订单列表
     * @return
     */
    @RequestMapping(value = "/getInbillList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getInbillList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Inbill> dataList = inBillService.findPageInbills(pageNum,pageSize);
        List inbillViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map inbillView = new HashMap();
            inbillView.put("id",dataList.get(i).getId());
            inbillView.put("time",dataList.get(i).getTime());
            inbillView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            inbillView.put("allprice",dataList.get(i).getAllprice());
            inbillView.put("allnumber",dataList.get(i).getAllnumber());
            inbillView.put("remarks",dataList.get(i).getRemarks());
            inbillView.put("addtime",dataList.get(i).getAddtime());

            inbillViews.add(inbillView);
        }

        int count = inBillService.countInbill();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(inbillViews)
                .build();
    }

    /**
     * 筛选进货订单
     * @return
     */

    @RequestMapping(value = "/findInbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findInbill(@RequestParam Map conditions){
        List<Inbill> dataList = inBillService.findInbillsByIf(conditions);
        List inbillViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map inbillView = new HashMap();
            inbillView.put("id",dataList.get(i).getId());
            inbillView.put("time",dataList.get(i).getTime());
            inbillView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            inbillView.put("allnumber",dataList.get(i).getAllnumber());
            inbillView.put("allprice",dataList.get(i).getAllprice());
            inbillView.put("remarks",dataList.get(i).getRemarks());
            inbillView.put("addtime",dataList.get(i).getAddtime());

            inbillViews.add(inbillView);
        }
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(inbillViews)
                .build();
    }

    /**
     * 获取订单详情页面数据
     * @return
     */
    @RequestMapping(value = "/getInbillDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getInbillDetail(@RequestParam("id") String id){
        Map inbillDate = new HashMap();
        Inbill inbill = inBillService.findInbillById(id);
        List<Purchase> dateilPurchaseList = purchaseService.findPurchaseByBillId(id);
        inbillDate.put("inbill",inbill);
        inbillDate.put("purchaseList",dateilPurchaseList);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(inbillDate)
                .build();
    }

}
