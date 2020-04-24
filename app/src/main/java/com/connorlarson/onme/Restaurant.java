package com.connorlarson.onme;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private static String resName;
    private static String resAddress;
    private static String resId;
    private static String resPhone;
    private static String resHours;
    private static LatLng resLatLong;

    public Restaurant(String resName,String resAddress, String resId, String resPhone, String resHours, LatLng resLatLong ){
        Restaurant.resName = resName;
        Restaurant.resAddress = resAddress;
        Restaurant.resId = resId;
        Restaurant.resPhone = resPhone;
        Restaurant.resHours = resHours;
        Restaurant.resLatLong = resLatLong;
    }

    public static String getResName() {
        return resName;
    }

    public static void setResName(String resName) {
        Restaurant.resName = resName;
    }

    public static String getResAddress() {
        return resAddress;
    }

    public static void setResAddress(String resAddress) {
        Restaurant.resAddress = resAddress;
    }

    public static String getResId() {
        return resId;
    }

    public static void setResId(String resId) {
        Restaurant.resId = resId;
    }

    public static String getResPhone() {
        return resPhone;
    }

    public static void setResPhone(String resPhone) {
        Restaurant.resPhone = resPhone;
    }

    public static String getResHours() {
        return resHours;
    }

    public static void setResHours(String resHours) {
        Restaurant.resHours = resHours;
    }

    public static LatLng getResLatLong() {
        return resLatLong;
    }

    public static void setResLatLong(LatLng resLatLong) {
        Restaurant.resLatLong = resLatLong;
    }
    @NonNull
    @Override
    public String toString() {
        return "Restaurant [resName=" + resName + ", resAddress=" + resAddress
                + "resId=" + resId + "resPhone=" + resPhone + "resHours=" + resHours +
                "resLatLong=" + resLatLong + "]";
    }

}
