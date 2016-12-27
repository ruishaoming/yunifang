package com.rui.yunifang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.activity.LoginActivity;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.MainActivity;
import com.rui.yunifang.activity.MyOrderActivity;
import com.rui.yunifang.activity.SettingsActivity;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.view.XCRoundImageView;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Mine_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView login_name;
    private ImageView no_login_icon;
    private ImageView is_login_icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        no_login_icon = (ImageView) view.findViewById(R.id.mine_nologin_iv);
        no_login_icon.setOnClickListener(this);
        is_login_icon = (ImageView) view.findViewById(R.id.mine_islogin_iv);
        is_login_icon.setOnClickListener(this);
        login_name = (TextView) view.findViewById(R.id.mine_login_name);
        login_name.setOnClickListener(this);
        view.findViewById(R.id.mine_login_btn).setOnClickListener(this);
        view.findViewById(R.id.mine_settings_iv).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_wait_pay).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_wait_send_good).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_wait_receive_good).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_wait_evaluate).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_wait_refund).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_order_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_invite_gift_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_face_test_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_coupon_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_lottery_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_collection_icon).setOnClickListener(this);
        view.findViewById(R.id.tv_mine_contact_service_icon).setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
    }

    private void initUserInfo() {
        String user_name = CommonUtils.getSp("user_name");
        String user_icon = CommonUtils.getSp("user_icon");
        if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_icon)) {
            login_name.setText(user_name);
            ImageLoader.getInstance().displayImage(user_icon, is_login_icon, ImageLoaderUtils.cycloOption());
            login_name.setVisibility(View.VISIBLE);
            is_login_icon.setVisibility(View.VISIBLE);
            no_login_icon.setVisibility(View.GONE);
        } else {
            login_name.setVisibility(View.GONE);
            is_login_icon.setVisibility(View.GONE);
            no_login_icon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录按钮或者会员中心
            case R.id.mine_login_btn:
                if (MyApplication.isLogin) {

                } else {
                    CommonUtils.startActivity(getActivity(), LoginActivity.class);
                }
                break;
            //点击未登录头像
            case R.id.mine_nologin_iv:
                CommonUtils.startActivity(getActivity(), LoginActivity.class);
                break;
            //点击登录头像
            case R.id.mine_islogin_iv:
                Toast.makeText(getActivity(), "已经登录。", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_mine_contact_service_icon:
                Toast.makeText(getActivity(), "515", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_settings_iv:
                CommonUtils.startActivity(getActivity(), SettingsActivity.class);
                break;
            //点击用户名
            case R.id.mine_login_name:
                CommonUtils.startActivity(getActivity(), SettingsActivity.class);
                break;
            //待付款
            case R.id.tv_mine_wait_pay:
                enterOrderActivity(0);
                break;
            //待发货
            case R.id.tv_mine_wait_send_good:
                enterOrderActivity(1);
                break;
            //待收货
            case R.id.tv_mine_wait_receive_good:
                enterOrderActivity(2);
                break;
            //待评价
            case R.id.tv_mine_wait_evaluate:
                enterOrderActivity(3);
                break;
            //退款
            case R.id.tv_mine_wait_refund:
                enterOrderActivity(4);
                break;
        }
    }

    private void enterOrderActivity(int position) {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        intent.putExtra("order_position",position);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
    }
}
