package com.hjm.tingshuo.Utils;

import java.util.Formatter;
import java.util.Locale;

public class TimeUtils {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private long lastTotalRxBytes  = 0 ;
    private long lastTimeStamp = 0;

    public TimeUtils(){
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }
    public String stringForTime(int timeMs){
        int totalSeconds = timeMs/1000;
        int seconds = totalSeconds%60;
        int minutes = (totalSeconds/60)%60;
        int hours = totalSeconds/3600;
        mFormatBuilder.setLength(0);
        if (hours > 0){
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else {
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }
}
