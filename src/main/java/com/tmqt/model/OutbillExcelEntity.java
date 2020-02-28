package com.tmqt.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;
import java.util.List;

/**
 * Created by chenw on 2018/12/20.
 */
@ExcelTarget("outbillExcelEntity")
public class OutbillExcelEntity {

    /*主键*/
    @Excel(name = "ID", width = 25,type=1)
    private String id;

    /*进货日期*/
    @Excel(name = "退货日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd",width = 30,needMerge=true,type=1)
    private Date time;

    /*结算月份*/
    @Excel(name = "结算月份", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM",width = 30,needMerge=true,type=1)
    private Date atmonth;

    /*进货厂家*/
    @Excel(name = "厂家", width = 25,needMerge=true,type=1)
    private String factory;

    /*总金额*/
    @Excel(name = "总金额", width = 25,needMerge=true,type=10)
    private Float allprice;

    /*备注*/
    @Excel(name = "备注", width = 35,needMerge=true,type=1)
    private String remarks;

    /*进货记录集合*/
    @ExcelCollection(name = "退货记录")
    private List<ReturnExcelEntity> returnList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getAtmonth() {
        return atmonth;
    }

    public void setAtmonth(Date atmonth) {
        this.atmonth = atmonth;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public Float getAllprice() {
        return allprice;
    }

    public void setAllprice(Float allprice) {
        this.allprice = allprice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ReturnExcelEntity> getReturnList() {
        return returnList;
    }

    public void setReturnList(List<ReturnExcelEntity> returnList) {
        this.returnList = returnList;
    }

    public OutbillExcelEntity(){};

    public OutbillExcelEntity(String id, Date time, Date atmonth, String factory, Float allprice, String remarks, List<ReturnExcelEntity> returnList) {
        this.id = id;
        this.time = time;
        this.atmonth = atmonth;
        this.factory = factory;
        this.allprice = allprice;
        this.remarks = remarks;
        this.returnList = returnList;
    }
}
