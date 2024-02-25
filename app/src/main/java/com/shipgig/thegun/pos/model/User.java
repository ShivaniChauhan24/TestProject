package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("categoryName")
@Expose
private String categoryName;
@SerializedName("sysUser_ID")
@Expose
private Integer sysUserID;
@SerializedName("store_ID")
@Expose
private int storeID;
@SerializedName("firstName")
@Expose
private String firstName;
@SerializedName("lastName")
@Expose
private String lastName;
@SerializedName("email")
@Expose
private String email;
@SerializedName("userName")
@Expose
private String userName;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("name")
@Expose
private String name;
@SerializedName("code")
@Expose
private String code;
@SerializedName("description")
@Expose
private String description;
@SerializedName("access_id")
@Expose
private Integer accessId;
@SerializedName("store")
@Expose
private String store;
@SerializedName("manager")
@Expose
private String manager;
@SerializedName("sales")
@Expose
private String sales;
@SerializedName("purchase")
@Expose
private String purchase;
@SerializedName("inventory")
@Expose
private String inventory;
@SerializedName("out_of_stock")
@Expose
private String outOfStock;
@SerializedName("inventory_reports")
@Expose
private String inventoryReports;
@SerializedName("transaction_report")
@Expose
private String transactionReport;
@SerializedName("expired_report")
@Expose
private String expiredReport;
@SerializedName("cash_summary")
@Expose
private String cashSummary;
@SerializedName("sales_report")
@Expose
private String salesReport;
@SerializedName("customerwise_report")
@Expose
private String customerwiseReport;
@SerializedName("user_billed_report")
@Expose
private String userBilledReport;
@SerializedName("bill_report")
@Expose
private String billReport;
@SerializedName("void_bill_report")
@Expose
private String voidBillReport;
@SerializedName("returned_item_report")
@Expose
private String returnedItemReport;
@SerializedName("out_of_stock_report")
@Expose
private String outOfStockReport;
@SerializedName("about_out_of_stock_report")
@Expose
private String aboutOutOfStockReport;
@SerializedName("profit_report")
@Expose
private String profitReport;
@SerializedName("returned_cash_report")
@Expose
private String returnedCashReport;
@SerializedName("store_wise_report")
@Expose
private String storeWiseReport;
@SerializedName("all_store_report")
@Expose
private String allStoreReport;
@SerializedName("expense_report")
@Expose
private String expenseReport;
@SerializedName("manager_wise_report")
@Expose
private String managerWiseReport;
@SerializedName("sell_using_card_report")
@Expose
private String sellUsingCardReport;
@SerializedName("gst_report")
@Expose
private String gstReport;
@SerializedName("special_discount_report")
@Expose
private String specialDiscountReport;
@SerializedName("pos_crud")
@Expose
private String posCrud;

public String getCategoryName() {
return categoryName;
}

public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public Integer getSysUserID() {
return sysUserID;
}

public void setSysUserID(Integer sysUserID) {
this.sysUserID = sysUserID;
}

public int getStoreID() {
return storeID;
}

public void setStoreID(int storeID) {
this.storeID = storeID;
}

public String getFirstName() {
return firstName;
}

public void setFirstName(String firstName) {
this.firstName = firstName;
}

public String getLastName() {
return lastName;
}

public void setLastName(String lastName) {
this.lastName = lastName;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public Integer getAccessId() {
return accessId;
}

public void setAccessId(Integer accessId) {
this.accessId = accessId;
}

public String getStore() {
return store;
}

public void setStore(String store) {
this.store = store;
}

public String getManager() {
return manager;
}

public void setManager(String manager) {
this.manager = manager;
}

public String getSales() {
return sales;
}

public void setSales(String sales) {
this.sales = sales;
}

public String getPurchase() {
return purchase;
}

public void setPurchase(String purchase) {
this.purchase = purchase;
}

public String getInventory() {
return inventory;
}

public void setInventory(String inventory) {
this.inventory = inventory;
}

public String getOutOfStock() {
return outOfStock;
}

public void setOutOfStock(String outOfStock) {
this.outOfStock = outOfStock;
}

public String getInventoryReports() {
return inventoryReports;
}

public void setInventoryReports(String inventoryReports) {
this.inventoryReports = inventoryReports;
}

public String getTransactionReport() {
return transactionReport;
}

public void setTransactionReport(String transactionReport) {
this.transactionReport = transactionReport;
}

public String getExpiredReport() {
return expiredReport;
}

public void setExpiredReport(String expiredReport) {
this.expiredReport = expiredReport;
}

public String getCashSummary() {
return cashSummary;
}

public void setCashSummary(String cashSummary) {
this.cashSummary = cashSummary;
}

public String getSalesReport() {
return salesReport;
}

public void setSalesReport(String salesReport) {
this.salesReport = salesReport;
}

public String getCustomerwiseReport() {
return customerwiseReport;
}

public void setCustomerwiseReport(String customerwiseReport) {
this.customerwiseReport = customerwiseReport;
}

public String getUserBilledReport() {
return userBilledReport;
}

public void setUserBilledReport(String userBilledReport) {
this.userBilledReport = userBilledReport;
}

public String getBillReport() {
return billReport;
}

public void setBillReport(String billReport) {
this.billReport = billReport;
}

public String getVoidBillReport() {
return voidBillReport;
}

public void setVoidBillReport(String voidBillReport) {
this.voidBillReport = voidBillReport;
}

public String getReturnedItemReport() {
return returnedItemReport;
}

public void setReturnedItemReport(String returnedItemReport) {
this.returnedItemReport = returnedItemReport;
}

public String getOutOfStockReport() {
return outOfStockReport;
}

public void setOutOfStockReport(String outOfStockReport) {
this.outOfStockReport = outOfStockReport;
}

public String getAboutOutOfStockReport() {
return aboutOutOfStockReport;
}

public void setAboutOutOfStockReport(String aboutOutOfStockReport) {
this.aboutOutOfStockReport = aboutOutOfStockReport;
}

public String getProfitReport() {
return profitReport;
}

public void setProfitReport(String profitReport) {
this.profitReport = profitReport;
}

public String getReturnedCashReport() {
return returnedCashReport;
}

public void setReturnedCashReport(String returnedCashReport) {
this.returnedCashReport = returnedCashReport;
}

public String getStoreWiseReport() {
return storeWiseReport;
}

public void setStoreWiseReport(String storeWiseReport) {
this.storeWiseReport = storeWiseReport;
}

public String getAllStoreReport() {
return allStoreReport;
}

public void setAllStoreReport(String allStoreReport) {
this.allStoreReport = allStoreReport;
}

public String getExpenseReport() {
return expenseReport;
}

public void setExpenseReport(String expenseReport) {
this.expenseReport = expenseReport;
}

public String getManagerWiseReport() {
return managerWiseReport;
}

public void setManagerWiseReport(String managerWiseReport) {
this.managerWiseReport = managerWiseReport;
}

public String getSellUsingCardReport() {
return sellUsingCardReport;
}

public void setSellUsingCardReport(String sellUsingCardReport) {
this.sellUsingCardReport = sellUsingCardReport;
}

public String getGstReport() {
return gstReport;
}

public void setGstReport(String gstReport) {
this.gstReport = gstReport;
}

public String getSpecialDiscountReport() {
return specialDiscountReport;
}

public void setSpecialDiscountReport(String specialDiscountReport) {
this.specialDiscountReport = specialDiscountReport;
}

public String getPosCrud() {
return posCrud;
}

public void setPosCrud(String posCrud) {
this.posCrud = posCrud;
}

}