package com.thegun.materialtagview.model;



public class TagModel {
    private String tagText;
    private boolean isFromList;

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public boolean isFromList() {
        return isFromList;
    }

    public void setFromList(boolean fromList) {
        isFromList = fromList;
    }
}
