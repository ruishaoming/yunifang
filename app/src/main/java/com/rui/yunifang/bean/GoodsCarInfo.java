package com.rui.yunifang.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 芮靖林
 * on 2016/12/13.
 * 购物车商品的Bean
 */
public class GoodsCarInfo implements Serializable {

    //用用户名作为标识
    private String user_name;
    private String goods_name;
    private String goods_img;
    private String goods_price;
    private String goods_id;
    private int goods_num;
    private boolean goods_pledge;
    private boolean goods_coupons;
    private boolean isChick = false;

    public GoodsCarInfo(boolean goods_coupons, String goods_id, String goods_img, String goods_name, int goods_num, boolean goods_pledge, String goods_price, String user_name, boolean isChick) {
        this.goods_coupons = goods_coupons;
        this.goods_id = goods_id;
        this.goods_img = goods_img;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
        this.goods_pledge = goods_pledge;
        this.goods_price = goods_price;
        this.user_name = user_name;
        this.isChick = isChick;
    }

    public GoodsCarInfo() {

    }



    public boolean isChick() {
        return isChick;
    }

    public void setChick(boolean chick) {
        isChick = chick;
    }

    public boolean isGoods_coupons() {
        return goods_coupons;
    }

    public void setGoods_coupons(boolean goods_coupons) {
        this.goods_coupons = goods_coupons;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public boolean isGoods_pledge() {
        return goods_pledge;
    }

    public void setGoods_pledge(boolean goods_pledge) {
        this.goods_pledge = goods_pledge;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "GoodsCarInfo{" +
                "goods_coupons=" + goods_coupons +
                ", user_name='" + user_name + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_img='" + goods_img + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_num=" + goods_num +
                ", goods_pledge=" + goods_pledge +
                ", isChick=" + isChick +
                '}';
    }
}
