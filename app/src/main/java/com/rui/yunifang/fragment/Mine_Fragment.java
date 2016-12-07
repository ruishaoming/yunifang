package com.rui.yunifang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rui.yunifang.activity.LoginActivity;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.MainActivity;
import com.rui.yunifang.activity.SettingsActivity;
import com.rui.yunifang.utils.CommonUtils;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Mine_Fragment extends Fragment implements View.OnClickListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        view.findViewById(R.id.mine_login_btn).setOnClickListener(this);
        view.findViewById(R.id.mine_login_iv).setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_login_btn:
            case R.id.mine_login_iv:
                CommonUtils.startActivity(getActivity(), LoginActivity.class);
                break;
            case R.id.tv_mine_contact_service_icon:
                Toast.makeText(getActivity(), "515", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_settings_iv:
                CommonUtils.startActivity(getActivity(), SettingsActivity.class);
                break;
        }
    }
}
