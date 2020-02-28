package com.tmqt.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * Created by chenw on 2018/12/20.
 */
@ExcelTarget("purchaseExcelEntity")
public class PurchaseExcelEntity {

    /*主键*/
    private int id;

    /*货号*/
    @Excel(name = "货号", width = 25,type=1)
    private String code;

    /*码段*/
    @Excel(name = "码段", width = 25,type=1)
    private String rangesize;

    /*颜色*/
    @Excel(name = "颜色", width = 25,type=1)
    private String color;

    /*价格*/
    @Excel(name = "价格", width = 25,type=10)
    private Float price;

    /*数量*/
    @Excel(name = "数量", width = 25,type=10)
    private int number;

    /*总金额*/
    @Excel(name = "总金额", width = 25,type=10)
    private Float allprice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRangesize() {
        return rangesize;
    }

    public void setRangesize(String rangesize) {
        this.rangesize = rangesize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Float getAllprice() {
        return allprice;
    }

    public void setAllprice(Float allprice) {
        this.allprice = allprice;
    }

    public PurchaseExcelEntity(int id, String code, String rangesize, String color, Float price, int number, Float allprice) {
        this.id = id;
        this.code = code;
        this.rangesize = rangesize;
        this.color = color;
        this.price = price;
        this.number = number;
        this.allprice = allprice;
    }
}
