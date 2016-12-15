package com.rui.yunifang.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.rui.yunifang.R;

/**
 * Created by 少明 on 2016/11/28.
 */
public class ImageLoaderUtils {

    public static void initConfiguration(Context context) {
        ImageLoaderConfiguration.Builder configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 4)
                // 一次性加载几张图片
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(30 * 1024 * 1024))
                .discCacheSize(500 * 1024 * 1024) // 缓冲大小
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)); // default

//---------------------------------------------------------------------
        ImageLoader.getInstance().init(configuration.build());
    }

    public static DisplayImageOptions initOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_2) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_2)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_2)  //设置图片加载/解码过程中错误时候显示的图片
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisc(true)
                .build();
        return options;
    }

    //设置圆形的图片
    public static DisplayImageOptions cycloOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_2)
                .showImageOnFail(R.drawable.default_2)
                .showImageOnLoading(R.drawable.default_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(90))
                .build();
        return options;
    }
}
