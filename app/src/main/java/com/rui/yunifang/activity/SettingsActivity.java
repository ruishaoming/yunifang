package com.rui.yunifang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.yunifang.R;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.DataClearManager;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;

public class SettingsActivity extends AutoLayoutActivity implements View.OnClickListener {

    private LinearLayout line_clearCache;
    private TextView tv_cacheSize;
    private String cacheSize;
    private TextView tv_number;
    private Button exitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("设置");
        findViewById(R.id.title_right_tv).setVisibility(View.GONE);
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        line_clearCache = (LinearLayout) findViewById(R.id.settings_tv_clearCache);
        tv_cacheSize = (TextView) findViewById(R.id.settings_tv_cacheSize);
        findViewById(R.id.settings_tv_notice).setOnClickListener(this);
        findViewById(R.id.settings_tv_idea).setOnClickListener(this);
        findViewById(R.id.settings_tv_about_us).setOnClickListener(this);
        findViewById(R.id.settings_line_call).setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.settings_tv_number);
        findViewById(R.id.settings_line_update).setOnClickListener(this);
        line_clearCache.setOnClickListener(this);
        exitLogin = (Button) findViewById(R.id.settings_exitLogin);
        exitLogin.setOnClickListener(this);
        getCacheSize();
    }

    private void getCacheSize() {
        File cacheDir = getCacheDir();
        try {
            cacheSize = DataClearManager.getCacheSize(cacheDir);
            tv_cacheSize.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(SettingsActivity.this);
                break;
            case R.id.settings_tv_clearCache:
                Toast.makeText(SettingsActivity.this, "clear", Toast.LENGTH_SHORT).show();
                DataClearManager.cleanApplicationData(this);
                getCacheSize();
                break;
            //购物须知
            case R.id.settings_tv_notice:
                Toast.makeText(SettingsActivity.this, "开心购物", Toast.LENGTH_SHORT).show();
                break;
            //意见反馈
            case R.id.settings_tv_idea:
                Toast.makeText(SettingsActivity.this, "有意见直接和芮靖林说。", Toast.LENGTH_SHORT).show();
                break;
            //关于我们
            case R.id.settings_tv_about_us:

                break;
            //拨打电话
            case R.id.settings_line_call:
                String number = tv_number.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(SettingsActivity.this, "号码有误！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            //版本更新
            case R.id.settings_line_update:
                Toast.makeText(SettingsActivity.this, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
                break;
            //退出登录
            case R.id.settings_exitLogin:
                MyApplication.isLogin = false;//设置登录状态
                CommonUtils.saveSp("user_name", null);//用户民
                CommonUtils.saveSp("user_icon", null);//头像
                break;
        }
    }
}
