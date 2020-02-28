package com.tmqt.web;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.tmqt.model.*;
import com.tmqt.service.*;
import com.tmqt.util.ExcelStyleUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by chenw on 2018/12/20.
 */
@Controller
@RequestMapping
public class ExportExcelController {

    @Autowired
    private InBillService inBillService;

    @Autowired
    private OutBillService outBillService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MonthCheckService monthCheckService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ReturnsService returnsService;

    @Autowired
    private FactoryService factoryService;

    /*
    导出进货记录表格
     */
    @RequestMapping(value = "/exportInbillList")
    public void exportInbillList(@RequestParam(value = "time", required = false, defaultValue = "") String time,
                                 @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
                                 @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                 @RequestParam(value = "factoryid", required = false, defaultValue = "") String factoryid,
                                 @RequestParam(value = "page", required = false) String pageNum,
                                 @RequestParam(value = "limit", required = false) String pageSize,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map condition = new HashMap();
        condition.put("time",time);
        condition.put("startTime",startTime);
        condition.put("endTime",endTime);
        condition.put("factoryid",factoryid);
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("进货订单表格", "UTF-8") + ".xls");
        //编码
        response.setCharacterEncoding("UTF-8");
        Workbook workbook;

        List<Inbill> inbillList;
        if(pageSize == "0"){
            inbillList = inBillService.findInbillsByIf(condition);
        }else{
            inbillList = inBillService.findInbillsByIfPage(condition,Integer.valueOf(pageNum),Integer.valueOf(pageSize));
        }
        List<InbillExcelEntity> resList = new ArrayList<>();
        for(int i=0;i<inbillList.size();i++){
            InbillExcelEntity inbillExcelEntity = new InbillExcelEntity();
            inbillExcelEntity.setId(inbillList.get(i).getId());
            inbillExcelEntity.setTime(inbillList.get(i).getTime());
            inbillExcelEntity.setAllprice(inbillList.get(i).getAllprice());
            inbillExcelEntity.setFactory(factoryService.selectNameById(inbillList.get(i).getFactoryid()));
            inbillExcelEntity.setRemarks(inbillList.get(i).getRemarks());
            List<PurchaseExcelEntity> purExcList = purchaseService.findPurExcEntityByBillId(inbillList.get(i).getId());
            inbillExcelEntity.setPurchaseList(purExcList);
            resList.add(inbillExcelEntity);
        }

