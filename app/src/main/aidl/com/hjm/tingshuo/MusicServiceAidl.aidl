// MusicServiceAidl.aidl
package com.hjm.tingshuo;

// Declare any non-default types here with import statements

interface MusicServiceAidl {

    void  prepare();

    void  start();

    void play();

            void  pause();

            void  stop();

            int  getCurrentPosition();

            int  getDuration();

            String getTitle();

            String getAuthor();

            String getUrl();

            void next();

            void pre();

            void setPlayMode();

            String getPlayMode();

            boolean isPlaying();

            void seekTo(int position);
}
