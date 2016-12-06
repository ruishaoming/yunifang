package com.rui.yunifang.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.rui.yunifang.R;
import com.rui.yunifang.utils.CommonUtils;
import com.zhy.autolayout.AutoLayoutActivity;

public class DynamicActivity extends AutoLayoutActivity implements View.OnClickListener {

    private WebView webView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        getSupportActionBar().hide();
        webView = (WebView) findViewById(R.id.dynamic_webView);
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        findViewById(R.id.title_right_tv).setVisibility(View.INVISIBLE);
        title = (TextView) findViewById(R.id.title_center_tv);
        String dynamic_data = getIntent().getStringExtra("url");
        String dynamic_title = getIntent().getStringExtra("title");
        title.setText("Title");
        webView.loadUrl(dynamic_data);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(DynamicActivity.this);
                break;
        }
    }
}
