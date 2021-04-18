package com.example.timer_10;

import android.media.MediaPlayer;

public class AlarmPlayer {

    private MediaPlayer soundObject;

    public AlarmPlayer(android.content.Context context, int soundID){
        soundObject = MediaPlayer.create(context, soundID);
    }
    public void startSoundObject(){
        soundObject.start();
        soundObject.setLooping(true);
    }
    public void stopSoundObject(){
        soundObject.stop();
        soundObject.release();
    }
}
