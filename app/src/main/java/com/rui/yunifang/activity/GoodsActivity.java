package com.rui.yunifang.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.bean.GoodsInfo;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.zhy.autolayout.AutoLayoutActivity;

//详情页
public class GoodsActivity extends AutoLayoutActivity {

    private TextView goods_title;
    private ViewPager goods_vp;
    private GoodsInfo goodsInfo;
    private DisplayImageOptions imageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        initView();
        getData();
        initData();
    }

    private void initData() {
//        initViewPager();
        goods_title.setText(goodsInfo.data.goods.goods_name);

    }

    private void initViewPager() {
        goods_vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GoodsActivity.this);
//                ImageLoader.getInstance().displayImage(goodsInfo.data.goods.goods_desc.get(position).url,imageView,imageOptions);
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
}
