package com.rui.yunifang.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.R;
import com.rui.yunifang.adapter.ViewHolder;
import com.rui.yunifang.application.MyApplication;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.CommonAdapter;
import com.rui.yunifang.bean.GoodsCarInfo;
import com.rui.yunifang.bean.GoodsDesc;
import com.rui.yunifang.bean.GoodsInfo;
import com.rui.yunifang.db.GoodsCarDao;
import com.rui.yunifang.fragment.Cart_Fragment;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.GoodsScrollView;
import com.rui.yunifang.view.InnerGridView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

//详情页
public class GoodsActivity extends AutoLayoutActivity implements View.OnClickListener, GoodsScrollView.OnScrollListener {

    private static final String TAG = "TAG";
    private static final int SUCCESS = 0;
    private TextView goods_title;
    private WindowManager mWindowManager;
    private ViewPager goods_vp;
    private GoodsInfo goodsInfo;
    private GoodsScrollView scrollView;
    private View title;
    private LinearLayout top;
    private int screenWidth;
    private int searchLayoutTop;
    private ListView btm_lv;
    private TextView top_par;
    private TextView top_args;
    private TextView top_comment;
    private String id;
    private TextView gods_price;
    private TextView goods_oldprice;
    private TextView sell_type;
    private TextView collect_num;
    private TextView sell_num;
    private LinearLayout top_line;
    private LinearLayout bottom_line;
    private LinearLayout line_getY;
    private int pop_tag = 1;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                initData();
            }
        }
    };
    private PopupWindow popWindow;
    private ImageButton add_ib;
    private ImageButton re_ib;
    private TextView pop_num_tv;
    private boolean joinCar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        getSupportActionBar().hide();

        initView();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        goods_title.setText(goodsInfo.data.goods.goods_name);
        top_comment.setText("评论(" + goodsInfo.data.commentNumber + ")");
        gods_price.setText("￥" + goodsInfo.data.goods.shop_price);
        goods_oldprice.setText("￥" + goodsInfo.data.goods.market_price);
        collect_num.setText("" + goodsInfo.data.goods.collect_count);
        sell_num.setText("" + goodsInfo.data.goods.sales_volume);
        initTopViewPager();
        initBottomImage();
    }

    private void initBottomComment() {
        btm_lv.setAdapter(new CommonAdapter<GoodsInfo.DataBean.CommentsBean>(GoodsActivity.this, goodsInfo.data.comments, R.layout.goods_comments_item) {
            @Override
            public void convert(ViewHolder viewHolder, GoodsInfo.DataBean.CommentsBean item) {
                GridView item_gv = viewHolder.getView(R.id.goods_comment_item_gv);
                ImageView icon = viewHolder.getView(R.id.goods_comment_icon);
                TextView username = viewHolder.getView(R.id.goods_comment_username);
                TextView time = viewHolder.getView(R.id.goods_comment_time);
                TextView content = viewHolder.getView(R.id.goods_comment_contnet);
                ImageLoader.getInstance().displayImage(item.user.icon, icon, ImageLoaderUtils.cycloOption());
                username.setText(item.user.nick_name);
                time.setText(item.createtime);
                content.setText(item.content);
            }
        });
    }

    private void initBottomArgs() {
        btm_lv.setAdapter(new CommonAdapter<GoodsInfo.DataBean.GoodsBean.AttributesBean>(GoodsActivity.this, goodsInfo.data.goods.attributes, R.layout.goods_args_lv_item) {
            @Override
            public void convert(ViewHolder holder, GoodsInfo.DataBean.GoodsBean.AttributesBean item) {
                TextView attr_name = holder.getView(R.id.goods_args_tv_attr_name);
                TextView attr_value = holder.getView(R.id.goods_args_tv_attr_value);
                attr_name.setText(item.attr_name);
                attr_value.setText(item.attr_value);
            }
        });
    }

    //初始化底部图片
    private void initBottomImage() {
        String goods_desc = goodsInfo.data.goods.goods_desc;
        Gson gson = new Gson();
        final ArrayList<String> listImge = new ArrayList<>();
        final GoodsDesc[] goodsDescs = gson.fromJson(goods_desc, GoodsDesc[].class);
        for (int i = 0; i < goodsDescs.length; i++) {
            listImge.add(goodsDescs[i].url);
        }
        btm_lv.setAdapter(new CommonAdapter<String>(GoodsActivity.this, listImge, R.layout.iv_layout) {
            @Override
            public void convert(ViewHolder holder, String item) {
                ImageView image = holder.getView(R.id.public_iv);
                ImageLoader.getInstance().displayImage(item, image);
            }
        });
        btm_lv.setFocusable(false);
    }

    private void initTopViewPager() {
        goods_vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return goodsInfo.data.goods.gallery.size();
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
                ImageView imageView = new ImageView(GoodsActivity.this);
                ImageLoader.getInstance().displayImage(goodsInfo.data.goods.gallery.get(position).normal_url, imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView);
                return imageView;
            }
        });
    }

    private void initView() {
        goods_title = (TextView) findViewById(R.id.goods_title);
        gods_price = (TextView) findViewById(R.id.goods_price);
        goods_oldprice = (TextView) findViewById(R.id.goods_oldprice);
        sell_type = (TextView) findViewById(R.id.goods_sell_type);
        collect_num = (TextView) findViewById(R.id.goods_collect_num);
        sell_num = (TextView) findViewById(R.id.goods_sell_num);
        findViewById(R.id.shop_title_back).setOnClickListener(this);
        ImageView title_car = (ImageView) findViewById(R.id.shop_title_car);
        title_car.setOnClickListener(this);
        ImageView title_share = (ImageView) findViewById(R.id.shop_title_share);
        title_share.setOnClickListener(this);

        goods_vp = (ViewPager) findViewById(R.id.goods_vp);
        scrollView = (GoodsScrollView) findViewById(R.id.goods_scrollView);
        title = findViewById(R.id.goods_top_title);
        top = (LinearLayout) findViewById(R.id.goods_line_top);
        top_line = (LinearLayout) findViewById(R.id.goods_top_line);
        bottom_line = (LinearLayout) findViewById(R.id.goods_bottom_line);
        line_getY = (LinearLayout) findViewById(R.id.goods_line_getY);

        findViewById(R.id.goods_joinCar_tv).setOnClickListener(this);
        findViewById(R.id.goods_buy_tv).setOnClickListener(this);

        scrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        btm_lv = (ListView) findViewById(R.id.goods_bottom_iv_lv);
        btm_lv.setFocusable(false);
        top_par = (TextView) findViewById(R.id.goods_top_particulars);
        top_args = (TextView) findViewById(R.id.goods_top_args);
        top_comment = (TextView) findViewById(R.id.goods_top_comment);
        top_par.setOnClickListener(this);
        top_args.setOnClickListener(this);
        top_comment.setOnClickListener(this);
        id = getIntent().getStringExtra("id");//接收参数
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            searchLayoutTop = line_getY.getBottom();//获取searchLayout的顶部位置
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= searchLayoutTop) {
            if (top.getParent() != top_line) {
                bottom_line.removeView(top);
                top_line.addView(top);
                top_line.setVisibility(View.VISIBLE);
            }
        } else {
            if (top.getParent() != bottom_line) {
                top_line.removeView(top);
                bottom_line.addView(top);
                top_line.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        BaseData baseData = new BaseData() {
            @Override
            protected void setResultData(String data) {
                Gson gson = new Gson();
                goodsInfo = gson.fromJson(data, GoodsInfo.class);
                if (goodsInfo != null) {
                    hd.obtainMessage(SUCCESS).sendToTarget();
                }
            }

            @Override
            protected void setFailData(String error_type) {

            }
        };
        baseData.getData(UrlUtils.GOODS_URL + id, "", BaseData.NO_TIME, 0, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //产品详情
            case R.id.goods_top_particulars:
                top_par.setTextColor(getResources().getColor(R.color.colorTextMain));
                top_args.setTextColor(getResources().getColor(R.color.colortvGray));
                top_comment.setTextColor(getResources().getColor(R.color.colortvGray));
                initBottomImage();
                break;
            //产品参数
            case R.id.goods_top_args:
                top_par.setTextColor(getResources().getColor(R.color.colortvGray));
                top_args.setTextColor(getResources().getColor(R.color.colorTextMain));
                top_comment.setTextColor(getResources().getColor(R.color.colortvGray));
                initBottomArgs();
                break;
            //评论
            case R.id.goods_top_comment:
                top_par.setTextColor(getResources().getColor(R.color.colortvGray));
                top_args.setTextColor(getResources().getColor(R.color.colortvGray));
                top_comment.setTextColor(getResources().getColor(R.color.colorTextMain));
                initBottomComment();
                break;
            case R.id.shop_title_back:
                CommonUtils.finishActivity(GoodsActivity.this);
                break;
            //加入购物车、
            case R.id.goods_joinCar_tv:
                if (!MyApplication.isLogin) {
                    CommonUtils.startActivity(GoodsActivity.this, LoginActivity.class);
                    return;
                }
                joinCar = true;
                if (goodsInfo == null) {
                    return;
                }
                showPopwindow();
                break;
            //立即购买
            case R.id.goods_buy_tv:
                if (!MyApplication.isLogin) {
                    CommonUtils.startActivity(GoodsActivity.this, LoginActivity.class);
                    return;
                }
                joinCar = false;
                if (goodsInfo == null) {
                    return;
                }
                showPopwindow();
                break;
            //弹出窗确定购买
            case R.id.goods_pop_btn:
                if (joinCar) {
                    joinCarMethod();
                    Toast.makeText(GoodsActivity.this, "成功加入购物车！", Toast.LENGTH_SHORT).show();
                } else {
                    //确定购买
                    Cart_Fragment.chioceList = new ArrayList<>();
                    String user_name = CommonUtils.getSp("user_name");
                    GoodsCarInfo goods = new GoodsCarInfo(goodsInfo.data.goods.is_coupon_allowed,
                            goodsInfo.data.goods.id, goodsInfo.data.goods.goods_img, goodsInfo.data.goods.goods_name,pop_tag, true,String.valueOf(goodsInfo.data.goods.shop_price), user_name, true);
                    Cart_Fragment.chioceList.add(goods);
                    CommonUtils.startActivity(GoodsActivity.this,IndentActivity.class);
                }
                closePopupWindow();
                break;
            //PopupWindow添加数量
            case R.id.goods_pop_ib_add:
                if (pop_tag == goodsInfo.data.goods.restrict_purchase_num) {
                    return;
                }
                pop_tag++;
                initPopTag();
                break;
            //PopupWindow减少数量
            case R.id.goods_pop_ib_reduce:
                if (pop_tag == 1) {
                    return;
                }
                pop_tag--;
                initPopTag();
                break;
            //右上角购物车按钮
            case R.id.shop_title_car:
                MyApplication.gotoShop = true;
                //开启事务添加Fragment
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.goods_frame, new Cart_Fragment());
                transaction.addToBackStack("car_fragment");
                transaction.commit();
                overridePendingTransition(R.animator.xin_right, R.animator.xout_left);
                break;
            //右上角分享按钮
            case R.id.shop_title_share:
                showPopwindowShare();
                break;
            //关闭popupWindow
            case R.id.goods_pop_close:
                //分享取消
            case R.id.share_but_cancel:
                closePopupWindow();
                break;
            //QQ分享
            case R.id.share_btn_qq:
                new ShareAction(GoodsActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withTitle(goodsInfo.data.goods.goods_name)
                        .withTargetUrl(UrlUtils.GOODS_URL + id)
                        .setCallback(umShareListener)
                        .share();
                break;
        }
    }

    //展示分享的PopUpWindow
    private void showPopwindowShare() {
        // 利用layoutInflater获得View
        View view = CommonUtils.inflate(R.layout.pop_share);
        initPopViewShare(view);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        popWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popWindow.setFocusable(true);
        setBackgroundAlpha();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                closePopupWindow();
                return false;
            }
        });
        // 设置popWindow的显示和消失动画
        popWindow.setAnimationStyle(R.style.popupAnimation);
        // 在底部显示
        popWindow.showAtLocation(goods_vp, Gravity.BOTTOM, 0, 0);
    }

    private void initPopViewShare(View view) {
        Button share_cancle = (Button) view.findViewById(R.id.share_but_cancel);
        share_cancle.setOnClickListener(this);
        Button share_qq = (Button) view.findViewById(R.id.share_btn_qq);
        share_qq.setOnClickListener(this);
    }

    //开启线程加入购物车
    private void joinCarMethod() {
        CommonUtils.executeRunnalbe(new Runnable() {
            @Override
            public void run() {
                GoodsCarDao goodsCarDao = new GoodsCarDao(GoodsActivity.this);
                GoodsCarInfo goodsCarInfo = new GoodsCarInfo();
                goodsCarInfo.setUser_name(CommonUtils.getSp("user_name"));
                goodsCarInfo.setGoods_name(goodsInfo.data.goods.goods_name);
                goodsCarInfo.setGoods_id(id);
                goodsCarInfo.setGoods_img(goodsInfo.data.goods.gallery.get(0).normal_url);
                goodsCarInfo.setGoods_price(goodsInfo.data.goods.shop_price + "");
                goodsCarInfo.setGoods_num(pop_tag);
                goodsCarInfo.setGoods_coupons(goodsInfo.data.goods.is_coupon_allowed);//可使用优惠券
                goodsCarInfo.setGoods_pledge(true);//可抵
                goodsCarDao.add(goodsCarInfo);
            }
        });
    }

    private void initPopTag() {
        if (pop_tag > 1 && pop_tag < 5) {
            re_ib.setImageResource(R.mipmap.reduce_able);
        } else if (pop_tag == goodsInfo.data.goods.restrict_purchase_num) {
            add_ib.setImageResource(R.mipmap.add_unable);
        } else {
            re_ib.setImageResource(R.mipmap.reduce_unable);
            add_ib.setImageResource(R.mipmap.add_able);
        }
        pop_num_tv.setText("" + pop_tag);
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {

        // 利用layoutInflater获得View
        View view = CommonUtils.inflate(R.layout.goods_pop);
        initPopView(view);
        initPopTag();

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        popWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popWindow.setFocusable(true);
        setBackgroundAlpha();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                closePopupWindow();
                return false;
            }
        });
        // 设置popWindow的显示和消失动画
        popWindow.setAnimationStyle(R.style.popupAnimation);
        // 在底部显示
        popWindow.showAtLocation(title, Gravity.BOTTOM, 0, 0);
    }

    //初始化PopWindow控件
    private void initPopView(View view) {
        ImageView pop_icon = (ImageView) view.findViewById(R.id.goods_pop_icon);
        ImageView pop_close_iv = (ImageView) view.findViewById(R.id.goods_pop_close);
        add_ib = (ImageButton) view.findViewById(R.id.goods_pop_ib_add);
        re_ib = (ImageButton) view.findViewById(R.id.goods_pop_ib_reduce);
        pop_num_tv = (TextView) view.findViewById(R.id.goods_pop_num_tv);
        TextView limit_tv = (TextView) view.findViewById(R.id.goods_pop_limit_tv);
        TextView price_tv = (TextView) view.findViewById(R.id.goods_pop_price_tv);
        TextView save_tv = (TextView) view.findViewById(R.id.goods_pop_savenum_tv);
        Button pop_btn = (Button) view.findViewById(R.id.goods_pop_btn);
        pop_btn.setOnClickListener(this);
        pop_close_iv.setOnClickListener(this);
        add_ib.setOnClickListener(this);
        re_ib.setOnClickListener(this);
//        ImageLoader.getInstance().displayImage(goodsInfo.data.goods.gallery.get(0).normal_url, pop_icon);
        price_tv.setText("￥" + goodsInfo.data.goods.shop_price);
        limit_tv.setText("限购" + goodsInfo.data.goods.restrict_purchase_num + "件");
        save_tv.setText("库存" + goodsInfo.data.goods.stock_number + "件");
        Glide.with(GoodsActivity.this)
                .load(goodsInfo.data.goods.gallery.get(0).normal_url)
                .error(R.drawable.default_1)
                .into(pop_icon);
    }

    //设置当前窗口背景背景
    public void setBackgroundAlpha() {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.7f;
        this.getWindow().setAttributes(params);
    }

    //关闭PopWindow
    private void closePopupWindow() {
        if (popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
            popWindow = null;
            WindowManager.LayoutParams params = this.getWindow().getAttributes();
            params.alpha = 1f;
            this.getWindow().setAttributes(params);
        }
    }

    //友盟第三方分享
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(GoodsActivity.this, platform + " 成功分享！", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(GoodsActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(GoodsActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
