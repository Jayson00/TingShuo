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

import com.hjm.tingshuo.Activity.ChatActivity;
import com.hjm.tingshuo.R;


/**
 * Created by linghao on 2018/9/27.
 */

public class UserNotificationUtils extends ContextWrapper {

    private Context mContext;
    private NotificationManager manager;
    public static final String id = "TingShuo";
    public static final String name = "user";

    public UserNotificationUtils(Context context) {
        super(context);
        this.mContext = context;
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
    public Notification.Builder getChannelNotification(String fri_user,String advice) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.user_notification);
        remoteViews.setTextViewText(R.id.tv_fri_user, fri_user);
        remoteViews.setTextViewText(R.id.tv_advice,advice);

        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("user",fri_user);
        intent.putExtra("type","1");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), id)
                .setContent(remoteViews)//自定义视图
                .setContentTitle("")
                .setContentText("")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_fri_user)
                .setContentIntent(pendingIntent)//notification的点击事件
                .setAutoCancel(true)//notification点击后自动取消
                .setOngoing(false);//侧滑不能取消
    }

    public NotificationCompat.Builder getNotification_25(String fri_user,String advice) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.user_notification);
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
}


