package com.hjm.tingshuo.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.UserBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTextView;
    @BindView(R.id.et_user)
    EditText mUserEt;
    @BindView(R.id.et_first_pwd)
    EditText mFirstEt;
    @BindView(R.id.et_second_pwd)
    EditText mSecondEt;
    @BindView(R.id.lv_warning)
    LinearLayout warningLv;


    private long firsttime, endtime;
    private String user;
    private String pwd;
    private UserBean mUserBean = new UserBean();

    @Override
    protected int getContentView() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initView() {
        mTextView.setText("注册");
        mUserEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (RegexUtils.isUsername(s)) {
                    user = mUserEt.getText().toString();
                }
            }
        });
        mSecondEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mFirstEt.getText().toString().equals(mSecondEt.getText().toString())) {
                    pwd = mSecondEt.getText().toString();
                } else {
                    ToastUtils.showShort("两次输入密码不同");
                }
            }
        });

        firsttime = System.currentTimeMillis();
        mHandler.sendEmptyMessage(6666);

    }

    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("")){
            mRegistDialog.dismiss();
            mUserBean = mGson.fromJson(json, UserBean.class);
            if (mUserBean.getCode() == 1) {
                ToastUtils.showShort(mUserBean.getMsg());
                finish();
            } else {
                ToastUtils.showShort(mUserBean.getMsg());
            }
        }
    }


    @OnClick({R.id.iv_exit, R.id.btn_commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_exit:
                finish();
                break;
            case R.id.btn_commit:
                if (NetworkUtils.isConnected()) {
                    if (RegexUtils.isUsername(user) && RegexUtils.isUsername(pwd)) {
                        mRegistDialog.show();
                        cachedThreadExecutor.execute(mRunnable);
                    } else {
                        ToastUtils.showShort("账号或密码格式不符合要求");
                    }
                }else {
                    ToastUtils.showShort("网络连接已断开");
                }
                break;
            default:
                break;
        }
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                EMClient.getInstance().createAccount(user, pwd);
                requestRegistJson(UrlValue.Regist, user, pwd);
            } catch (final HyphenateException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int errorCode = e.getErrorCode();
                        String message = e.getMessage();
                        switch (errorCode) {
                            // 网络错误
                            case EMError.NETWORK_ERROR:
                                ToastUtils.showShort("网络错误 code: " + errorCode + ", message:" + message);
                                break;
                            // 用户已存在
                            case EMError.USER_ALREADY_EXIST:
                                ToastUtils.showShort("用户已存在 code: " + errorCode + ", message:" + message);
                                break;
                            default:
                                break;
                        }
                    }
                });
                e.printStackTrace();
            }
        }
    };


    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 6666:
                    endtime = System.currentTimeMillis();
                    if (endtime - firsttime > 5000) {
                        warningLv.setVisibility(View.GONE);
                        mHandler.removeMessages(6666);
                    } else {
                        warningLv.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessage(6666);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
