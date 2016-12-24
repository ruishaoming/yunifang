package com.rui.yunifang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.AddressActivity;
import com.rui.yunifang.bean.AddressInfo;
import com.rui.yunifang.db.AddressDao;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;

/**
 * Created by 芮靖林
 * on 2016/12/17.
 */

public class AddAddress_Fragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private AddressDao addressDao;
    private EditText ed_name;
    private EditText ed_city;
    private EditText ed_address;
    private EditText ed_number;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.addaddress_fragmnt, null);
        rootView.findViewById(R.id.title_back_iv).setOnClickListener(this);
        TextView save = (TextView) rootView.findViewById(R.id.title_right_tv);
        save.setText("保存");
        save.setOnClickListener(this);
        TextView title = (TextView) rootView.findViewById(R.id.title_center_tv);
        title.setText("新建收货地址");
        ed_name = (EditText) rootView.findViewById(R.id.frag_add_et_name);
        ed_city = (EditText) rootView.findViewById(R.id.frag_add_et_city);
        ed_address = (EditText) rootView.findViewById(R.id.frag_add_et_address);
        ed_number = (EditText) rootView.findViewById(R.id.frag_add_et_number);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addressDao = new AddressDao(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存
            case R.id.title_right_tv:
                //保存至数据库
                String city = ed_city.getText().toString().trim();
                String address = ed_address.getText().toString().trim();
                String name = ed_name.getText().toString().trim();
                String number = ed_number.getText().toString().trim();
                if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    AddressInfo addressInfo = new AddressInfo(city, address, name, number, CommonUtils.getSp("user_name"));
                    addressDao.add(addressInfo);
                    Toast.makeText(getActivity(), "已成功添加！", Toast.LENGTH_SHORT).show();
                }
                closeThis();
                break;
            case R.id.title_back_iv:
                closeThis();
                break;
        }
    }

    private void closeThis() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.xin_left, R.anim.xout_right);
        fragmentTransaction.remove(AddAddress_Fragment.this);
        AddressActivity addressActivity = (AddressActivity) getActivity();
        AddressActivity.listAddress = addressDao.queryAll(CommonUtils.getSp("user_name"));
        addressActivity.adapter.notifyDataSetChanged();
        fragmentTransaction.commit();
    }

}
