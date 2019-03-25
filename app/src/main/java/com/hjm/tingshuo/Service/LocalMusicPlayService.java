package com.hjm.tingshuo.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hjm.tingshuo.Bean.LocalMusicBean;
import com.hjm.tingshuo.Constant.ModeValue;
import com.hjm.tingshuo.LocalMusicServiceAidl;
import com.hjm.tingshuo.MusicServiceAidl;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.LocalMusicUtils;
import com.hjm.tingshuo.Utils.NotificationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicPlayService extends Service {

    public static final String OPEN_LOCAL_AUDIO = "com.LocalMusicPlayService.play";
    public static final String NEXT_LOCAL_AUDIO = "com.LocalMusicPlayService.next";
    public static final String PRE_LOCAL_AUDIO = "com.LocalMusicPlayService.pre";

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int position = 0;

    private List<LocalMusicBean> mLocalMusicBeans = new ArrayList<>();

    LocalMusicPlayService service = LocalMusicPlayService.this;
    private MyReceiver myReceiver;

    int times = 0;
    String url = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        position = SPUtils.getInstance().getInt("local_position");

        mLocalMusicBeans = LocalMusicUtils.getInstance().getDataFromLocal(this);

        url = mLocalMusicBeans.get(position).getData();

        RegistReceiver();

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
                System.out.println("Local,接收到上一首的广播");
                service.pre();
            }else if (action.equals("PlayService")){
                System.out.println("Local,接收到播放或暂停的广播");
                if (service.isPlaying()){
                    service.pause();
                }else {
                    service.start();
                }
            }else if (action.equals("NextService")){
                System.out.println("Local,接收到下一首的广播");
                service.next();
            }
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
    private LocalMusicServiceAidl.Stub stub = new LocalMusicServiceAidl.Stub() {

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
        SPUtils.getInstance().put("local_first","1");
        if (EmptyUtils.isEmpty(SPUtils.getInstance().getString("playmode"))){
            SPUtils.getInstance().put("playmode",ModeValue.REREAT_NORMAL);
            System.out.println("第一次执行");
        }
        System.out.println("执行了");
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



    class  MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("Local,音乐正在准备...");
            play();//这里运行了多长时间
            times = 0;
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            times ++;
            if (times < 5){
                System.out.println("错误次数："+times+"Local,音乐onError..."+what+"extra:"+extra);
                play();
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
            System.out.println("Local,音乐播放结束...");
            times = 0;
            next();
        }
    }


    //根据动作发广播
    private void notifyChange(String action) {
        System.out.println("local,发送广播");
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    /**正式播放音乐*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play(){
        SPUtils.getInstance().put("local_first","2");
        mediaPlayer.start();
        SPUtils.getInstance().put("local_start",1);
        notifyChange(OPEN_LOCAL_AUDIO);
        SendNotification(R.drawable.ic_play_pause);
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendNotification(int icon){
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification(1,mLocalMusicBeans.get(position).getTitle(),mLocalMusicBeans.get(position).getAuthor(),R.mipmap.ic_launcher,icon);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void  pause(){
        mediaPlayer.pause();
        SPUtils.getInstance().put("local_start",0);
        notifyChange(OPEN_LOCAL_AUDIO);
        System.out.println("local,暂停");
        SendNotification(R.drawable.ic_play_playing);
    }


    public void  stop(){
        mediaPlayer.stop();
        mediaPlayer.prepareAsync();
    }


    public int  getCurrentPosition(){   return mediaPlayer.getCurrentPosition();}

    public int  getDuration(){  return mediaPlayer.getDuration();}

    private String getTitle(){   return mLocalMusicBeans.get(position).getTitle();}

    private String getAuthor(){  return mLocalMusicBeans.get(position).getAuthor();}

    private String getUrl(){  return mLocalMusicBeans.get(position).getData();}


    public void next(){
        if (getPlayMode().equals(ModeValue.REREAT_SINGLE)){

        }else if (getPlayMode().equals(ModeValue.REREAT_NORMAL)){

            if (position < mLocalMusicBeans.size()-1){
                position  = position + 1;
            }else if (position == mLocalMusicBeans.size()-1){
                position = 0;
            }
        }
        SPUtils.getInstance().put("local_position",position);
        url = mLocalMusicBeans.get(position).getData();
        start();
        notifyChange(NEXT_LOCAL_AUDIO);
    }


    public void pre(){
        if (getPlayMode().equals(ModeValue.REREAT_SINGLE)){

        }else if (getPlayMode().equals(ModeValue.REREAT_NORMAL)){
            if (position > 0){
                position = position--;
            }else {
                position = 0;
            }
        }
        SPUtils.getInstance().put("local_position",position);
        url = mLocalMusicBeans.get(position).getData();
        start();
        notifyChange(PRE_LOCAL_AUDIO);
    }




    public void setPlayMode(){
        if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString("playmode"))){
            if (SPUtils.getInstance().getString("playmode").equals(ModeValue.REREAT_NORMAL)){
                SPUtils.getInstance().put("playmode",ModeValue.REREAT_SINGLE);
            }else {
                SPUtils.getInstance().put("playmode",ModeValue.REREAT_NORMAL);
            }
        }
    }


    public String getPlayMode(){
        if (EmptyUtils.isEmpty(SPUtils.getInstance().getString("playmode"))){
            return ModeValue.REREAT_NORMAL;
        }else {
            return SPUtils.getInstance().getString("playmode");
        }
    }


    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }


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
