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
public class GoodsActivity extends AutoLayoutActivity implements GoodsScrollView.OnScrollListener, View.OnClickListener {

    private static final String TAG = "TAG";
    private static final int SUCCESS = 0;
    private TextView goods_title;
    private WindowManager mWindowManager;
    private ViewPager goods_vp;
    private GoodsInfo goodsInfo;
    private DisplayImageOptions imageOptions;
    private GoodsScrollView scrollView;
    private View title;
    private LinearLayout line_top;
    private int screenWidth;
    private static View suspendView;
    private static WindowManager.LayoutParams suspendLayoutParams;
    private int buyLayoutHeight;
    private int myScrollViewTop;
    private int buyLayoutTop;
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
                Log.i(TAG, "convert: -----------" + holder);
                ImageView image = holder.getView(R.id.public_iv);
                ImageLoader.getInstance().displayImage(item, image, imageOptions);
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
                ImageLoader.getInstance().displayImage(goodsInfo.data.goods.gallery.get(position).normal_url, imageView, imageOptions);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView);
                return imageView;
            }
        });
    }

    private void initView() {
        imageOptions = ImageLoaderUtils.initOptions();
        goods_title = (TextView) findViewById(R.id.goods_title);
        gods_price = (TextView) findViewById(R.id.goods_price);
        goods_oldprice = (TextView) findViewById(R.id.goods_oldprice);
        sell_type = (TextView) findViewById(R.id.goods_sell_type);
        collect_num = (TextView) findViewById(R.id.goods_collect_num);
        sell_num = (TextView) findViewById(R.id.goods_sell_num);

        goods_vp = (ViewPager) findViewById(R.id.goods_vp);
        scrollView = (GoodsScrollView) findViewById(R.id.goods_scrollView);
        title = findViewById(R.id.goods_top_title);
        line_top = (LinearLayout) findViewById(R.id.goods_line_top);
        scrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        btm_lv = (ListView) findViewById(R.id.goods_bottom_iv_lv);
        top_par = (TextView) findViewById(R.id.goods_top_particulars);
        top_args = (TextView) findViewById(R.id.goods_top_args);
        top_comment = (TextView) findViewById(R.id.goods_top_comment);
        top_par.setOnClickListener(this);
        top_args.setOnClickListener(this);
        top_comment.setOnClickListener(this);
        id = getIntent().getStringExtra("id");//接收参数
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
        baseData.getData(UrlUtils.GOODS_URL + "14", "", BaseData.LONG_TIME, 0);
    }

    /**
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            buyLayoutHeight = line_top.getHeight();
            buyLayoutTop = line_top.getTop();

            myScrollViewTop = scrollView.getTop();
        }
    }

    /**
     * 滚动的回调方法，当滚动的Y距离大于或者等于 悬浮框距离父类布局顶部的位置，就显示购买的悬浮框
     * 当滚动的Y的距离小于 悬浮框距离父类布局顶部的位置加上悬浮框的高度就移除悬浮框
     */
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= buyLayoutTop) {
            if (suspendView == null) {
                showSuspend();
            }
        } else if (scrollY <= buyLayoutTop + buyLayoutHeight) {
            if (suspendView != null) {
                removeSuspend();
            }
        }
    }

    /**
     * 显示悬浮框
     */
    private void showSuspend() {
        if (suspendView == null) {
            suspendView = LayoutInflater.from(this).inflate(R.layout.goods_top_layout, null);
            top_par = (TextView) suspendView.findViewById(R.id.goods_top_particulars);
            top_args = (TextView) suspendView.findViewById(R.id.goods_top_args);
            top_comment = (TextView) suspendView.findViewById(R.id.goods_top_comment);
            top_par.setOnClickListener(this);
            top_args.setOnClickListener(this);
            top_comment.setOnClickListener(this);
            top_comment.setText("评论(" + goodsInfo.data.commentNumber + ")");
            if (suspendLayoutParams == null) {
                suspendLayoutParams = new WindowManager.LayoutParams();
                suspendLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE; //悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
                suspendLayoutParams.gravity = Gravity.TOP;  //悬浮窗的对齐方式
                suspendLayoutParams.width = screenWidth;
                suspendLayoutParams.height = buyLayoutHeight;
                suspendLayoutParams.x = 0;  //悬浮窗X的位置
                suspendLayoutParams.y = myScrollViewTop;  ////悬浮窗Y的位置
            }
        }
        mWindowManager.addView(suspendView, suspendLayoutParams);
    }

    /**
     * 移除悬浮框
     */
    private void removeSuspend() {
        if (suspendView != null) {
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
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
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeSuspend();
    }
}
