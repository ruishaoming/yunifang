package com.rui.yunifang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 少明 on 2016/12/13.
 */
public class GoodsSQLiteOpenhelper extends SQLiteOpenHelper {

    //创建数据库，模拟用户信息、购物车、收藏、收货地址
    public GoodsSQLiteOpenhelper(Context context) {
        super(context, "ynf", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //购物车表 ：主键_id 自增，user_name 作为用户标识，其他为商品信息
        db.execSQL("create table goods_car(_id integer primary key autoincrement," +
                "user_name varchar(20),goods_name varchar(50),goods_img varchar(200)," +
                "goods_price varchar(50),goods_id varchar(10),goods_num integer," +
                "goods_pledge varchar(10),goods_coupons varchar(10))");
    }

    //版本更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
