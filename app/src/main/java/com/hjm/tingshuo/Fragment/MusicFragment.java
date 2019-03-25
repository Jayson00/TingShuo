package com.hjm.tingshuo.Fragment;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Activity.PlayActivity;
import com.hjm.tingshuo.Adapter.Home1Adapter;
import com.hjm.tingshuo.Base.BaseFragment;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Service.LocalMusicPlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * Created by linghao on 2018/9/30.
 */

public class MusicFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recycler_music)
    RecyclerView mRecyclerView;

    Home1Adapter mAdapter;
    List<MusicBean.DataBean> mDataBeans;

    private int Type = 2;

    @Override
    protected int getContentView() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new Home1Adapter(null,getActivity());
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mApi = new Api(mHandler,getActivity());

        mMyProgressDialog.show();

        if (NetworkUtils.isConnected()){
            requestJson(UrlValue.Home_url+"2","music");
        }else {
            ReadLocalFile();
        }
    }


    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("music")){
            mMyProgressDialog.dismiss();
            mDataBeans = mGson.fromJson(json,MusicBean.class).getData();
            FileIOUtils.writeFileFromString(PathValue.json_directory+"json1.txt",json);
            mAdapter.setNewData(mDataBeans);
        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.LocalMusicPlayService")){
            Intent intent = new Intent(getActivity(), LocalMusicPlayService.class);
            getActivity().stopService(intent);
        }
        SPUtils.getInstance().put("net_position",position);
        SPUtils.getInstance().put("type",UrlValue.Home_url+Type);
        com.hjm.tingshuo.Utils.IntentUtils.getInstance().SkipIntent(getActivity(), PlayActivity.class);
    }


    /**读取本地缓存数据*/
    private void ReadLocalFile(){
        mMyProgressDialog.dismiss();
        File file = new File(PathValue.json_directory+"json1.txt");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDataBeans = mGson.fromJson(FileIOUtils.readFile2String(new File(PathValue.json_directory+"json1.txt")),MusicBean.class).getData();
        mAdapter.setNewData(mDataBeans);
    }



    /**接受分类界面的返回信息，更新UI*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer type){
        Type = type;
        System.out.println("接收到消息："+type);
        requestJson(UrlValue.Home_url ,"music");
    }


    /**销毁mHandle*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
