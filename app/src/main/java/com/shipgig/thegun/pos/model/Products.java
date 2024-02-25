package com.shipgig.thegun.pos.model;

public class Products {

    public String productName;
    public String price;
    public String discount;
    public String amount;
    public String quantity;
    public String imagePath;
    public String totalQty;
    public String hsn_No;
    public int gst_Percent;
    public String product_Id;

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

    public String getHsn_No() {
        return hsn_No;
    }

    public void setHsn_No(String hsn_No) {
        this.hsn_No = hsn_No;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
