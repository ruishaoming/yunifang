package com.rui.yunifang.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.GoodsDesc;
import com.rui.yunifang.bean.GoodsInfo;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.GoodsScrollView;
import com.rui.yunifang.view.InnerGridView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

//详情页
public class GoodsActivity extends AutoLayoutActivity implements View.OnClickListener, GoodsScrollView.OnScrollListener {

    private static final String TAG = "TAG";
    private static final int SUCCESS = 0;
    private TextView goods_title;
    private WindowManager mWindowManager;
    private ViewPager goods_vp;
    private GoodsInfo goodsInfo;
    private GoodsScrollView scrollView;
    private View title;
    private LinearLayout top;
    private int screenWidth;
    private int searchLayoutTop;
    private ListView btm_lv;
    private TextView top_par;
    private TextView top_args;
    private TextView top_comment;
    private String id;
    private TextView gods_price;
    private TextView goods_oldprice;
    private TextView sell_type;
    private TextView collect_num;
    private TextView sell_num;
    private LinearLayout top_line;
    private LinearLayout bottom_line;
    private LinearLayout line_getY;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                initData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        getSupportActionBar().hide();

        initView();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        goods_title.setText(goodsInfo.data.goods.goods_name);
        top_comment.setText("评论(" + goodsInfo.data.commentNumber + ")");
        gods_price.setText("￥" + goodsInfo.data.goods.shop_price);
        goods_oldprice.setText("￥" + goodsInfo.data.goods.market_price);
        collect_num.setText("" + goodsInfo.data.goods.collect_count);
        sell_num.setText("" + goodsInfo.data.goods.sales_volume);
        initTopViewPager();
        initBottomImage();
    }

    private void initBottomComment() {
        btm_lv.setAdapter(new CommonAdapter<GoodsInfo.DataBean.CommentsBean>(GoodsActivity.this, goodsInfo.data.comments, R.layout.goods_comments_item) {
            @Override
            public void convert(ViewHolder viewHolder, GoodsInfo.DataBean.CommentsBean item) {
                GridView item_gv = viewHolder.getView(R.id.goods_comment_item_gv);
                ImageView icon = viewHolder.getView(R.id.goods_comment_icon);
                TextView username = viewHolder.getView(R.id.goods_comment_username);
                TextView time = viewHolder.getView(R.id.goods_comment_time);
                TextView content = viewHolder.getView(R.id.goods_comment_contnet);
                ImageLoader.getInstance().displayImage(item.user.icon, icon, ImageLoaderUtils.cycloOption());
                username.setText(item.user.nick_name);
                time.setText(item.createtime);
                content.setText(item.content);
            }
        });
    }

    private void initBottomArgs() {
        btm_lv.setAdapter(new CommonAdapter<GoodsInfo.DataBean.GoodsBean.AttributesBean>(GoodsActivity.this, goodsInfo.data.goods.attributes, R.layout.goods_args_lv_item) {
            @Override
            public void convert(ViewHolder holder, GoodsInfo.DataBean.GoodsBean.AttributesBean item) {
                TextView attr_name = holder.getView(R.id.goods_args_tv_attr_name);
                TextView attr_value = holder.getView(R.id.goods_args_tv_attr_value);
                attr_name.setText(item.attr_name);
                attr_value.setText(item.attr_value);
            }
        });
    }

    //初始化底部图片
    private void initBottomImage() {
        String goods_desc = goodsInfo.data.goods.goods_desc;
        Gson gson = new Gson();
        final ArrayList<String> listImge = new ArrayList<>();
        final GoodsDesc[] goodsDescs = gson.fromJson(goods_desc, GoodsDesc[].class);
        for (int i = 0; i < goodsDescs.length; i++) {
            listImge.add(goodsDescs[i].url);
        }
        btm_lv.setAdapter(new CommonAdapter<String>(GoodsActivity.this, listImge, R.layout.iv_layout) {
            @Override
            public void convert(ViewHolder holder, String item) {
                ImageView image = holder.getView(R.id.public_iv);
                ImageLoader.getInstance().displayImage(item, image);
            }
        });
        btm_lv.setFocusable(false);
    }

    private void initTopViewPager() {
        goods_vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return goodsInfo.data.goods.gallery.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GoodsActivity.this);
                ImageLoader.getInstance().displayImage(goodsInfo.data.goods.gallery.get(position).normal_url, imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView);
                return imageView;
            }
        });
    }

    private void initView() {
        goods_title = (TextView) findViewById(R.id.goods_title);
        gods_price = (TextView) findViewById(R.id.goods_price);
        goods_oldprice = (TextView) findViewById(R.id.goods_oldprice);
        sell_type = (TextView) findViewById(R.id.goods_sell_type);
        collect_num = (TextView) findViewById(R.id.goods_collect_num);
        sell_num = (TextView) findViewById(R.id.goods_sell_num);
        findViewById(R.id.shop_title_back).setOnClickListener(this);

        goods_vp = (ViewPager) findViewById(R.id.goods_vp);
        scrollView = (GoodsScrollView) findViewById(R.id.goods_scrollView);
        title = findViewById(R.id.goods_top_title);
        top = (LinearLayout) findViewById(R.id.goods_line_top);
        top_line = (LinearLayout) findViewById(R.id.goods_top_line);
        bottom_line = (LinearLayout) findViewById(R.id.goods_bottom_line);
        line_getY = (LinearLayout) findViewById(R.id.goods_line_getY);

        scrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        btm_lv = (ListView) findViewById(R.id.goods_bottom_iv_lv);
        btm_lv.setFocusable(false);
        top_par = (TextView) findViewById(R.id.goods_top_particulars);
        top_args = (TextView) findViewById(R.id.goods_top_args);
        top_comment = (TextView) findViewById(R.id.goods_top_comment);
        top_par.setOnClickListener(this);
        top_args.setOnClickListener(this);
        top_comment.setOnClickListener(this);
        id = getIntent().getStringExtra("id");//接收参数
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            searchLayoutTop = line_getY.getBottom();//获取searchLayout的顶部位置
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= searchLayoutTop){
            if (top.getParent()!=top_line) {
                bottom_line.removeView(top);
                top_line.addView(top);
                top_line.setVisibility(View.VISIBLE);
            }
        }else{
            if (top.getParent()!=bottom_line) {
                top_line.removeView(top);
                bottom_line.addView(top);
                top_line.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        BaseData baseData = new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                goodsInfo = gson.fromJson(data, GoodsInfo.class);
                if (goodsInfo != null) {
                    hd.obtainMessage(SUCCESS).sendToTarget();
                }
            }

            @Override
            protected void setFailData(String error_type) {

            }
        };
        baseData.getData(UrlUtils.GOODS_URL + id, "", BaseData.LONG_TIME, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //产品详情
            case R.id.goods_top_particulars:
                top_par.setTextColor(getResources().getColor(R.color.colorTextMain));
                top_args.setTextColor(getResources().getColor(R.color.colortvGray));
                top_comment.setTextColor(getResources().getColor(R.color.colortvGray));
                initBottomImage();
                break;
            //产品参数
            case R.id.goods_top_args:
                top_par.setTextColor(getResources().getColor(R.color.colortvGray));
                top_args.setTextColor(getResources().getColor(R.color.colorTextMain));
                top_comment.setTextColor(getResources().getColor(R.color.colortvGray));
                initBottomArgs();
                break;
            //评论
            case R.id.goods_top_comment:
                top_par.setTextColor(getResources().getColor(R.color.colortvGray));
                top_args.setTextColor(getResources().getColor(R.color.colortvGray));
                top_comment.setTextColor(getResources().getColor(R.color.colorTextMain));
                initBottomComment();
                break;
            case R.id.shop_title_back:
                CommonUtils.finishActivity(GoodsActivity.this);
                break;
        }
    }

}
