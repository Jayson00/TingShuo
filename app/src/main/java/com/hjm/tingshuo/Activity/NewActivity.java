package com.hjm.tingshuo.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Adapter.Home1Adapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Service.LocalMusicPlayService;

import java.util.List;

import butterknife.BindView;

public class NewActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.tv_title)
    TextView mTextView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private Home1Adapter mAdapter;
    private List<MusicBean.DataBean> mDataBeans;

    @Override
    protected int getContentView() {
        return R.layout.activity_new;
    }

    @Override
    protected void initView() {
        mTextView.setText("新歌");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Home1Adapter(null,this);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoadingDialog.show();
        if (NetworkUtils.isConnected()) {
            requestJson(UrlValue.Home_url + "1");
        }else {
            ToastUtils.showShort("网络连接已断开");
        }
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("json")){
            mLoadingDialog.dismiss();
            mDataBeans = mGson.fromJson(json,MusicBean.class).getData();
            mAdapter.setNewData(mDataBeans);
        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.LocalMusicPlayService")){
            Intent intent = new Intent(this, LocalMusicPlayService.class);
            stopService(intent);
        }
        SPUtils.getInstance().put("net_position",position);
        SPUtils.getInstance().put("type",UrlValue.Home_url+"1");
        SPUtils.getInstance().put("collection",false);
        com.hjm.tingshuo.Utils.IntentUtils.getInstance().SkipIntent(this, PlayActivity.class);
    }

}
