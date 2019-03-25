package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.LocalMusicBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.ModeValue;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.LocalMusicServiceAidl;
import com.hjm.tingshuo.MusicServiceAidl;
import com.hjm.tingshuo.MyView.LyricView;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Service.LocalMusicPlayService;
import com.hjm.tingshuo.Utils.LocalMusicUtils;
import com.hjm.tingshuo.Utils.TimeUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;



public class LocalPlayActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_play_name)
    TextView nameTv;
    @BindView(R.id.tv_play_author)
    TextView authorTv;
    @BindView(R.id.tv_play_current_duration)
    TextView currentTv;
    @BindView(R.id.tv_play_duration)
    TextView durationTv;
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
    @BindView(R.id.slv_play_show_lrc)
    LyricView mLyricView;


    private MyReceiver myReceiver;
    private LocalMusicServiceAidl service;
    private int position = 0;
    private boolean islyric = false;
    private TimeUtils mTimeUtils;

    private List<LocalMusicBean> mLocalMusicBeans = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_play_local;
    }

    @Override
    protected void initView() {
        initUI();
        PrepareForBindService();
        RegistReceiver();
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }


    /**设置界面*/
    private void initUI(){
        if (!SPUtils.getInstance().getString("playmode").isEmpty()){
            if (SPUtils.getInstance().getString("playmode").equals(ModeValue.REREAT_NORMAL)){
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
            }else {
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_one);
            }
        }else {
            modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
        }

        if (ServiceUtils.isServiceRunning("com.hjm.tingshuo.Service.LocalMusicPlayService")){

            if (SPUtils.getInstance().getInt("local_start") == 1){
                playIv.setBackgroundResource(R.drawable.ic_play_pause);
            }else{
                playIv.setBackgroundResource(R.drawable.ic_play_playing);
            }
        }else {
            playIv.setBackgroundResource(R.drawable.ic_play_playing);
        }

        mTimeUtils = new TimeUtils();
        mLyricView.setLineSpace(30);
        mLyricView.setHighLightTextColor(R.color.colorAccent);
        mLyricView.setTextSize(20);
        mLyricView.setPlayable(true);
        seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }



    /**开始绑定服务*/
    private void  PrepareForBindService(){
        //播放音乐的位置
        position = SPUtils.getInstance().getInt("local_position");
        mLocalMusicBeans = LocalMusicUtils.getInstance().getDataFromLocal(this);
        if (mLocalMusicBeans.size()>0){
            bindAndStartService();
            mHandler.sendEmptyMessage(HandleValue.mhandleWhat19);
        }
    }



    /**注册动态广播接收器(接收Service的广播)*/
    private void RegistReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalMusicPlayService.OPEN_LOCAL_AUDIO);
        intentFilter.addAction(LocalMusicPlayService.NEXT_LOCAL_AUDIO);
        intentFilter.addAction(LocalMusicPlayService.PRE_LOCAL_AUDIO);
        registerReceiver(myReceiver,intentFilter);
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalMusicPlayService.OPEN_LOCAL_AUDIO)){
                //接收广播，播放或暂停
                try {
                    if (service.isPlaying()){
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat20);
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat21);
                    }else {
                        mHandler.removeMessages(HandleValue.mhandleWhat21);
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat22);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if (intent.getAction().equals(LocalMusicPlayService.NEXT_LOCAL_AUDIO)){
                try {
                    if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){

                    }else if (service.getPlayMode().equals(ModeValue.REREAT_NORMAL)){
                        position = SPUtils.getInstance().getInt("local_position");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                islyric = false;
                mLyricView.reset("");
                mHandler.removeMessages(HandleValue.mhandleWhat21);
                mHandler.sendEmptyMessage(HandleValue.mhandleWhat19);
            }else if (intent.getAction().equals(LocalMusicPlayService.PRE_LOCAL_AUDIO)){
                try {
                    if (service.getPlayMode().equals(ModeValue.REREAT_SINGLE)){

                    }else if (service.getPlayMode().equals(ModeValue.REREAT_NORMAL)){
                        position = SPUtils.getInstance().getInt("local_position");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                islyric = false;
                mLyricView.reset("");
                mHandler.removeMessages(HandleValue.mhandleWhat21);
                mHandler.sendEmptyMessage(HandleValue.mhandleWhat19);
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




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandleValue.mhandleWhat19:
                    nameTv.setText(mLocalMusicBeans.get(position).getTitle());
                    authorTv.setText(mLocalMusicBeans.get(position).getAuthor());
                    durationTv.setText((int)mLocalMusicBeans.get(position).getDuration()/60+":"+
                            ((int)mLocalMusicBeans.get(position).getDuration()-(int)(mLocalMusicBeans.get(position).getDuration()/60)*60));
                    break;
                case HandleValue.mhandleWhat20:
                    playIv.setBackgroundResource(R.drawable.ic_play_pause);
                    break;
                case HandleValue.mhandleWhat21:
                    try {
                        if (islyric){
                            mLyricView.setCurrentTimeMillis(service.getCurrentPosition());
                        }
                        int current = service.getCurrentPosition();
                        seekBar.setMax(service.getDuration());
                        seekBar.setProgress(current);
                        currentTv.setText(mTimeUtils.stringForTime(current));
                        System.out.println("设置进度current:"+current+"\n总进度："+service.getDuration());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mHandler.removeMessages(HandleValue.mhandleWhat21);
                    mHandler.sendEmptyMessageDelayed(HandleValue.mhandleWhat21,1000);
                    break;
                case HandleValue.mhandleWhat22:
                    playIv.setBackgroundResource(R.drawable.ic_play_playing);
                    break;
                default:
                    break;
            }
        }
    };


    /**绑定服务*/
    private void bindAndStartService() {
        Intent intent = new Intent(LocalPlayActivity.this,LocalMusicPlayService.class);
        intent.setAction("com.LocalMusicPlayService.play");
        bindService(intent,con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            System.out.println("onServiceConnected");
            //连接成功时
            service = LocalMusicServiceAidl.Stub.asInterface(iBinder);
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
    @OnClick({R.id.iv_play_mode_all,R.id.iv_play_exit,R.id.iv_play_playing,R.id.iv_play_pre,R.id.iv_play_next,R.id.iv_play_lrc})
    public void onClick(View v)  {
        try {
            switch (v.getId()){
                case R.id.iv_play_mode_all:
                    setPlayMode();
                    break;
                case R.id.iv_play_exit:
                    finish();
                    break;
                case R.id.iv_play_playing:
                    if (service != null){
                        if (service.isPlaying()){
                            service.pause();
                            playIv.setBackgroundResource(R.drawable.ic_play_playing);
                        }else {
                            if (SPUtils.getInstance().getString("local_first").equals("1")){
                                System.out.println("第一次播放");
                                service.start();
                            }else {
                                service.play();
                            }
                        }
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
                case R.id.iv_play_lrc:
                    File file = new File(PathValue.lyric_directory+mLocalMusicBeans.get(position).getTitle().replace(".mp3","")+".lrc");
                    System.out.println("歌词路径："+PathValue.lyric_directory+mLocalMusicBeans.get(position).getTitle()+".lrc");
                    if (file.exists()){
                        islyric = true;
                        mLyricView.setLyricFile(file,"utf-8");
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat21);
                    }else {
                        ToastUtils.showShort("歌词文件不存在");
                    }
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
        String playmode = "";
        if (service != null){
            playmode = service.getPlayMode();
            if (playmode.equals(ModeValue.REREAT_NORMAL)){
                //单曲循环
                playmode = ModeValue.REREAT_SINGLE;
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_one);
                ToastUtils.showShort("单曲循环");
            }else if (playmode.equals(ModeValue.REREAT_SINGLE)){
                //全部循环
                playmode = ModeValue.REREAT_NORMAL;
                modeIv.setBackgroundResource(R.drawable.ic_play_loop_all);
                ToastUtils.showShort("全部循环");
            }
            service.setPlayMode();
        }
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
