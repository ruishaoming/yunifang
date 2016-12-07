package com.rui.yunifang.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rui.yunifang.R;
import com.rui.yunifang.utils.CommonUtils;
import com.zhy.autolayout.AutoLayoutActivity;

public class SettingsActivity extends AutoLayoutActivity implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(SettingsActivity.this);
                break;
        }
    }
}
