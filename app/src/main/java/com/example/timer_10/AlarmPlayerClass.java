package com.example.timer_10;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class AlarmPlayerClass {

    private MediaPlayer soundObject;
    private boolean released;
    private final Context context;
    private final int soundID;
    private Uri uri;


    public AlarmPlayerClass(Context context, int soundID) {
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
        if (uri == null) soundObject = MediaPlayer.create(context, soundID);
        else soundObject = MediaPlayer.create(context, uri);

    }

    public void playNotification() {
        soundObject.start();
    }

    public boolean isPlaying() {
        return !released;
    }

    public void setRingtone(Uri ringtone) throws IOException {
        uri = ringtone;
        soundObject = MediaPlayer.create(context, uri);
    }

    ;
}
