package com.connorlarson.onme.ui.profile;

public class FavBar {
    private String fBarName;
    private String fBarAddress;
    private String fBarId;
    private String fBarResId;


    public FavBar(String fBarName, String fBarAddress, String fBarId, String fBarResId) {
        this.fBarName = fBarName;
        this.fBarAddress = fBarAddress;
        this.fBarId = fBarId;
        this.fBarResId = fBarResId;
    }

    public String getfBarName() {
        return fBarName;
    }

    public void setfBarName(String fBarName) {
        this.fBarName = fBarName;
    }

    public String getfBarAddress() {
        return fBarAddress;
    }

    public void setfBarAddress(String fBarAddress) {
        this.fBarAddress = fBarAddress;
    }

    public String getfBarId() {
        return fBarId;
    }

    public void setfBarId(String fBarId) {
        this.fBarId = fBarId;
    }

    public String getfBarResId() {
        return fBarResId;
    }

    public void setfBarResId(String fBarResId) {
        this.fBarResId = fBarResId;
    }

}
