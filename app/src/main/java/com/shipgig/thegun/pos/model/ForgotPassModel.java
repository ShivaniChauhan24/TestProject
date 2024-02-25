package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassModel {

@SerializedName("msg")
@Expose
private String msg;

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

}