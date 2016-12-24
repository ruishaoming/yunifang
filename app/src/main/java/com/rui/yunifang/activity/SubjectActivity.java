package com.rui.yunifang.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.HomeData;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.Serializable;

public class SubjectActivity extends AutoLayoutActivity implements View.OnClickListener {

    private GridView gv;
    private DisplayImageOptions imageOptions;
    private TextView sub_title;
    private TextView sub_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        initView();
        initData();
    }

    private void initData() {
        final HomeData.DataBean.SubjectsBean subject = (HomeData.DataBean.SubjectsBean) getIntent().getSerializableExtra("subject");

        sub_title.setText("#" + subject.title + "#");
        sub_detail.setText(subject.detail);

        gv.setAdapter(new CommonAdapter<HomeData.DataBean.SubjectsBean.GoodsListBean>(this, subject.goodsList, R.layout.home_gv_item) {
            @Override
            public void convert(ViewHolder holder, HomeData.DataBean.SubjectsBean.GoodsListBean item) {
                ImageView image = holder.getView(R.id.home_gv_item_iv);
                TextView title = holder.getView(R.id.home_gv_item_tv_title);
                TextView des = holder.getView(R.id.home_gv_item_tv_des);
                TextView price = holder.getView(R.id.home_gv_item_tv_price);
                TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);

                ImageLoader.getInstance().displayImage(item.goods_img, image, imageOptions);
                title.setText(item.efficacy);
                des.setText(item.goods_name);
                price.setText("￥" + item.shop_price);
                oldprice.setText("￥" + item.market_price);
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubjectActivity.this, GoodsActivity.class);
                intent.putExtra("id", subject.goodsList.get(position).id);
                startActivity(intent);
                overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
            }
        });
    }

    private void initView() {
        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("专题");
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        findViewById(R.id.title_right_tv).setVisibility(View.GONE);
        sub_title = (TextView) findViewById(R.id.subject_tv_title);
        sub_detail = (TextView) findViewById(R.id.subject_tv_detail);
        gv = (GridView) findViewById(R.id.subject_gv);
        imageOptions = ImageLoaderUtils.initOptions();
        gv.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(this);
                break;
        }
    }

}
