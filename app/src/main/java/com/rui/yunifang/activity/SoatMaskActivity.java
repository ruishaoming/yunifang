package com.rui.yunifang.activity;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.MaskInfo;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.InnerGridView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SoatMaskActivity extends AutoLayoutActivity implements View.OnClickListener, SpringView.OnFreshListener {

    public static final int SUCCESS = 0;
    private MaskInfo maskInfo;
    private RadioGroup mask_rg;
    private SpringView springView;
    private GridView maskGv;
    private String currentUrl;
    private DisplayImageOptions imageOptions;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                initData();
            }
        }
    };
    private ViewPager maskVp;
    private ArrayList<String> listUrl;
    private CommonAdapter gv_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soat_mask);
        getSupportActionBar().hide();
        initView();
        getData(UrlUtils.SORT_URL + "9");
    }

    private void getData(String url) {
        currentUrl = url;
        new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                maskInfo = gson.fromJson(data, MaskInfo.class);
                hd.obtainMessage(SUCCESS).sendToTarget();
            }

            @Override
            protected void setFailData(String error_type) {

            }
        }.getData(url, "", BaseData.LONG_TIME, 0);
    }

    private void initData() {
        listUrl = new ArrayList<>();
        listUrl.add(UrlUtils.SORT_URL + "9");
        listUrl.add(UrlUtils.SORT_URL + "10");
        listUrl.add(UrlUtils.SORT_URL + "23");
        maskVp.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return listUrl.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                maskGv = (GridView) CommonUtils.inflate(R.layout.mask_lv);
                getData(listUrl.get(0));
                container.addView(maskGv);
                return maskGv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        maskVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mask_rg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) mask_rg.getChildAt(i);
                    if (i == position) {
                        rb.setChecked(true);
                        rb.setTextColor(getResources().getColor(R.color.colorTextMain));
                        getData(listUrl.get(position));
                        if (gv_adapter == null) {
                            gv_adapter = new CommonAdapter<MaskInfo.DataBean>(SoatMaskActivity.this, maskInfo.data, R.layout.home_gv_item) {
                                @Override
                                public void convert(ViewHolder holder, MaskInfo.DataBean item) {
                                    ImageView image = holder.getView(R.id.home_gv_item_iv);
                                    TextView title = holder.getView(R.id.home_gv_item_tv_title);
                                    TextView des = holder.getView(R.id.home_gv_item_tv_des);
                                    TextView price = holder.getView(R.id.home_gv_item_tv_price);
                                    TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);
                                    ImageLoader.getInstance().displayImage(item.goods_img, image, imageOptions);
                                    title.setText(item.goods_name);
                                    des.setText(item.efficacy);
                                    price.setText("￥" + item.shop_price);
                                    oldprice.setText("￥" + item.market_price);
                                    oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                            };
                            maskGv.setAdapter(gv_adapter);
                        } else {
                            gv_adapter.notifyDataSetChanged();
                        }
                    } else {
                        rb.setChecked(false);
                        rb.setTextColor(getResources().getColor(R.color.colorTextBlack));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("面膜");
        findViewById(R.id.title_right_tv).setVisibility(View.INVISIBLE);
        mask_rg = (RadioGroup) findViewById(R.id.soatmask_rg);
        springView = (SpringView) findViewById(R.id.soatmask_springView);
        maskVp = (ViewPager) findViewById(R.id.soatmask_vp);
        springView.setFocusable(false);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);
        imageOptions = ImageLoaderUtils.initOptions();
        mask_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.getId() == checkedId) {
                        maskVp.setCurrentItem(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        stopLoad();
    }

    @Override
    public void onRefresh() {
        getData(currentUrl);
        stopLoad();
    }

    public void stopLoad() {
        springView.scrollTo(0, 0);
    }

    @Override
    public void onLoadmore() {

    }
}
