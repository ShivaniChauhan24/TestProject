package com.shipgig.thegun.pos.model;

public class CustomerwiseReportsModel {

    private String srno,customerid,name,gender,storeId,totaltransaction,totalsale;

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String store) {
        this.storeId = store;
    }

    public String getTotaltransaction() {
        return totaltransaction;
    }

    public void setTotaltransaction(String totaltransaction) {
        this.totaltransaction = totaltransaction;
    }

    public String getTotalsale() {
        return totalsale;
    }

    public void setTotalsale(String totalsale) {
        this.totalsale = totalsale;
    }
}
