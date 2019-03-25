package com.hjm.tingshuo.Utils;

import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by linghao on 2018/12/14.
 */

public class IntentUtils {

    private static  volatile IntentUtils instance = null;
    private Intent mIntent;

    public static IntentUtils getInstance(){
        if (instance == null){
            synchronized (IntentUtils.class){
                if (instance == null){
                    instance = new IntentUtils();
                }
            }
        }
        return instance;
    }

    /**直接跳转*/
    public void SkipIntent(Context context, Class cla){
        mIntent = new Intent(context,cla);
        context.startActivity(mIntent);
    }

    /**带参跳转*/
    public void SkipIntent(Context context, Class cla, List<IntentBean> beans){
        mIntent = new Intent(context,cla);
        for (int i=0;i<beans.size();i++){
            mIntent.putExtra(beans.get(i).getMark(),beans.get(i).getMessage());
        }
        context.startActivity(mIntent);
    }
}
