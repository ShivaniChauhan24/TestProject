package com.shipgig.thegun.pos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afroz Ahmad on 11/11/19.
 */
public class PassData {

    @SerializedName("currentPass")
    @Expose
    private String currentPass;


    @SerializedName("newPass")
    @Expose
    private String newPass;


    @SerializedName("confirmPass")
    @Expose
    private String confirmPass;

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }
}
