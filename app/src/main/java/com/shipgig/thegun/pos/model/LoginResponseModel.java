package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {

@SerializedName("status")
@Expose
private String status;
@SerializedName("code")
@Expose
private Integer code;
@SerializedName("data")
@Expose
private Data data;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public Integer getCode() {
return code;
}

public void setCode(Integer code) {
this.code = code;
}

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

}