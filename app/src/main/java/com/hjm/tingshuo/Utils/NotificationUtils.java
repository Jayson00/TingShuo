package com.hjm.tingshuo.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.hjm.tingshuo.Activity.LocalPlayActivity;
import com.hjm.tingshuo.Activity.PlayActivity;
import com.hjm.tingshuo.R;


/**
 * Created by linghao on 2018/9/27.
 */

public class NotificationUtils extends ContextWrapper {
    private NotificationManager manager;
    public static final String id = "TingShuo";
    public static final String name = "music";
    private Intent intent;

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
        channel.enableVibration(false);
        channel.setVibrationPattern(new long[]{0});
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        }
        return manager;
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(int type,String title, String content, int icon, int icon2) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.music_notification);
        remoteViews.setTextViewText(R.id.text_content_title, title);
        remoteViews.setTextViewText(R.id.text_content,content);

        if (type == 0){
            intent = new Intent(this, PlayActivity.class);
        }else{
            intent = new Intent(this, LocalPlayActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        //发送上一首的广播
        Intent intent1 = new Intent("PreService");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,0,intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_pre,pendingIntent1);
        //发送播放/暂停的广播
        Intent intent2 = new Intent("PlayService");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,0,intent2,0);
        remoteViews.setImageViewResource(R.id.iv_notification_play,icon2);
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play,pendingIntent2);

        //发送下一首的广播
        Intent intent3 = new Intent("NextService");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,0,intent3,0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_next,pendingIntent3);

        return new Notification.Builder(getApplicationContext(), id)
                .setContent(remoteViews)//自定义视图
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)//notification的点击事件
                .setAutoCancel(true)//notification点击后自动取消
                .setOngoing(true);//侧滑不能取消
    }

    public NotificationCompat.Builder getNotification_25(String title, String content, int icon) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.music_notification);
        remoteViews.setTextViewText(R.id.text_content_title, title);
        remoteViews.setTextViewText(R.id.text_content,content);

        return new NotificationCompat.Builder(getApplicationContext())
                .setContent(remoteViews)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),icon))
                .setAutoCancel(true);
    }



    public void sendNotification(int type,String title, String content, int icon, int icon2) {
        if (Build.VERSION.SDK_INT >= 26) {
            Log.e("Android api > 26","创建NotificationChannel");
            createNotificationChannel();
            Notification notification = getChannelNotification(type,title, content,icon,icon2).build();
            getManager().notify(1, notification);
        } else {
            Log.e("android api < 26","直接创建Notification");
            Notification notification = getNotification_25(title, content,icon).build();
            getManager().notify(1, notification);
        }
    }
}


