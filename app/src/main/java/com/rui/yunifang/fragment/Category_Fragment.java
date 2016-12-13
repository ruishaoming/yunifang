package com.rui.yunifang.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.AllGoodsActivity;
import com.rui.yunifang.activity.GoodsActivity;
import com.rui.yunifang.activity.SoatMaskActivity;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.SortBean;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.InnerGridView;
import com.rui.yunifang.view.ShowingPage;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Category_Fragment extends BaseFragment implements SpringView.OnFreshListener, View.OnClickListener {

    private static final String TAG = "TAG";
    private InnerGridView skin_gv;
    private String data;
    private ArrayList<Integer> colorSkinArray = new ArrayList<>();
    private InnerGridView star_gv;
    private SpringView springView;
    private View rootView;
    private SortBean sortBean;

    @Override
    protected void onLoad() {
        getData(BaseData.LONG_TIME);
    }

    @Override
    protected View createSuccessView() {
        if (rootView == null) {
            rootView = CommonUtils.inflate(R.layout.fragment_category);
            initView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        skin_gv = (InnerGridView) view.findViewById(R.id.category_skin_gv);
        star_gv = (InnerGridView) view.findViewById(R.id.category_star_gv);
        springView = (SpringView) view.findViewById(R.id.category_springView);
        view.findViewById(R.id.category_mask_normal).setOnClickListener(this);
        view.findViewById(R.id.category_emollient_water).setOnClickListener(this);
        view.findViewById(R.id.category_body_lotion).setOnClickListener(this);
        view.findViewById(R.id.category_facial_cleanser).setOnClickListener(this);
        view.findViewById(R.id.category_other).setOnClickListener(this);
        view.findViewById(R.id.category_kit).setOnClickListener(this);
        view.findViewById(R.id.category_hydrating).setOnClickListener(this);
        view.findViewById(R.id.category_soothing).setOnClickListener(this);
        view.findViewById(R.id.category_control_oil).setOnClickListener(this);
        view.findViewById(R.id.category_whitening).setOnClickListener(this);
        view.findViewById(R.id.category_firming).setOnClickListener(this);
        view.findViewById(R.id.category_allshop_tv).setOnClickListener(this);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);//设置隐藏
        skin_gv.setFocusable(false);
        star_gv.setFocusable(false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        initSkinColor();

        skin_gv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<SortBean.DataBean.CategoryBean.ChildrenBean>(getActivity(), R.layout.category_skin_tv_item, sortBean.data.category.get(2).children) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder viewHolder, SortBean.DataBean.CategoryBean.ChildrenBean item, int position) {
                TextView textView = viewHolder.getView(R.id.category_skin_tv);
                textView.setText("#" + item.cat_name + "#");
                textView.setBackgroundColor(colorSkinArray.get(position));
            }
        });
        skin_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enterActivity(sortBean.data.category.get(2), null, position);
            }
        });
        star_gv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<SortBean.DataBean.GoodsBriefBean>(getActivity(), R.layout.home_gv_item, sortBean.data.goodsBrief) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, SortBean.DataBean.GoodsBriefBean item, int position) {
                ImageView image = holder.getView(R.id.home_gv_item_iv);
                TextView title = holder.getView(R.id.home_gv_item_tv_title);
                TextView des = holder.getView(R.id.home_gv_item_tv_des);
                TextView price = holder.getView(R.id.home_gv_item_tv_price);
                TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);
                ImageLoader.getInstance().displayImage(item.goods_img, image);
                title.setText(item.efficacy);
                des.setText(item.goods_name);
                price.setText("￥" + item.shop_price);
                oldprice.setText("￥" + item.market_price);
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
        star_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                intent.putExtra("id", sortBean.data.goodsBrief.get(position).id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
            }
        });
    }

    private void initSkinColor() {
        colorSkinArray.clear();
        colorSkinArray.add(getResources().getColor(R.color.colorSkin0));
        colorSkinArray.add(getResources().getColor(R.color.colorSkin1));
        colorSkinArray.add(getResources().getColor(R.color.colorSkin2));
        colorSkinArray.add(getResources().getColor(R.color.colorSkin3));
        colorSkinArray.add(getResources().getColor(R.color.colorSkin4));
        colorSkinArray.add(getResources().getColor(R.color.colorSkin5));
    }

    private void getData(int time) {

        BaseData baseData = new BaseData() {

            @Override
            protected void setResultData(final String data) {
                Category_Fragment.this.data = data;
                Category_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        sortBean = gson.fromJson(data, SortBean.class);
                        if (sortBean != null) {
                            initData();
                        }
                    }
                });
            }

            @Override
            protected void setFailData(String error_type) {

            }
        };
        baseData.getData(UrlUtils.CATEGORY_URL, "", time, 0,true);
    }

    @Override
    public void onRefresh() {
        getData(BaseData.NO_TIME);
        stopLoad();
    }

    public void stopLoad() {
        springView.onFinishFreshAndLoad();
    }

    @Override
    public void onLoadmore() {
        stopLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //属性
            case R.id.category_mask_normal:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(0), 0);
                break;
            case R.id.category_kit:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(5), 33);
                break;
            case R.id.category_emollient_water:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(1), 39);
                break;
            case R.id.category_body_lotion:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(2), 40);
                break;
            case R.id.category_facial_cleanser:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(3), 24);
                break;
            case R.id.category_other:
                enterActivity(sortBean.data.category.get(1), sortBean.data.category.get(1).children.get(4), 35);
                break;

            //按功效
            case R.id.category_hydrating:
                enterActivity(sortBean.data.category.get(0), null, 0);
                break;
            case R.id.category_soothing:
                enterActivity(sortBean.data.category.get(0), null, 1);
                break;
            case R.id.category_control_oil:
                enterActivity(sortBean.data.category.get(0), null, 2);
                break;
            case R.id.category_whitening:
                enterActivity(sortBean.data.category.get(0), null, 3);
                break;
            case R.id.category_firming:
                enterActivity(sortBean.data.category.get(0), null, 4);
                break;

            //查询所有商品
            case R.id.category_allshop_tv:
                CommonUtils.startActivity(getActivity(), AllGoodsActivity.class);
                break;

        }
    }

    private void enterActivity(SortBean.DataBean.CategoryBean bean, SortBean.DataBean.CategoryBean.ChildrenBean child, int id) {
        Intent intent = new Intent(getActivity(), SoatMaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mask", bean);
        bundle.putSerializable("child", child);
        intent.putExtras(bundle);
        intent.putExtra("id", id);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
    }
}
