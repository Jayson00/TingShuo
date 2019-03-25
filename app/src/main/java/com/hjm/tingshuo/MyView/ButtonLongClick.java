package com.hjm.tingshuo.MyView;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.hjm.tingshuo.Bean.ChatBean;
import com.hjm.tingshuo.Constant.PathValue;
import com.hjm.tingshuo.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by linghao on 2018/12/26.
 */

public class ButtonLongClick  implements View.OnTouchListener{


    public VoiceCallback mVoiceCallback;
    public interface VoiceCallback{
        void onSuccess(EMMessage message,String path);
        void onError(int code, String error);
    }

    private void Success(EMMessage message,String path,VoiceCallback callback){
        System.out.println("Success执行到这了");
        callback.onSuccess(message,path);
    }

    private void Error(int code, String error,VoiceCallback callback){
        System.out.println("Error执行到这了");
        callback.onError(code,error);
    }


    private Button mButton;
    private String userId;
    private int duration;

    /**录音数队列*/
    private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    private AudioRecord mAudioRecord = null;
    private int mRecorderBufferSize;
    private byte[] mAudioData;

    /*默认数据*/
    private int mSampleRateInHZ = 8000; //采样率
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;  //位数
    private int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;   //声道

    private boolean isRecording = false;
    private String mTmpFileAbs = null;


    public ButtonLongClick(Button button,String userId,VoiceCallback callback) {
        this.mButton = button;
        this.userId = userId;
        this.mVoiceCallback = callback;
        if (mAudioRecord != null) {
            mAudioRecord.release();
        }
        mSampleRateInHZ = 8000;
        mAudioFormat = 2;
        initData();
    }


    private void initData() {
        mRecorderBufferSize = AudioRecord.getMinBufferSize(mSampleRateInHZ, mChannelConfig, mAudioFormat);
        mAudioData = new byte[320];
        if (mAudioRecord == null){
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, mSampleRateInHZ, mChannelConfig, mAudioFormat, mRecorderBufferSize);
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            mButton.setBackgroundResource(R.drawable.voice_bg_press);
            if (!isRecording) {

                String tmpName = System.currentTimeMillis() + "_" + mSampleRateInHZ + "";
                final File tmpFile = createFile(tmpName + ".pcm");
                final File tmpOutFile = createFile(tmpName + ".wav");
                mTmpFileAbs = tmpOutFile.getAbsolutePath();
                System.out.println("路径名："+mTmpFileAbs);

                isRecording = true;
                mAudioRecord.startRecording();
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileOutputStream outputStream = new FileOutputStream(tmpFile.getAbsoluteFile());

                            while (isRecording) {
                                int readSize = mAudioRecord.read(mAudioData, 0, mAudioData.length);
                                Log.i("录音", "run: ------>" + readSize);
                                outputStream.write(mAudioData);
                            }

                            outputStream.close();
                            pcmToWave(tmpFile.getAbsolutePath(), tmpOutFile.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("已松开.....");
            mButton.setBackgroundResource(R.drawable.voice_bg);
            if (isRecording) {
                ToastUtils.showShort("已结束");
                isRecording = false;
                mAudioRecord.stop();

                getAudioDuration(mTmpFileAbs);
                final EMMessage message = EMMessage.createVoiceSendMessage(mTmpFileAbs,duration,userId);
                EMClient.getInstance().chatManager().sendMessage(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // 消息发送成功，打印下日志，正常操作应该去刷新ui
                        Success(message,mTmpFileAbs,mVoiceCallback);
                    }
                    @Override
                    public void onError(int code, String error) {
                        System.out.println("System.out.println(\"发送失败\");");
                        Error(code,error,mVoiceCallback);
                    }
                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        }
        return true;
    }



    /**获取视频总时长*/
    private void getAudioDuration(String path){
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(path);  //为音频文件的路径
            player.prepare();
            duration = player.getDuration()/1000;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void pcmToWave(String inFileName, String outFileName) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long longSampleRate = mSampleRateInHZ;
        long totalDataLen = totalAudioLen + 36;
        int channels = 1;//你录制是单声道就是1 双声道就是2（如果错了声音可能会急促等）
        long byteRate = 16 * longSampleRate * channels / 8;

        byte[] data = new byte[mRecorderBufferSize];
        try {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(outFileName);

            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    /*
    任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，wave是RIFF文件结构，每一部分为一个chunk，其中有RIFF WAVE chunk，
    FMT Chunk，Fact chunk,Data chunk,其中Fact chunk是可以选择的，
     */
    private void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }


    private File createFile(String name) {

        String dirPath = PathValue.audio_directory;
        File file = new File(dirPath);

        if (!file.exists()) {
            file.mkdirs();
            System.out.println("创建文件夹");
        }

        String filePath = dirPath + name;
        File objFile = new File(filePath);
        if (!objFile.exists()) {
            try {
                objFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("新建文件");
            return objFile;

        }
        return null;
    }
}
