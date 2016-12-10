package com.rui.yunifang.bean;

import java.util.List;

/**
 * Created by 少明 on 2016/12/9.
 */
public class MaskInfo {

    public int code;
    public String msg;

    public List<DataBean> data;

    public static class DataBean {
        public String efficacy;
        public String goods_img;
        public String goods_name;
        public String id;
        public boolean is_allow_credit;
        public boolean is_coupon_allowed;
        public double market_price;
        public int sales_volume;
        public double shop_price;
        public int sort;
        public String watermarkUrl;
    }
}
