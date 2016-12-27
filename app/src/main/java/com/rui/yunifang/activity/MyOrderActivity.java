package com.rui.yunifang.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.yunifang.R;
import com.rui.yunifang.factory.FragmentFactory;
import com.rui.yunifang.fragment.Mine_Fragment;
import com.rui.yunifang.fragment.Order_Fragment;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.view.LazyViewPager;
import com.zhy.autolayout.AutoLayoutActivity;

public class MyOrderActivity extends AutoLayoutActivity implements View.OnClickListener {

    private TextView title;
    private TextView right_tv;
    private LazyViewPager vp;
    private RadioGroup rg;
    private int order_position;
    private String[] strType = {"全部", "待付款", "待发货", "待收货", "待评价"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initViews();

        initData();
    }

    private void initData() {
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Order_Fragment.setStateType(strType[position]);
            }

            @Override
            public int getCount() {
                return strType.length;
            }
        });

        //设置当前的索引位置
        vp.setCurrentItem(order_position);
        RadioButton rb_normal = (RadioButton) rg.getChildAt(order_position);
        rb_normal.setChecked(true);
        rb_normal.setTextColor(getResources().getColor(R.color.colorTextMain));

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        vp.setCurrentItem(i);
                    }
                }
            }
        });

        vp.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
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

    private void initViews() {
        getSupportActionBar().hide();
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("我的订单");
        right_tv = (TextView) findViewById(R.id.title_right_tv);
        right_tv.setText("退款订单");
        right_tv.setTextColor(getResources().getColor(R.color.colorTextMain));
        rg = (RadioGroup) findViewById(R.id.my_order_rg);
        vp = (LazyViewPager) findViewById(R.id.my_order_vp);
        right_tv.setOnClickListener(this);
        order_position = getIntent().getIntExtra("order_position", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(MyOrderActivity.this);
                break;
            case R.id.title_right_tv:
                Toast.makeText(this, "退款订单。", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
