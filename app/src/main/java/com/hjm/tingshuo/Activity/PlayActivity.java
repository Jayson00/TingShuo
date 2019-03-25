package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.Bean.PlayBean;
import com.hjm.tingshuo.Bean.UserBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.ModeValue;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.MusicServiceAidl;
import com.hjm.tingshuo.MyView.LyricView;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Service.MusicPlayService;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.TimeUtils;
import com.hjm.tingshuo.Utils.UserUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;



public class PlayActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_play_name)
    TextView nameTv;
    @BindView(R.id.tv_play_author)
    TextView authorTv;
    @BindView(R.id.tv_play_current_duration)
    TextView currentTv;
    @BindView(R.id.tv_play_duration)
    TextView durationTv;
    @BindView(R.id.slv_play_show_lrc)
    com.hjm.tingshuo.MyView.LyricView LyricView;
    @BindView(R.id.sb_play_position)
    SeekBar seekBar;
    @BindView(R.id.iv_play_mode_all)
    ImageView modeIv;
    @BindView(R.id.iv_play_pre)
    ImageView preIv;
    @BindView(R.id.iv_play_next)
    ImageView nextIv;
    @BindView(R.id.iv_play_playing)
    ImageView playIv;
    @BindView(R.id.iv_play_exit)
    ImageView exitIv;
    @BindView(R.id.iv_play_lrc)
    ImageView mlrcIv;
    @BindView(R.id.iv_play_love)
    ImageView mLoveIv;
    @BindView(R.id.iv_song_download)
    ImageView mDowlloadIv;
    @BindView(R.id.iv_recommend)
    ImageView mRecommendIv;


    private List<MusicBean.DataBean> mDataBeans = new ArrayList<>();
    private MyReceiver myReceiver;
    private MusicServiceAidl service;
    private int position = 0;
    private TimeUtils mTimeUtils;
    private Api mApi;
    /**默认的请求数据*/
    private String type = UrlValue.Home_url+"2";
    private String playmode = "";
    private Boolean love = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        initLyricView();
        initUI();
    }

    @Override
    protected void onStart() {
        LoadingData();
        PrepareForBindService();
        RegistReceiver();
        super.onStart();
    }




    /**设置ui界面的显示*/
    private void initUI(){
        /**根据字段显示播放模式*/
        if (!SPUtils.getInstance().getString("playmode").isEmpty()){
            if (SPUtils.getInstance().getString("playmode").equals(ModeValue.REREAT_NORMAL)){
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
            }else {
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_one);
            }
        }else {
            modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
        }
        /**根据字段显示播放或暂停*/
        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.MusicPlayService")){
            if (SPUtils.getInstance().getInt("net_start") == 1){
                playIv.setBackgroundResource(R.drawable.ic_play_pause);
            }else {
                playIv.setBackgroundResource(R.drawable.ic_play_playing);
            }
        }else {
            playIv.setBackgroundResource(R.drawable.ic_play_playing);
        }

        mTimeUtils = new TimeUtils();
        mApi = new Api(mHandler,PlayActivity.this);
        seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    /**初始化歌词*/
    private void initLyricView(){
        LyricView.setLineSpace(30);
        LyricView.setHighLightTextColor(R.color.colorAccent);
        LyricView.setTextSize(20);
        LyricView.setPlayable(true);
    }


    /**获取上一个页面传过来的参数*/
    private void LoadingData(){
        position = SPUtils.getInstance().getInt("net_position");
        type = SPUtils.getInstance().getString("type");
        System.out.println("接收到地址："+type);
//        if(SPUtils.getInstance().getBoolean("collection")){
//            System.out.println("请求收藏");
//            requestGetCollectJson(type,UserUtils.getInstance().getUser());--------
//            mApi.GetCollection(type,UserUtils.getInstance().getUser(),HandleValue.mhandleWhat5);
//        }else {
            requestJson(type);
            mApi.GetJson(type,HandleValue.mhandleWhat5);
//        }
    }


    /**请求网络音乐的播放地址后开始绑定服务*/
    private void  PrepareForBindService(){
        if (NetworkUtils.isConnected()){
            bindAndStartService();
        }else {
            ToastUtils.showShort("网络连接已断开...");
        }
    }



    /**注册动态广播接收器(接收Service的广播)*/
    private void RegistReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayService.OPEN_NET_AUDIO);
        intentFilter.addAction(MusicPlayService.NEXT_NET_AUDIO);
        intentFilter.addAction(MusicPlayService.PRE_NET_AUDIO);
        registerReceiver(myReceiver,intentFilter);
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicPlayService.OPEN_NET_AUDIO)){
                //接收广播，播放或暂停
                try {
                    if (service.isPlaying()){
                        System.out.println("PlayActivity接收到广播---------播放");
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat7);
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat8);
                    }else {
                        System.out.println("PlayActivity接收到广播---------暂停");
                        mHandler.removeMessages(HandleValue.mhandleWhat8);
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat9);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if (intent.getAction().equals(MusicPlayService.NEXT_NET_AUDIO)){
                //接收广播，播放下一首
                try {
                    if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){

                    }else if (service.getPlayMode().equals(ModeValue.REREAT_NORMAL)){
                        if (position < mDataBeans.size()-1){
                            position = position+1;
                        }else if (position == mDataBeans.size()-1){
                            position = 0;
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SPUtils.getInstance().put("position",position);
                LyricView.reset("");
                mHandler.sendEmptyMessage(HandleValue.mhandleWhat6);
                mHandler.removeMessages(HandleValue.mhandleWhat8);
                System.out.println("接收到网络音乐的广播---播放下一首");
            }else if (intent.getAction().equals(MusicPlayService.PRE_NET_AUDIO)){
                //接收广播，播放上一首
                try {
                    if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){

                    }else if (service.getPlayMode().equals(ModeValue.REREAT_NORMAL)){
                        if (position > 0){
                            position = position-1;
                        }else if (position ==0){
                            position = mDataBeans.size()-1;
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SPUtils.getInstance().put("position",position);
                LyricView.reset("");
                mHandler.sendEmptyMessage(HandleValue.mhandleWhat6);
                mHandler.removeMessages(HandleValue.mhandleWhat8);
                System.out.println("接收到网络音乐的广播---播放上一首");
            }
        }
    }


    /**seekbar的监听事件*/
    public class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}

    }


    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("json")){
            mDataBeans = mGson.fromJson(json,MusicBean.class).getData();
            mHandler.sendEmptyMessage(HandleValue.mhandleWhat6);
        }else if (TAG.equals("querycollect")){
            if (mGson.fromJson(json,UserBean.class).getCode() == 1){
                mLoveIv.setImageResource(R.drawable.ic_song_collection);
            }else {
                mLoveIv.setImageResource(R.drawable.ic_song_no_collection);
            }
        }else if (TAG.equals("deletcollect")){
            //请求数据
            mLoveIv.setImageResource(R.drawable.ic_song_no_collection);
            ToastUtils.showShort("删除收藏成功");
        }else if (TAG.equals("downloadprepare")){
            //请求数据
            PlayBean playBean = mGson.fromJson(json,PlayBean.class);
            if (playBean != null){
                DownloadFile(playBean.getBitrate().getFile_link(), PathValue.music_directory,mDataBeans.get(position).getTitle()+".mp3");
            }
        }else if (TAG.equals("addcollect")){
            UserBean userBean = mGson.fromJson(json,UserBean.class);
            if (userBean.getCode() == 1){
                love = true;
                mLoveIv.setImageResource(R.drawable.ic_song_collection);
                ToastUtils.showShort("收藏成功！");
            }else if (userBean.getCode() == 0){
                ToastUtils.showShort("您已收藏！");
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandleValue.mhandleWhat6:
                    if (mDataBeans != null){
                        System.out.println("position:"+position);
                        System.out.println("数量："+mDataBeans.size());
                        nameTv.setText(mDataBeans.get(position).getTitle());
                        authorTv.setText(mDataBeans.get(position).getAuthor());
                        durationTv.setText(Integer.parseInt(mDataBeans.get(position).getDuration())/60+":"+
                                (Integer.parseInt(mDataBeans.get(position).getDuration())-(Integer.parseInt(mDataBeans.get(position).getDuration())/60)*60));
                        if (UserUtils.getInstance().getStatus()){
                            requestQueryCollectJson(UrlValue.QueryCollection,UserUtils.getInstance().getUser(),mDataBeans.get(position).getSongid());
                        }
                    }
                    break;
                case HandleValue.mhandleWhat8:
                    try {
                        LyricView.setCurrentTimeMillis(service.getCurrentPosition());
                        int current = service.getCurrentPosition();
                        seekBar.setMax((int) service.getDuration());
                        seekBar.setProgress(current);
                        currentTv.setText(mTimeUtils.stringForTime(current));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mHandler.removeMessages(HandleValue.mhandleWhat8);
                    mHandler.sendEmptyMessageDelayed(HandleValue.mhandleWhat8,1000);
                    break;
                case HandleValue.mhandleWhat7:
                    playIv.setBackgroundResource(R.drawable.ic_play_pause);
                    break;
                case HandleValue.mhandleWhat9:
                    playIv.setBackgroundResource(R.drawable.ic_play_playing);
                    break;
                case HandleValue.mhandleWhat14:
                    LyricView.setLyricFile(new File(PathValue.lyric_directory + mDataBeans.get(position).getTitle()+".lrc"),"utf-8");
                    break;
                default:
                    break;
            }
        }
    };

    /**绑定服务*/
    private void bindAndStartService() {
        Intent intent = new Intent(PlayActivity.this,MusicPlayService.class);
        intent.setAction("com.MusicPlayService.play");
        bindService(intent,con, Context.BIND_AUTO_CREATE);
        startService(intent);
        System.out.println("开始绑定service");
    }
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            System.out.println("onServiceConnected");
            //连接成功时
            service = MusicServiceAidl.Stub.asInterface(iBinder);
            if (service != null){
                try {
                    service.prepare();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected");
            //连接断开时
            if (service != null){
                try {
                    service.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                service =null;
            }
        }
    };




    @Override
    @OnClick({R.id.iv_play_mode_all,R.id.iv_play_exit,R.id.iv_play_love,R.id.iv_song_download,
            R.id.iv_play_lrc,R.id.iv_play_playing,R.id.iv_play_pre,R.id.iv_play_next,R.id.iv_recommend})
    public void onClick(View v)  {
        try {
            switch (v.getId()){
                case R.id.iv_play_mode_all:
                    setPlayMode();
                    break;
                case R.id.iv_play_exit:
                    finish();
                    break;
                case R.id.iv_play_love:
                    if (UserUtils.getInstance().getStatus()){
                        System.out.println("输出一下love的值"+love);
                        if (love){
                            mLoveIv.setImageResource(R.drawable.ic_song_no_collection);
                            requestDeletCollectJson(UrlValue.DelCollection,UserUtils.getInstance().getUser(),mDataBeans.get(position).getSongid());
                        }else {
                            love = false;
                            requestAddCollectJson(UrlValue.AddCollection,UserUtils.getInstance().getUser(),mDataBeans.get(position).getTitle(),
                                    mDataBeans.get(position).getAuthor(),mDataBeans.get(position).getDuration(),mDataBeans.get(position).getSongid(),
                                    mDataBeans.get(position).getPic(),mDataBeans.get(position).getLrc());
                        }
                    }
                    break;
                case R.id.iv_song_download:
                    mDowlloadIv.setImageResource(R.drawable.ic_song_download);
                    DownloadSong();
                    break;
                case R.id.iv_play_lrc:
                    if (!new File(PathValue.lyric_directory+mDataBeans.get(position).getTitle()+".lrc").exists()){
                        downloadLrc();
                    }else {
                        LyricView.setLyricFile(new File(PathValue.lyric_directory+mDataBeans.get(position).getTitle()+".lrc"),"utf-8");
                    }
                    break;
                case R.id.iv_play_playing:
                    if (NetworkUtils.isConnected()){
                        if (service != null){
                            if (service.isPlaying()){
                                service.pause();
                                playIv.setBackgroundResource(R.drawable.ic_play_playing);
                            }else {
                                if (SPUtils.getInstance().getString("net_first").equals("1")){
                                    System.out.println("第一次播放");
                                    service.start();
                                }else {
                                    service.play();
                                }
                            }
                        }
                    }else {
                        ToastUtils.showShort("网络连接已断开，请检查网络！");
                    }

                    break;
                case R.id.iv_play_pre:
                    if (service != null){
                        if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){
                            service.start();
                        }else {
                            service.pre();
                        }
                    }
                    break;
                case R.id.iv_play_next:
                    if (service != null){
                        if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){
                            service.start();
                        }else {
                            service.next();
                        }
                    }
                    break;
                case R.id.iv_recommend:
                    IntentUtils.getInstance().SkipIntent(PlayActivity.this,RecommendActivity.class);
                    break;
                default:
                    break;
            }
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }




    /**设置播放模式*/
    private void setPlayMode() throws RemoteException {
        if (service != null){
            playmode = service.getPlayMode();
            System.out.println("播放模式"+playmode);
            if (playmode.equals(ModeValue.REREAT_NORMAL)){
                playmode = ModeValue.REREAT_SINGLE;
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_one);
                Toast.makeText(this,"单曲循环", Toast.LENGTH_SHORT).show();
            }else if (playmode.equals(ModeValue.REREAT_SINGLE)){
                playmode = ModeValue.REREAT_NORMAL;
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
                Toast.makeText(this,"全部循环", Toast.LENGTH_SHORT).show();
            }
            service.setPlayMode();
        }
    }


    /**下载歌曲*/
    private void DownloadSong(){
        requestDownloadparms(UrlValue.Play + mDataBeans.get(position).getSongid());
    }


    /**下载歌词*/
    private void downloadLrc(){
        DownloadFile(mDataBeans.get(position).getLrc(), PathValue.lyric_directory,mDataBeans.get(position).getTitle()+".lrc");
    }




    /**注释掉本身的动画效果*/
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    /**销毁Activity时释放引用*/
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        if (myReceiver != null){
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        if (service != null){
            if (con != null){
                unbindService(con);
                con = null;
            }
        }
        super.onDestroy();
    }

}
