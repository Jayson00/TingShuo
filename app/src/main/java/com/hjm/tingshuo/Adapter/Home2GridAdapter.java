package com.hjm.tingshuo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjm.tingshuo.Bean.MusicBean;
import com.hjm.tingshuo.R;

import java.util.List;

/**
 * Created by linghao on 2018/10/17.
 */

public class Home2GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<MusicBean.DataBean> mSongListBeans;

    public Home2GridAdapter(Context context, List<MusicBean.DataBean> mSongListBeans) {
        this.mContext = context;
        this.mSongListBeans = mSongListBeans;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return mSongListBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new MyViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid,viewGroup,false);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_show_new);
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.iv_show_new);
            view.setTag(viewHolder);
        }else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        viewHolder.mTextView.setText(mSongListBeans.get(i).getTitle());
        Glide.with(mContext).load(mSongListBeans.get(i).getPic()).into(viewHolder.mImageView);

        return view;
    }


    class MyViewHolder{
        TextView mTextView;
        ImageView mImageView;
    }
}
