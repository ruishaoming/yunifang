package com.rui.yunifang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rui.yunifang.bean.AddressInfo;
import com.rui.yunifang.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by 芮靖林
 * on 2016/12/16.
 */
public class AddressDao {
    private final GoodsSQLiteOpenhelper sqLiteOpenhelper;

    //        SoftReference
    public AddressDao(Context context) {
        sqLiteOpenhelper = new GoodsSQLiteOpenhelper(context);
    }

    //添加收货地址
    public void add(AddressInfo addressInfo) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", addressInfo.getUser_name());
        values.put("ad_name", addressInfo.getAd_name());
        values.put("ad_number", addressInfo.getAd_number());
        values.put("ad_city", addressInfo.getAd_city());
        values.put("ad_district", addressInfo.getAd_district());
        db.insert("address", null, values);
        db.close();
    }

    //删除
    public void delete(AddressInfo addressInfo) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        db.delete("address", "user_name=? and ad_name=? and ad_district=?", new String[]{addressInfo.getUser_name(), addressInfo.getAd_name(), addressInfo.getAd_district()});
        db.close();
    }

    //查询
    public AddressInfo query(String uname, String _id) {
        AddressInfo addressInfo = null;
        SQLiteDatabase db = sqLiteOpenhelper.getReadableDatabase();
        Cursor rawQuery = db.rawQuery("select * from address where user_name=? and _id=?", new String[]{uname, _id});
        if (rawQuery.moveToNext()) {
            int id = rawQuery.getInt(rawQuery.getColumnIndex("_id"));
            String user_name = rawQuery.getString(rawQuery.getColumnIndex("user_name"));
            String ad_name = rawQuery.getString(rawQuery.getColumnIndex("ad_name"));
            String ad_number = rawQuery.getString(rawQuery.getColumnIndex("ad_number"));
            String ad_city = rawQuery.getString(rawQuery.getColumnIndex("ad_city"));
            String ad_district = rawQuery.getString(rawQuery.getColumnIndex("ad_district"));
            addressInfo = new AddressInfo(ad_city, ad_district, ad_name, ad_number, user_name, id, false);
        }
        db.close();
        return addressInfo;
    }

    //修改
    public void update() {

    }

    ArrayList<AddressInfo> listAddress = new ArrayList<>();

    //查询所有的收货地址
    public ArrayList<AddressInfo> queryAll(String uname) {
        AddressInfo addressInfo = null;
        SQLiteDatabase db = sqLiteOpenhelper.getReadableDatabase();
        Cursor rawQuery = db.rawQuery("select * from address where user_name=?", new String[]{uname});
        listAddress.clear();
        while (rawQuery.moveToNext()) {
            int id = rawQuery.getInt(rawQuery.getColumnIndex("_id"));
            String user_name = rawQuery.getString(rawQuery.getColumnIndex("user_name"));
            String ad_name = rawQuery.getString(rawQuery.getColumnIndex("ad_name"));
            String ad_number = rawQuery.getString(rawQuery.getColumnIndex("ad_number"));
            String ad_city = rawQuery.getString(rawQuery.getColumnIndex("ad_city"));
            String ad_district = rawQuery.getString(rawQuery.getColumnIndex("ad_district"));
            addressInfo = new AddressInfo(ad_city, ad_district, ad_name, ad_number, user_name, id, false);
            listAddress.add(addressInfo);
        }
        db.close();
        return listAddress;
    }
}
