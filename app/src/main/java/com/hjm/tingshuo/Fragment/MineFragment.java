package com.hjm.tingshuo.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Activity.CollectActivity;
import com.hjm.tingshuo.Activity.FriendActivity;
import com.hjm.tingshuo.Activity.LocalActivity;
import com.hjm.tingshuo.Activity.LoginActivity;
import com.hjm.tingshuo.Activity.SettingActivity;
import com.hjm.tingshuo.Adapter.Home3Adapter;
import com.hjm.tingshuo.Base.BaseFragment;
import com.hjm.tingshuo.Bean.SongNameListBean;
import com.hjm.tingshuo.MyView.DividerItemDecoration;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.IntentUtils;
import com.hjm.tingshuo.Utils.UserUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by linghao on 2018/9/30.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {


    @Override
    protected void onClientSuccess(String json, String TAG) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
    }


    @Override
    @OnClick({R.id.lv_local,R.id.lv_love,R.id.lv_friend,R.id.lv_login,R.id.lv_setting})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lv_local:
                IntentUtils.getInstance().SkipIntent(getContext(),LocalActivity.class);
                break;
            case R.id.lv_love:
                if (UserUtils.getInstance().getStatus()){
                    IntentUtils.getInstance().SkipIntent(getContext(), CollectActivity.class);
                }else {
                    IntentUtils.getInstance().SkipIntent(getContext(), LoginActivity.class);
                }
                break;
            case R.id.lv_friend:
                if (UserUtils.getInstance().getStatus()){
                    IntentUtils.getInstance().SkipIntent(getContext(), FriendActivity.class);
                }else {
                    IntentUtils.getInstance().SkipIntent(getContext(), LoginActivity.class);
                }
                break;
            case R.id.lv_login:
                IntentUtils.getInstance().SkipIntent(getContext(),LoginActivity.class);
                break;
            case R.id.lv_setting:
                IntentUtils.getInstance().SkipIntent(getContext(),SettingActivity.class);
                break;
            default:
                break;
        }
    }


}
