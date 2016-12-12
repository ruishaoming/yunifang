package com.rui.yunifang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rui.yunifang.R;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.NetUtils;
import com.rui.yunifang.utils.UrlUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

/**
 * create : 芮靖林
 * time : 2016/11/29
 * LaunchActivity : 实现导航界面，广告界面，主界面数据初始化
 */
public class LaunchActivity extends AutoLayoutActivity implements View.OnClickListener {

    private RelativeLayout launchloadingpage;
    private ViewPager launchloadingvp;
    private RelativeLayout launchloadingslogan;
    private boolean auroStart = true;
    private int i = 5;
    private Button launchloadingbtn;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    launchloadingvp.setVisibility(View.GONE);
                    launchloadingpage.setVisibility(View.GONE);
                    launchloadingslogan.setVisibility(View.VISIBLE);
                    initMainData();
                    CommonUtils.executeRunnalbe(new Runnable() {
                        @Override
                        public void run() {
                            for (i = 5; i > -1; i--) {
                                if (auroStart) {
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    msg.arg1 = i;
                                    hd.sendMessage(msg);
                                }
                                SystemClock.sleep(1000);
                            }
                        }
                    });
                    break;
                case 1:
                    initVPData();
                    launchloadingpage.setVisibility(View.GONE);
                    launchloadingslogan.setVisibility(View.GONE);
                    launchloadingvp.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    int index = msg.arg1;
                    if (index == 0) {
                        launchStartActivity();
                        return;
                    }
                    launchloadingbtn.setText("跳过" + i + "s");
                    break;
            }
        }
    };

    /**
     * 初始化主界面的数据
     */
    private void initMainData() {
        int netWorkType = NetUtils.getNetWorkType(CommonUtils.getContext());
        if (netWorkType != NetUtils.NETWORKTYPE_INVALID) {
            CommonUtils.executeRunnalbe(new Runnable() {
                @Override
                public void run() {
                    BaseData baseData = new BaseData() {
                        @Override
                        protected void setResultData(String data) {

                        }

                        @Override
                        protected void setFailData(String error_type) {

                        }
                    };
                    baseData.getData(UrlUtils.HOME_URl, "", BaseData.NO_TIME, 0, true);
                    baseData.getData(UrlUtils.CATEGORY_URL, "", BaseData.NO_TIME, 0, true);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        getSupportActionBar().hide();
        initView();
        final boolean noFirstLaunch = CommonUtils.getBoolean("launch");
        CommonUtils.executeRunnalbe(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                if (noFirstLaunch) {
                    hd.sendEmptyMessage(0);
                } else {
                    CommonUtils.putBoolean("launch", true);
                    hd.sendEmptyMessage(1);
                }
            }
        });

    }

    //初始化数据
    private void initVPData() {
        final ArrayList<Integer> picList = new ArrayList<>();
        picList.add(R.mipmap.launch_guidance_1);
        picList.add(R.mipmap.launch_guidance_2);
        picList.add(R.mipmap.launch_guidance_3);
        picList.add(R.mipmap.launch_guidance_4);
        launchloadingvp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return picList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(LaunchActivity.this);
                imageView.setImageResource(picList.get(position));
                if (position == 3) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launchStartActivity();
                        }
                    });
                }
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }

    private void initView() {
        launchloadingpage = (RelativeLayout) findViewById(R.id.launch_loading_page);
        launchloadingvp = (ViewPager) findViewById(R.id.launch_loading_vp);
        launchloadingslogan = (RelativeLayout) findViewById(R.id.launch_loading_slogan);
        launchloadingbtn = (Button) findViewById(R.id.launch_loading_btn);
        launchloadingbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launch_loading_btn:
                launchStartActivity();
                break;
        }
    }

    private void launchStartActivity() {
        auroStart = false;
        CommonUtils.startActivity(LaunchActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                hd.removeCallbacksAndMessages(null);
                break;
        }
    }
}
