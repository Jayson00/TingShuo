package com.hjm.tingshuo.Utils;

import android.os.Environment;

import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件的创建与删除
 * @author hjm
 */

public class FileUtils {

    private static volatile FileUtils instance = null;
    private File mFile;


    public static FileUtils getInstance(){
        if (instance == null){
            synchronized (FileUtils.class){
                if (instance == null){
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }


    /**创建文件夹*/
    public void CreatDir(String dirname){
        mFile = new File(dirname);
        if (!mFile.exists()){
            mFile.mkdirs();
        }
    }


    /**创建多个文件夹*/
    public void CreatDirs(String[] dirnames){
        for (int i=0;i<dirnames.length;i++){
            mFile = new File(dirnames[i]);
            if (!mFile.exists()){
                mFile.mkdirs();
            }
        }
    }


    /**创建文件*/
    public void CreatFile(String filename){
        mFile = new File(filename);
        if (!mFile.exists()){
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**创建多个文件*/
    public void CreatFiles(String[] filenames){
        for (int i=0;i<filenames.length;i++){
            mFile = new File(filenames[i]);
            if (!mFile.exists()){
                try {
                    mFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**删除文件夹*/
    public void DelDir(String dirname){
        mFile = new File(dirname);
        if (mFile.exists()){
            mFile.delete();
        }
    }

    /**删除多个文件夹*/
    public void DelDirs(String[] dirnames){
        for(int i=0;i<dirnames.length;i++){
            mFile = new File(dirnames[i]);
            if (mFile.exists()){
                mFile.delete();
            }
        }
    }


    /**删除文件*/
    public void DelFile(String filename){
        mFile = new File(filename);
        if (mFile.exists()){
            mFile.delete();
        }
    }


    /**删除多个文件*/
    public void DelFiles(String[] dirnames){
        for(int i=0;i<dirnames.length;i++){
            mFile = new File(dirnames[i]);
            if (mFile.exists()){
                mFile.delete();
            }
        }
    }

    /**将文件写入本地*/
    public void WriteToFile(String path,String content){
        com.hjm.tingshuo.Utils.FileUtils.getInstance().CreatFile(path);
        File file = new File(path);
        if (file.exists()){
            FileIOUtils.writeFileFromString(file,content);
        }
    }

}
