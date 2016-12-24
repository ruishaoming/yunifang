package com.rui.yunifang.activity;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
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
import com.rui.yunifang.bean.MaskInfo;
import com.rui.yunifang.bean.SortBean;
import com.rui.yunifang.fragment.SoatMaskFragment;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class SoatMaskActivity extends AutoLayoutActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private RadioGroup mask_rg;
    private ViewPager maskVp;
    private TextView title;
    private SortBean.DataBean.CategoryBean maskData;
    private SortBean.DataBean.CategoryBean.ChildrenBean child;
    private int getId;
    private HorizontalScrollView horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soat_mask);
        getSupportActionBar().hide();
        initView();
    }

    //初始化控件
    private void initView() {
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("面膜");
        findViewById(R.id.title_right_tv).setVisibility(View.INVISIBLE);
        mask_rg = (RadioGroup) findViewById(R.id.soatmask_rg);
        maskVp = (ViewPager) findViewById(R.id.soatmask_vp);
        horizontal = (HorizontalScrollView) findViewById(R.id.soatmask_hs);

        maskData = (SortBean.DataBean.CategoryBean) getIntent().getSerializableExtra("mask");
        child = (SortBean.DataBean.CategoryBean.ChildrenBean) getIntent().getSerializableExtra("child");
        getId = getIntent().getIntExtra("id", -1);
        //按类型划分，显示不同的界面
        if (maskData.cat_name.equals("按功效")) {
            virtueMask();
        } else if (maskData.cat_name.equals("按属性")) {
            natureMask();
        } else if (maskData.cat_name.equals("按肤质")) {
            skinMask();
        }
    }

    /**
     * @param rg     设置需要加载的RadioGroup
     * @param size   设置ViewPager中Fragment的个数
     * @param typeId 判断当前传入的条目的类型
     */
    private void initData(final RadioGroup rg, final int size, final int typeId) {

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        maskVp.setCurrentItem(i);
                    }
                }
            }
        });

        maskVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (typeId == 0) {
                    return SoatMaskFragment.getUrlData(maskData.children.get(position).id + "");
                } else if (typeId == -1) {
                    return SoatMaskFragment.getUrlData(maskData.children.get(position + 6).id + "");
                } else {
                    return SoatMaskFragment.getUrlData(typeId + "");
                }
            }

            @Override
            public int getCount() {
                return size;
            }
        });

        maskVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rg.getChildAt(i);
                    if (i == position) {
                        rb.setChecked(true);
                        rb.setTextColor(getResources().getColor(R.color.colorTextMain));
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

    //肤质
    private void skinMask() {
        horizontal.setVisibility(View.VISIBLE);
        mask_rg.setVisibility(View.GONE);
        title.setText(maskData.cat_name);
        RadioGroup hotiRadioGroup = new RadioGroup(this);
        hotiRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(250, RadioGroup.LayoutParams.MATCH_PARENT, 1.0f);
        for (int i = 0; i < maskData.children.size(); i++) {
            RadioButton radioButton = initRadipButton(0, i);
            hotiRadioGroup.addView(radioButton, params);
        }
        horizontal.addView(hotiRadioGroup, new HorizontalScrollView.LayoutParams(1000, RadioGroup.LayoutParams.MATCH_PARENT));
        initData(hotiRadioGroup, maskData.children.size(), 0);
        maskVp.setCurrentItem(getId);
    }

    //属性
    private void natureMask() {
        horizontal.setVisibility(View.GONE);
        if (getId == 0) {
            title.setText("面膜");
            mask_rg.setVisibility(View.VISIBLE);
            for (int i = 6; i < maskData.children.size(); i++) {
                RadioButton radioButton = initRadipButton(6, i);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1.0f);
                mask_rg.addView(radioButton, params);
            }
            initData(mask_rg, 3, -1);
        } else {
            title.setText(child.cat_name);
            mask_rg.setVisibility(View.GONE);
            initData(mask_rg, 1, Integer.parseInt(child.id));
        }
    }

    //功效
    private void virtueMask() {
        horizontal.setVisibility(View.GONE);
        mask_rg.setVisibility(View.VISIBLE);
        title.setText(maskData.cat_name);
        mask_rg.removeAllViews();
        for (int i = 0; i < maskData.children.size(); i++) {
            RadioButton radioButton = initRadipButton(0, i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1.0f);
            mask_rg.addView(radioButton, params);
        }
        initData(mask_rg, maskData.children.size(), 0);
        maskVp.setCurrentItem(getId);
    }

    //初始化RadioButton
    public RadioButton initRadipButton(int selectId, int i) {
        RadioButton radioButton = (RadioButton) CommonUtils.inflate(R.layout.radiobutton_layout);
        radioButton.setText(maskData.children.get(i).cat_name);
        if (i == selectId) {
            radioButton.setChecked(true);
            radioButton.setTextColor(getResources().getColor(R.color.colorTextMain));
        } else {
            radioButton.setChecked(false);
            radioButton.setTextColor(getResources().getColor(R.color.colorTextBlack));
        }
        return radioButton;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(SoatMaskActivity.this);
                break;
        }
    }
}
