package com.rui.yunifang.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
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

    private GridView gv;
    private DisplayImageOptions imageOptions;
    private SpringView springView;
    private CommonAdapter goodsAdapter;
    private AllGoodsBean goodsBean;
    private LinearLayout line_order;
    private TextView order_asc;
    private TextView order_desc;
    private TextView order_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allgoods);
        initView();

        getData(BaseData.LONG_TIME);

        if (goodsBean == null)
            return;
        initGvData();
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
    }

    //初始化控件
    private void initView() {
        getSupportActionBar().hide();
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
        line_order = (LinearLayout) findViewById(R.id.allgoods_order_line);
        order_asc = (TextView) findViewById(R.id.allgoods_order_asc);
        order_desc = (TextView) findViewById(R.id.allgoods_order_desc);
        order_normal = (TextView) findViewById(R.id.allgoods_order_normal);
        order_asc.setOnClickListener(this);
        order_desc.setOnClickListener(this);
        order_normal.setOnClickListener(this);
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
                        scrollVisibAnim(true);
                    }

                    if (firstVisibleItem < lastVisibleItemPosition) {
                        scrollVisibAnim(false);
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
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
        } else {
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        }
        translateAnimation.setDuration(300);
        line_order.setAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (down) {
                    line_order.setVisibility(View.GONE);
                } else {
                    line_order.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //获取数据
    private void getData(int time) {
        new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                goodsBean = gson.fromJson(data, AllGoodsBean.class);
            }

            @Override
            protected void setFailData(String error_type) {

            }
        }.getData(UrlUtils.ALL_GOODS_URL, null, time, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(AllGoodsActivity.this);
                break;
            case R.id.allgoods_order_asc:
                Collections.sort(goodsBean.data);
                initGvData();
                order_asc.setTextColor(getResources().getColor(R.color.colorTextMain));
                order_desc.setTextColor(getResources().getColor(R.color.colorTextBlack));
                order_normal.setTextColor(getResources().getColor(R.color.colorTextBlack));
                break;
            case R.id.allgoods_order_desc:
                Collections.reverse(goodsBean.data);
                initGvData();
                order_asc.setTextColor(getResources().getColor(R.color.colorTextBlack));
                order_desc.setTextColor(getResources().getColor(R.color.colorTextMain));
                order_normal.setTextColor(getResources().getColor(R.color.colorTextBlack));
                break;
            case R.id.allgoods_order_normal:
                goodsAdapter = null;
                getData(BaseData.SHORT_TIME);
                initGvData();
                order_asc.setTextColor(getResources().getColor(R.color.colorTextBlack));
                order_desc.setTextColor(getResources().getColor(R.color.colorTextBlack));
                order_normal.setTextColor(getResources().getColor(R.color.colorTextMain));
                break;
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
