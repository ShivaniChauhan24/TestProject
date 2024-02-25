package com.shipgig.thegun.pos.inventoryAPI;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("result")
@Expose
private ArrayList<Result> result = null;
@SerializedName("code")
@Expose
private Integer code;

public ArrayList<Result> getResult() {
return result;
}

public void setResult(ArrayList<Result> result) {
this.result = result;
}

public Integer getCode() {
return code;
}

public void setCode(Integer code) {
this.code = code;
}

}