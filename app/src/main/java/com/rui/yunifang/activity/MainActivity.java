package com.rui.yunifang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rui.yunifang.R;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.factory.FragmentFactory;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.view.NoScrollViewPager;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.TimerTask;

public class MainActivity extends AutoLayoutActivity {

    private NoScrollViewPager vp;
    private RadioGroup rg;

    public RadioGroup getRg() {
        return rg;
    }

    public NoScrollViewPager getVp() {
        return vp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        vp = (NoScrollViewPager) findViewById(R.id.vp_main);
        rg = (RadioGroup) findViewById(R.id.rg_main);
//        vp.setOffscreenPageLimit(2);

        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 2) {
                    //如果是未登录状态
                    if (!MyApplication.isLogin) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        return FragmentFactory.getFragment(position);
                    }
                }
                return FragmentFactory.getFragment(position);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rg.getChildAt(i);
                    if (rb.getId() == checkedId) {
                        rb.setTextColor(getResources().getColor(R.color.colorTextMain));
                        vp.setCurrentItem(i);
                    } else {
                        rb.setTextColor(Color.BLACK);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.gotoShop = false;//当前所在位置即是逛一逛的目的地
    }
}
