package com.rui.yunifang.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.SortBean;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
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
public class Category_Fragment extends BaseFragment implements SpringView.OnFreshListener {

    private InnerGridView skin_gv;
    private String data;
    private ArrayList<Integer> colorSkinArray = new ArrayList<>();
    private InnerGridView star_gv;
    private DisplayImageOptions imageOptions;
    private SpringView springView;

    @Override
    protected void onLoad() {
        getData(BaseData.LONG_TIME);
    }

    @Override
    protected View createSuccessView() {
        View view = CommonUtils.inflate(R.layout.fragment_category);
        skin_gv = (InnerGridView) view.findViewById(R.id.category_skin_gv);
        star_gv = (InnerGridView) view.findViewById(R.id.category_star_gv);
        springView = (SpringView) view.findViewById(R.id.category_springView);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);//设置隐藏
        imageOptions = ImageLoaderUtils.initOptions();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (data != null) {
            initData();
        }
    }

    private void initData() {
        initSkinColor();
        Gson gson = new Gson();
        SortBean sortBean = gson.fromJson(data, SortBean.class);

        skin_gv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<SortBean.DataBean.CategoryBean.ChildrenBean>(getActivity(), R.layout.category_skin_tv_item, sortBean.getData().getCategory().get(2).getChildren()) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder viewHolder, SortBean.DataBean.CategoryBean.ChildrenBean item, int position) {
                TextView textView = viewHolder.getView(R.id.category_skin_tv);
                textView.setText("#" + item.getCat_name() + "#");
                textView.setBackgroundColor(colorSkinArray.get(position));
            }
        });

        star_gv.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<SortBean.DataBean.GoodsBriefBean>(getActivity(), R.layout.home_gv_item, sortBean.getData().getGoodsBrief()) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, SortBean.DataBean.GoodsBriefBean item, int position) {
                ImageView image = holder.getView(R.id.home_gv_item_iv);
                TextView title = holder.getView(R.id.home_gv_item_tv_title);
                TextView des = holder.getView(R.id.home_gv_item_tv_des);
                TextView price = holder.getView(R.id.home_gv_item_tv_price);
                TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);
                ImageLoader.getInstance().displayImage(item.getGoods_img(), image, imageOptions);
                title.setText(item.getEfficacy());
                des.setText(item.getGoods_name());
                price.setText("￥" + item.getShop_price());
                oldprice.setText("￥" + item.getMarket_price());
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
            protected void setResultData(String data) {
                Category_Fragment.this.data = data;
                Category_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
            }

            @Override
            protected void setFailData(String error_type) {

            }
        };
        baseData.getData(UrlUtils.SORT_URL, "", time, 0);
    }

    @Override
    public void onRefresh() {
        getData(BaseData.NO_TIME);
        stopLoad();
    }

    public void stopLoad() {
        springView.scrollTo(0, 0);
    }

    @Override
    public void onLoadmore() {
        stopLoad();
    }
}
