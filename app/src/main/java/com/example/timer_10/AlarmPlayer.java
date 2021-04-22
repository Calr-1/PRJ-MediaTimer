package com.example.timer_10;

import android.media.MediaPlayer;

public class AlarmPlayer {

    private MediaPlayer soundObject;
    private boolean released;

    public AlarmPlayer(android.content.Context context, int soundID) {
        soundObject = MediaPlayer.create(context, soundID);
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
    }

    public void playNotification() {
        soundObject.start();
    }

    public boolean isPlaying() {
        return !released;
    }
}
