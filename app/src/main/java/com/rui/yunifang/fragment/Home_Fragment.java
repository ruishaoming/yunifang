package com.rui.yunifang.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.activity.DynamicActivity;
import com.rui.yunifang.activity.AllGoodsActivity;
import com.rui.yunifang.activity.GoodsActivity;
import com.rui.yunifang.activity.MoreShopActivity;
import com.rui.yunifang.R;
import com.rui.yunifang.activity.SubjectActivity;
import com.rui.yunifang.adapter.HomeLvRecyclerAdapter;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.HomeData;
import com.rui.yunifang.divider.SpacesItemDecoration;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.InnerGridView;
import com.rui.yunifang.view.RootViewPager;
import com.rui.yunifang.view.ShowingPage;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Home_Fragment extends BaseFragment implements SpringView.OnFreshListener, View.OnClickListener {

    private static final int SUCCESS = 0;
    private String data;
    private LinearLayout line_ad5;
    private DisplayImageOptions options;
    private HomeData homeData;
    private RootViewPager home_vp;
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<ImageView> dotList = new ArrayList<>();
    private int[] dotArray = {R.mipmap.vp_dot_check, R.mipmap.vp_dot_normal};
    private LinearLayout line_dot;
    private LinearLayout line_hot_week;
    private TextView tv_hotweek_title;
    private InnerGridView home_lv;
    private InnerGridView home_gv;
    private SpringView springView;
    private ViewPager yh_vp;
    private TextView home_tv_more;
    private PagerAdapter pagerAdapter;
    private CommonAdapter<HomeData.DataBean.SubjectsBean> lvCommonAdapter;
    private CommonAdapter<HomeData.DataBean.DefaultGoodsListBean> gvCommonAdapter;
    private InnerGridView ad5_gv;
    private View rootView;
    private ImageView iv_more;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==SUCCESS){
                initViewData();//更新数据
            }
        }
    };

    @Override
    protected void onLoad() {
        loadData(BaseData.LONG_TIME);
    }

    @Override
    protected View createSuccessView() {
        if (rootView == null) {
            rootView = CommonUtils.inflate(R.layout.fragment_home);
            initView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        springView = (SpringView) view.findViewById(R.id.home_springView);
        line_ad5 = (LinearLayout) view.findViewById(R.id.home_line_ad5);
        home_vp = (RootViewPager) view.findViewById(R.id.home_vp);
        line_dot = (LinearLayout) view.findViewById(R.id.home_line_dot);
        line_hot_week = (LinearLayout) view.findViewById(R.id.home_line_hot_week);
        tv_hotweek_title = (TextView) view.findViewById(R.id.home_line_hot_week_title);
        home_lv = (InnerGridView) view.findViewById(R.id.home_lv);
        home_gv = (InnerGridView) view.findViewById(R.id.home_gv);
        yh_vp = (ViewPager) view.findViewById(R.id.home_activity_vp);
        home_tv_more = (TextView) view.findViewById(R.id.home_bottom_tv_more);
        home_tv_more.setOnClickListener(this);
        ad5_gv = (InnerGridView) view.findViewById(R.id.home_gv_ad5);
        iv_more = (ImageView) view.findViewById(R.id.home_lv_more);

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(this);
        springView.setType(SpringView.Type.FOLLOW);//设置隐藏
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadData(int time) {
        new BaseData() {
            @Override
            protected void setResultData(String data) {
                if (TextUtils.isEmpty(data)) {
                    Home_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_EMPTY);
                } else {
                    Home_Fragment.this.data = data;
                    Home_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
                }
                options = ImageLoaderUtils.initOptions();
                Gson gson = new Gson();
                homeData = gson.fromJson(data, HomeData.class);
                handler.obtainMessage(SUCCESS).sendToTarget();
            }

            @Override
            protected void setFailData(String error_type) {
                Home_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_ERROR);
            }
        }.getData(UrlUtils.HOME_URl, "", time, 0, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (springView != null)
            springView.scrollTo(0, 0);
        if (homeData != null)
            home_lv.setFocusable(false);
    }

    private void initViewData() {

        //ViewPager无限轮播
        initViewPager();

        initAd5Data();

        initYhVpData();

//        tv_hotweek_title.setText("~~~ " + homeData.data.bestSellers.get(0).name + " ~~~");

        initHotWeek(line_hot_week);

        initLvData();
        initGvData();
    }

    //设置顶部ViewPager
    private void initViewPager() {
        line_dot.removeAllViews();
        imageList.clear();
        dotList.clear();
        for (int i = 0; i < homeData.data.ad1.size(); i++) {
            imageList.add(homeData.data.ad1.get(i).image);

            ImageView imageView = new ImageView(getActivity());
            if (i == 0) {
                imageView.setImageResource(dotArray[0]);
            } else {
                imageView.setImageResource(dotArray[1]);
            }
            dotList.add(imageView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            line_dot.addView(imageView, params);
        }
        home_vp.startViewPager(imageList, dotList, dotArray);
        home_vp.setOnRootViewPagerClickListener(new RootViewPager.OnRootViewPagerClickListener() {
            @Override
            public void setOnPage(int position) {
                if (homeData.data.ad1.get(position % imageList.size()).ad_type_dynamic_data != null)
                    homeStartActivity(DynamicActivity.class, homeData.data.ad1.get(position % imageList.size()).ad_type_dynamic_data, homeData.data.ad1.get(position % imageList.size()).ad_type_dynamic);
            }
        });
    }

    //每日签到、积分商城、兑换专区、真伪查询
    private void initAd5Data() {
        ad5_gv.setAdapter(new CommonAdapter<HomeData.DataBean.Ad5Bean>(getActivity(), homeData.data.ad5, R.layout.home_line_item) {
            @Override
            public void convert(ViewHolder holder, HomeData.DataBean.Ad5Bean item) {
                ImageView home_line_item_iv = holder.getView(R.id.home_line_item_iv);
                TextView home_line_item_tv = holder.getView(R.id.home_line_item_tv);
                ImageLoader.getInstance().displayImage(item.image, home_line_item_iv, options);
                home_line_item_tv.setText(item.title);
//                home_line_item_tv.setTextSize(CommonUtils.dip2px(13));
//                //设置权重
//                home_line_item.setLayoutParams(new LinearLayout.LayoutParams(0, 250, 1.0f));
//                line_ad5.addView(home_line_item);
            }
        });

        ad5_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeStartActivity(DynamicActivity.class, homeData.data.ad5.get(position).ad_type_dynamic_data, homeData.data.ad5.get(position).title);
            }
        });
