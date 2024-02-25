package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("result")
@Expose
private String result;
@SerializedName("code")
@Expose
private Integer code;
@SerializedName("token")
@Expose
private String token;
@SerializedName("user")
@Expose
private User user;

public String getResult() {
return result;
}

public void setResult(String result) {
this.result = result;
}

public Integer getCode() {
return code;
}

public void setCode(Integer code) {
this.code = code;
}

public String getToken() {
return token;
}

public void setToken(String token) {
this.token = token;
}

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

}