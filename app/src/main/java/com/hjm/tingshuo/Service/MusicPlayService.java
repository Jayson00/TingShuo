package com.hjm.tingshuo.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Bean.PlayBean;
import com.hjm.tingshuo.Constant.ModeValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.MusicServiceAidl;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Utils.NotificationUtils;
import com.hjm.tingshuo.Utils.UserUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MusicPlayService extends Service {

    /**广播*/
    public static final String OPEN_NET_AUDIO = "com.MusicPlayService.play";
    public static final String NEXT_NET_AUDIO = "com.MusicPlayService.next";
    public static final String PRE_NET_AUDIO = "com.MusicPlayService.pre";

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<MusicBean.DataBean> mDataBeans = new ArrayList<>();
    private MusicPlayService service = MusicPlayService.this;
    private PlayBean mPlayBean;
    private MyReceiver myReceiver;
    private Api mApi;
    private String url = "";
    private int position = 0;
    private String type = UrlValue.Home_url+"2";
    int times = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**获取播放的位置信息（用sp主要是为了解耦）*/
        position = SPUtils.getInstance().getInt("net_position");
        type = SPUtils.getInstance().getString("type");
        System.out.println("type"+type);
        /**注册广播*/
        RegistReceiver();

        mApi = new Api(mHandler,this);
        if (SPUtils.getInstance().getBoolean("collection")){
            mApi.GetCollection(type, UserUtils.getInstance().getUser(),8088);
        }else {
            mApi.GetJson(type,8088);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**注册动态广播接收器(接收Notification的广播)*/
    private void RegistReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("PreService");
        intentFilter.addAction("PlayService");
        intentFilter.addAction("NextService");
        registerReceiver(myReceiver,intentFilter);
    }

    class MyReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("PreService")){
                System.out.println("net,接收到上一首的广播");
                service.pre();
            }else if (action.equals("PlayService")){
                System.out.println("net,接收到播放或暂停的广播");
                if (service.isPlaying()){
                    service.pause();
                }else {
                    service.play();
                }
            }else if (action.equals("NextService")){
                System.out.println("net,接收到下一首的广播");
                service.next();
            }
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 8088:
                    mDataBeans = new Gson().fromJson((String)msg.obj,MusicBean.class).getData();
                    System.out.println("mDataBeans:"+mDataBeans);
                    mApi.GetJson(UrlValue.Play + mDataBeans.get(position).getSongid(),8089);
                    System.out.println("请求地址："+UrlValue.Play + mDataBeans.get(position).getSongid());
                    break;
                case 8089:
                    parseJson(msg);
                    break;
                case 8090:
                    parseJson(msg);
                    start();
                    break;
            }
        }
    };

    /**解析json数据*/
    private void parseJson(Message msg){
        mPlayBean = new Gson().fromJson((String)msg.obj,PlayBean.class);
        if (mPlayBean != null){
            url = mPlayBean.getBitrate().getFile_link();
        }
    }



    /**实现Aidl文件*/
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
    private MusicServiceAidl.Stub stub = new MusicServiceAidl.Stub() {

        @Override
        public void prepare() throws RemoteException {
            service.prepare();
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void play() throws RemoteException {
            service.play();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getTitle() throws RemoteException {
            return service.getTitle();
        }

        @Override
        public String getAuthor() throws RemoteException {
            return service.getAuthor();
        }

        @Override
        public String getUrl() throws RemoteException {
            return service.getUrl();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode() throws RemoteException {
            service.setPlayMode();
        }

        @Override
        public String getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }
    };


    public void  prepare(){
        /**验证是否第一次打开该音乐（首次打开和暂停后打开执行方法不同）*/
        SPUtils.getInstance().put("net_first","1");
        if (EmptyUtils.isEmpty(SPUtils.getInstance().getString("playmode"))){
            SPUtils.getInstance().put("playmode",ModeValue.REREAT_NORMAL);
            System.out.println("第一次执行");
        }
    }


    /**加载播放地址*/
    public void  start(){
        try {
            if (mediaPlayer != null){
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            System.out.println("播放地址："+url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**播放音乐的三个监听事件*/
    class  MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("net,音乐正在准备...");
            play();
            times = 0;
        }
    }
    /**错误监听采用5次尝试，都错误后，播放下一首*/
    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            times ++;
            if (times < 5){
                System.out.println("net,音乐onError..."+what+"extra:"+extra+"\n错误次数："+times);
                start();
            }else {
                next();
                times = 0;
            }
            return true;
        }
    }
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            times = 0;
            next();
        }
    }



    /**正式播放音乐*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play(){
        SPUtils.getInstance().put("net_first","2");
        mediaPlayer.start();
        SPUtils.getInstance().put("net_start",1);
        notifyChange(OPEN_NET_AUDIO);
        SendNotification(R.drawable.ic_play_pause);
    }


    /**发送Notification*/
    private void notifyChange(String action) {
        System.out.println("net,发送广播");
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    /**发送Notification*/
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendNotification(int icon){
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification(0,mDataBeans.get(position).getTitle(),mDataBeans.get(position).getAuthor(),R.mipmap.ic_launcher,icon);
    }


    /**暂停方法*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void  pause(){
        mediaPlayer.pause();
        SPUtils.getInstance().put("net_start",0);
        notifyChange(OPEN_NET_AUDIO);
        SendNotification(R.drawable.ic_play_playing);
    }

    /**停止播放方法，关闭服务时用到*/
    public void  stop(){
        mediaPlayer.stop();
        mediaPlayer.prepareAsync();
    }

    /**当前播放进度*/
    public int  getCurrentPosition(){   return mediaPlayer.getCurrentPosition();}

    /**音乐时长*/
    public int  getDuration(){  return mediaPlayer.getDuration();}

    /**歌名*/
    private String getTitle(){   return mDataBeans.get(position).getTitle();}

    /**歌手*/
    private String getAuthor(){  return mDataBeans.get(position).getAuthor();}

    /**播放地址*/
    private String getUrl(){  return url;}

    /**下一首*/
    public void next(){
        notifyChange(NEXT_NET_AUDIO);
        if (getPlayMode().equals(ModeValue.REREAT_SINGLE)){
            //单曲循环时不做任何操作
        }else if (getPlayMode().equals(ModeValue.REREAT_NORMAL)){
            if (position < mDataBeans.size()-1){
                position = position+1;
            }else if (position == mDataBeans.size()-1){
                position = 0;
            }
        }
        SPUtils.getInstance().put("net_position",position);
        mApi.GetJson(UrlValue.Play + mDataBeans.get(position).getSongid(),8090);
    }

    /**上一首*/
    public void pre(){
        notifyChange(PRE_NET_AUDIO);
        if (getPlayMode().equals(ModeValue.REREAT_SINGLE)){

        }else if (getPlayMode().equals(ModeValue.REREAT_NORMAL)){
            if (position > 0){
                position = position-1;
            }else if (position ==0){
                position = mDataBeans.size()-1;
            }
        }
        SPUtils.getInstance().put("net_position",position);
        mApi.GetJson(UrlValue.Play + mDataBeans.get(position).getSongid(),8090);
    }

    /**设置播放模式*/
    public void setPlayMode(){

        if (SPUtils.getInstance().getString("playmode").equals(ModeValue.REREAT_NORMAL)){
            System.out.println("将模式切换成单曲");
            SPUtils.getInstance().put("playmode",ModeValue.REREAT_SINGLE);
            System.out.println("获取播放模式："+SPUtils.getInstance().getString("playmode"));
        }else if (SPUtils.getInstance().getString("playmode").equals(ModeValue.REREAT_SINGLE)){
            System.out.println("将模式切换成单曲");
            SPUtils.getInstance().put("playmode",ModeValue.REREAT_NORMAL);
            System.out.println("获取播放模式："+SPUtils.getInstance().getString("playmode"));
        }
    }

    /**获取播放模式*/
    public String getPlayMode(){
        System.out.println("net,得到播放模式");
        System.out.println("getPlayMode获取播放模式："+SPUtils.getInstance().getString("playmode"));
        if (EmptyUtils.isEmpty(SPUtils.getInstance().getString("playmode"))){
            return ModeValue.REREAT_NORMAL;
        }else {
            return SPUtils.getInstance().getString("playmode");
        }
    }

    /**查询音乐是否在播放*/
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    /**拖动进度*/
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver != null){
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
