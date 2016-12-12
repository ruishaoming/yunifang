package com.rui.yunifang.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.rui.yunifang.utils.ImageLoaderUtils;

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

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化操作
        ImageLoaderUtils.initConfiguration(getApplicationContext());
        context = getApplicationContext();
        handler = new Handler();//创建Handle
        mainThreadId = Process.myTid();//得到主线程id
        threadPool = Executors.newFixedThreadPool(5);//创建线程池
        x.Ext.init(this);
        x.Ext.setDebug(true);
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
