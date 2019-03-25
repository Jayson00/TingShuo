package com.hjm.tingshuo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

/**
 * Created by linghao on 2018/12/5.
 */

public class PermissionUtil {


    private static volatile PermissionUtil instance = null;

    private static String[] permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};


    public static PermissionUtil getInstance(){
        if (instance == null){
            synchronized (PermissionUtil.class){
                if (instance == null){
                    instance = new PermissionUtil();
                }
            }
        }
        return instance;
    }


    public  boolean RequestPermissions(Context context){
        ActivityCompat.requestPermissions((Activity) context, permissions,100);
        return true;
    }

}
