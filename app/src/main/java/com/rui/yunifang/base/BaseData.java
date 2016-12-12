package com.rui.yunifang.base;

import android.text.TextUtils;
import android.util.Log;

import com.rui.yunifang.utils.CommonUtils;
import com.rui.yunifang.utils.LogUtils;
import com.rui.yunifang.utils.Md5Encoder;
import com.rui.yunifang.utils.NetUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 少明 on 2016/11/28.
 */
public abstract class BaseData {

    public static final int NO_TIME = 0;//要最新的数据
    public static final int SHORT_TIME = 60 * 60 * 1000;//一小时
    public static final int LONG_TIME = 24 * 60 * 60 * 1000;//一天
    private final File fileDir;

    public BaseData() {
        File cacheDir = CommonUtils.getContext().getCacheDir();
        fileDir = new File(cacheDir, "yunifang_cache");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    public void getData(String path, String args, int time, int index,boolean joinCache) {
        if (time == 0) {
            getDataFromNet(path, args, time, index,joinCache);
        } else {
            String data = getDataFromLocal(path, args, time, index);
            if (TextUtils.isEmpty(data)) {
                //如果本地没有数据，则请求网络
                getDataFromNet(path, args, time, index,joinCache);
            } else {
                //将本地获取到的数据返回
                setResultData(data);
            }
        }
    }

    /**
     * setResultData：得到获取的数据
     * setFailData ： 数据请求失败
     *
     * @param data
     */
    protected abstract void setResultData(String data);

    protected abstract void setFailData(String error_type);

    /**
     * 网络请求数据
     *
     * @param path
     * @param time
     * @param index
     */
    private void getDataFromNet(final String path, String args, int time, int index, final boolean joinCache) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(path + args)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                setFailData("网络请求错误。");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String data = response.body().string();
                int netWorkType = NetUtils.getNetWorkType(CommonUtils.getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    //当没有网络时
                    setFailData("网络异常");
                } else {
                    setResultData(data);//将数据作返回
                    if (joinCache) {
                        CommonUtils.executeRunnalbe(new Runnable() {
                            @Override
                            public void run() {
                                saveDataToLocal(path, data);//将数据存入本地
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取本地数据
     *
     * @param path
     * @param time
     * @param index @return
     */
    private String getDataFromLocal(String path, String args, int time, int index) {
        BufferedReader bufferedReader = null;
        try {
            File file = new File(fileDir, Md5Encoder.encode(path));
            bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            //去除存储的时间，作比较
            String lineTime = bufferedReader.readLine();
            long saveTime = Long.parseLong(lineTime);
            if (System.currentTimeMillis() - saveTime < time) {
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将数据写入本地
     *
     * @param path
     * @param data
     */
    private void saveDataToLocal(String path, String data) {
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(fileDir, Md5Encoder.encode(path));
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bufferedWriter.write(System.currentTimeMillis() + "\r\n");//存一下当前的时间
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