//        line_ad5.removeAllViews();
//        for (int i = 0; i < homeData.data.ad5.size(); i++) {
//            View home_line_item = CommonUtils.inflate(R.layout.home_line_item);
//            home_line_item.findViewById(R.id.hom_line_item).setOnClickListener(this);
//            ImageView home_line_item_iv = (ImageView) home_line_item.findViewById(R.id.home_line_item_iv);
//            TextView home_line_item_tv = (TextView) home_line_item.findViewById(R.id.home_line_item_tv);
//            ImageLoader.getInstance().displayImage(homeData.data.ad5.get(i).image, home_line_item_iv, options);
//            home_line_item_tv.setText(homeData.data.ad5.get(i).title);
//            //设置权重
//            home_line_item.setLayoutParams(new LinearLayout.LayoutParams(0, 250, 1.0f));
//            line_ad5.addView(home_line_item);
//        }
    }

    //本周热销
    private void initHotWeek(LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320, 450);
        params.setMargins(18, 0, 0, 0);
        for (int i = 0; i < 6; i++) {
            View hotweek_item = CommonUtils.inflate(R.layout.home_line_hotweek_item);
            ImageView hotweek_iv = (ImageView) hotweek_item.findViewById(R.id.home_line_hot_week_iv);
            AutoLinearLayout hotWeek_item = (AutoLinearLayout) hotweek_item.findViewById(R.id.home_line_hotweek_item);
            hotWeek_item.setOnClickListener(this);
            TextView hotweek_title = (TextView) hotweek_item.findViewById(R.id.home_line_hot_week_tv_title);
            TextView hotweek_price = (TextView) hotweek_item.findViewById(R.id.home_line_hot_week_tv_price);
            TextView hotweek_oldprice = (TextView) hotweek_item.findViewById(R.id.home_line_hot_week_tv_oldPrice);
            ImageLoader.getInstance().displayImage(homeData.data.bestSellers.get(0).goodsList.get(i).goods_img, hotweek_iv, options);
            hotweek_title.setText(homeData.data.bestSellers.get(0).goodsList.get(i).goods_name);
            hotweek_price.setText("￥" + homeData.data.bestSellers.get(0).goodsList.get(i).shop_price);
            hotweek_oldprice.setText("￥" + homeData.data.bestSellers.get(0).goodsList.get(i).market_price);
            hotweek_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            hotweek_item.setLayoutParams(params);
            linearLayout.addView(hotweek_item);
        }
//        ImageView hotweek_right_iv = new ImageView(getActivity());
//        hotweek_right_iv.setBackgroundColor(getResources().getColor(R.color.moreColor));
//        hotweek_right_iv.setPadding(60,60,60,60);
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("week_hot", homeData.data.bestSellers.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        hotweek_right_iv.setImageResource(R.mipmap.home_rank_list_more);
//        LinearLayout.LayoutParams hotweek_right_iv_params = new LinearLayout.LayoutParams(250, 120);
//        hotweek_right_iv_params.setMargins(20, 0, 20, 0);
//        hotweek_right_iv.setLayoutParams(hotweek_right_iv_params);
//        linearLayout.addView(hotweek_right_iv);
    }

    //优惠活动
    private void initYhVpData() {
//        if (pagerAdapter == null) {
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(getActivity());
                ImageLoader.getInstance().displayImage(homeData.data.activityInfo.activityInfoList.get(position % homeData.data.activityInfo.activityInfoList.size()).activityImg, imageView, options);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                imageView.setPadding(1, 1, 1, 1);
                imageView.setBackgroundResource(R.drawable.share_square);
                container.addView(imageView, params);
                return imageView;
            }
        };
        yh_vp.setAdapter(pagerAdapter);
