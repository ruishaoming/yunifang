package com.rui.yunifang.bean;

import java.util.List;

/**
 * Created by 少明 on 2016/12/5.
 */
public class GoodsInfo {

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {

        public GoodsBean goods;
        public boolean collected;
        public boolean reserved;
        public int commentNumber;

        public List<CommentsBean> comments;

        public List<ActivityBean> activity;

        public static class GoodsBean {
            public String id;
            public String goods_name;
            public int stock_number;
            public int collect_count;
            public double market_price;
            public double shop_price;
            public double shipping_fee;
            public String goods_desc;
            public String goods_img;
            public String is_on_sale;
            public double quality;
            public double valueformoney;
            public double desmatch;
            public int sales_volume;
            public boolean reservable;
            public String createuser;
            public String lastupdateuser;
            public int sort;
            public int restriction;
            public int restrict_purchase_num;
            public String is_activity_goods;
            public boolean is_coupon_allowed;
            public int allocated_stock;
            public String efficacy;
            public int is_gift;
            public String goods_source;
            public String redeem_goods_restrict_flag;
            public String is_allow_credit;
            public double commission_scale;
            public String watermarkUrl;
            public String goods_type;

            public List<GalleryBean> gallery;

            public List<AttributesBean> attributes;

            public static class GalleryBean {
                public String id;
                public int sort;
                public String goods_id;
                public String normal_url;
                public String thumb_url;
                public String original_url;
                public boolean enable;
            }

            public static class AttributesBean {
                public String id;
                public String goods_id;
                public String attr_name;
                public String attr_value;
            }
        }

        public static class MatchPriceRuleBean {
            public int restriction;
            public int state;
            public String matchPriceRuleEnable;
        }

        public static class CommentsBean {
            public String id;
            public String createtime;
            public String content;

            public UserBean user;

            public static class UserBean {
                public String id;
                public String nick_name;
                public String icon;
            }
        }

        public static class ActivityBean {
            public String id;
            public String title;
            public String description;
        }
    }
}
