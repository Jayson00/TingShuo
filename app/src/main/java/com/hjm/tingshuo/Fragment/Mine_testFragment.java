package com.hjm.tingshuo.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.hjm.tingshuo.Adapter.Home3Adapter;
import com.hjm.tingshuo.Base.BaseFragment;
import com.hjm.tingshuo.Bean.SongNameListBean;
import com.hjm.tingshuo.MyView.DividerItemDecoration;
import com.hjm.tingshuo.R;
import com.hjm.tingshuo.Utils.IntentUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by linghao on 2018/9/30.
 */

public class Mine_testFragment extends BaseFragment implements View.OnClickListener,Home3Adapter.OnItemClickListener {

    //新建歌单的状态选择
    private static final int MODE_CHECK = 0;
    private static final int MODE_EDIT = 1;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.tv_select_num)
    TextView mTvSelectNum;
    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    @BindView(R.id.select_all)
    TextView mSelectAll;
    @BindView(R.id.ll_mycollection_bottom_dialog)
    LinearLayout mLlMycollectionBottomDialog;
    @BindView(R.id.lv_editor)
    LinearLayout mLvEditor;
    @BindView(R.id.lv_add)
    LinearLayout mLvAdd;
    @BindView(R.id.btn_editor)
    Button mTvEditor;

    private Home3Adapter mRadioAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private List<SongNameListBean> mList = new ArrayList<>();
    private SongNameListBean mMyLiveList;
    private int mEditMode = MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;

    //自定义dialog获取歌单名称
    Dialog mDialog ;
    private ArrayList<String> mStrings = new ArrayList<>();

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerview.setLayoutManager(mLinearLayoutManager);

        initData();
        mLvAdd.setOnClickListener(this);
    }

    private void initData() {

        mStrings.add("收藏");
        SPUtils.getInstance().put("namelist","收藏");
        System.out.println("initData");
        if (mList.size()>0){
            mHandler.sendEmptyMessage(10);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    mRadioAdapter = new Home3Adapter(getContext(),mList);

                    DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
                    itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_main_bg_height_1));
                    mRecyclerview.addItemDecoration(itemDecorationHeader);
                    mRecyclerview.setAdapter(mRadioAdapter);

                    initListener();
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    @OnClick({R.id.lv_local,R.id.lv_love,R.id.lv_friend,R.id.lv_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lv_local:
                IntentUtils.getInstance().SkipIntent(getContext(),LocalActivity.class);
                break;
            case R.id.lv_love:
                IntentUtils.getInstance().SkipIntent(getContext(), CollectActivity.class);
                break;
            case R.id.lv_friend:
                IntentUtils.getInstance().SkipIntent(getContext(), FriendActivity.class);
                break;
            case R.id.lv_login:
                IntentUtils.getInstance().SkipIntent(getContext(),LoginActivity.class);
                break;
            case R.id.btn_delete:
                deleteVideo();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.lv_editor:
                updataEditMode();
                break;
            case R.id.lv_add:
                mDialog = new Dialog(getContext());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_et,null);
                final EditText editText = (EditText) view1.findViewById(R.id.dialog_et);
                Button mBtnCancle = (Button) view1.findViewById(R.id.cancle);
                mBtnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });
                Button mBtnConfirm = (Button) view1.findViewById(R.id.confirm);
                mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //按下确定键后的事件
                        mMyLiveList = new SongNameListBean();
                        if (!editText.getText().toString().equals("")){

                            mMyLiveList.setTitle(editText.getText().toString());
                            mStrings.add(editText.getText().toString());
                            SPUtils.getInstance().put("namelist", StringUtils.join(mStrings,","));
                            mList.add(mMyLiveList);
                            mHandler.sendEmptyMessage(10);
                            mDialog.dismiss();
                        }else {
                            ToastUtils.showShort("歌单名称不能为空！");
                        }
                    }
                });
                mDialog.setContentView(view1);
                mDialog.show();
                break;
            default:
                break;

        }
    }



    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
            mBtnDelete.setTextColor(Color.WHITE);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnDelete.setEnabled(false);
            mBtnDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.color_B7B8BD));
        }
    }

    private void initListener() {
        mRadioAdapter.setOnItemClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mLvEditor.setOnClickListener(this);
    }


    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mRadioAdapter == null) {
            return;
        }
        if (!isSelectAll) {
            for (int i = 0, j = mRadioAdapter.getSongList().size(); i < j; i++) {
                mRadioAdapter.getSongList().get(i).setSelect(true);
            }
            index = mRadioAdapter.getSongList().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = mRadioAdapter.getSongList().size(); i < j; i++) {
                mRadioAdapter.getSongList().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        mRadioAdapter.notifyDataSetChanged();
        setBtnBackground(index);
        mTvSelectNum.setText(String.valueOf(index));
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0){
            mBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .create();
        builder.show();
        if (builder.getWindow() == null) {
            return;
        }
        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg = (TextView) builder.findViewById(R.id.tv_msg);
        Button cancle = (Button) builder.findViewById(R.id.btn_cancle);
        Button sure = (Button) builder.findViewById(R.id.btn_sure);
        if (msg == null || cancle == null || sure == null) {
            return;
        }

        if (index == 1) {
            msg.setText("删除后不可恢复，确定删除该歌单？");
        } else {
            msg.setText("删除后不可恢复，确定删除这" + index + "个歌单？");
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = mRadioAdapter.getSongList().size(), j =0 ; i > j; i--) {
                    SongNameListBean myLive = mRadioAdapter.getSongList().get(i-1);
                    if (myLive.isSelect()) {

                        mRadioAdapter.getSongList().remove(myLive);
                        index--;
                    }
                }
                index = 0;
                mTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (mRadioAdapter.getSongList().size() == 0){
                    mLlMycollectionBottomDialog.setVisibility(View.GONE);
                }
                mRadioAdapter.notifyDataSetChanged();
                builder.dismiss();
            }
        });
    }
    private void updataEditMode() {
        mEditMode = mEditMode == MODE_CHECK ? MODE_EDIT : MODE_CHECK;
        if (mEditMode == MODE_EDIT) {
            mTvEditor.setText("取消");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mTvEditor.setText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
            clearAll();
        }
        mRadioAdapter.setEditMode(mEditMode);
    }


    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        setBtnBackground(0);
    }

    @Override
    public void onItemClickListener(int pos, List<SongNameListBean> myLiveList) {
        if (editorStatus) {
            SongNameListBean myLive = myLiveList.get(pos);
            boolean isSelect = myLive.isSelect();
            if (!isSelect) {
                index++;
                myLive.setSelect(true);
                if (index == myLiveList.size()) {
                    isSelectAll = true;
                    mSelectAll.setText("取消全选");
                }

            } else {
                myLive.setSelect(false);
                index--;
                isSelectAll = false;
                mSelectAll.setText("全选");
            }
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
            mRadioAdapter.notifyDataSetChanged();
        }else {
            ToastUtils.showShort("跳转页面并获取服务器端数据");
        }
    }

}
