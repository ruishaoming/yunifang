package com.rui.yunifang.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.AddressInfo;
import com.rui.yunifang.bean.GoodsCarInfo;
import com.rui.yunifang.bean.OrderInfo;
import com.rui.yunifang.db.AddressDao;
import com.rui.yunifang.db.OrderDao;
import com.rui.yunifang.fragment.Cart_Fragment;
import com.rui.yunifang.paydemo.PayDemoActivity;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.view.InnerGridView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.Serializable;
import java.util.ArrayList;

import static com.rui.yunifang.activity.AddressActivity.listAddress;

public class IndentActivity extends AutoLayoutActivity implements View.OnClickListener {

    private static final int GETADDRESS_REQUESTCODE = 100;
    private InnerGridView goods_lv;
    private TextView goods_args_tv;
    private ArrayList<GoodsCarInfo> goodsList;
    private TextView par_tv;
    private com.zhy.adapter.abslistview.CommonAdapter adapter;
    private String address_id;
    private AddressDao addressDao;
    private TextView address_name;
    private TextView address_number;
    private TextView address_dis;
    private TextView no_address;
    private LinearLayout line_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent);
        initViews();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取收货地址的id
        address_id = CommonUtils.getSp("address_id");
        if (!TextUtils.isEmpty(address_id)) {
            AddressInfo address = addressDao.query(CommonUtils.getSp("user_name"), address_id + "");
            if (address != null) {
                address_name.setText(address.getAd_name());
                address_number.setText(address.getAd_number() + "");
                address_dis.setText(address.getAd_city() + " " + address.getAd_district());
                no_address.setVisibility(View.GONE);
                line_address.setVisibility(View.VISIBLE);
            } else {
                no_address.setVisibility(View.VISIBLE);
                line_address.setVisibility(View.GONE);
            }
        }
    }

    private void initData() {
        //得到选中的商品集合
        goodsList = Cart_Fragment.getGoods();
        //设置当前的数量改变监听
        adapter = new com.zhy.adapter.abslistview.CommonAdapter<GoodsCarInfo>(this, R.layout.indent_goods_item, goodsList) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final GoodsCarInfo item, final int position) {
                holder.setText(R.id.indent_item_title, item.getGoods_name());
                holder.setText(R.id.indent_item_price, "￥" + item.getGoods_price());
                ImageView icon = holder.getView(R.id.indent_item_icon);
                ImageLoader.getInstance().displayImage(item.getGoods_img(), icon);
                final TextView item_num_tv = holder.getView(R.id.goods_pop_num_tv);
                item_num_tv.setText(item.getGoods_num() + "");
                final ImageButton add_ib = holder.getView(R.id.goods_pop_ib_add);
                final ImageButton re_ib = holder.getView(R.id.goods_pop_ib_reduce);
                //设置当前的数量改变监听
                add_ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShopCount(true, item.getGoods_num(), position, item_num_tv, add_ib, re_ib);
                    }
                });
                re_ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShopCount(false, item.getGoods_num(), position, item_num_tv, add_ib, re_ib);
                    }
                });
            }
        };
        //设置数据适配器
        goods_lv.setAdapter(adapter);

        setGoodsPrice();
    }

    //设置当前的数量
    private void setShopCount(boolean add, int currentNum, int position, TextView itemCount, ImageButton ad, ImageButton re) {
        if (add) {
            //添加数量
            if (currentNum == 5) {
                return;
            }
            currentNum++;
        } else {
            //减少数量
            if (currentNum == 1) {
                return;
            }
            currentNum--;
        }
        if (currentNum > 1 && currentNum < 5) {
            re.setImageResource(R.mipmap.reduce_able);
        } else if (currentNum >= 5) {
            ad.setImageResource(R.mipmap.add_unable);
        } else {
            re.setImageResource(R.mipmap.reduce_unable);
            ad.setImageResource(R.mipmap.add_able);
        }

        goodsList.get(position).setGoods_num(currentNum);
        itemCount.setText("" + goodsList.get(position).getGoods_num());
        setGoodsPrice();
        adapter.notifyDataSetChanged();
    }

    //计算数量和价格的方法
    private void setGoodsPrice() {
        int num = 0;
        float price = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            num += goodsList.get(i).getGoods_num();//算出总数量
            price += Float.parseFloat(goodsList.get(i).getGoods_price()) * goodsList.get(i).getGoods_num();//算出总价格
        }
        price = (float) (Math.round(price * 100)) / 100;
        goods_args_tv.setText("共计" + num + "件商品  总计：￥" + price);
        par_tv.setText("实付：￥" + price);
    }

    private void initViews() {
        getSupportActionBar().hide();
        findViewById(R.id.title_back_iv).setOnClickListener(this);
        findViewById(R.id.title_right_tv).setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.title_center_tv);
        title.setText("确认订单");
        no_address = (TextView) findViewById(R.id.indent_no_address_tv);
        no_address.setOnClickListener(this);
        line_address = (LinearLayout) findViewById(R.id.indent_address);
        line_address.setOnClickListener(this);
        goods_lv = (InnerGridView) findViewById(R.id.indent_goods_lv);
        goods_args_tv = (TextView) findViewById(R.id.indent_goods_args_tv);
        par_tv = (TextView) findViewById(R.id.indent_pay_tv);
        addressDao = new AddressDao(this);
        findViewById(R.id.address_item_click).setVisibility(View.GONE);
        address_name = (TextView) findViewById(R.id.address_item_name);
        address_number = (TextView) findViewById(R.id.address_item_number);
        address_dis = (TextView) findViewById(R.id.address_item_dis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                CommonUtils.finishActivity(IndentActivity.this);
                break;
            //选择收货地址
            case R.id.indent_no_address_tv:
            case R.id.indent_address:
//                Intent intent = new Intent(this, AddressActivity.class);
//                startActivityForResult(intent, GETADDRESS_REQUESTCODE);
//                overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
                CommonUtils.startActivity(this, AddressActivity.class);
                break;
        }
    }

    public void indentPay(View view) {
        if (!TextUtils.isEmpty(address_id)) {
            //前往付款
//            CommonUtils.startActivity(this, PayDemoActivity.class);

            final OrderDao orderDao = new OrderDao(IndentActivity.this);
            final ArrayList<OrderInfo> listOrder = new ArrayList<>();

            for (int i = 0; i < goodsList.size(); i++) {
                OrderInfo orderInfo = new OrderInfo(goodsList.get(i).getGoods_id(), goodsList.get(i).getGoods_img(), goodsList.get(i).getGoods_name(),
                        goodsList.get(i).getGoods_num(), goodsList.get(i).getGoods_price(), OrderDao.WAIT_PAY, goodsList.get(i).getUser_name(), null,Integer.parseInt(CommonUtils.getSp("address_id")));
                listOrder.add(orderInfo);
            }
            CommonUtils.executeRunnalbe(new Runnable() {
                @Override
                public void run() {
                    orderDao.add(listOrder);//存入数据库
                }
            });

            Intent intent = new Intent(this, PayDemoActivity.class);
            intent.putExtra("listOrder", (Serializable) listOrder);
            startActivity(intent);
            overridePendingTransition(R.animator.xin_right, R.animator.xout_left);

        }else {
            Toast.makeText(this, "请填写收货地址！", Toast.LENGTH_SHORT).show();
        }
    }

}
