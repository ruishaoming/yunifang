package com.rui.yunifang.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rui.yunifang.R;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();

    }

}
