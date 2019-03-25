package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Adapter.SearchAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Service.LocalMusicPlayService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.et_input)
    EditText inputEt;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_searcher)
    TextView mSearchTv;

    private SearchAdapter mAdapter;
    private String title;
    private MusicBean mMusicBeans;
    private List<MusicBean.DataBean> mDataBeans;

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter(null,this);
        mAdapter.openLoadAnimation();
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        inputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    title = inputEt.getText().toString();
                    mLoadingDialog.show();
                    if (NetworkUtils.isConnected()) {
                        requestSearchJson(UrlValue.Search, title);
                    }else {
                        ToastUtils.showShort("网络连接已断开");
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("search")){
            mLoadingDialog.dismiss();
            mMusicBeans = mGson.fromJson(json,MusicBean.class);
            if (mMusicBeans.getCode() == 1){
                mDataBeans = mMusicBeans.getData();
                mAdapter.setNewData(mDataBeans);
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.LocalMusicPlayService")){
            Intent intent = new Intent(this, LocalMusicPlayService.class);
            stopService(intent);
        }
        SPUtils.getInstance().put("net_position",position);
        SPUtils.getInstance().put("type",UrlValue.Search+"?title="+title);
        SPUtils.getInstance().put("collection",false);
        com.hjm.tingshuo.Utils.IntentUtils.getInstance().SkipIntent(this, PlayActivity.class);
    }

    @OnClick({R.id.iv_exit})
    void onclick(View view){
        switch (view.getId()){
            case R.id.iv_exit:
                finish();
                break;
            default:
                break;
        }
    }

}
