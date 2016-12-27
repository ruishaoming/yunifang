package com.rui.yunifang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.yunifang.R;
import com.rui.yunifang.bean.AddressInfo;
import com.rui.yunifang.db.AddressDao;
import com.rui.yunifang.fragment.AddAddress_Fragment;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

public class AddressActivity extends AutoLayoutActivity implements View.OnClickListener {

    private AddressDao addressDao;
    public static ArrayList<AddressInfo> listAddress;
    private ListView lvAddress;
    public CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().hide();
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("选择收货地址");
        TextView edit = (TextView) findViewById(R.id.title_right_tv);
        edit.setText("管理");
        lvAddress = (ListView) findViewById(R.id.address_lv);
        addressDao = new AddressDao(AddressActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //得到所有的收货地址的信息
        String user_name = CommonUtils.getSp("user_name");
        listAddress = addressDao.queryAll(user_name);

        setAddressAdapter();
    }

    private void setAddressAdapter() {
        String address_id_chick = CommonUtils.getSp("address_id_chick");
        if (!TextUtils.isEmpty(address_id_chick)) {
            int index = Integer.parseInt(address_id_chick);
            if (index < listAddress.size()) {
                listAddress.get(index).setCheck(true);
            }
        }
        adapter = new CommonAdapter<AddressInfo>(this, R.layout.address_item, listAddress) {
            @Override
            protected void convert(ViewHolder viewHolder, AddressInfo item, int position) {
                viewHolder.setText(R.id.address_item_name, item.getAd_name());
                viewHolder.setText(R.id.address_item_number, item.getAd_number() + "");
                viewHolder.setText(R.id.address_item_dis, item.getAd_city() + " " + item.getAd_district());
                CheckBox click = viewHolder.getView(R.id.address_item_click);
                if (item.isCheck()) {
                    click.setVisibility(View.VISIBLE);
                } else {
                    click.setVisibility(View.INVISIBLE);
                }
            }
        };
        lvAddress.setAdapter(adapter);

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonUtils.saveSp("address_id", listAddress.get(position).get_id() + "");//将Id存入sp
                CommonUtils.saveSp("address_id_chick", position + "");//保存当前选中的条目
                //设置其他为false
                for (int i = 0; i < listAddress.size(); i++) {
                    if (i == position) {
                        listAddress.get(position).setCheck(true);
                    } else {
                        listAddress.get(i).setCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
                finish();
            }
        });

        lvAddress.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);
                builder.setTitle("确定删除该收获地址？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = listAddress.size() - 1; i > -1; i--) {
                            //如果当前选中的是之前默认的地址
                            if (listAddress.get(i).isCheck() && position == i) {
                                CommonUtils.saveSp("address_id", "");
                            }
                        }
                        addressDao.delete(listAddress.get(position));
                        listAddress.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(AddressActivity.this, "已成功删除。", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(AddressActivity.this);
                break;
        }
    }

    public void addAddressBtn(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.xin_right, R.anim.xout_left);
        fragmentTransaction.add(R.id.address_fram, new AddAddress_Fragment(), "addaddress");
//        fragmentTransaction.addToBackStack("addaddress");
        fragmentTransaction.commit();
    }
}
