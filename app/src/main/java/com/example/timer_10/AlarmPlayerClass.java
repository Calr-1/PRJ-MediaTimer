package com.example.timer_10;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;

public class AlarmPlayerClass {

    private MediaPlayer soundObject;
    private boolean released;
    private final Context context;
    private final int soundID;
    Vibrator vibrator;


    public AlarmPlayerClass(Context context, int soundID) {
        soundObject = MediaPlayer.create(context, soundID);
        this.context = context;
        this.soundID = soundID;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

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

    public void startVibrateEnd(long pattern[]) {
        vibrator.vibrate(pattern, 0);
    }

    public void startVibrateInterval(long time) {
        vibrator.vibrate(time);
    }

    public void stopVibrate() {
        vibrator.cancel();
    }

    public void playNotification() {
        soundObject.start();
    }

    public boolean isPlaying() {
        return !released;
    }
}
