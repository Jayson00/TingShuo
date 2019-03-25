package com.hjm.tingshuo.Base;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hjm.tingshuo.MyView.MyProgressDialog;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import butterknife.ButterKnife;

/**
 * Created by linghao on 2018/9/30.
 */

public abstract class BaseFragment extends Fragment {

    public Api mApi;
    public Handler mHandler;
    public Gson mGson;
    ExecutorService cachedThreadExecutor;
    public MyProgressDialog mMyProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(),null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        ButterKnife.bind(this,view);
        mGson = new Gson();
        cachedThreadExecutor = Executors.newCachedThreadPool();
        mHandler = new Handler();
        mMyProgressDialog = new MyProgressDialog(getActivity(), R.style.LoadingDialog,R.layout.dialog_loading);
        initView();
        return view;
    }

    /**获取json数据*/
    public void requestJson(String url, final String TAG){
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),TAG);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }

    protected abstract void onClientSuccess(String json,String TAG);
    protected abstract int getContentView();
    protected abstract void initView();



}
