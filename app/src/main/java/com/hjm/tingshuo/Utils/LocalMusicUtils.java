package com.hjm.tingshuo.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.hjm.tingshuo.Bean.LocalMusicBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linghao on 2018/12/11.
 */

public class LocalMusicUtils {


    private volatile static LocalMusicUtils instance;
    public LocalMusicBean mBean;
    private List<LocalMusicBean> mLocalMusicBeans;


    public static LocalMusicUtils getInstance() {
        if (instance == null) {
            synchronized (IntentUtils.class) {
                if (instance == null) {
                    instance = new LocalMusicUtils();
                }
            }
        }
        return instance;
    }

    /**从本地的sdcard得到数据*/
    public List<LocalMusicBean> getDataFromLocal(final Context context) {
                mLocalMusicBeans = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者

                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        mBean = new LocalMusicBean();
                        mBean.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                        mBean.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                        mBean.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                        mBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))/1000);
                        mBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                        mLocalMusicBeans.add(mBean);
                    }
                    cursor.close();
                }
        return mLocalMusicBeans;
    }

}
