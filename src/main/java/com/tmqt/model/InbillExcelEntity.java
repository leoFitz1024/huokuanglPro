package com.tmqt.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;
import java.util.List;

/**
 * Created by chenw on 2018/12/20.
 */
@ExcelTarget("inbillExcelEntity")
public class InbillExcelEntity {

    /*主键*/
    @Excel(name = "ID", width = 25,type=1)
    private String id;

    /*进货日期*/
    @Excel(name = "进货日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd",width = 30,needMerge=true,type=1)
    private Date time;

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
    @ExcelCollection(name = "进货记录")
    private List<PurchaseExcelEntity> purchaseList;

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

    public List<PurchaseExcelEntity> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<PurchaseExcelEntity> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public InbillExcelEntity() {}

    public InbillExcelEntity(String id, Date time, String factory, Float allprice, String remarks, List<PurchaseExcelEntity> purchaseList) {
        this.id = id;
        this.time = time;
        this.factory = factory;
        this.allprice = allprice;
        this.remarks = remarks;
        this.purchaseList = purchaseList;
    }
}