//        } else {
//            pagerAdapter.notifyDataSetChanged();
//        }
        yh_vp.setOffscreenPageLimit(3);
        yh_vp.setPageMargin(30);
        yh_vp.setCurrentItem(100 * homeData.data.activityInfo.activityInfoList.size());
    }

    //ListView（大图 + RecyclerView）
    private void initLvData() {
//        if (lvCommonAdapter == null) {
        lvCommonAdapter = new CommonAdapter<HomeData.DataBean.SubjectsBean>(getActivity(), homeData.data.subjects, R.layout.home_lv_item) {
            @Override
            public void convert(ViewHolder holder, final HomeData.DataBean.SubjectsBean item) {
                ImageView imageView = holder.getView(R.id.home_lv_item_iv);
                ImageView more_iv = holder.getView(R.id.home_lv_item_more);
                more_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoSubject(item);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoSubject(item);
                    }
                });
                ImageLoader.getInstance().displayImage(item.image, imageView, options);
                RecyclerView home_lv_recy = holder.getView(R.id.home_lv_item_recycler);

                home_lv_recy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                home_lv_recy.addItemDecoration(new SpacesItemDecoration(5));//设置间距
                HomeLvRecyclerAdapter recyclerAdapter = new HomeLvRecyclerAdapter(getActivity(), item.goodsList);
                home_lv_recy.setAdapter(recyclerAdapter);
                recyclerAdapter.setOnitemClickListener(new HomeLvRecyclerAdapter.OnitemClickListener() {
                    @Override
                    public void itemClickListener(int position) {
                        Intent intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("id", item.goodsList.get(position).id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
                    }
                });
//                    LinearLayout home_lv_item_line = holder.getView(R.id.home_lv_item_line);
                //initLvitem(home_lv_item_line, item);
            }
        };
        home_lv.setAdapter(lvCommonAdapter);
//        } else {
//            lvCommonAdapter.notifyDataSetChanged();
//        }
    }

    private void gotoSubject(HomeData.DataBean.SubjectsBean item) {
        Intent intent = new Intent(getActivity(), SubjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("subject", item);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
    }

    //底部GridView
    private void initGvData() {
//        if (gvCommonAdapter == null) {
        gvCommonAdapter = new CommonAdapter<HomeData.DataBean.DefaultGoodsListBean>(getActivity(), homeData.data.defaultGoodsList, R.layout.home_gv_item) {
            @Override
            public void convert(ViewHolder holder, HomeData.DataBean.DefaultGoodsListBean item) {
                ImageView image = holder.getView(R.id.home_gv_item_iv);
                TextView title = holder.getView(R.id.home_gv_item_tv_title);
                TextView des = holder.getView(R.id.home_gv_item_tv_des);
                TextView price = holder.getView(R.id.home_gv_item_tv_price);
                TextView oldprice = holder.getView(R.id.home_gv_item_tv_oldPrice);

                ImageLoader.getInstance().displayImage(item.goods_img, image, options);
                title.setText(item.efficacy);
                des.setText(item.goods_name);
                price.setText("￥" + item.shop_price);
                oldprice.setText("￥" + item.market_price);
                oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        };
        home_gv.setAdapter(gvCommonAdapter);
//        } else {
//            gvCommonAdapter.notifyDataSetChanged();
//        }
        home_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                intent.putExtra("id", homeData.data.defaultGoodsList.get(position).id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
            }
        });
    }

    //跳转的方法
    private void homeStartActivity(Class activity, String url, String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        loadData(BaseData.NO_TIME);
        stopLoad();
    }

    //设置springView的位置
    public void stopLoad() {
//        springView.scrollTo(0, 0);
        springView.onFinishFreshAndLoad();
    }

    //------上拉加载
    @Override
    public void onLoadmore() {

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_bottom_tv_more:
                CommonUtils.startActivity(getActivity(), AllGoodsActivity.class);
                break;
            case R.id.hom_line_item:
                for (int i = 0; i < line_ad5.getChildCount(); i++) {
                    if (line_ad5.getChildAt(i) == v) {
//                        homeStartActivity(DynamicActivity.class, homeData.data.ad5.get(i).ad_type_dynamic_data, homeData.data.ad5.get(i).title);
                    }
                }
                break;
            //本周热销条目点击
            case R.id.home_line_hotweek_item:
                for (int i = 0; i < line_hot_week.getChildCount(); i++) {
                    if (line_hot_week.getChildAt(i) == v) {
                        Intent intent = new Intent(getActivity(), GoodsActivity.class);
                        intent.putExtra("id", homeData.data.bestSellers.get(0).goodsList.get(i).id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }
}
