package com.zhengquan.zqlibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.zhengquan.zqlibrary.util.ToastUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/16/016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        setContentView(setLayoutId());
        //初始化数据
        initData();
        //view与数据绑定
        initView();
        //设置监听
        setListener();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    protected abstract int setLayoutId();

    /**
     * Activity的跳转
     */
    public void startActivity(Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        startActivity(intent);
    }

    /**
     * Activity的跳转-带参数
     */
    public void startActivity(Class<?> cla, Object obj) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        intent.putExtra("Object", (Serializable) obj);
        startActivity(intent);
    }

    protected void showLongToast(int resId) {

        ToastUtils.showLong(context, resId);
    }


    protected void showLongToast(String text) {
        ToastUtils.showLong(context, text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