        //这里的XXX.class是带有easypoi的返回实体
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        workbook = ExcelExportUtil.exportExcel(exportParams, InbillExcelEntity.class, resList);
        workbook.write(response.getOutputStream());
    }

    /*
    导出退货记录表格
     */
    @RequestMapping(value = "/exportOutbillList")
    public void exportOutbillList(@RequestParam(value = "time", required = false, defaultValue = "") String time,
                                  @RequestParam(value = "atmonth", required = false, defaultValue = "") String atmonth,
                                 @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
                                 @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                 @RequestParam(value = "factoryid", required = false, defaultValue = "") String factoryid,
                                 @RequestParam(value = "page", required = false) String pageNum,
                                 @RequestParam(value = "limit", required = false) String pageSize,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map condition = new HashMap();
        condition.put("time",time);
        condition.put("atmonth",atmonth);
        condition.put("startTime",startTime);
        condition.put("endTime",endTime);
        condition.put("factoryid",factoryid);
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("退货订单表格", "UTF-8") + ".xls");
        //编码
        response.setCharacterEncoding("UTF-8");
        Workbook workbook;

        List<Outbill> outbillList;

        if(pageSize == "0"){
            outbillList = outBillService.findOutbillsByIf(condition);
        }else{
            outbillList = outBillService.findOutbillsByIfPage(condition,Integer.valueOf(pageNum),Integer.valueOf(pageSize));
        }

        List<OutbillExcelEntity> resList = new ArrayList<>();
        for(int i=0;i<outbillList.size();i++){
            OutbillExcelEntity outbillExcelEntity = new OutbillExcelEntity();
            outbillExcelEntity.setId(outbillList.get(i).getId());
            outbillExcelEntity.setTime(outbillList.get(i).getTime());
            outbillExcelEntity.setAtmonth(outbillList.get(i).getAtmonth());
            outbillExcelEntity.setAllprice(outbillList.get(i).getAllprice());
            outbillExcelEntity.setFactory(factoryService.selectNameById(outbillList.get(i).getFactoryid()));
            outbillExcelEntity.setRemarks(outbillList.get(i).getRemarks());
            List<ReturnExcelEntity> retExcList = returnsService.findRetExcEntityByBillId(outbillList.get(i).getId());
            outbillExcelEntity.setReturnList(retExcList);
            resList.add(outbillExcelEntity);
        }

        //这里的XXX.class是带有easypoi的返回实体
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        workbook = ExcelExportUtil.exportExcel(exportParams, OutbillExcelEntity.class, resList);
        workbook.write(response.getOutputStream());
    }

    /*
    导出付款记录表格
     */
    @RequestMapping(value = "/exportPaymentList")
    public void exportPaymentList(@RequestParam(value = "month", required = false, defaultValue = "") String month,
                                  @RequestParam(value = "startMonth", required = false, defaultValue = "") String startMonth,
                                  @RequestParam(value = "endMonth", required = false, defaultValue = "") String endMonth,
                                  @RequestParam(value = "startPaytime", required = false, defaultValue = "") String startPaytime,
                                  @RequestParam(value = "endPaytime", required = false, defaultValue = "") String endPaytime,
                                  @RequestParam(value = "factoryid", required = false, defaultValue = "") String factoryid,
                                  @RequestParam(value = "page", required = false) String pageNum,
                                  @RequestParam(value = "limit", required = false) String pageSize,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map condition = new HashMap();
        condition.put("month",month);
        condition.put("startMonth",startMonth);
        condition.put("endMonth",endMonth);
        condition.put("startPaytime",startPaytime);
        condition.put("endPaytime",endPaytime);
        condition.put("factoryid",factoryid);
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("付款记录表格", "UTF-8") + ".xls");
        //编码
        response.setCharacterEncoding("UTF-8");
        Workbook workbook;

        List<Payment> paymentList;
        if(pageSize == "0"){
            paymentList = paymentService.findPaymentsByIf(condition);
        }else{
            paymentList = paymentService.findPaymentsByIfPage(condition,Integer.valueOf(pageNum),Integer.valueOf(pageSize));
        }
        List<PaymentExcelEntity> resList = new ArrayList<>();
        for(int i=0;i<paymentList.size();i++){
            PaymentExcelEntity paymentExcelEntity = new PaymentExcelEntity();
            paymentExcelEntity.setId(paymentList.get(i).getId());
            paymentExcelEntity.setMonth(paymentList.get(i).getMonth());
            paymentExcelEntity.setMoney(paymentList.get(i).getMoney());
            paymentExcelEntity.setFactory(factoryService.selectNameById(paymentList.get(i).getFactoryid()));
            paymentExcelEntity.setRemarks(paymentList.get(i).getRemarks());
            paymentExcelEntity.setPaytime(paymentList.get(i).getPaytime());
            paymentExcelEntity.setAdddtime(paymentList.get(i).getAdddtime());
            resList.add(paymentExcelEntity);
        }

        //这里的XXX.class是带有easypoi的返回实体
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        workbook = ExcelExportUtil.exportExcel(exportParams, PaymentExcelEntity.class, resList);
        workbook.write(response.getOutputStream());
    }

    /*
导出月账单表格
 */
    @RequestMapping(value = "/exportMonthCheckList")
    public void exportMonthCheckList(@RequestParam(value = "month", required = false, defaultValue = "") String month,
                                  @RequestParam(value = "startMonth", required = false, defaultValue = "") String startMonth,
                                  @RequestParam(value = "endMonth", required = false, defaultValue = "") String endMonth,
                                  @RequestParam(value = "factoryid", required = false, defaultValue = "") String factoryid,
                                  @RequestParam(value = "page", required = false) String pageNum,
                                  @RequestParam(value = "limit", required = false) String pageSize,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map condition = new HashMap();
        condition.put("month",month);
        condition.put("startMonth",startMonth);
        condition.put("endMonth",endMonth);
        condition.put("factoryid",factoryid);
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("欠款记录表格", "UTF-8") + ".xls");
        //编码
        response.setCharacterEncoding("UTF-8");
        Workbook workbook;

        List<Monthcheck> monthchecksList;
        if(pageSize == "0"){
            monthchecksList = monthCheckService.findMonthcheckByIf(condition);
        }else{
            monthchecksList = monthCheckService.findMonthcheckByIfPage(condition,Integer.valueOf(pageNum),Integer.valueOf(pageSize));
        }
        List<MonthcheckExcelEntity> resList = new ArrayList<>();
        Float allAllmoney = 0.0f;
        Float allDiscountmoney = 0.0f;
        Float allAmountpaid = 0.0f;
        Float allDebtmoney = 0.0f;
        for(int i=0;i<monthchecksList.size();i++){
            MonthcheckExcelEntity monthcheckExcelEntity = new MonthcheckExcelEntity();
            monthcheckExcelEntity.setId(monthchecksList.get(i).getId().toString());
            monthcheckExcelEntity.setFactory(factoryService.selectNameById(monthchecksList.get(i).getFactoryid()));
            monthcheckExcelEntity.setMonth(monthchecksList.get(i).getMonth());
            Float allmoney = monthchecksList.get(i).getAllmoney();
            monthcheckExcelEntity.setAllmoney(allmoney);
            Float discountmoney = monthchecksList.get(i).getDiscountmoney();
            Float amountpaid = monthchecksList.get(i).getAmountpaid();
            Float debtmoney = discountmoney-amountpaid;
            monthcheckExcelEntity.setDiscountmoney(discountmoney);
            monthcheckExcelEntity.setAmountpaid(amountpaid);
            monthcheckExcelEntity.setDebtmoney(debtmoney);
            resList.add(monthcheckExcelEntity);
            allAllmoney += allmoney;
            allDiscountmoney += discountmoney;
            allAmountpaid += amountpaid;
            allDebtmoney += debtmoney;
        }

        //汇总项
        MonthcheckExcelEntity huizong = new MonthcheckExcelEntity();
        huizong.setId("汇总");
        huizong.setAllmoney(allAllmoney);
        huizong.setDiscountmoney(allDiscountmoney);
        huizong.setAmountpaid(allAmountpaid);
        huizong.setDebtmoney(allDebtmoney);
        resList.add(huizong);

        //这里的XXX.class是带有easypoi的返回实体
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        workbook = ExcelExportUtil.exportExcel(exportParams, MonthcheckExcelEntity.class, resList);
        workbook.write(response.getOutputStream());
    }

}
