package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
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
import com.hjm.tingshuo.Adapter.CollectAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.UserBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Service.LocalMusicPlayService;
import com.hjm.tingshuo.Service.MusicPlayService;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class RecommendActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.tv_title)
    TextView textView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    CollectAdapter mAdapter;
    private Api mApi;
    private UserBean mUserBeans = new UserBean();

    @Override
    protected int getContentView() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initView() {
        textView.setText("推荐");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectAdapter(null,this);
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("recommend")){
            mLoadingDialog.dismiss();
            mUserBeans = mGson.fromJson(json,UserBean.class);
            mAdapter.setNewData(mUserBeans.getData());
        }
    }


    @Override
    protected void onStart() {
        if (NetworkUtils.isConnected()) {
            requestRecommendJson(UrlValue.Recommend, UserUtils.getInstance().getUser());
        }else {
            ToastUtils.showShort("网络连接已断开");
        }
        super.onStart();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.LocalMusicPlayService")){
            Intent intent = new Intent(this, LocalMusicPlayService.class);
            stopService(intent);
        }
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.MusicPlayService")){
            Intent intent = new Intent(this, MusicPlayService.class);
            stopService(intent);
        }
        SPUtils.getInstance().put("net_position",position);
        SPUtils.getInstance().put("type",UrlValue.Recommend);
        SPUtils.getInstance().put("collection",true);
        IntentUtils.getInstance().SkipIntent(this, PlayActivity.class);
    }

    @OnClick({R.id.iv_exit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_exit:
                finish();
                break;
            default:
                break;
        }
    }

}
