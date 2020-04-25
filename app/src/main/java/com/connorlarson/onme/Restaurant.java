package com.connorlarson.onme;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    private String resName;
    private String resAddress;
    private String resId;
    private String resPhone;
    private String resHours;
    private LatLng resLatLong;

    public Restaurant(String resName,String resAddress, String resId, String resPhone, String resHours, LatLng resLatLong ){
        this.resName = resName;
        this.resAddress = resAddress;
        this.resId = resId;
        this.resPhone = resPhone;
        this.resHours = resHours;
        this.resLatLong = resLatLong;
    }


    public LatLng getResLatLong() {
        return resLatLong;
    }

    public void setResLatLong(LatLng resLatLong) {
        this.resLatLong = resLatLong;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResPhone() {
        return resPhone;
    }

    public void setResPhone(String resPhone) {
        this.resPhone = resPhone;
    }

    public String getResHours() {
        return resHours;
    }

    public void setResHours(String resHours) {
        this.resHours = resHours;
    }
    @NonNull
    @Override
    public String toString() {
        return "Restaurant [resName=" + resName + ", resAddress=" + resAddress
                + "resId=" + resId + "resPhone=" + resPhone + "resHours=" + resHours +
                "resLatLong=" + resLatLong + "]";
    }
}
