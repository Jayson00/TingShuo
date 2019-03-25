package com.hjm.tingshuo.Request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hjm.tingshuo.Utils.FileUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.Map;


/**
 * Created by linghao on 2018/12/17.
 */

public class HttpRequest {

    private Handler mHandler;
    private Context mContext;

    public HttpRequest(Handler handler, Context context) {
        this.mHandler = handler;
        this.mContext = context;
    }


    /**请求json数据*/
    public void RequestJson(String url, final int what){
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = what;
                        message.obj = response.body().toString().trim();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = -1;
                        mHandler.sendMessage(message);
                        super.onError(response);
                    }
                });
    }


    /**登录*/
    public void userOperation(String url, final int what, final Map<String,String> paramsMap){
        OkGo.<String>post(url)
                .params(paramsMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = what;
                        message.obj = response.body().toString().trim();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = what;
                        message.obj = -1;
                        mHandler.sendMessage(message);
                        super.onError(response);
                    }
                });
    }


    /**下载文件*/
    public void DownloadFile(String url, final int what, final String filedir, final String filename){
        OkGo.<File>get(url)
                .execute(new FileCallback(filedir,filename) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = what;
                        message.obj = response.body().toString().trim();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        Message message = mHandler.obtainMessage();
                        message.what = what;
                        message.obj = -1;
                        mHandler.sendMessage(message);
                        super.onError(response);
                    }
                });
    }
}
