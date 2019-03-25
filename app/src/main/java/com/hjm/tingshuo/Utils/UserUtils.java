package com.hjm.tingshuo.Utils;

import com.hjm.tingshuo.Constant.PathValue;

/**
 * Created by linghao on 2018/12/25.
 */

public class UserUtils {

    private static volatile UserUtils instance = null;
    private String user;
    private String pwd;
    private boolean status;


    public static UserUtils getInstance(){
        if (instance == null){
            synchronized (UserUtils.class){
                if (instance == null){
                    instance = new UserUtils();
                }
            }
        }
        return instance;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }


    public void setUser(String user,String pwd,boolean status){
        this.user = user;
        this.pwd = pwd;
        this.status = status;
    }

    public String getUser(){
        return user;
    }

    public String getPwd(){
        return pwd;
    }

    public boolean getStatus(){
        return status;
    }
}
