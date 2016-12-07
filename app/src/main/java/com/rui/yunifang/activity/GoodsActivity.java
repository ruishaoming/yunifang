package com.rui.yunifang.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.GoodsScrollView;
import com.rui.yunifang.view.InnerGridView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

//详情页
public class GoodsActivity extends AutoLayoutActivity implements GoodsScrollView.OnScrollListener {

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

    /**
     * 悬浮框的参数
     */
    private static WindowManager.LayoutParams suspendLayoutParams;
    /**
     * 购买布局的高度
     */
    private int buyLayoutHeight;
    /**
     * myScrollView与其父类布局的顶部距离
     */
    private int myScrollViewTop;

    /**
     * 购买布局与其父类布局的顶部距离
     */
    private int buyLayoutTop;
    private InnerGridView btm_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        initView();
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        goods_title.setText(goodsInfo.data.goods.goods_name);
        initTopViewPager();
        initBottomViewPager();

    }

    private void initBottomViewPager() {
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
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(item, image, imageOptions);
            }
        });
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
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                return imageView;
            }
        });
    }

    private void initView() {
        getSupportActionBar().hide();
        imageOptions = ImageLoaderUtils.initOptions();
        goods_title = (TextView) findViewById(R.id.goods_title);
        goods_vp = (ViewPager) findViewById(R.id.goods_vp);
        scrollView = (GoodsScrollView) findViewById(R.id.goods_scrollView);
        title = findViewById(R.id.goods_top_title);
        line_top = (LinearLayout) findViewById(R.id.goods_line_top);
        scrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        btm_lv = (InnerGridView) findViewById(R.id.goods_bottom_iv_lv);
    }

    private void getData() {
        BaseData baseData = new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                goodsInfo = gson.fromJson(data, GoodsInfo.class);
            }

            @Override
            protected void setFailData(String error_type) {

            }
        };
        baseData.getData(UrlUtils.GOODS_URL, "", BaseData.LONG_TIME, 0);
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
     *  botomPage = CommonUtils.inflate(R.layout.goods_bottom_vp_image);
     ListView lv_img = (ListView) botomPage.findViewById(R.id.goods_bottom_iv_lv);
     lv_img.setAdapter(new CommonAdapter<String>(GoodsActivity.this, listImge, R.layout.iv_layout) {
    @Override
    public void convert(ViewHolder holder, String item) {
    ImageView image = holder.getView(R.id.public_iv);
    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    ImageLoader.getInstance().displayImage(item, image, imageOptions);
    }
    });
     */

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

}
