package com.hjm.tingshuo.Utils;

/**
 * Created by linghao on 2018/12/14.
 */

public class IntentBean {

    String mark;
    String message;


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IntentBean{" +
                "mark='" + mark + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
