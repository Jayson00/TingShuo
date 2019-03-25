package com.hjm.tingshuo.Bean;

/**
 * Created by linghao on 2018/12/26.
 */

public class ChatBean {

    int where;
    String type;
    String time;
    String txtcontent;
    String voiceurl;

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxtcontent() {
        return txtcontent;
    }

    public void setTxtcontent(String txtcontent) {
        this.txtcontent = txtcontent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVoiceurl() {
        return voiceurl;
    }

    public void setVoiceurl(String voiceurl) {
        this.voiceurl = voiceurl;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "where='" + where + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", txtcontent='" + txtcontent + '\'' +
                ", voiceurl='" + voiceurl + '\'' +
                '}';
    }
}
