package com.shipgig.thegun.pos.inventoryAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afroz Ahmad on 26/11/19.
 */
public class SendInventoryData {

    @SerializedName("lim")
    @Expose
    private int lim;
    @SerializedName("ski")
    @Expose
    private int ski;

    public Integer getLim() {
        return lim;
    }

    public void setLim(int lim) {
        this.lim = lim;
    }

    public Integer getSki() {
        return ski;
    }

    public void setSki(int ski) {
        this.ski = ski;
    }

}