package com.hjm.tingshuo.Base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gyf.barlibrary.ImmersionBar;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.MyView.MyProgressDialog;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Utils.FileUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {


    protected Gson mGson;
    public ExecutorService cachedThreadExecutor;
    public Handler mHandler;
    public HashMap<String,String> hashMap;
    private RxPermissions mRxPermissions;
    public MyProgressDialog mLoadingDialog;
    public MyProgressDialog mLoginDialog;
    public MyProgressDialog mLoginOutDialog;
    public MyProgressDialog mRegistDialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(Color.parseColor("#ffff0000"));
        setContentView(getContentView());

        ButterKnife.bind(this);
        mGson = new Gson();
        cachedThreadExecutor = Executors.newCachedThreadPool();
        mHandler =  new Handler();
        mRxPermissions = new RxPermissions(this);
        requestPermissions();
        mLoadingDialog = new MyProgressDialog(this, R.style.LoadingDialog,R.layout.dialog_loading);
        mLoginDialog = new MyProgressDialog(this, R.style.LoadingDialog,R.layout.dialog_login);
        mLoginOutDialog = new MyProgressDialog(this, R.style.LoadingDialog,R.layout.dialog_loginout);
        mRegistDialog = new MyProgressDialog(this, R.style.LoadingDialog,R.layout.dialog_regist);

        initView();
    }


    protected abstract int getContentView();

    protected abstract void initView();


    //请求权限
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermissions(){
        mRxPermissions.request( Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.ACCESS_NETWORK_STATE)

                .subscribe(new io.reactivex.functions.Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean){
                            Toast.makeText(BaseActivity.this,"为保证应用正常运行,请在权限管理开启必要权限!",Toast.LENGTH_SHORT).show();
                        }else {
                            System.out.println("权限已全部获得，创建文件夹");
                            FileUtils.getInstance().CreatDirs(new String[]{PathValue.Home_directory,PathValue.music_directory,PathValue.lyric_directory,PathValue.lyric_directory});
                        }
                    }
                });
    }


    //设置标题栏颜色。
    public void setStatusBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            //给状态栏设置颜色。设置透明。
            window.setStatusBarColor(color);
        }
    }


    /**获取json数据*/
    public void requestJson(String url){
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"json");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**获取json数据*/
    public void requestDownloadparms(String url){
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"downloadprepare");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }

    /**获取收藏的json数据*/
    public void requestGetCollectJson(String url,String user){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"getcollect");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }

    /**获取收藏的json数据*/
    public void requestRecommendJson(String url,String user){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"recommend");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }

    /**登录*/
    public void requestLoginJson(String url,String user,String pwd){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"login");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**注销登录*/
    public void requestLoginOutJson(String url,String user,String pwd){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"loginout");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**注册*/
    public void requestRegistJson(String url,String user,String pwd){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"regist");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**查询收藏的音乐*/
    public void requestQueryCollectJson(String url,String user,String songid){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("songid",songid);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"querycollect");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**查询收藏的音乐*/
    public void requestDeletCollectJson(String url,String user,String songid){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("songid",songid);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"deletcollect");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**查询收藏的音乐*/
    public void requestAddCollectJson(String url,String user,String title,String author,String duration,String songid,String pic,String lrc){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("title",title);
        stringMap.put("author",author);
        stringMap.put("duration",duration);
        stringMap.put("songid",songid);
        stringMap.put("pic",pic);
        stringMap.put("lrc",lrc);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"addcollect");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }


    /**搜索音乐*/
    public void requestSearchJson(String url,String title){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("title",title);
        OkGo.<String>get(url)
                .params(stringMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onClientSuccess(response.body().toString().trim(),"search");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("服务器连接失败！");
                    }
                });
    }




    /**下载文件*/
    public void DownloadFile(String url, final String filedir, final String filename){
        OkGo.<File>get(url)
                .execute(new FileCallback(filedir,filename) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        downloadFileSuccess();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        downloadFileFail();
                    }
                });
    }

    protected abstract void onClientSuccess( String json , String TAG);

    protected  void downloadFileSuccess(){
        ToastUtils.showShort("下载成功");
    };

    protected  void downloadFileFail(){
        ToastUtils.showShort("下载失败");
    };


}
