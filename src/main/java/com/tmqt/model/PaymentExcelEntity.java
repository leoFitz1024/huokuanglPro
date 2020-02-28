package com.tmqt.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;

/**
 * Created by chenw on 2019/1/4.
 */
@ExcelTarget("paymentExcelEntity")
public class PaymentExcelEntity {

    /*主键*/
    @Excel(name = "ID", width = 25,type=10)
    private Integer id;

    /*结算厂家*/
    @Excel(name = "厂家", width = 25,needMerge=true,type=1)
    private String factory;

    /*结算月份*/
    @Excel(name = "结算月份",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM", width = 25,needMerge=true,type=1)
    private Date month;

    /*付款金额*/
    @Excel(name = "金额", width = 25,needMerge=true,type=10)
    private Float money;

    /*付款时间*/
    @Excel(name = "付款时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", width = 25,needMerge=true,type=1)
    private Date paytime;

    /*备注*/
    @Excel(name = "备注", width = 25,needMerge=true,type=1)
    private String remarks;

    /*记录时间*/
    @Excel(name = "记录时间",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", width = 25,needMerge=true,type=1)
    private Date adddtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factoryid) {
        this.factory = factoryid;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getAdddtime() {
        return adddtime;
    }

    public void setAdddtime(Date adddtime) {
        this.adddtime = adddtime;
    }

    public PaymentExcelEntity() {};

    public PaymentExcelEntity(Integer id, String factory, Date month, Float money, Date paytime, String remarks, Date adddtime) {
        this.id = id;
        this.factory = factory;
        this.month = month;
        this.money = money;
        this.paytime = paytime;
        this.remarks = remarks;
        this.adddtime = adddtime;
    }
}
