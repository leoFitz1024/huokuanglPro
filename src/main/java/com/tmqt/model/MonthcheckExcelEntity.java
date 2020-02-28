package com.tmqt.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;

/**
 * Created by chenw on 2019/1/4.
 */
@ExcelTarget("monthcheckExcelEntity")
public class MonthcheckExcelEntity {

    /*主键*/
    @Excel(name = "ID", width = 25,type=1)
    private String id;

    /*厂家*/
    @Excel(name = "厂家", width = 25,type=1)
    private String factory;

    /*月份*/
    @Excel(name = "月份",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM", width = 25,type=1)
    private Date month;

    /*总金额*/
    @Excel(name = "总金额", width = 25,type=10)
    private Float allmoney;

    /*优惠后金额*/
    @Excel(name = "优惠后金额", width = 25,type=10)
    private Float discountmoney;

    /*已付金额*/
    @Excel(name = "已付金额", width = 25,type=10)
    private Float amountpaid;

    /*还欠款*/
    @Excel(name = "还欠款", width = 25,type=10)
    private Float debtmoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Float getAllmoney() {
        return allmoney;
    }

    public void setAllmoney(Float allmoney) {
        this.allmoney = allmoney;
    }

    public Float getDiscountmoney() {
        return discountmoney;
    }

    public void setDiscountmoney(Float discountmoney) {
        this.discountmoney = discountmoney;
    }

    public Float getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(Float amountpaid) {
        this.amountpaid = amountpaid;
    }

    public Float getDebtmoney() {
        return debtmoney;
    }

    public void setDebtmoney(Float debtmoney) {
        this.debtmoney = debtmoney;
    }

    public MonthcheckExcelEntity() {};

    public MonthcheckExcelEntity(String id, String factory, Date month, Float allmoney, Float discountmoney, Float amountpaid, Float debtmoney) {
        this.id = id;
        this.factory = factory;
        this.month = month;
        this.allmoney = allmoney;
        this.discountmoney = discountmoney;
        this.amountpaid = amountpaid;
        this.debtmoney = debtmoney;
    }
}
