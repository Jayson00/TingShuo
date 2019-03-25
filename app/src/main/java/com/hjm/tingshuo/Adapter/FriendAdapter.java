package com.hjm.tingshuo.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjm.tingshuo.R;

import java.util.List;


/**
 * Created by guohao on 2017/9/6.
 */

public class FriendAdapter extends BaseQuickAdapter<String,BaseViewHolder> {



    public FriendAdapter(@Nullable List<String> data) {
        super(R.layout.item_friend, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_fri_user,item)
                .addOnClickListener(R.id.lv_all)
                .addOnLongClickListener(R.id.lv_all);
    }
}
