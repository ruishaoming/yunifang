package com.rui.yunifang.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.bean.HomeData;
import com.rui.yunifang.holder.HomeRecyclerHolder;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by 少明 on 2016/12/6.
 */
public class HomeLvRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerHolder> {

    private Context context;
    private List<HomeData.DataBean.SubjectsBean.GoodsListBean> listGoods;
    private final DisplayImageOptions imageOptions;
    private OnitemClickListener onitemClickListener;

    public HomeLvRecyclerAdapter(Context context, List<HomeData.DataBean.SubjectsBean.GoodsListBean> listSubject) {
        this.context = context;
        this.listGoods = listSubject;
        imageOptions = ImageLoaderUtils.initOptions();
    }

    @Override
    public HomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = CommonUtils.inflate(R.layout.home_recycle_item);
        HomeRecyclerHolder homeRecyclerHolder = new HomeRecyclerHolder(view);
        return homeRecyclerHolder;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(listGoods.get(position).goods_img, holder.hotweek_iv, imageOptions);
        holder.hotweek_title.setText(listGoods.get(position).goods_name);
        holder.hotweek_price.setText("￥" + listGoods.get(position).shop_price);
        holder.hotweek_oldprice.setText("￥" + listGoods.get(position).market_price);
        holder.hotweek_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.hotweek_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickListener.itemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public interface OnitemClickListener {
        void itemClickListener(int position);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener) {
        HomeLvRecyclerAdapter.this.onitemClickListener = onitemClickListener;
    }
}
