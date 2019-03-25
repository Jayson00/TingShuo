package com.hjm.tingshuo.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 退出程序
 * @author hjm
 */

public class ExitAppUtils {

    private static volatile  ExitAppUtils instance = null;
    long exitTime = 0;

    public static ExitAppUtils getInstance(){
        if (instance == null){
            synchronized (ExitAppUtils.class){
                if (instance == null){
                    instance = new ExitAppUtils();
                }
            }
        }
        return instance;
    }

    public void Exit(Context context){

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(context,"再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            //彻底关闭整个APP
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);
                System.exit(0);
            } else {// android2.1
                ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                am.restartPackage(context.getPackageName());
            }
        }
    }
}
