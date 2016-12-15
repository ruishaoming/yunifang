package com.rui.yunifang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 少明 on 2016/11/29.
 */
public class HomeData implements Serializable{

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean implements Serializable{

        public ActivityInfoBean activityInfo;
        public boolean creditRecived;


        public List<SubjectsBean> subjects;


        public List<BestSellersBean> bestSellers;

        public List<Ad1Bean> ad1;

        public List<Ad5Bean> ad5;

        public List<DefaultGoodsListBean> defaultGoodsList;

        public static class ActivityInfoBean {
            public String activityAreaDisplay;

            public List<ActivityInfoListBean> activityInfoList;

            public static class ActivityInfoListBean implements Serializable{
                public String id;
                public String activityImg;
                public String activityType;
                public String activityData;
                public String activityDataDetail;
                public String startSeconds;
                public String endSeconds;
                public String activityStatus;
                public String activityAreaDisplay;
                public String countDownEnable;
                public String starttime;
                public String endtime;
                public int sort;
            }
        }

        public static class SubjectsBean implements Serializable{
            public String id;
            public String title;
            public String detail;
            public String image;
            public String start_time;
            public String end_time;
            public int show_number;
            public String state;
            public int sort;

            public List<GoodsListBean> goodsList;

            public static class GoodsListBean implements Serializable{
                public String id;
                public String goods_name;
                public double shop_price;
                public double market_price;
                public String goods_img;
                public boolean reservable;
                public String efficacy;
            }
        }

        public static class BestSellersBean implements Serializable{
            public String id;
            public String name;
            public String descript;
            public String state;
            public int show_number;
            public String begin_date;
            public String end_date;


            public List<GoodsListBean> goodsList;

            public static class GoodsListBean implements Serializable{
                public String id;
                public String goods_name;
                public double shop_price;
                public double market_price;
                public String goods_img;
                public boolean reservable;
                public String efficacy;
            }
        }

        public static class Ad1Bean implements Serializable{
            public String id;
            public String image;
            public int ad_type;
            public int sort;
            public int position;
            public int enabled;
            public String createtime;
            public String createuser;
            public String ad_type_dynamic;
            public String ad_type_dynamic_data;
            public String show_channel;
            public String channelType;
        }

        public static class Ad5Bean implements Serializable{
            public String id;
            public String image;
            public int ad_type;
            public int sort;
            public int position;
            public int enabled;
            public String ad_type_dynamic;
            public String ad_type_dynamic_data;
            public String ad_type_dynamic_detail;
            public String show_channel;
            public String title;
        }

        public static class DefaultGoodsListBean implements Serializable{
            public String id;
            public String goods_name;
            public double shop_price;
            public double market_price;
            public String goods_img;
            public boolean reservable;
            public String efficacy;
        }
    }
}
