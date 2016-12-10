package com.rui.yunifang.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rui.yunifang.R;

/**
 * Created by 少明 on 2016/12/6.
 */
public class HomeRecyclerHolder extends RecyclerView.ViewHolder{

    public ImageView hotweek_iv;
    public TextView hotweek_title;
    public TextView hotweek_price;
    public TextView hotweek_oldprice;
    public LinearLayout hotweek_item;

    public HomeRecyclerHolder(View itemView) {
        super(itemView);
        hotweek_item = (LinearLayout) itemView.findViewById(R.id.home_line_hotweek_item);
        hotweek_iv = (ImageView) itemView.findViewById(R.id.home_line_hot_week_iv);
        hotweek_title = (TextView) itemView.findViewById(R.id.home_line_hot_week_tv_title);
        hotweek_price = (TextView) itemView.findViewById(R.id.home_line_hot_week_tv_price);
        hotweek_oldprice = (TextView) itemView.findViewById(R.id.home_line_hot_week_tv_oldPrice);
    }
}
