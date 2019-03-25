package com.hjm.tingshuo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjm.tingshuo.Bean.SongNameListBean;
import com.hjm.tingshuo.R;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by guohao on 2017/9/6.
 */

public class Home3Adapter extends RecyclerView.Adapter<Home3Adapter.ViewHolder> {

    private static final int MODE_CHECK = 0;
    int mEditMode = MODE_CHECK;

    private Context context;
    private List<SongNameListBean> mMyLiveList;
    private OnItemClickListener mOnItemClickListener;

    public Home3Adapter(Context context, List<SongNameListBean> myLiveList) {
        this.context = context;
        this.mMyLiveList = myLiveList;
    }


    public List<SongNameListBean> getSongList() {
        if (mMyLiveList == null) {
            mMyLiveList = new ArrayList<>();
        }
        return mMyLiveList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_live, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mMyLiveList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SongNameListBean myLive = mMyLiveList.get(holder.getAdapterPosition());
        holder.mTvTitle.setText(myLive.getTitle());
        if (mEditMode == MODE_CHECK) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);

            if (myLive.isSelect()) {
                holder.mCheckBox.setImageResource(R.drawable.ic_checked);
            } else {
                holder.mCheckBox.setImageResource(R.drawable.ic_uncheck);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mMyLiveList);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos, List<SongNameListBean> myLiveList);
    }
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mRadioImg;
        TextView mTvTitle;
        RelativeLayout mRootView;
        ImageView mCheckBox;



        public ViewHolder(View itemView) {
            super(itemView);
            mRadioImg = itemView.findViewById(R.id.radio_img);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mRootView = itemView.findViewById(R.id.root_view);
            mCheckBox = itemView.findViewById(R.id.check_box);

        }
    }


}
