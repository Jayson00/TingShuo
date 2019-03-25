package com.hjm.tingshuo.Activity;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjm.tingshuo.Adapter.ChatAdapter;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.ChatBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.MyView.ButtonLongClick;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.UserUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.tv_title)
    TextView mTextView;
    @BindView(R.id.recycler_chat)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_txt)
    EditText mTxtEt;
    @BindView(R.id.btn_voice)
    Button mVoiceBtn;
    @BindView(R.id.iv_switch_type)
    ImageView mSwitchIv;
    @BindView(R.id.btn_send)
    Button mSendBtn;

    private String userId = "";
    private String type = null;
    private ChatAdapter mAdapter;
    private boolean txt = true;
    private String txtcontent = null;
    private ChatBean mChatBean;
    private EMConversation mConversation;
    private ClipboardManager mCM;
    private MediaPlayer mMediaPlayer;
    private List<ChatBean> mChatBeans = new ArrayList<>();
    private SimpleDateFormat formatter;
    private String time;


    @Override
    protected int getContentView() {
        return R.layout.activity_chat;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {

        formatter  = new SimpleDateFormat("YY-MM-dd  HH:mm:ss ");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        userId = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("type");
        mTextView.setText(userId);
        mTxtEt.setVisibility(View.VISIBLE);
        mVoiceBtn.setVisibility(View.GONE);
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChatAdapter(null);
        System.out.println("外部构造方法执行");
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mVoiceBtn.setOnTouchListener(new ButtonLongClick(mVoiceBtn, userId, new ButtonLongClick.VoiceCallback() {
            @Override
            public void onSuccess(EMMessage message, String path) {
                time = formatter.format(new Date(System.currentTimeMillis()));
                System.out.println("这里讲会将语音发送显示出来");
                mChatBean = new ChatBean();
                mChatBean.setWhere(0);
                mChatBean.setType("voice");
                mChatBean.setTime(time);
                mChatBean.setVoiceurl(path);
                mChatBeans.add(mChatBean);
                mHandler.sendEmptyMessage(HandleValue.mhandleWhat16);
            }

            @Override
            public void onError(int code, String error) {
                ToastUtils.showShort("发送错误，请重试。");
            }
        }));
        mTxtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mTxtEt.getText().toString().equals("")) {
                    txtcontent = mTxtEt.getText().toString();
                    mSendBtn.setBackgroundResource(R.color.color_9AFF9A);
                }else {
                    mSendBtn.setBackgroundResource(R.color.color_CDCDCD);
                }
            }
        });
        mTxtEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEND){

                    final EMMessage message = EMMessage.createTxtSendMessage(txtcontent, userId);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            time = formatter.format(new Date(System.currentTimeMillis()));
                            mTxtEt.setText("");
                            mChatBean = new ChatBean();
                            mChatBean.setWhere(0);
                            mChatBean.setType("txt");
                            mChatBean.setTime(time);
                            mChatBean.setTxtcontent(txtcontent);
                            mChatBeans.add(mChatBean);
                            mHandler.sendEmptyMessage(HandleValue.mhandleWhat16);
                        }

                        @Override
                        public void onError(int code, String error) {

                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });

                    return true;
                }
                return false;
            }
        });
        mConversation = EMClient.getInstance().chatManager().getConversation(userId,null,false);
        if (UserUtils.getInstance().getStatus()) {
            System.out.println("1");
            if (type != null){
                System.out.println("2");
                initConversation();
            }
        }
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view,final int position) {
        if (mChatBeans.get(position).getType().equals("txt")){
            if (mChatBeans.get(position).getWhere() == 1){
                TextView textView = view.findViewById(R.id.tv_chat_left_content);
                mCM.setPrimaryClip(ClipData.newPlainText(null,textView.getText().toString()));
                Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
            }else {
                TextView textView = view.findViewById(R.id.tv_chat_right_content);
                mCM.setPrimaryClip(ClipData.newPlainText(null,textView.getText().toString()));
                Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
            }
        }else if (mChatBeans.get(position).getType().equals("voice")){
            final ImageView rightimageView = view.findViewById(R.id.iv_right_chat_voice);
            final ImageView leftimageView = view.findViewById(R.id.iv_left_chat_voice);

            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(mChatBeans.get(position).getVoiceurl());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        if (mChatBeans.get(position).getWhere() == 0){
                            rightimageView.setImageResource(R.drawable.ic_chat_right_voice_start);
                        }else if (mChatBeans.get(position).getWhere() == 1){
                            leftimageView.setImageResource(R.drawable.ic_chat_left_voice_start);
                        }
                        mediaPlayer.start();
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (mChatBeans.get(position).getWhere() == 0){
                            rightimageView.setImageResource(R.drawable.ic_chat_right_voice_stop);
                        }else if (mChatBeans.get(position).getWhere() == 1){
                            leftimageView.setImageResource(R.drawable.ic_chat_left_voice_stop);
                        }
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                });
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        System.out.println("i:"+i+"<---->"+"i1:"+i1);
                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick({R.id.iv_switch_type, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_type:
                if (txt) {
                    txt = false;
                    mTxtEt.setVisibility(View.VISIBLE);
                    mVoiceBtn.setVisibility(View.GONE);
                } else {
                    txt = true;
                    mTxtEt.setVisibility(View.GONE);
                    mVoiceBtn.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_send:
                final EMMessage message = EMMessage.createTxtSendMessage(txtcontent, userId);
                EMClient.getInstance().chatManager().sendMessage(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        time = formatter.format(new Date(System.currentTimeMillis()));
                        mTxtEt.setText("");
                        mChatBean = new ChatBean();
                        mChatBean.setWhere(0);
                        mChatBean.setType("txt");
                        mChatBean.setTime(time);
                        mChatBean.setTxtcontent(txtcontent);
                        mChatBeans.add(mChatBean);
                        mHandler.sendEmptyMessage(HandleValue.mhandleWhat16);
                    }

                    @Override
                    public void onError(int code, String error) {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                break;
            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandleValue.mhandleWhat16:
                    mAdapter.setNewData(mChatBeans);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mChatBeans.size()-1);
                    break;
            }
        }
    };



    /**获取聊天记录*/
    private void initConversation() {
        System.out.println("初始化数据，加载聊天记录");
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage message = mConversation.getLastMessage();
                mChatBean = new ChatBean();
                if (message != null){
                    if (message.getType() == EMMessage.Type.TXT) {
                        time = formatter.format(new Date(System.currentTimeMillis()));
                        mChatBean.setWhere(1);
                        mChatBean.setType("txt");
                        mChatBean.setTime(time);
                        mChatBean.setTxtcontent(((EMTextMessageBody) message.getBody()).getMessage());
                        mChatBeans.add(mChatBean);
                    } else if (message.getType() == EMMessage.Type.VOICE) {
                        time = formatter.format(new Date(System.currentTimeMillis()));
                        mChatBean.setWhere(1);
                        mChatBean.setType("voice");
                        mChatBean.setTime(time);
                        mChatBean.setVoiceurl(((EMVoiceMessageBody) message.getBody()).getRemoteUrl());
                        mChatBeans.add(mChatBean);
                    }
                }
            }
            mAdapter.setNewData(mChatBeans);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}

