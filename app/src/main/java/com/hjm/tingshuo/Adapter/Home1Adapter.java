package com.hjm.tingshuo.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.R;

import java.util.List;


/**
 * Created by linghao on 2018/8/10.
 */

public class Home1Adapter extends BaseQuickAdapter<MusicBean.DataBean,BaseViewHolder> {

    private Context mContext;

    public Home1Adapter(@Nullable List<MusicBean.DataBean> data, Context context) {
        super(R.layout.item_music, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, MusicBean.DataBean item) {
        ImageView imageView = helper.getView(R.id.iv_photo);
        Glide.with(mContext).load(item.getPic())
                .asBitmap()
                .error(R.drawable.ic_load_error)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        helper.setText(R.id.tv_songname,item.getTitle())
                .setText(R.id.tv_singername,item.getAuthor())
                .addOnClickListener(R.id.lv_all);
    }

}
