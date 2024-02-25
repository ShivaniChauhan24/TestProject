package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afroz Ahmad on 11/11/19.
 */
public class FogetPasswordData {

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("passData")
    @Expose
    private PassData passData;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PassData getPassData() {
        return passData;
    }

    public void setPassData(PassData passData) {
        this.passData = passData;
    }
}
