package com.shipgig.thegun.pos.model;

/**
 * Created by Afroz on 12/4/19.
 */
public class AddItemCartModel {
    String productName;
    String productPrice;
    String quantity;
    String srNo;
    String amount;
    String hsn_Number;
    String totalQty;
    int gst_Percent;
    String product_Id;
    String discount;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.product_Id = product_Id;
    }

    public int getGst_Percent() {
        return gst_Percent;
    }

    public void setGst_Percent(int gst_Percent) {
        this.gst_Percent = gst_Percent;
    }

    public String getHsn_Number() {
        return hsn_Number;
    }

    public void setHsn_Number(String hsn_Number) {
        this.hsn_Number = hsn_Number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }
}
