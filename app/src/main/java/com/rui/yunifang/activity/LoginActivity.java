package com.rui.yunifang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.rui.yunifang.R;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;

import java.util.Map;

public class LoginActivity extends AutoLayoutActivity implements View.OnClickListener {

    private TextView loginynf, loginmobile, loginforgettvps, logintvother, getCode;
    private EditText loginetnum, loginetpsw, loginetcode;
    private LinearLayout line_code;
    private PopupWindow popWindow;
    private LinearLayout login_layout;
    private LinearLayout login_bg;
    private DisplayImageOptions imageOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        login_bg = (LinearLayout) findViewById(R.id.login_bg_line);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        loginynf = (TextView) findViewById(R.id.login_ynf);
        loginmobile = (TextView) findViewById(R.id.login_mobile);
        loginetnum = (EditText) findViewById(R.id.login_et_num);
        loginetpsw = (EditText) findViewById(R.id.login_et_psw);
        loginetcode = (EditText) findViewById(R.id.login_et_code);
        loginforgettvps = (TextView) findViewById(R.id.login_forget_tv_ps);
        logintvother = (TextView) findViewById(R.id.login_tv_other);
        getCode = (TextView) findViewById(R.id.login_getCode);
        line_code = (LinearLayout) findViewById(R.id.login_line_code);
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        findViewById(R.id.title_right_tv).setOnClickListener(this);
        loginynf.setOnClickListener(this);
        loginmobile.setOnClickListener(this);
        logintvother.setOnClickListener(this);
        getCode.setOnClickListener(this);
        imageOptions = ImageLoaderUtils.initOptions();

    }

    public void login_btn(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        login_bg.setBackgroundResource(R.mipmap.bg_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择御泥坊账号登录
            case R.id.login_ynf:
                loginynf.setTextColor(getResources().getColor(R.color.colorTextMain));
                loginmobile.setTextColor(Color.GRAY);
                loginynf.setBackgroundColor(Color.WHITE);
                loginmobile.setBackgroundColor(00000000);
                loginforgettvps.setVisibility(View.VISIBLE);
                loginetpsw.setVisibility(View.VISIBLE);
                line_code.setVisibility(View.GONE);
                break;
            //选择手机登录
            case R.id.login_mobile:
                loginmobile.setBackgroundColor(Color.WHITE);
                loginynf.setBackgroundColor(00000000);
                loginynf.setTextColor(Color.GRAY);
                loginmobile.setTextColor(getResources().getColor(R.color.colorTextMain));
                loginforgettvps.setVisibility(View.INVISIBLE);
                line_code.setVisibility(View.VISIBLE);
                loginetpsw.setVisibility(View.GONE);
                break;
            case R.id.login_tv_other:
                showPopwindow();
                break;
            case R.id.login_getCode:
                break;
            //返回
            case R.id.title_back_iv:
                CommonUtils.finishActivity(LoginActivity.this);
                break;
            //注册
            case R.id.title_right_tv:

                break;
            //QQ登录
            case R.id.login_pop_qq:
                UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
//                mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            default:
                break;
        }
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {

        // 利用layoutInflater获得View
        View view = CommonUtils.inflate(R.layout.login_pop_bottom);
        ImageView qq_login = (ImageView) view.findViewById(R.id.login_pop_qq);
        qq_login.setOnClickListener(this);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        popWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT, 350);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popWindow.setFocusable(true);
        setBackgroundAlpha();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                closePopupWindow();
                return false;
            }
        });
        // 设置popWindow的显示和消失动画
        popWindow.setAnimationStyle(R.style.popupAnimation);
        // 在底部显示
        popWindow.showAtLocation(loginforgettvps,
                Gravity.BOTTOM, 0, 0);
    }

    //设置当前窗口背景背景
    public void setBackgroundAlpha() {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.7f;
        this.getWindow().setAttributes(params);
    }

    //关闭PopWindow
    private void closePopupWindow() {
        if (popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
            popWindow = null;
            WindowManager.LayoutParams params = this.getWindow().getAttributes();
            params.alpha = 1f;
            this.getWindow().setAttributes(params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            MyApplication.isLogin = true;//设置登录状态
            CommonUtils.saveSp("user_name", data.get("screen_name"));//用户民
            CommonUtils.saveSp("user_icon", data.get("profile_image_url"));//头像
            finish();
            Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
}


