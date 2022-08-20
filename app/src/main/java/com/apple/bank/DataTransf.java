package com.apple.bank;

public class DataTransf {
    public  String type;
    public  String amount;
    public  String description;
    public  String date;
    public  Boolean estate;
     public DataTransf(){}

    public DataTransf(String type, String amount, String description, String date, Boolean estate) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.estate = estate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getEstate() {
        return estate;
    }

    public void setEstate(Boolean estate) {
        this.estate = estate;
    }
}

