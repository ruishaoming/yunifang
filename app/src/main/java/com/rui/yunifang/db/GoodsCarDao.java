package com.rui.yunifang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rui.yunifang.bean.GoodsCarInfo;

import java.util.ArrayList;

/**
 * Created by 芮靖林
 * on 2016/12/13.
 * 购物车表的操作类
 */
public class GoodsCarDao {

    private final GoodsSQLiteOpenhelper sqLiteOpenhelper;

    public GoodsCarDao(Context context) {
        sqLiteOpenhelper = new GoodsSQLiteOpenhelper(context);
    }

    //添加数据
    public void add(GoodsCarInfo goodsInfo) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        //在添加之前先进行查询，防止重复的条目
        Cursor cursor = db.rawQuery("select * from goods_car where user_name=? and goods_name=?", new String[]{goodsInfo.getUser_name(), goodsInfo.getGoods_name()});
        if (cursor.moveToNext()) {
            int goods_num = cursor.getInt(cursor.getColumnIndex("goods_num"));
            goods_num = goods_num + goodsInfo.getGoods_num();
            update(goodsInfo.getUser_name(), goodsInfo.getGoods_name(), goods_num);
            return;
        }
        //开启事务添加数据
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("user_name", goodsInfo.getUser_name());
            values.put("goods_name", goodsInfo.getGoods_name());
            values.put("goods_img", goodsInfo.getGoods_img());
            values.put("goods_price", goodsInfo.getGoods_price() + "");
            values.put("goods_id", goodsInfo.getGoods_id());
            values.put("goods_num", goodsInfo.getGoods_num());
            values.put("goods_pledge", goodsInfo.isGoods_pledge() + "");
            values.put("goods_coupons", goodsInfo.isGoods_coupons() + "");
            db.insert("goods_car", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //删除数据
    public void delete(String user_name, String goods_name) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        db.delete("goods_car", "user_name=? and goods_name=?", new String[]{user_name, goods_name});
        db.close();
    }

    //修改数量
    public void update(String user_name, String goods_name, int goods_num) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goods_num", goods_num);
        db.update("goods_car", values, "user_name=? and goods_name=?", new String[]{user_name, goods_name});
        db.close();
    }

    ArrayList<GoodsCarInfo> listGoodsCar = new ArrayList<>();

    public ArrayList<GoodsCarInfo> query(String user_name) {
        SQLiteDatabase db = sqLiteOpenhelper.getReadableDatabase();
        //查询相关用户的所有购物车商品
        Cursor cursor = db.rawQuery("select * from goods_car where user_name=?", new String[]{user_name});
        if (cursor != null) {
            //先清空集合
            listGoodsCar.clear();
            while (cursor.moveToNext()) {
                String goods_name = cursor.getString(cursor.getColumnIndex("goods_name"));
                String goods_img = cursor.getString(cursor.getColumnIndex("goods_img"));
                String goods_price = cursor.getString(cursor.getColumnIndex("goods_price"));
                String goods_id = cursor.getString(cursor.getColumnIndex("goods_id"));
                int goods_num = cursor.getInt(cursor.getColumnIndex("goods_num"));
                String goods_pledge = cursor.getString(cursor.getColumnIndex("goods_pledge"));
                String goods_coupons = cursor.getString(cursor.getColumnIndex("goods_coupons"));
                GoodsCarInfo goodsCarInfo = new GoodsCarInfo(Boolean.parseBoolean(goods_coupons), goods_id, goods_img, goods_name, goods_num, Boolean.parseBoolean(goods_pledge), goods_price, user_name,false);
                listGoodsCar.add(goodsCarInfo);//加入集合
            }
            cursor.close();
        }
        return listGoodsCar;
    }

}
