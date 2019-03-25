package com.hjm.tingshuo.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Adapter.LocalAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.LocalMusicBean;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Service.LocalMusicPlayService;
import com.hjm.tingshuo.Service.MusicPlayService;
import com.hjm.tingshuo.Utils.LocalMusicUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocalActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.tv_title)
    TextView textView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private ProgressDialog mProgressDialog;

    private List<LocalMusicBean> mLocalMusics;

    private LocalAdapter mAdapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_local;
    }

    @Override
    protected void initView() {
        textView.setText("本地音乐");
        mProgressDialog = ProgressDialog.show(LocalActivity.this,"","数据加载中！");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LocalAdapter(null);
        mAdapter.openLoadAnimation();
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mLocalMusics = LocalMusicUtils.getInstance().getDataFromLocal(LocalActivity.this);
        if (mLocalMusics != null){
            mProgressDialog.dismiss();
            mAdapter.setNewData(mLocalMusics);
        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.MusicPlayService")){
            Intent intent = new Intent(this, MusicPlayService.class);
            stopService(intent);
        }
        SPUtils.getInstance().put("local_position",position);
        com.hjm.tingshuo.Utils.IntentUtils.getInstance().SkipIntent(this, LocalPlayActivity.class);
    }



    @OnClick({R.id.iv_exit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_exit:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }

}
