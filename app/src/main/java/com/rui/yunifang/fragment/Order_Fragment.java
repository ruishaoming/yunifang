package com.rui.yunifang.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.AddressInfo;
import com.rui.yunifang.bean.OrderInfo;
import com.rui.yunifang.db.AddressDao;
import com.rui.yunifang.db.OrderDao;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import static com.rui.yunifang.R.id.pay;
import static com.rui.yunifang.utils.CommonUtils.inflate;

/**
 * Created by ${芮靖林}
 * on 2016/12/26 11:36.
 */

public class Order_Fragment extends Fragment implements SpringView.OnFreshListener {

    private View rootView;
    private AutoLinearLayout empty;
    private ListView order_lv;
    private OrderDao orderDao;
    private ArrayList<ArrayList<OrderInfo>> listAll;
    private SpringView springView;
    private com.zhy.adapter.abslistview.CommonAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = CommonUtils.inflate(R.layout.order_fragment);
        empty = (AutoLinearLayout) rootView.findViewById(R.id.order_fragment_empty);
        order_lv = (ListView) rootView.findViewById(R.id.order_fragment_lv);
        springView = (SpringView) rootView.findViewById(R.id.order_springView);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);
        orderDao = new OrderDao(getActivity());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String stateType = getArguments().getString("stateType");
        //查询出所有当前状态的数据
        listAll = orderDao.queryAll(CommonUtils.getSp("user_name"), stateType);
        setRootView();
    }

    //判断显示的界面
    private void setRootView() {
        if (listAll.size() <= 0) {
            springView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            springView.setVisibility(View.VISIBLE);
            initLvAdapter();
        }
    }

    private void initLvAdapter() {
        adapter = new com.zhy.adapter.abslistview.CommonAdapter<ArrayList<OrderInfo>>(getActivity(), R.layout.order_fragment_lv, listAll) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final ArrayList<OrderInfo> item, final int position) {
                GridView item_lv = holder.getView(R.id.order_fragment_item_lv);
                TextView item_ttile = holder.getView(R.id.order_fragment_lv_tv);
                TextView item_sumPrice = holder.getView(R.id.order_fragment_sumPrice_tv);
                TextView item_cancle = holder.getView(R.id.order_fragment_cancle_btn);
                item_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancleOrder(item, position);
                    }
                });
                TextView item_pay_btn = holder.getView(R.id.order_fragment_pay_btn);
                item_pay_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payOrder(position);
                    }
                });
                item_ttile.setText(item.get(0).getOrder_state());
                setGoodsPrice(item, item_sumPrice);
                setItemLvAdapter(item, item_lv);
            }
        };
        order_lv.setAdapter(adapter);
    }

    //前往付款
    private void payOrder(int position) {
        AddressDao addressDao = new AddressDao(getActivity());
        AddressInfo user_name = addressDao.query(CommonUtils.getSp("user_name"), "" + listAll.get(position).get(0).getAddress_id());
        Toast.makeText(getActivity(), "懒得做了。。。\r\n收货地址信息："+user_name.toString()+"****商品信息："+listAll.get(position).toString(), Toast.LENGTH_SHORT).show();
    }

    //取消订单的操作
    private void cancleOrder(final ArrayList<OrderInfo> list, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("确定取消这个订单吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //移除
                orderDao.deleteOrder(CommonUtils.getSp("user_name"), list.get(0).getCreate_time());
                listAll.remove(position);
                adapter.notifyDataSetChanged();
                setRootView();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void setItemLvAdapter(ArrayList<OrderInfo> item, GridView item_lv) {
        item_lv.setAdapter(new CommonAdapter<OrderInfo>(getActivity(), item, R.layout.order_fragment_lv_item) {
            @Override
            public void convert(ViewHolder holder, OrderInfo item) {
                TextView title = holder.getView(R.id.order_item_title);
                TextView price = holder.getView(R.id.order_item_price);
                TextView number = holder.getView(R.id.order_item_number);
                ImageView img = holder.getView(R.id.order_item_img);
                Glide.with(getActivity()).load(item.getGoods_img()).into(img);
                title.setText(item.getGoods_name());
                price.setText("￥" + item.getGoods_price());
                number.setText("数量" + item.getGoods_num());
            }
        });
    }

    //计算总价格
    private void setGoodsPrice(ArrayList<OrderInfo> item, TextView item_sumPrice) {
        float priceSum = 0;
        int goodsNum = 0;
        for (int i = 0; i < item.size(); i++) {
            priceSum += Float.parseFloat(item.get(i).getGoods_price()) * item.get(i).getGoods_num();
            goodsNum += item.get(i).getGoods_num();
        }
        priceSum = (float) (Math.round(priceSum * 100)) / 100;
        item_sumPrice.setText("共" + goodsNum + "件商品  合计：￥" + priceSum);
    }

    public static Fragment setStateType(String stateType) {
        Order_Fragment order_fragment = new Order_Fragment();
        Bundle bunder = new Bundle();
        bunder.putString("stateType", stateType);
        order_fragment.setArguments(bunder);
        return order_fragment;
    }

    @Override
    public void onRefresh() {
        springView.onFinishFreshAndLoad();
    }

    @Override
    public void onLoadmore() {

    }
}
