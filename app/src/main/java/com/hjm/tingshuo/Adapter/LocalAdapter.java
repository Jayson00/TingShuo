package com.hjm.tingshuo.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.transition.Visibility;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjm.tingshuo.Bean.LocalMusicBean;
import com.hjm.tingshuo.R;
import java.util.List;

/**
 * 本地音乐的适配器
 * Created by linghao on 2018/8/13.
 */

public class LocalAdapter extends BaseQuickAdapter<LocalMusicBean,BaseViewHolder> {


    public LocalAdapter(@Nullable List<LocalMusicBean> data) {
        super(R.layout.item_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalMusicBean item) {
        helper.setVisible(R.id.iv_sign, false);
        helper.setText(R.id.tv_songname,item.getTitle().toString())
                .setText(R.id.tv_singername,item.getAuthor().toString())
                .addOnClickListener(R.id.lv_all);
    }
}






