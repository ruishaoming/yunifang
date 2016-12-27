package com.rui.yunifang.bean;

import java.io.Serializable;

/**
 * Created by 芮靖林
 * on 2016/12/26.
 */

public class OrderInfo implements Serializable{

    private String user_name;
    private String order_state;
    private String goods_name;
    private String goods_img;
    private String goods_price;
    private String goods_id;
    private String create_time;
    private int goods_num;
    private int address_id;


    public OrderInfo(String goods_id, String goods_img, String goods_name, int goods_num, String goods_price, String order_state, String user_name, String create_time,int address_id) {
        this.goods_id = goods_id;
        this.goods_img = goods_img;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
        this.goods_price = goods_price;
        this.order_state = order_state;
        this.user_name = user_name;
        this.create_time = create_time;
        this.address_id=address_id;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "create_time='" + create_time + '\'' +
                ", user_name='" + user_name + '\'' +
                ", order_state='" + order_state + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_img='" + goods_img + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_num=" + goods_num +
                '}';
    }

    public OrderInfo() {
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
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

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
