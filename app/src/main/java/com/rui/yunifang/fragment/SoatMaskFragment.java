package com.rui.yunifang.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.GoodsActivity;
import com.rui.yunifang.activity.SoatMaskActivity;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.MaskInfo;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.ShowingPage;

/**
 * Created by 少明 on 2016/12/10.
 */
public class SoatMaskFragment extends BaseFragment implements SpringView.OnFreshListener {

    public static final int SUCCESS = 0;
    private static final String TAG = "TAG";
    private GridView maskGv;
    private CommonAdapter gv_adapter;
    private String getUrlId;
    private String currentUrl;
    private SpringView springView;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                MaskInfo maskInfo = (MaskInfo) msg.obj;
                initAdapterData(maskInfo);
            }
        }
    };

    @Override
    protected void onLoad() {
        SoatMaskFragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
    }

    //请求数据
    private void getData(String urlId, int time) {
        currentUrl = urlId;
        new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                MaskInfo maskInfo = gson.fromJson(data, MaskInfo.class);
                hd.obtainMessage(SUCCESS, maskInfo).sendToTarget();
            }

            @Override
            protected void setFailData(String error_type) {

            }
        }.getData(UrlUtils.SORT_URL + urlId, "", time, 0, false);
    }

    @Override
    protected View createSuccessView() {
        View rootView = CommonUtils.inflate(R.layout.soatmask_fragment);
        maskGv = (GridView) rootView.findViewById(R.id.soatmask_gv);
        springView = (SpringView) rootView.findViewById(R.id.soatmask_springView);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);
        //获取需要拼接的urlId
        getUrlId = getArguments().getString("urlId");
        getData(getUrlId, BaseData.NO_TIME);
        return rootView;
    }

    //设置数据适配器
    private void initAdapterData(final MaskInfo maskInfo) {
        gv_adapter = new CommonAdapter<MaskInfo.DataBean>(getActivity(), maskInfo.data, R.layout.home_gv_item) {
            @Override
            public void convert(ViewHolder holder, MaskInfo.DataBean item) {
                ImageView image = holder.getView(R.id.home_gv_item_iv);
                TextView title = holder.getView(R.id.home_gv_item_tv_title);
                TextView des = holder.getView(R.id.home_gv_item_tv_des);
                TextView price = holder.getView(R.id.home_gv_item_tv_price);
                TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);
                ImageLoader.getInstance().displayImage(item.goods_img, image);
                title.setText(item.goods_name);
                des.setText(item.efficacy);
                price.setText("￥" + item.shop_price);
                oldprice.setText("￥" + item.market_price);
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        };
        if (maskGv != null)
            maskGv.setAdapter(gv_adapter);
        maskGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                intent.putExtra("id", maskInfo.data.get(position).id);
                startActivity(intent);
            }
        });
    }

    //对外提供一个加载自己的方法
    public static Fragment getUrlData(String urlId) {
        SoatMaskFragment fragment = new SoatMaskFragment();
        Bundle bundle = new Bundle();
        //根据URLId的不同，获取不同的数据
        bundle.putString("urlId", urlId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh() {
        getData(currentUrl, BaseData.NO_TIME);
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
