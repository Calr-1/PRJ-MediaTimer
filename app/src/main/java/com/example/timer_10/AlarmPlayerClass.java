package com.example.timer_10;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

import java.io.IOException;

public class AlarmPlayerClass {

    private transient MediaPlayer soundObject;
    private boolean released;

    private int soundID;
    transient Vibrator vibrator;
    private Uri uri;


    public AlarmPlayerClass(Context context, int soundID) {
        soundObject = MediaPlayer.create(context, soundID);
        this.soundID = soundID;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void AlarmPlayerClassRecreate(Context context){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(uri!=null){
        soundObject = MediaPlayer.create(context, uri);}
        else soundObject = MediaPlayer.create(context, soundID);
    }

    public void startSoundObject() {
        soundObject.start();
        soundObject.setLooping(true);
        released = true;
    }

    public void stopSoundObject(Context context) {
        soundObject.stop();
        soundObject.release();
        released = false;
        if (uri == null) soundObject = MediaPlayer.create(context, soundID);
        else soundObject = MediaPlayer.create(context, uri);


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
        return released;
    }

    public void setRingtone(Uri ringtone,Context context) throws IOException {
        uri = ringtone;

        soundObject = MediaPlayer.create(context, uri);
    }


}
