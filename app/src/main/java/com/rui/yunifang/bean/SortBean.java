package com.rui.yunifang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 少明 on 2016/12/3.
 */
public class SortBean {

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable{

        public List<CategoryBean> category;

        public List<GoodsBriefBean> goodsBrief;

        public static class CategoryBean  implements Serializable{
            public String cat_name;
            public String id;
            public String is_leaf;

            public List<ChildrenBean> children;

            public static class ChildrenBean  implements Serializable{
                public String cat_name;
                public String id;
                public String is_leaf;
            }
        }

        public static class GoodsBriefBean implements Serializable{
            public String efficacy;
            public String goods_img;
            public String goods_name;
            public String id;
            public double market_price;
            public boolean reservable;
            public double shop_price;
            public String watermarkUrl;
        }
    }
}
