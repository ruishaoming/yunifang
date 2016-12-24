package com.rui.yunifang.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.bean.HomeData;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class MoreShopActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView shop_lv;
    private DisplayImageOptions imageOptions;
    private HomeData.DataBean.BestSellersBean best;
    private HomeData.DataBean.SubjectsBean subjectsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_shop);
        getSupportActionBar().hide();

        findViewById(R.id.title_back_iv).setOnClickListener(this);
        findViewById(R.id.title_right_tv).setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("本周热销");
        shop_lv = (ListView) findViewById(R.id.moreShop_lv);
        imageOptions = ImageLoaderUtils.initOptions();
        Serializable week_hot = getIntent().getSerializableExtra("week_hot");
        Serializable subject = getIntent().getSerializableExtra("subject");
        if (week_hot != null) {
            best = (HomeData.DataBean.BestSellersBean) week_hot;
            shop_lv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<HomeData.DataBean.BestSellersBean.GoodsListBean>(MoreShopActivity.this, R.layout.more_shop_lv_item, best.goodsList) {
                @Override
                protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, HomeData.DataBean.BestSellersBean.GoodsListBean item, int position) {
                    ImageView image = holder.getView(R.id.more_item_image);
                    ImageView iv = holder.getView(R.id.more_item_iv);
                    TextView title = holder.getView(R.id.more_item_tv_title);
                    TextView price = holder.getView(R.id.more_item_tv_price);
                    TextView oldprice = holder.getView(R.id.more_item_tv_oldprice);
                    TextView sort = holder.getView(R.id.more_item_tv_sort);

                    ImageLoader.getInstance().displayImage(item.goods_img, image, imageOptions);
                    title.setText(item.efficacy);
                    price.setText("￥" + item.shop_price);
                    oldprice.setText("￥" + item.market_price);
                    oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    if (position == 0) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        iv.setImageResource(R.mipmap.hot_rank_1);
                    } else if (position == 1) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        iv.setImageResource(R.mipmap.hot_rank_2);
                    } else if (position == 2) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        iv.setImageResource(R.mipmap.hot_rank_3);
                    } else {
                        iv.setVisibility(View.GONE);
                        sort.setVisibility(View.VISIBLE);
                        sort.setText("No." + (++position));
                    }
                }
            });
            shop_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MoreShopActivity.this, GoodsActivity.class);
                    intent.putExtra("id", best.goodsList.get(position).id);
                    startActivity(intent);
                    overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
                }
            });
        } else if (subject != null) {
            subjectsBean = (HomeData.DataBean.SubjectsBean) subject;
            shop_lv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<HomeData.DataBean.SubjectsBean.GoodsListBean>(MoreShopActivity.this, R.layout.more_shop_lv_item, subjectsBean.goodsList) {
                @Override
                protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, HomeData.DataBean.SubjectsBean.GoodsListBean item, int position) {
                    ImageView image = holder.getView(R.id.more_item_image);
                    ImageView iv = holder.getView(R.id.more_item_iv);
                    TextView title = holder.getView(R.id.more_item_tv_title);
                    TextView price = holder.getView(R.id.more_item_tv_price);
                    TextView oldprice = holder.getView(R.id.more_item_tv_oldprice);
                    TextView sort = holder.getView(R.id.more_item_tv_sort);

                    ImageLoader.getInstance().displayImage(item.goods_img, image, imageOptions);
                    title.setText(item.efficacy);
                    price.setText("￥" + item.shop_price);
                    oldprice.setText("￥" + item.market_price);
                    oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    if (position == 0) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        iv.setImageResource(R.mipmap.hot_rank_1);
                    } else if (position == 1) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        iv.setImageResource(R.mipmap.hot_rank_2);
                    } else if (position == 2) {
                        iv.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                    } else {
                        iv.setImageResource(R.mipmap.hot_rank_3);
                        iv.setVisibility(View.GONE);
                        sort.setVisibility(View.VISIBLE);
                        sort.setText("No." + (++position));
                    }
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(MoreShopActivity.this);
                break;
        }
    }

}
