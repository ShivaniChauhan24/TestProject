package com.shipgig.thegun.pos.inventoryAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

@SerializedName("product_ID")
@Expose
private String productID;
@SerializedName("store_ID")
@Expose
private Integer storeID;
@SerializedName("sysUser_ID")
@Expose
private Integer sysUserID;
@SerializedName("barcodeReader_ID")
@Expose
private Integer barcodeReaderID;
@SerializedName("UOM_ID")
@Expose
private Integer uOMID;
@SerializedName("productName")
@Expose
private String productName;
@SerializedName("brandName")
@Expose
private Object brandName;
@SerializedName("description")
@Expose
private Object description;
@SerializedName("constrains")
@Expose
private Object constrains;
@SerializedName("unitPrice")
@Expose
private Integer unitPrice;
@SerializedName("category")
@Expose
private String category;
@SerializedName("gst_ID")
@Expose
private Integer gstID;
@SerializedName("supplier_ID")
@Expose
private Integer supplierID;
@SerializedName("discount")
@Expose
private Integer discount;
@SerializedName("createdOn")
@Expose
private String createdOn;
@SerializedName("modifiedOn")
@Expose
private String modifiedOn;
@SerializedName("deletedOn")
@Expose
private String deletedOn;
@SerializedName("isDeleted")
@Expose
private Integer isDeleted;
@SerializedName("deletedBy")
@Expose
private Integer deletedBy;
@SerializedName("HSN_ID")
@Expose
private String hSNID;
@SerializedName("happyHours")
@Expose
private Integer happyHours;
@SerializedName("expiryDate")
@Expose
private String expiryDate;
@SerializedName("deleteReason")
@Expose
private Object deleteReason;
@SerializedName("costPrice")
@Expose
private Integer costPrice;
@SerializedName("totalstock")
@Expose
private int totalstock;

public String getProductID() {
return productID;
}

public void setProductID(String productID) {
this.productID = productID;
}

public Integer getStoreID() {
return storeID;
}

public void setStoreID(Integer storeID) {
this.storeID = storeID;
}

public Integer getSysUserID() {
return sysUserID;
}

public void setSysUserID(Integer sysUserID) {
this.sysUserID = sysUserID;
}

public Integer getBarcodeReaderID() {
return barcodeReaderID;
}

public void setBarcodeReaderID(Integer barcodeReaderID) {
this.barcodeReaderID = barcodeReaderID;
}

public Integer getUOMID() {
return uOMID;
}

public void setUOMID(Integer uOMID) {
this.uOMID = uOMID;
}

public String getProductName() {
return productName;
}

public void setProductName(String productName) {
this.productName = productName;
}

public Object getBrandName() {
return brandName;
}

public void setBrandName(Object brandName) {
this.brandName = brandName;
}

public Object getDescription() {
return description;
}

public void setDescription(Object description) {
this.description = description;
}

public Object getConstrains() {
return constrains;
}

public void setConstrains(Object constrains) {
this.constrains = constrains;
}

public Integer getUnitPrice() {
return unitPrice;
}

public void setUnitPrice(Integer unitPrice) {
this.unitPrice = unitPrice;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public Integer getGstID() {
return gstID;
}

public void setGstID(Integer gstID) {
this.gstID = gstID;
}

public Integer getSupplierID() {
return supplierID;
}

public void setSupplierID(Integer supplierID) {
this.supplierID = supplierID;
}

public Integer getDiscount() {
return discount;
}

public void setDiscount(Integer discount) {
this.discount = discount;
}

public String getCreatedOn() {
return createdOn;
}

public void setCreatedOn(String createdOn) {
this.createdOn = createdOn;
}

public String getModifiedOn() {
return modifiedOn;
}

public void setModifiedOn(String modifiedOn) {
this.modifiedOn = modifiedOn;
}

public String getDeletedOn() {
return deletedOn;
}

public void setDeletedOn(String deletedOn) {
this.deletedOn = deletedOn;
}

public Integer getIsDeleted() {
return isDeleted;
}

public void setIsDeleted(Integer isDeleted) {
this.isDeleted = isDeleted;
}

public Integer getDeletedBy() {
return deletedBy;
}

public void setDeletedBy(Integer deletedBy) {
this.deletedBy = deletedBy;
}

public String getHSNID() {
return hSNID;
}

public void setHSNID(String hSNID) {
this.hSNID = hSNID;
}

public Integer getHappyHours() {
return happyHours;
}

public void setHappyHours(Integer happyHours) {
this.happyHours = happyHours;
}

public String getExpiryDate() {
return expiryDate;
}

public void setExpiryDate(String expiryDate) {
this.expiryDate = expiryDate;
}

public Object getDeleteReason() {
return deleteReason;
}

public void setDeleteReason(Object deleteReason) {
this.deleteReason = deleteReason;
}

public Integer getCostPrice() {
return costPrice;
}

public void setCostPrice(Integer costPrice) {
this.costPrice = costPrice;
}

public int getTotalstock() {
return totalstock;
}

public void setTotalstock(int totalstock) {
this.totalstock = totalstock;
}

}