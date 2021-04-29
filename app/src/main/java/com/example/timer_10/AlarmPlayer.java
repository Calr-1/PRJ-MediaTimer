package com.example.timer_10;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayer {

    private MediaPlayer soundObject;
    private boolean released;
    private Context context;
    private int soundID;


    public AlarmPlayer(Context context, int soundID) {
        soundObject = MediaPlayer.create(context, soundID);
        this.context = context;
        this.soundID = soundID;

    }

    public void startSoundObject() {
        soundObject.start();
        soundObject.setLooping(true);
        released = false;
    }

    public void stopSoundObject() {
        soundObject.stop();
        soundObject.release();
        released = true;
        soundObject = MediaPlayer.create(context, soundID);

    }

    public void playNotification() {
        soundObject.start();
    }

    public boolean isPlaying() {
        return !released;
    }
}
