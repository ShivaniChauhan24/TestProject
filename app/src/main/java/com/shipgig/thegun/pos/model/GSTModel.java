package com.shipgig.thegun.pos.model;

/**
 * Created by Afroz Ahmad on 11/10/19.
 */
public class GSTModel {

    private int gst_Id,gst_Cat_ID;
    private String item,item_Desc,schedule_no,hsn_no,igst,cgst,sgst;

    public int getGst_Id() {
        return gst_Id;
    }

    public void setGst_Id(int gst_Id) {
        this.gst_Id = gst_Id;
    }

    public int getGst_Cat_ID() {
        return gst_Cat_ID;
    }

    public void setGst_Cat_ID(int gst_Cat_ID) {
        this.gst_Cat_ID = gst_Cat_ID;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem_Desc() {
        return item_Desc;
    }

    public void setItem_Desc(String item_Desc) {
        this.item_Desc = item_Desc;
    }

    public String getSchedule_no() {
        return schedule_no;
    }

    public void setSchedule_no(String schedule_no) {
        this.schedule_no = schedule_no;
    }

    public String getHsn_no() {
        return hsn_no;
    }

    public void setHsn_no(String hsn_no) {
        this.hsn_no = hsn_no;
    }
}
