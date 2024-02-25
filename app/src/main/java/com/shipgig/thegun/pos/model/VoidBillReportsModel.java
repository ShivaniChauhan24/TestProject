package com.shipgig.thegun.pos.model;

public class VoidBillReportsModel {

    private String srno,disc,gst,cgst,sgst,totalamount,quantity,date,time;

    public VoidBillReportsModel(String srno, String disc, String totalamount, String gst, String sgst, String cgst, String quantity, String date, String time) {
        this.srno = srno;
        this.disc = disc;
        this.gst = gst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.totalamount = totalamount;
        this.quantity = quantity;
        this.date = date;
        this.time = time;
    }


    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
