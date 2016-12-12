package com.rui.yunifang.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by 少明 on 2016/12/1.
 */
public class RootViewPager extends ViewPager {

    private OnRootViewPagerClickListener onRootViewPagerClickListener;
    private static final int START = 0;
    private DisplayImageOptions imageOptions;
    private ArrayList<String> imageList;
    private int[] dotArray;
    private RootVpAdapter rootVpAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START) {
                //开启无限轮播
                int currentItem = RootViewPager.this.getCurrentItem();
                currentItem++;
                RootViewPager.this.setCurrentItem(currentItem);
                this.sendEmptyMessageDelayed(START, 2000);
            }
        }
    };

    public RootViewPager(Context context) {
        super(context);
        init();
    }

    public RootViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化
    private void init() {
        imageOptions = ImageLoaderUtils.initOptions();
    }

    public void startViewPager(final ArrayList<String> imageList, final ArrayList<ImageView> dotList, final int[] dotArray) {
        this.imageList = imageList;
        this.dotArray = dotArray;
        this.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < imageList.size(); i++) {
                    if (position % imageList.size() == i) {
                        dotList.get(i).setImageResource(dotArray[0]);
                    } else {
                        dotList.get(i).setImageResource(dotArray[1]);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (rootVpAdapter == null) {
            rootVpAdapter = new RootVpAdapter();
        }
        this.setAdapter(rootVpAdapter);
        handler.sendEmptyMessageDelayed(START, 3000);
        this.setCurrentItem(imageList.size() * 100);//设置初始位置
    }

    private class RootVpAdapter extends PagerAdapter implements OnTouchListener {

        private float downX;
        private float downY;
        private long downTime;
        private int position;

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
            this.position = position;
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(imageList.get(position % imageList.size()), imageView, imageOptions);
            container.addView(imageView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setOnTouchListener(this);
            return imageView;
        }

        //imageView的滑动及点击事件
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);
                    downTime = System.currentTimeMillis();
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();
                    //当按下的位置与抬起的位置相同，且时间在1秒以内时，视作点击事件
                    if (upX == downX && upY == downY && System.currentTimeMillis() - downTime < 1000) {
                        onRootViewPagerClickListener.setOnPage(position);
                    }
                case MotionEvent.ACTION_CANCEL:
                    handler.sendEmptyMessageDelayed(START, 2000);
                    break;
                default:
                    handler.removeCallbacksAndMessages(null);
                    break;
            }
            return true;
        }
    }

    //当前界面不可见时调用
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    //定义一个接口
    public interface OnRootViewPagerClickListener {
        void setOnPage(int position);
    }

    public void setOnRootViewPagerClickListener(OnRootViewPagerClickListener onRootViewPagerClickListener) {
        this.onRootViewPagerClickListener = onRootViewPagerClickListener;
    }
}
