package com.hjm.tingshuo.Activity;


import android.view.View;
import android.widget.TextView;
import com.hjm.tingshuo.Base.BaseActivity;
import com.hjm.tingshuo.R;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;


/**根据点击的类型返回(已完成)
 * @author 黄健明
 * */
public class ClassificationActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView mTextView;

    @Override
    protected int getContentView() {
        return R.layout.activity_classification;
    }

    @Override
    protected void initView() {
        mTextView.setText("歌曲分类");
    }

    @Override
    protected void onClientSuccess(String json, String TAG) {

    }


    @OnClick({R.id.btn_type_1,R.id.btn_type_2,R.id.btn_type_20,R.id.btn_type_15,R.id.btn_type_22,
            R.id.btn_type_25, R.id.btn_type_21,R.id.btn_type_14,R.id.btn_type_23,
            R.id.btn_type_10,    R.id.iv_exit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_type_1:
                sendType(1);
                finish();
                break;
            case R.id.btn_type_2:
                sendType(2);
                finish();
                break;
            case R.id.btn_type_20:
                sendType(20);
                finish();
                break;
            case R.id.btn_type_15:
                sendType(15);
                finish();
                break;
            case R.id.btn_type_22:
                sendType(22);
                finish();
                break;
            case R.id.btn_type_25:
                sendType(25);
                finish();
                break;
            case R.id.btn_type_21:
                sendType(21);
                finish();
                break;
            case R.id.btn_type_14:
                sendType(14);
                finish();
                break;
            case R.id.btn_type_23:
                sendType(23);
                finish();
                break;
            case R.id.btn_type_10:
                sendType(10);
                finish();
                break;
            case R.id.iv_exit:
                finish();
                break;
            default:
                break;
        }
    }


    private void sendType(int type){
        System.out.println("发送消息");
        EventBus.getDefault().post(type);
    }

}
