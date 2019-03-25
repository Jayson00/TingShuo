package com.hjm.tingshuo.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.NetworkUtils;
import com.hjm.tingshuo.Adapter.ViewPagerAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Fragment.FriendFragment;
import com.hjm.tingshuo.Fragment.MineFragment;
import com.hjm.tingshuo.Fragment.Mine_testFragment;
import com.hjm.tingshuo.Fragment.MusicFragment;
import com.hjm.tingshuo.MyView.ViewPagerFix;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.BottomNavigationUtils;
import com.hjm.tingshuo.Utils.ExitAppUtils;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.FriendNotificationUtils;
import com.hjm.tingshuo.Utils.UserNotificationUtils;
import com.hjm.tingshuo.Utils.UserUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hjm
 */
public class HomeActivity extends BaseActivity implements EMMessageListener {

    private List<Fragment> mFragmentArrayList = new ArrayList<>();
    private MusicFragment mMusicFragment = new MusicFragment();
    private FriendFragment mFriendFragment = new FriendFragment();
    private MineFragment mMineFragment = new MineFragment();


    @BindView(R.id.home_bottemnavigation)
    BottomNavigationBar mNavigationBar;
    @BindView(R.id.home_viewpager)
    ViewPagerFix mViewPager;

    private Activity mActivity;
    private MyReceiver myReceiver;
    private EMMessageListener mEMMessageListener;
    private FriendNotificationUtils Friendutils;
    private UserNotificationUtils Userutils;


    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initView() {

        mActivity = this;
        mEMMessageListener = this;
        Friendutils = new FriendNotificationUtils(mActivity);
        Userutils = new UserNotificationUtils(mActivity);

        mFragmentArrayList.add(mMusicFragment);
        mFragmentArrayList.add(mFriendFragment);
        mFragmentArrayList.add(mMineFragment);
        mViewPager.setScrollable(true);
        mViewPager.setOffscreenPageLimit(mFragmentArrayList.size());
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentArrayList));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNavigationBar.setMode(BottomNavigationBar.MODE_FIXED).setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .addItem(new BottomNavigationItem(R.drawable.net_music_press, "乐库").setInactiveIconResource(R.drawable.net_music))
                .addItem(new BottomNavigationItem(R.drawable.recommend_press, "推荐").setInactiveIconResource(R.drawable.recommend))
                .addItem(new BottomNavigationItem(R.drawable.mine_press, "我的").setInactiveIconResource(R.drawable.mine))
                .setFirstSelectedPosition(0)
                .setActiveColor(R.color.color_EE0000)
                .initialise();
        new BottomNavigationUtils(mNavigationBar,HomeActivity.this).setBottomNavigationItem(2,25);
        mNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });//设置监听

        /**注册广播*/
        RegistReceiver();
    }




    /**注册动态广播接收器(接收Notification的广播)*/
    private void RegistReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("accept");
        intentFilter.addAction("refuse");
        registerReceiver(myReceiver,intentFilter);
    }
    class MyReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null){
                Friendutils.ClearNotification();
                if (action.equals("accept")){
                    System.out.println("接收到添加的广播");
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(intent.getStringExtra("fri_user"));
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }else if (action.equals("refuse")){
                    System.out.println("接收到拒绝的广播");
                    try {
                        EMClient.getInstance().contactManager().deleteContact(intent.getStringExtra("fri_user"));
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }




    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        System.out.println("接收到消息");
        for(EMMessage message : messages){
            if (message.getType() == EMMessage.Type.TXT){
                System.out.println("接收到文本消息");
                Userutils.sendNotification(message.getFrom(), "接收到："+((EMTextMessageBody) message.getBody()).getMessage());
            }else if (message.getType() == EMMessage.Type.VOICE){
                System.out.println("接收到语音消息");
                Userutils.sendNotification(message.getFrom(), "接收语音");
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }


    @Override
    protected void onRestart() {
        System.out.println("onRestart再次执行");
        if (UserUtils.getInstance().getStatus()){
            /**添加好友的监听*/
            EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

                @Override
                public void onContactInvited(String username, String reason) {
                    //收到好友邀请
                    System.out.println("收到好友邀请----username："+username+"\nreason:"+reason);
                    Friendutils.sendNotification(username,"请求添加好友");
                }

                @Override
                public void onFriendRequestAccepted(String username) {
                    System.out.println("对方同意添加----username:"+username);
                    Friendutils.sendNotification(username,"添加好友成功");
                }

                @Override
                public void onFriendRequestDeclined(String username) {
                    System.out.println("对方拒绝添加-----username:"+username);
                    Friendutils.sendNotification(username,"拒绝添加您为好友");
                }

                @Override
                public void onContactDeleted(String username) {
                    //被删除时回调此方法
                    System.out.println("删除好友----username:"+username);
                }


                @Override
                public void onContactAdded(String username) {
                    //增加了联系人时回调此方法
                    System.out.println("添加好友+username："+username);
                }
            });
            /**接收消息的监听*/
            EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
            System.out.println("消息的监听事件已注册");
        }
        super.onRestart();
    }



    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
        super.onDestroy();
    }


    @OnClick({R.id.iv_skip_classification,R.id.iv_skip_friend,R.id.iv_skip_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_skip_classification:
                IntentUtils.getInstance().SkipIntent(this,ClassificationActivity.class);
                break;
            case R.id.iv_skip_friend:
                if (UserUtils.getInstance().getStatus()){
                    IntentUtils.getInstance().SkipIntent(this,FriendActivity.class);
                }else {
                    IntentUtils.getInstance().SkipIntent(this,LoginActivity.class);
                }
                break;
            case R.id.iv_skip_search:
                IntentUtils.getInstance().SkipIntent(this,SearchActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        ExitAppUtils.getInstance().Exit(this);
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }


}
