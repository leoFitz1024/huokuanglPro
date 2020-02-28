package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Outbill;
import com.tmqt.model.Returns;
import com.tmqt.service.FactoryService;
import com.tmqt.service.OutBillService;
import com.tmqt.service.ReturnsService;
import com.tmqt.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class OutbillController {

    @Autowired
    private OutBillService outBillService;

    @Autowired
    private ReturnsService returnsService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private ReturnsController returnsController;

    /**
     * 增加进货订单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOutbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addOutbill(@RequestBody Map outbillMap) throws Exception{
        Outbill outbill = new Outbill();
        String newId = new IDUtils().createID();
        outbill.setId(newId);
        outbill.setFactoryid((int)outbillMap.get("factoryid"));
        SimpleDateFormat sdayformat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdayformat.parse((String) outbillMap.get("time"));
        SimpleDateFormat smonformat = new SimpleDateFormat("yyyy-MM");
        Date atmonth = smonformat.parse((String) outbillMap.get("atmonth"));
        outbill.setTime(time);
        outbill.setAtmonth(atmonth);
        outbill.setAllnumber((int)outbillMap.get("allnumber"));
        outbill.setAllprice(Float.parseFloat(outbillMap.get("allprice").toString()));
        outbill.setRemarks((String)outbillMap.get("remarks"));
        outbill.setAddtime(new Date());

        List<Map> returnss = new ArrayList();
        returnss = (List)outbillMap.get("returnss");
        int successNum = 0;
        for( int i = 0 ; i < returnss.size() ; i++) {
            Returns returns = new Returns();
            returns.setBillid(newId);
            returns.setTime(time);
            returns.setAtmonth(atmonth);
            returns.setFactoryid((int)outbillMap.get("factoryid"));
            returns.setCode(returnss.get(i).get("code").toString());
            returns.setColor(returnss.get(i).get("color").toString());
            returns.setRangesize(returnss.get(i).get("rangesize").toString());
            int number = (int)returnss.get(i).get("number");
            float price = Float.parseFloat(returnss.get(i).get("price").toString());
            returns.setNumber(number);
            returns.setPrice(price);
            returns.setAllprice(number*price);
            int res = returnsController.addReturns(returns);
            successNum += res;
        }

        try{
            if(successNum == returnss.size()){
                return outBillService.addOutbill(outbill);
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
    @RequestMapping(value = "/updateOutbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateOutbill(@RequestBody Map updateOutbillMap) throws Exception{
        //先执行删除操作
        String updateId = updateOutbillMap.get("outbillid").toString();
        delOutbill(updateId);
        Outbill outbill = new Outbill();
        outbill.setId(updateId);
        outbill.setFactoryid((int)updateOutbillMap.get("factoryid"));
        SimpleDateFormat sdateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdateformat.parse((String) updateOutbillMap.get("time"));
        SimpleDateFormat smonformat = new SimpleDateFormat("yyyy-MM");
        Date atmonth = smonformat.parse((String) updateOutbillMap.get("atmonth"));
        outbill.setTime(time);
        outbill.setAtmonth(atmonth);
        outbill.setAllnumber((int)updateOutbillMap.get("allnumber"));
        outbill.setAllprice(Float.parseFloat(updateOutbillMap.get("allprice").toString()));
        outbill.setRemarks((String)updateOutbillMap.get("remarks"));
        outbill.setAddtime(new Date());

        List<Map> returnss = new ArrayList();
        returnss = (List)updateOutbillMap.get("returnss");
        int successNum = 0;
        for( int i = 0 ; i < returnss.size() ; i++) {
            Returns returns = new Returns();
            returns.setBillid(updateId);
            returns.setTime(time);
            returns.setAtmonth(atmonth);
            returns.setFactoryid((int)updateOutbillMap.get("factoryid"));
            returns.setCode(returnss.get(i).get("code").toString());
            returns.setColor(returnss.get(i).get("color").toString());
            returns.setRangesize(returnss.get(i).get("rangesize").toString());
            int number = (int)returnss.get(i).get("number");
            float price = Float.parseFloat(returnss.get(i).get("price").toString());
            returns.setNumber(number);
            returns.setPrice(price);
            returns.setAllprice(number*price);
            int res = returnsController.addReturns(returns);
            successNum += res;
        }

        try{
            if(successNum == returnss.size()){
                return outBillService.addOutbill(outbill);
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
    @RequestMapping(value = "/delOutbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delOutbill(String id){
        List<Returns> outBillReturnss = returnsService.findReturnsByBillId(id);
        int successNum = 0;
        for(int i=0;i<outBillReturnss.size();i++){
            int res = returnsController.delReturns(outBillReturnss.get(i).getId());
            successNum += res;
        }

        try{
            if(successNum == outBillReturnss.size()){
                return outBillService.deleteOutbill(id);
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
    @RequestMapping(value = "/getOutbillList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getOutbillList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Outbill> dataList = outBillService.findPageOutbills(pageNum,pageSize);
        List outbillViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map outbillView = new HashMap();
            outbillView.put("id",dataList.get(i).getId());
            outbillView.put("time",dataList.get(i).getTime());
            outbillView.put("atmonth",dataList.get(i).getAtmonth());
            outbillView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            outbillView.put("allprice",dataList.get(i).getAllprice());
            outbillView.put("allnumber",dataList.get(i).getAllnumber());
            outbillView.put("remarks",dataList.get(i).getRemarks());
            outbillView.put("addtime",dataList.get(i).getAddtime());

            outbillViews.add(outbillView);
        }

        int count = outBillService.countOutbill();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(outbillViews)
                .build();
    }

    /**
     * 筛选进货订单
     * @return
     */

    @RequestMapping(value = "/findOutbill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findOutbill(@RequestParam Map conditions){
        List<Outbill> dataList = outBillService.findOutbillsByIf(conditions);
        List outbillViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map outbillView = new HashMap();
            outbillView.put("id",dataList.get(i).getId());
            outbillView.put("time",dataList.get(i).getTime());
            outbillView.put("atmonth",dataList.get(i).getAtmonth());
            outbillView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            outbillView.put("allnumber",dataList.get(i).getAllnumber());
            outbillView.put("allprice",dataList.get(i).getAllprice());
            outbillView.put("remarks",dataList.get(i).getRemarks());
            outbillView.put("addtime",dataList.get(i).getAddtime());

            outbillViews.add(outbillView);
        }
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(outbillViews)
                .build();
    }

    /**
     * 获取订单详情页面数据
     * @return
     */
    @RequestMapping(value = "/getOutbillDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getOutbillDetail(@RequestParam("id") String id){
        Map outbillDate = new HashMap();
        Outbill outbill = outBillService.findOutbillById(id);
        List<Returns> dateilReturnsList = returnsService.findReturnsByBillId(id);
        outbillDate.put("outbill",outbill);
        outbillDate.put("returnsList",dateilReturnsList);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(outbillDate)
                .build();
    }

}
