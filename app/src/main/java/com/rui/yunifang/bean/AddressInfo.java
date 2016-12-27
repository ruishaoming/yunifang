package com.rui.yunifang.bean;

/**
 * Created by 芮靖林
 * on 2016/12/16.
 */
public class AddressInfo {
    private String user_name;
    private String ad_name;
    private String ad_number;
    private String ad_city;
    private String ad_district;
    private boolean isCheck = false;
    private int _id;

    public AddressInfo(String ad_city, String ad_district, String ad_name, String ad_number, String user_name, int _id, boolean isCheck) {
        this._id = _id;
        this.ad_city = ad_city;
        this.ad_district = ad_district;
        this.ad_name = ad_name;
        this.ad_number = ad_number;
        this.isCheck = isCheck;
        this.user_name = user_name;
    }

    public AddressInfo(String ad_city, String ad_district, String ad_name, String ad_number, String user_name) {
        this.ad_city = ad_city;
        this.ad_district = ad_district;
        this.ad_name = ad_name;
        this.ad_number = ad_number;
        this.user_name = user_name;
    }

    public AddressInfo() {
    }

    public String getAd_city() {
        return ad_city;
    }

    public void setAd_city(String ad_city) {
        this.ad_city = ad_city;
    }

    public String getAd_district() {
        return ad_district;
    }

    public void setAd_district(String ad_district) {
        this.ad_district = ad_district;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_number() {
        return ad_number;
    }

    public void setAd_number(String ad_number) {
        this.ad_number = ad_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "_id=" + _id +
                ", user_name='" + user_name + '\'' +
                ", ad_name='" + ad_name + '\'' +
                ", ad_number='" + ad_number + '\'' +
                ", ad_city='" + ad_city + '\'' +
                ", ad_district='" + ad_district + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
