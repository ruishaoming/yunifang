package com.rui.yunifang.factory;

import android.support.v4.app.Fragment;

import com.rui.yunifang.fragment.Cart_Fragment;
import com.rui.yunifang.fragment.Category_Fragment;
import com.rui.yunifang.fragment.Home_Fragment;
import com.rui.yunifang.fragment.Mine_Fragment;

import java.util.HashMap;

/**
 * Created by 少明 on 2016/11/28.
 * Fragment的工厂
 */
public class FragmentFactory {

    private static HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

    public static Fragment getFragment(int position) {
        Fragment fragment = fragmentMap.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case 0:
                fragment = new Home_Fragment();
                break;
            case 1:
                fragment = new Category_Fragment();
                break;
            case 2:
                fragment = new Cart_Fragment();
                break;
            case 3:
                fragment = new Mine_Fragment();
                break;
        }
        fragmentMap.put(position, fragment);
        return fragment;
    }
}
