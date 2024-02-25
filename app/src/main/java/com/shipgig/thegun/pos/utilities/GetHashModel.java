package com.shipgig.thegun.pos.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetHashModel {

@SerializedName("key")
@Expose
private String key;

@SerializedName("txnid")
@Expose
private String txnid;

@SerializedName("amount")
@Expose
private String amount;

@SerializedName("productinfo")
@Expose
private String productinfo;

@SerializedName("firstname")
@Expose
private String firstname;

@SerializedName("email")
@Expose
private String email;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}