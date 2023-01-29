package com.example.messbook.Model;

public class CostModel {
    private String name;
    private String date;
    private String amount;
    private String desc;

    public CostModel(){}

    public CostModel(String name,String date, String  amount, String desc) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
