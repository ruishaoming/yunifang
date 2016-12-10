package com.rui.yunifang.bean;

import java.util.List;

/**
 * Created by 少明 on 2016/12/3.
 */
public class SortBean {


    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<CategoryBean> category;

        private List<GoodsBriefBean> goodsBrief;

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<GoodsBriefBean> getGoodsBrief() {
            return goodsBrief;
        }

        public void setGoodsBrief(List<GoodsBriefBean> goodsBrief) {
            this.goodsBrief = goodsBrief;
        }

        public static class CategoryBean {
            private String id;
            private String cat_name;
            private String is_leaf;

            private List<ChildrenBean> children;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }

            public String getIs_leaf() {
                return is_leaf;
            }

            public void setIs_leaf(String is_leaf) {
                this.is_leaf = is_leaf;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean {
                private String id;
                private String cat_name;
                private String is_leaf;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCat_name() {
                    return cat_name;
                }

                public void setCat_name(String cat_name) {
                    this.cat_name = cat_name;
                }

                public String getIs_leaf() {
                    return is_leaf;
                }

                public void setIs_leaf(String is_leaf) {
                    this.is_leaf = is_leaf;
                }
            }
        }

        public static class GoodsBriefBean {
            private String id;
            private String goods_name;
            private double shop_price;
            private double market_price;
            private String goods_img;
            private boolean reservable;
            private String efficacy;
            private String watermarkUrl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public double getShop_price() {
                return shop_price;
            }

            public void setShop_price(double shop_price) {
                this.shop_price = shop_price;
            }

            public double getMarket_price() {
                return market_price;
            }

            public void setMarket_price(double market_price) {
                this.market_price = market_price;
            }

            public String getGoods_img() {
                return goods_img;
            }

            public void setGoods_img(String goods_img) {
                this.goods_img = goods_img;
            }

            public boolean isReservable() {
                return reservable;
            }

            public void setReservable(boolean reservable) {
                this.reservable = reservable;
            }

            public String getEfficacy() {
                return efficacy;
            }

            public void setEfficacy(String efficacy) {
                this.efficacy = efficacy;
            }

            public String getWatermarkUrl() {
                return watermarkUrl;
            }

            public void setWatermarkUrl(String watermarkUrl) {
                this.watermarkUrl = watermarkUrl;
            }
        }
    }
}
