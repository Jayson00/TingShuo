package com.hjm.tingshuo.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.hjm.tingshuo.R;


/**
 * Created by linghao on 2018/9/27.
 */

public class FriendNotificationUtils extends ContextWrapper {
    private NotificationManager manager;
    private NotificationChannel channel;
    public static final String id = "TingShuo";
    public static final String name = "user";

    public FriendNotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
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
    public Notification.Builder getChannelNotification(String fri_user,String advice) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.friend_notification);
        remoteViews.setTextViewText(R.id.tv_fri_user, fri_user);
        remoteViews.setTextViewText(R.id.tv_advice,advice);


        //发送接收的广播
        Intent intent1 = new Intent("accept");
        intent1.putExtra("fri_user",fri_user);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,0,intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.btn_accept,pendingIntent1);

        //发送接收的广播
        Intent intent2 = new Intent("refuse");
        intent2.putExtra("fri_user",fri_user);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,0,intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.btn_cancle,pendingIntent2);

        return new Notification.Builder(getApplicationContext(), id)
                .setContent(remoteViews)//自定义视图
                .setContentTitle("")
                .setContentText("")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_fri_user)
                .setAutoCancel(true)//notification点击后自动取消
                .setOngoing(false);//侧滑不能取消
    }

    public NotificationCompat.Builder getNotification_25(String fri_user,String advice) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.friend_notification);
        remoteViews.setTextViewText(R.id.tv_fri_user, fri_user);
        remoteViews.setTextViewText(R.id.tv_advice,advice);

        return new NotificationCompat.Builder(getApplicationContext())
                .setContent(remoteViews)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_fri_user)
                .setAutoCancel(true)
                .setOngoing(false);
    }



    public void sendNotification(String fri_user,String advice) {
        if (Build.VERSION.SDK_INT >= 26) {
            Log.e("Android api > 26","创建NotificationChannel");
            createNotificationChannel();
            Notification notification = getChannelNotification(fri_user,advice).build();
            getManager().notify(1, notification);
        } else {
            Log.e("android api < 26","直接创建Notification");
            Notification notification = getNotification_25(fri_user,advice).build();
            getManager().notify(1, notification);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ClearNotification(){
        System.out.println("清楚通知欄");
        manager.deleteNotificationChannel(id);
    }
}


