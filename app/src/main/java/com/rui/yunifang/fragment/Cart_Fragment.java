package com.rui.yunifang.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rui.yunifang.base.BaseData;
import com.rui.yunifang.base.BaseFragment;
import com.rui.yunifang.utils.UrlUtils;
import com.rui.yunifang.view.ShowingPage;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 少明 on 2016/11/28.
 */
public class Cart_Fragment extends BaseFragment {
    public String s;

    @Override
    protected void onLoad() {
        BaseData baseData = new BaseData() {
            @Override
            protected void setResultData(String data) {
                Cart_Fragment.this.s = data;
                Cart_Fragment.this.showCurrentPage(ShowingPage.StateType.STATE_LOAD_SUCCESS);
            }

            @Override
            protected void setFailData(String error_type) {
                
            }
        };
        baseData.getData(UrlUtils.HOME_URl, "",BaseData.SHORT_TIME, 0,false);

    }

    @Override
    protected View createSuccessView() {
        ImageView imageView = new ImageView(getActivity());
        ImageLoader.getInstance().displayImage("http://img1.gtimg.com/sports/pics/hv1/169/148/2135/138866284.jpg", imageView);
        return imageView;
    }
}
