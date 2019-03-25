package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Adapter.FriendAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.IntentBean;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.UserUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;


import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.OnItemChildLongClickListener {

    @BindView(R.id.tv_show_title)
    TextView textView;
    @BindView(R.id.friend_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_friuser)
    TextView mNoFritv;

    private FriendAdapter mAdapter;
    private List<String> mFriUserList;

    @Override
    protected int getContentView() {
        return R.layout.activity_friend;
    }

    @Override
    protected void initView() {
        textView.setText("好友列表");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FriendAdapter(null);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserUtils.getInstance().getStatus()){
            cachedThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mFriUserList = new ArrayList<>();
                        mFriUserList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                        System.out.println("获得的好友数量："+mFriUserList.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mFriUserList.size()>0){
                                    mNoFritv.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    mAdapter.setNewData(mFriUserList);
                                }else {
                                    mNoFritv.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.GONE);
                                }
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            ToastUtils.showShort("请先登录");
        }
    }


    @OnClick({R.id.iv_add_friend,R.id.lv_exit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_add_friend:
                AddFriend();
                break;
            case R.id.lv_exit:
                finish();
                break;
            default:
                break;
        }
    }


    /**添加好友*/
    @SuppressLint("ResourceType")
    private void AddFriend(){
        View myview = LayoutInflater.from(this).inflate(R.layout.dialog_addfriend,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(myview);
        final EditText UserEt = myview.findViewById(R.id.et_fri_user);
        final Button button = myview.findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserEt.getText().toString().equals("")){
                    ToastUtils.showShort("用户名不能为空");
                }
                cachedThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(UserEt.getText().toString(),"");
                            ToastUtils.showShort("添加好友请求已发送");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        builder.show();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<IntentBean> mUsers = new ArrayList<>();
        IntentBean bean = new IntentBean();
        bean.setMark("user");
        bean.setMessage(mFriUserList.get(position));
        mUsers.add(bean);
        IntentUtils.getInstance().SkipIntent(this,ChatActivity.class,mUsers);
    }

    @Override
    public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, final int position) {
        final String items[] = new String[]{"删除好友"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    EMClient.getInstance().contactManager().deleteContact(mFriUserList.get(position));
                    mFriUserList.remove(mFriUserList.get(position));
                    mAdapter.setNewData(mFriUserList);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.create().show();
        return false;
    }
}
