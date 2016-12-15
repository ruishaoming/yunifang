package com.rui.yunifang.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;

import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.ImageLoaderUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.x;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 少明 on 2016/11/28.
 */
public class MyApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    private static ExecutorService threadPool;
    public static boolean isLogin = false;//判定是否登录
    public static boolean gotoShop = false;//逛一逛

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化操作
        AutoLayoutConifg.getInstance().useDeviceSize();
        ImageLoaderUtils.initConfiguration(getApplicationContext());
        context = getApplicationContext();
        handler = new Handler();//创建Handle
        mainThreadId = Process.myTid();//得到主线程id
        threadPool = Executors.newFixedThreadPool(5);//创建线程池
        x.Ext.init(this);
        x.Ext.setDebug(true);
        UMShareAPI.get(this);
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        String user_name = CommonUtils.getSp("user_name");
        if (!TextUtils.isEmpty(user_name)) {
            MyApplication.isLogin = true;
        }
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}
