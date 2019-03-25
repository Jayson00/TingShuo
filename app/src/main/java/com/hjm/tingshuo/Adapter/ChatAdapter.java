package com.hjm.tingshuo.Adapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjm.tingshuo.Bean.ChatBean;
import com.hjm.tingshuo.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by guohao on 2017/9/6.
 */

public class ChatAdapter extends BaseQuickAdapter<ChatBean,BaseViewHolder> {



    public ChatAdapter( @Nullable List<ChatBean> data) {
        super(R.layout.item_chat, data);
        System.out.println("构造方法执行");

    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
        if (item.getWhere() == 1){
            helper.setVisible(R.id.rl_left,true)
                    .setVisible(R.id.rl_right,false)
                    .setText(R.id.tv_show_time,item.getTime());
            System.out.println("转换后时间："+item.getTime());
            if (item.getType().equals("txt")){
                helper.setVisible(R.id.tv_chat_left_content,true)
                        .setText(R.id.tv_chat_left_content,item.getTxtcontent())
                        .setVisible(R.id.iv_left_chat_voice,false)
                        .addOnClickListener(R.id.tv_chat_left_content);
            }else {
                helper.setVisible(R.id.tv_chat_left_content,false)
                        .setVisible(R.id.iv_left_chat_voice,true)
                        .addOnClickListener(R.id.iv_left_chat_voice);
            }
        }else {
            helper.setVisible(R.id.rl_left,false)
                    .setVisible(R.id.rl_right,true)
                    .setText(R.id.tv_show_time,item.getTime()+"");
            if (item.getType().equals("txt")){
                helper.setVisible(R.id.tv_chat_right_content,true)
                        .setText(R.id.tv_chat_right_content,item.getTxtcontent())
                        .setVisible(R.id.iv_right_chat_voice,false)
                        .addOnClickListener(R.id.tv_chat_right_content);
            }else {
                helper.setVisible(R.id.tv_chat_right_content,false)
                        .setVisible(R.id.iv_right_chat_voice,true)
                        .addOnClickListener(R.id.iv_right_chat_voice);
            }
        }
    }

}
