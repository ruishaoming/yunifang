package com.rui.yunifang.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.AllGoodsBean;
import com.zhy.autolayout.AutoLayoutActivity;

import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Collection;
import java.util.Collections;

public class AllGoodsActivity extends AutoLayoutActivity implements View.OnClickListener, SpringView.OnFreshListener {

    private static final String TAG = "TAG";
    private GridView gv;
    private DisplayImageOptions imageOptions;
    private SpringView springView;
    private CommonAdapter goodsAdapter;
    private AllGoodsBean goodsBean;
    private RadioGroup rg_order;
    private LinearLayout line;
    private boolean pullFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allgoods);
        initView();
        getData(BaseData.NO_TIME);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //初始化GridView
    private void initGvData() {
        if (goodsAdapter == null) {
            goodsAdapter = new CommonAdapter<AllGoodsBean.DataBean>(AllGoodsActivity.this, goodsBean.data, R.layout.home_gv_item) {
                @Override
                public void convert(ViewHolder holder, AllGoodsBean.DataBean item) {
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
            };
            gv.setAdapter(goodsAdapter);
        } else {
            goodsAdapter.notifyDataSetChanged();
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllGoodsActivity.this, GoodsActivity.class);
                intent.putExtra("id", goodsBean.data.get(position).id);
                startActivity(intent);
                overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
            }
        });
    }

    //初始化控件
    private void initView() {
        getSupportActionBar().hide();
//        line = (LinearLayout) findViewById(R.id.allgoods_line);
        findViewById(R.id.title_right_tv).setVisibility(View.INVISIBLE);
        TextView title_center = (TextView) findViewById(R.id.title_center_tv);
        title_center.setText("全部商品");
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        gv = (GridView) findViewById(R.id.goods_gv);
        springView = (SpringView) findViewById(R.id.goods_springView);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);//设置隐藏
        imageOptions = ImageLoaderUtils.initOptions();
        rg_order = (RadioGroup) findViewById(R.id.allgoods_order_rg);
        rg_order.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < rg_order.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.getId() == checkedId) {
                        radioButton.setChecked(true);
                        radioButton.setTextColor(getResources().getColor(R.color.colorTextMain));
                        if (i == 0) {
                            goodsAdapter = null;
                            getData(BaseData.SHORT_TIME);
                            initGvData();
                        } else if (i == 1) {
                            Collections.sort(goodsBean.data);
                            goodsAdapter.notifyDataSetChanged();
                        } else {
                            Collections.reverse(goodsBean.data);
                            goodsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        radioButton.setChecked(false);
                        radioButton.setTextColor(getResources().getColor(R.color.colorTextBlack));
                    }
                }
            }
        });
        gvScroll();
    }

    //滑动监听
    private void gvScroll() {
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastVisibleItemPosition;
            private boolean scrollFlag;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrollFlag = true;
                } else {
                    scrollFlag = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollFlag) {

                    if (firstVisibleItem > lastVisibleItemPosition) {
                        //上拉
                        Log.i(TAG, "onScroll: 上拉----------------");
                        scrollVisibAnim(true);
                    }

                    if (firstVisibleItem < lastVisibleItemPosition) {
                        //下拉
                        Log.i(TAG, "onScroll: 下拉----------------");
                        if (pullFlag) {
                            scrollVisibAnim(false);
                        }
                    }

                    if (firstVisibleItem == lastVisibleItemPosition) {
                        return;
                    }

                    lastVisibleItemPosition = firstVisibleItem;

                }
            }
        });
    }

    //设置隐藏动画
    private void scrollVisibAnim(final boolean down) {
        TranslateAnimation translateAnimation = null;
        if (down) {
            pullFlag = true;
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -0.2f);
        } else {
            pullFlag = false;
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -0.5f,
                    Animation.RELATIVE_TO_SELF, 0.2f);
        }
        if (translateAnimation != null) {
            translateAnimation.setDuration(300);
//        translateAnimation.setRepeatMode(Animation.REVERSE);
            rg_order.setAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (down) {
                        rg_order.setVisibility(View.GONE);
                    } else {
                        rg_order.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    //获取数据
    private void getData(int time) {
        new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                goodsBean = gson.fromJson(data, AllGoodsBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initGvData();
                    }
                });
            }

            @Override
            protected void setFailData(String error_type) {

            }
        }.getData(UrlUtils.ALL_GOODS_URL, null, time, 0,false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onRefresh() {
        getData(BaseData.NO_TIME);
        stopLoad();
    }

    public void stopLoad() {
        springView.scrollTo(0, 0);
    }

    @Override
    public void onLoadmore() {

        stopLoad();
    }
}
