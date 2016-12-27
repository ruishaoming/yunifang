package com.rui.yunifang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rui.yunifang.bean.OrderInfo;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${芮靖林}
 * on 2016/12/26 13:29.
 */

public class OrderDao {

    //定义状态类型
    public static final String ORDER_ALL = "全部";
    public static final String WAIT_PAY = "待付款";
    public static final String WAIT_SEND = "待发货";
    public static final String WAIT_RECEIVE = "待收货";
    public static final String WAIT_EVALUATE = "待评价";

    private final GoodsSQLiteOpenhelper sqLiteOpenhelper;

    public OrderDao(Context context) {
        sqLiteOpenhelper = new GoodsSQLiteOpenhelper(context);
    }

    //添加
    public void add(ArrayList<OrderInfo> listOrder) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        long millis = System.currentTimeMillis();//获取系统当前时间
        try {
            for (int i = 0; i < listOrder.size(); i++) {
                values.put("user_name", listOrder.get(i).getUser_name());
                values.put("order_state", listOrder.get(i).getOrder_state());
                values.put("goods_name", listOrder.get(i).getGoods_name());
                values.put("goods_img", listOrder.get(i).getGoods_img());
                values.put("goods_price", listOrder.get(i).getGoods_price());
                values.put("goods_id", listOrder.get(i).getGoods_id());
                values.put("goods_num", listOrder.get(i).getGoods_num());
                values.put("create_time", millis + "");//保存当前的时间
                values.put("address_id", listOrder.get(i).getAddress_id());//保存当前的时间
                LogUtils.i("TAG","存id-----------------"+ listOrder.get(i).getAddress_id());
                db.insert("goods_order", null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    //查询方法
    ArrayList<ArrayList<OrderInfo>> listAll = new ArrayList<ArrayList<OrderInfo>>();
    private String currentTime = "0";

    public ArrayList<ArrayList<OrderInfo>> queryAll(String userName, String stateType) {
        SQLiteDatabase db = sqLiteOpenhelper.getReadableDatabase();
        listAll.clear();
        Cursor cursor;
        //查询出相应的列表
        if (stateType.equals(OrderDao.ORDER_ALL)) {
            //查询全部
            cursor = db.query("goods_order", null, "user_name=?", new String[]{userName}, null, null, null, null);
        } else {
            //按照某种类型查询
            cursor = db.query("goods_order", null, "user_name=? and order_state = ?", new String[]{userName, stateType}, null, null, null, null);
        }
        while (cursor.moveToNext()) {
            String creatTime = cursor.getString(cursor.getColumnIndex("create_time"));
            if (currentTime.equals(creatTime)) {
                continue;
            }
            currentTime = creatTime;
            ArrayList<OrderInfo> listOrderInfo = new ArrayList<>();
            //按照某一时间段查询
            Cursor cursorTime = db.query("goods_order", null, "create_time = ?", new String[]{creatTime}, null, null, null, null);
            while (cursorTime.moveToNext()) {
                String user_name = cursorTime.getString(cursorTime.getColumnIndex("user_name"));
                String order_state = cursorTime.getString(cursorTime.getColumnIndex("order_state"));
                String goods_name = cursorTime.getString(cursorTime.getColumnIndex("goods_name"));
                String goods_img = cursorTime.getString(cursorTime.getColumnIndex("goods_img"));
                String goods_price = cursorTime.getString(cursorTime.getColumnIndex("goods_price"));
                String goods_id = cursorTime.getString(cursorTime.getColumnIndex("goods_id"));
                String create_time = cursorTime.getString(cursorTime.getColumnIndex("create_time"));
                int goods_num = cursorTime.getInt(cursorTime.getColumnIndex("goods_num"));
                int address_id = cursorTime.getInt(cursorTime.getColumnIndex("address_id"));
                LogUtils.i("TAG","取id-----------------"+ address_id);
                //添加相应的商品状态信息
                OrderInfo orderInfo = new OrderInfo(goods_id, goods_img, goods_name, goods_num, goods_price, order_state, user_name, create_time,address_id);
                listOrderInfo.add(orderInfo);
            }
            //将集合返回
            listAll.add(listOrderInfo);
        }
        db.close();
        return listAll;
    }

    //修改订单状态
    public void updateState(String userName, String goodsTime, String goodsState) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_state", goodsState);
        db.update("goods_order", values, "user_name=? and create_time=?", new String[]{userName, goodsTime});
        db.close();
    }

    //移除某个订单
    public void deleteOrder(String userName, String goodsTime) {
        SQLiteDatabase db = sqLiteOpenhelper.getWritableDatabase();
        db.delete("goods_order", "user_name=? and create_time=?", new String[]{userName, goodsTime});
        db.close();
    }

}
