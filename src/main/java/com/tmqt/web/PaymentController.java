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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tmqt.util.RemoveNullKeyValue.removeNullValue;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MonthCheckService monthCheckService;

    @Autowired
    private FactoryService factoryService;


    /**
     * 增加付款记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addPayment", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addPayment(@RequestBody Payment payment){
        Map conditions = new HashMap();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String monthStr = format.format(payment.getMonth());
        conditions.put("month", monthStr);
        conditions.put("factoryid", payment.getFactoryid());
        List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
        if(!MonthcheckList.isEmpty()){
            Monthcheck monthcheck = MonthcheckList.get(0);
            Float newAmountpaid = monthcheck.getAmountpaid()+payment.getMoney();
            if(newAmountpaid <= monthcheck.getDiscountmoney()) {
                int addRes = paymentService.addPayment(payment);
                if(addRes > 0) {
                    Date monthCheckupdatetime = new Date();
                    monthcheck.setUpdatetime(monthCheckupdatetime);
                    monthcheck.setAmountpaid(newAmountpaid);
                    return monthCheckService.updateMonthcheck(monthcheck);
                }else{
                    return 0;
                }
            }else {
                return -2;//付款金额超出欠款金额
            }
        }else{
            return -1;//条件筛选月账单记录为0
        }

    }

    /**
     * 删除付款记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delPayment", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delPayment(int id){
        Payment payment = paymentService.findPaymentById(id);
        Map conditions = new HashMap();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String monthStr = format.format(payment.getMonth());
        conditions.put("month", monthStr);
        conditions.put("factoryid", payment.getFactoryid());
        List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
        Monthcheck monthcheck = MonthcheckList.get(0);
        Date monthCheckupdatetime = new Date();
        monthcheck.setUpdatetime(monthCheckupdatetime);
        monthcheck.setAmountpaid(monthcheck.getAmountpaid()-payment.getMoney());
        if(paymentService.deletePayment(id) > 0) {
            return monthCheckService.updateMonthcheck(monthcheck);
        }else{
            return 0;
        }
    }


    /**
     * 获取付款记录列表
     * @return
     */
    @RequestMapping(value = "/getPaymentList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getPurchaseList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Payment> dataList = paymentService.findPagePayment(pageNum,pageSize);
        List paymentViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map paymentView = new HashMap();
            paymentView.put("id",dataList.get(i).getId());
            paymentView.put("type",dataList.get(i).getType());
            paymentView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            paymentView.put("month",dataList.get(i).getMonth());
            paymentView.put("money",dataList.get(i).getMoney());
            paymentView.put("remarks",dataList.get(i).getRemarks());
            paymentView.put("paytime",dataList.get(i).getPaytime());
            paymentView.put("addtime",dataList.get(i).getAdddtime());

            paymentViews.add(paymentView);
        }
        int count = paymentService.countPayment();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(paymentViews)
                .build();
    }

    /**
     * 筛选进货记录
     * @return
     */

    @RequestMapping(value = "/findPayment", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findPayment(@RequestParam Map conditions){
        List<Payment> dataList = paymentService.findPaymentsByIf(conditions);
        List paymentViews = new ArrayList<>();
        for( int i = 0 ; i < dataList.size() ; i++) {
            Map paymentView = new HashMap();
            paymentView.put("id",dataList.get(i).getId());
            paymentView.put("type",dataList.get(i).getType());
            paymentView.put("factory",factoryService.selectNameById(dataList.get(i).getFactoryid()));
            paymentView.put("month",dataList.get(i).getMonth());
            paymentView.put("money",dataList.get(i).getMoney());
            paymentView.put("remarks",dataList.get(i).getRemarks());
            paymentView.put("paytime",dataList.get(i).getPaytime());
            paymentView.put("addtime",dataList.get(i).getAdddtime());

            paymentViews.add(paymentView);
        }
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(paymentViews)
                .build();
    }

    /**
     * 计算欠款
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/countNeedPay", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public float countNeedPay(@RequestParam Map conditions){
        removeNullValue(conditions);
        List<Monthcheck> MonthcheckList = monthCheckService.findMonthcheckByIf(conditions);
        float needPay = 0;
        for( int i = 0 ; i < MonthcheckList.size() ; i++) {
            needPay = needPay+ (MonthcheckList.get(i).getAllmoney()-MonthcheckList.get(i).getAmountpaid());
        }
        return needPay;
    }
}



