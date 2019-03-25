package com.hjm.tingshuo.Fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Activity.JustListenActivity;
import com.hjm.tingshuo.Activity.NewActivity;
import com.hjm.tingshuo.Adapter.Home2Adapter;
import com.hjm.tingshuo.Adapter.Home2GridAdapter;
import com.hjm.tingshuo.Base.BaseFragment;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Bean.MusicInfoBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Utils.FileUtils;
import com.hjm.tingshuo.Utils.IntentUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import butterknife.BindView;



/**
 * Created by linghao on 2018/9/30.
 */

public class FriendFragment extends BaseFragment implements View.OnClickListener,BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.grid_1)
    GridView mGrid1;
    @BindView(R.id.grid_2)
    GridView mGrid2;
    @BindView(R.id.recycler_new)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_more_1)
    Button mBtnmore1;
    @BindView(R.id.btn_more_2)
    Button mBtnmore2;

    private List<MusicInfoBean.DataBean> mDataBeans;
    private List<MusicBean.DataBean> mBeans1,mBeans2;

    private Api mApi;
    private File mFile1,mFile2,mFile3;
    private Home2Adapter mAdapter;
    private Home2GridAdapter mGridAdapter;


    @Override
    protected int getContentView() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView() {
        mBtnmore1.setOnClickListener(this);
        mBtnmore2.setOnClickListener(this);
        mRecyclerView.setFocusable(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new Home2Adapter(null, getActivity());
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        CreatFile();
        mMyProgressDialog.show();
        mApi = new Api(mHandler,getActivity());
        if (NetworkUtils.isConnected()){
            requestJson(UrlValue.Home_url+"10","json1");
            requestJson(UrlValue.Home_url+"1","json2");
            requestJson(UrlValue.Home_url2_3,"json3");
        }else {
            ReadLocalFile();
        }
    }


    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("json1")){
            setData1(json);
        }else if (TAG.equals("json2")){
            setData3(json);
        }else if (TAG.equals("json3")){
            setData3(json);
        }
    }

    //读取本地文件
    private void ReadLocalFile(){
        setData1(FileIOUtils.readFile2String(mFile1,"utf-8"));
        setData2(FileIOUtils.readFile2String(mFile2,"utf-8"));
        setData3(FileIOUtils.readFile2String(mFile3,"utf-8"));
    }


    //创建文件夹
    private void CreatFile(){
        mFile1 = new File(PathValue.json_directory+"json11.txt");
        mFile2 = new File(PathValue.json_directory+"json12.txt");
        mFile3 = new File(PathValue.json_directory+"json13.txt");
        if (!mFile1.exists()){
            try {
                mFile1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileUtils.getInstance().CreatFile(PathValue.json_directory+"json11.txt");
        }
        if (!mFile2.exists()){
            try {
                mFile2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileUtils.getInstance().CreatFile(PathValue.json_directory+"json12.txt");
        }
        if (!mFile3.exists()){
            try {
                mFile3.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileUtils.getInstance().CreatFile(PathValue.json_directory+"json13.txt");
        }
    }


    //
    public void setData1(String json){
        mMyProgressDialog.dismiss();
        mBeans1 = mGson.fromJson(json,MusicBean.class).getData();
        FileIOUtils.writeFileFromString(PathValue.json_directory+"json11.txt",json);
        mGridAdapter = new Home2GridAdapter(getActivity(),mBeans1);
        mGrid1.setAdapter(mGridAdapter);
    }

    public void setData2(String json){
        mMyProgressDialog.dismiss();
        mBeans2 = mGson.fromJson(json,MusicBean.class).getData();
        FileIOUtils.writeFileFromString(PathValue.json_directory+"json12.txt",json);
        mGridAdapter = new Home2GridAdapter(getActivity(),mBeans2);
        mGrid2.setAdapter(mGridAdapter);
    }

    public void setData3(String json){
        mMyProgressDialog.dismiss();
        mDataBeans = mGson.fromJson(json,MusicInfoBean.class).getData();
        FileIOUtils.writeFileFromString(PathValue.json_directory+"json13.txt",json);
        mAdapter.setNewData(mDataBeans);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        String str = mDataBeans.get(position).getInfo().replace("\\n","\n     ");
        singleChoice(mDataBeans.get(position).getTitle(),str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more_1:
                IntentUtils.getInstance().SkipIntent(getActivity(), JustListenActivity.class);
                break;
            case R.id.btn_more_2:
                IntentUtils.getInstance().SkipIntent(getActivity(), NewActivity.class);
                break;
                default:
                    break;
        }
    }



    /**弹出新专辑的信息对话框*/
    private void singleChoice(String title,String info){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_music_info,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        TextView textView = view.findViewById(R.id.tv_title);
        TextView textView1 = view.findViewById(R.id.tv_info);

        textView.setText("新专辑： "+title);
        textView1.setText(info);

        builder.setView(view);
        builder.create().show();
    }
}
