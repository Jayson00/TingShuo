package com.hjm.tingshuo.Request;

import android.content.Context;
import android.os.Handler;

import java.util.TreeMap;

/**
 * Created by linghao on 2018/12/17.
 */

public class Api {

    private HttpRequest mHttpRequest;
    private Handler mHandler;
    private Context context;

    public Api(Handler handler, Context context){
        mHttpRequest = new HttpRequest(handler,context);
        this.mHandler = handler;
    }


    /**获取json数据*/
    public void GetJson(String url, int what){
        mHttpRequest.RequestJson(url,what);
    }

    /**下载文件*/
    public void DownloadFile(String url, int what, String filedir, String filename){
        mHttpRequest.DownloadFile(url,what,filedir,filename);
    }


    /**登录(用户名，真实姓名，密码)*/
    public void Login(String url,String user,String pwd, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        mHttpRequest.userOperation(url,what,stringMap);
    }

    /**注销登录(用户名，真实姓名，密码)*/
    public void LoginOut(String url,String user,String pwd, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        mHttpRequest.userOperation(url,what,stringMap);
    }



    /**注册(用户名，密码，手机号，所在地)*/
    public void Regist(String url,String user,String pwd, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("pwd",pwd);
        mHttpRequest.userOperation(url,what,stringMap);
    }



    /**查询收藏的音乐*/
    public void QueryCollection(String url,String user,String songid,int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("songid",songid);
        mHttpRequest.userOperation(url,what,stringMap);
    }


    /**删除收藏的音乐*/
    public void DelCollection(String url,String user,String songid,int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("songid",songid);
        mHttpRequest.userOperation(url,what,stringMap);
    }


    /**获取收藏*/
    public void GetCollection(String url,String user, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        mHttpRequest.userOperation(url,what,stringMap);
    }

    /**添加收藏*/
    public void AddCollection(String url,String user,String title,String author,String duration,String songid,String pic,String lrc, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("user",user);
        stringMap.put("title",title);
        stringMap.put("author",author);
        stringMap.put("duration",duration);
        stringMap.put("songid",songid);
        stringMap.put("pic",pic);
        stringMap.put("lrc",lrc);
        mHttpRequest.userOperation(url,what,stringMap);
    }


    /**搜索音乐*/
    public void Search(String url,String title, int what){
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("title",title);
        mHttpRequest.userOperation(url,what,stringMap);
    }

}
