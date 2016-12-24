package com.rui.yunifang.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.GoodsActivity;
import com.rui.yunifang.activity.IndentActivity;
import com.rui.yunifang.activity.LoginActivity;
import com.rui.yunifang.activity.MainActivity;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.bean.GoodsCarInfo;
import com.rui.yunifang.db.GoodsCarDao;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.view.ShowingPage;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Cart_Fragment extends BaseFragment implements View.OnClickListener, SpringView.OnFreshListener {
    private static final String TAG = "TAG";
    public String s;
    private int shopCount = 1;
    private boolean edit = true;
    private TextView right_tv;
    private SpringView springView;
    private ListView listView;
    private RelativeLayout haveShop;
    private RelativeLayout noShop;
    private CommonAdapter adapter;
    private CheckBox checkAll;
    private ArrayList<GoodsCarInfo> listGoods;
    private LinearLayout setNumLayout;
    private int pop_tag;
    private ImageView pledge;
    private ImageView coupons;
    private TextView price_tv;
    private int selectCount = 0;
    private CheckBox item_check;
    private TextView item_num;
    private Button car_btn;
    private GoodsCarDao carDao;
    private TextView title;
    public static ArrayList<GoodsCarInfo> chioceList = new ArrayList<>();

    @Override
    protected void onLoad() {
        Cart_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
    }

    @Override
    protected View createSuccessView() {
        View rootView = CommonUtils.inflate(R.layout.fragment_car);
        rootView = initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();//初始化数据
    }

    private void initData() {
        carDao = new GoodsCarDao(getActivity());
        listGoods = carDao.query(CommonUtils.getSp("user_name"));
        initCurrentView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkAll != null)
            checkAll.setChecked(false);
    }

    //设置当前的布局界面
    private void initCurrentView() {
        //如果购物车中有商品
        if (listGoods.size() > 0) {
            title.setText("购物车(" + listGoods.size() + ")");
            haveShop.setVisibility(View.VISIBLE);
            noShop.setVisibility(View.GONE);
            right_tv.setVisibility(View.VISIBLE);
            setLvAdapter(listGoods);
        } else {
            title.setText("购物车");
            haveShop.setVisibility(View.GONE);
            noShop.setVisibility(View.VISIBLE);
            right_tv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    private View initView(View rootView) {
        rootView.findViewById(R.id.title_back_iv).setVisibility(View.INVISIBLE);
        title = (TextView) rootView.findViewById(R.id.title_center_tv);
        right_tv = (TextView) rootView.findViewById(R.id.title_right_tv);
        right_tv.setTextColor(getResources().getColor(R.color.colorTextMain));
        right_tv.setText("编辑");
        right_tv.setOnClickListener(this);
        //有商品的界面
        haveShop = (RelativeLayout) rootView.findViewById(R.id.car_rela_haveShop);
        //无商品的界面
        noShop = (RelativeLayout) rootView.findViewById(R.id.car_rela_noShop);
        rootView.findViewById(R.id.noshop_btn).setOnClickListener(this);//逛一逛
        springView = (SpringView) rootView.findViewById(R.id.car_springView);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);
        listView = (ListView) rootView.findViewById(R.id.car_listView);
        checkAll = (CheckBox) rootView.findViewById(R.id.car_all_check);
        price_tv = (TextView) rootView.findViewById(R.id.car_price_tv);
        car_btn = (Button) rootView.findViewById(R.id.car_btn);
        car_btn.setOnClickListener(this);
        checkAll.setOnClickListener(this);
        return rootView;
    }

    //设置数据适配器
    private void setLvAdapter(final ArrayList<GoodsCarInfo> listGoods) {
        adapter = new CommonAdapter<GoodsCarInfo>(getActivity(), R.layout.fragment_car_lv_item, listGoods) {
            @Override
            protected void convert(ViewHolder viewHolder, GoodsCarInfo item, final int position) {
                viewHolder.setText(R.id.car_item_name, listGoods.get(position).getGoods_name());
                viewHolder.setText(R.id.car_item_price, "￥" + listGoods.get(position).getGoods_price());
                item_num = viewHolder.getView(R.id.car_item_num);
                ImageView icon = viewHolder.getView(R.id.car_item_icon);
                final TextView pop_num_tv = viewHolder.getView(R.id.goods_pop_num_tv);
                pop_num_tv.setTag(position);
                Glide.with(getActivity())
                        .load(listGoods.get(position).getGoods_img())
                        .into(icon);
//                ImageLoader.getInstance().displayImage(listGoods.get(position).getGoods_img(), icon);
                pledge = viewHolder.getView(R.id.car_item_pledge);
                coupons = viewHolder.getView(R.id.car_item_coupons);
                final ImageButton add_ib = viewHolder.getView(R.id.goods_pop_ib_add);
                final ImageButton re_ib = viewHolder.getView(R.id.goods_pop_ib_reduce);
                //设置当前的数量改变监听
                add_ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShopCount(true, listGoods.get(position).getGoods_num(), position, pop_num_tv, add_ib, re_ib);
                    }
                });
                re_ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShopCount(false, listGoods.get(position).getGoods_num(), position, pop_num_tv, add_ib, re_ib);
                    }
                });
                item_check = viewHolder.getView(R.id.car_item_check);
                setNumLayout = viewHolder.getView(R.id.car_lv_num_layout);
                setNumLayout.setVisibility(View.GONE);
                item_check.setChecked(listGoods.get(position).isChick());
                item_num.setText("数量：" + listGoods.get(position).getGoods_num());
                setAdapterData(position);//设置其他数据
            }
        };
        listView.setAdapter(adapter);

        //点击条目跳转至商品详情界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                intent.putExtra("id", listGoods.get(position).getGoods_id());
                startActivity(intent);
                getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
            }
        });
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

        listGoods.get(position).setGoods_num(currentNum);
        itemCount.setText("" + listGoods.get(position).getGoods_num());
        adapter.notifyDataSetChanged();
    }

    //结算
    private void getSumPrice() {
        float priceSum = 0;
        for (int i = 0; i < listGoods.size(); i++) {
            if (listGoods.get(i).isChick()) {
                priceSum += Float.parseFloat(listGoods.get(i).getGoods_price()) * listGoods.get(i).getGoods_num();
            }
        }
        priceSum = (float) (Math.round(priceSum * 100)) / 100;
        price_tv.setText("总计：" + priceSum);
    }

    //设置适配器中的数据
    private void setAdapterData(final int position) {
        //设置优惠券和抵用券的显示隐藏
        if (listGoods.get(position).isGoods_coupons()) {
            coupons.setVisibility(View.VISIBLE);
        } else {
            coupons.setVisibility(View.INVISIBLE);
        }
        if (listGoods.get(position).isGoods_pledge()) {
            pledge.setVisibility(View.VISIBLE);
        } else {
            pledge.setVisibility(View.INVISIBLE);
        }
        //设置数量的隐藏显示
        if (right_tv.getText().toString().trim().equals("编辑")) {
            setNumLayout.setVisibility(View.GONE);
            price_tv.setVisibility(View.VISIBLE);
            item_num.setVisibility(View.VISIBLE);
        } else {
            setNumLayout.setVisibility(View.VISIBLE);
            item_num.setVisibility(View.INVISIBLE);
            price_tv.setVisibility(View.INVISIBLE);
        }

        //设置条目的选中状态
        item_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listGoods.get(position).isChick()) {
                    selectCount--;
                    listGoods.get(position).setChick(false);
                } else {
                    selectCount++;
                    listGoods.get(position).setChick(true);
                }
                if (selectCount >= listGoods.size()) {
                    checkAll.setChecked(true);
                } else {
                    checkAll.setChecked(false);
                }
                getSumPrice();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //购物车是空的，点击前去逛逛
            case R.id.noshop_btn:
                if (MyApplication.gotoShop) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    //移动至Home页
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.getVp().setCurrentItem(0);
                    RadioButton rbHome = (RadioButton) mainActivity.getRg().getChildAt(0);
                    rbHome.setChecked(true);
                    rbHome.setTextColor(getResources().getColor(R.color.colorTextMain));
                }
                break;
            //点击编辑或者完成
            case R.id.title_right_tv:
                if (right_tv.getText().toString().trim().equals("编辑")) {
                    right_tv.setText("完成");
                    car_btn.setText("删除");
                    item_num.setVisibility(View.INVISIBLE);
                    price_tv.setVisibility(View.VISIBLE);
                } else {
                    right_tv.setText("编辑");
                    car_btn.setText("结算");
                    price_tv.setVisibility(View.INVISIBLE);
                    item_num.setVisibility(View.VISIBLE);
                }
                edit = !edit;
                adapter.notifyDataSetChanged();
                break;
            case R.id.car_all_check:
                if (checkAll.isChecked()) {
                    selectCount = listGoods.size();
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).setChick(true);
                    }
                } else {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).setChick(false);
                    }
                    selectCount = 0;
                }
                getSumPrice();
                adapter.notifyDataSetChanged();
                break;
            //结算或者删除
            case R.id.car_btn:
                if (car_btn.getText().toString().equals("删除")) {
                    for (int i = listGoods.size() - 1; i > -1; i--) {
                        if (listGoods.get(i).isChick()) {
                            //删除选中的条目
                            carDao.delete(listGoods.get(i).getUser_name(), listGoods.get(i).getGoods_name());
                            listGoods.remove(i);
                        }
                    }

                    checkAll.setChecked(false);
                    initCurrentView();
                    adapter.notifyDataSetChanged();
                } else {
                    if (selectCount <= 0) {
                        Toast.makeText(getActivity(), "您还没有选中任何商品哦", Toast.LENGTH_SHORT).show();
                    } else {
                        chioceList.clear();
                        for (int i = 0; i < listGoods.size(); i++) {
                            if (listGoods.get(i).isChick()) {
                                chioceList.add(listGoods.get(i));
                            }
                        }
                        //前往确认订单
                        CommonUtils.startActivity(getActivity(), IndentActivity.class);
                    }
                }
                break;
        }
    }

    //获得选中的商品
    public static ArrayList<GoodsCarInfo> getGoods() {
        return chioceList;
    }

    @Override
    public void onRefresh() {
        stopLoad();
    }

    public void stopLoad() {
        springView.onFinishFreshAndLoad();
    }

    @Override
    public void onLoadmore() {
        stopLoad();
    }
}
