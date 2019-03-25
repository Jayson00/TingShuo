package com.hjm.tingshuo.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.Bean.UserBean;
import com.hjm.tingshuo.Constant.HandleValue;
import com.hjm.tingshuo.Constant.UrlValue;
import com.hjm.tingshuo.MyView.ButtomDialogView;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Request.Api;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.PathUtils;
import com.hjm.tingshuo.Utils.UserUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView mTextView;
    @BindView(R.id.iv_upload)
    ImageView mImageView;
    @BindView(R.id.et_user)
    EditText mUserEt;
    @BindView(R.id.et_pwd)
    EditText mPwdEt;
    @BindView(R.id.btn_commit)
    Button mCommitBtn;
    @BindView(R.id.btn_login_out)
    Button mLoginOutBtn;
    @BindView(R.id.cb_remember)
    CheckBox mCheckBox;

    private UserBean mUserBean = new UserBean();


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initUI();
        hashMap = new HashMap<>();
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {
        if (TAG.equals("login")){
            mLoginDialog.dismiss();
            mUserBean = mGson.fromJson(json,UserBean.class);
            if (mUserBean.getCode() == 1){
                ToastUtils.showShort("登录成功");

                UserUtils.getInstance().setUser(mUserEt.getText().toString(),mPwdEt.getText().toString(),true);
                SPUtils.getInstance().put("user",mUserEt.getText().toString());
                SPUtils.getInstance().put("pwd",mPwdEt.getText().toString());
                finish();
            }else {
                ToastUtils.showShort("登录失败");
            }
        }else if (TAG.equals("loginout")){
            mLoginOutDialog.dismiss();
            mUserBean = mGson.fromJson(json,UserBean.class);
            if (mUserBean.getCode() == 1){
                ToastUtils.showShort("退出登录");
                UserUtils.getInstance().setUser("","",false);
                finish();
            }else {
                ToastUtils.showShort("操作失败");
            }
        }
    }


    /**获取checkbox状态*/
    private void initUI(){
        mTextView.setText("登录");
        if (UserUtils.getInstance().getStatus()){
            mCommitBtn.setVisibility(View.GONE);
            mLoginOutBtn.setVisibility(View.VISIBLE);
        }else {
            mCommitBtn.setVisibility(View.VISIBLE);
            mLoginOutBtn.setVisibility(View.GONE);
        }

        if (SPUtils.getInstance().getBoolean("remember")){
            mCheckBox.setChecked(true);
            if (UserUtils.getInstance().getUser() != null){
                mUserEt.setText(UserUtils.getInstance().getUser());
                mPwdEt.setText(UserUtils.getInstance().getPwd());
            }
        }else {
            mCheckBox.setChecked(false);
        }

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SPUtils.getInstance().put("remember",true);
                }else {
                    SPUtils.getInstance().put("remember",false);
                }

            }
        });
    }


    @OnClick({R.id.iv_exit,R.id.btn_commit,R.id.tv_regist,R.id.btn_login_out})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_exit:
                finish();
                break;
            case R.id.btn_commit:
                if (NetworkUtils.isConnected()) {
                    final String user = mUserEt.getText().toString();
                    final String pwd = mPwdEt.getText().toString();
                    if (RegexUtils.isUsername(user) && RegexUtils.isUsername(pwd)) {
                        mLoginDialog.show();
                        EMClient.getInstance().login(user, pwd, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                requestLoginJson(UrlValue.Login, user, pwd);
                            }

                            @Override
                            public void onError(int code, String error) {

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    } else {
                        ToastUtils.showShort("用户名或密码错误，请检查！");
                    }
                }else {
                    ToastUtils.showShort("网络连接已断开");
                }
                break;
            case R.id.tv_regist:
                IntentUtils.getInstance().SkipIntent(this,RegistActivity.class);
                break;
            case R.id.btn_login_out:
                mLoginOutDialog.show();
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        requestLoginOutJson(UrlValue.LoginOut,UserUtils.getInstance().getUser(),UserUtils.getInstance().getPwd());
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

}
