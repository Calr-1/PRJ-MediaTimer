package com.example.timer_10;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;

public class TimerClass {

    private long currentTimerValue, timerInitialValue;

    private int timerCountdownInterval,randomIntervalsMax, randomIntervalsMin;;

    private String mode,timerName;

    private List<Long> intervalArrayNumber,intervalArrayTime,intervalArrayRandom;

    private boolean timerRunning,timerCreated,vibration;

    private transient TextView small,medium,big;

    private AlarmPlayerClass soundObject, notificationSound, random;

    private NotificationsClass notifications;

    private CountDownTimer countDownTimerTimerObject;

    private Upload image;

    public TimerClass(long timerInitialValue, int timerCountdownInterval, String timerName, Context context, int soundID, TextView small, TextView medium, TextView big) {
        currentTimerValue = timerInitialValue;

        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;

        notificationSound = new AlarmPlayerClass(context, R.raw.notification);
        random = new AlarmPlayerClass(context, R.raw.random);
        soundObject = new AlarmPlayerClass(context, soundID);

        mode = "HH/MM/SS";

        timerRunning = false;
        timerCreated = false;
        vibration = false;

        this.small = small;
        this.medium = medium;
        this.big = big;

        notifications = new NotificationsClass();

        randomIntervalsMax = 1;
        randomIntervalsMin = 1;

    }
    public void TimerClassRecreate(Context context,TextView small, TextView medium, TextView big){
        this.small = small;
        this.medium = medium;
        this.big = big;
        notificationSound.AlarmPlayerClassRecreate(context);
        random.AlarmPlayerClassRecreate(context);
        soundObject.AlarmPlayerClassRecreate(context);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createTimer(long timerInitialValue, int timerCountdownInterval, Context context) {
        countDownTimerTimerObject = new CountDownTimer(timerInitialValue, timerCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimerValue = millisUntilFinished;
                switch (notifications.getTypeOfInterval()) {
                    case "Number":
                        notifications.setNumberIntervals(timerInitialValue);
                        intervalArrayNumber = notifications.getNumberIntervals();
                        for (Long l : intervalArrayNumber) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                notificationSound.stopSoundObject(context);
                                notificationSound.playNotification();
                                if (vibration) {
                                    soundObject.startVibrateInterval(500);
                                }
                                intervalArrayNumber.remove(l);
                                break;
                            }
                        }
                        break;
                    case "Time":
                        notifications.setTimeIntervals(timerInitialValue);
                        intervalArrayTime = notifications.getTimeIntervals();
                        for (Long l : intervalArrayTime) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                notificationSound.stopSoundObject(context);
                                notificationSound.playNotification();
                                if (vibration) {
                                    soundObject.startVibrateInterval(500);
                                }
                                intervalArrayTime.remove(l);
                                break;
                            }
                        }
                        break;
                    case "Random":
                        for (Long l : intervalArrayRandom) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                random.stopSoundObject(context);
                                random.playNotification();
                                if (vibration) {
                                    soundObject.startVibrateInterval(500);
                                }
                                intervalArrayRandom.remove(l);
                                break;
                            }
                        }
                        break;
                }
                TimersWrapper.updateViews(currentTimerValue, mode, small, medium, big);
            }

            @Override
            public void onFinish() {
                currentTimerValue = 0;
                soundObject.startSoundObject();
                notificationSound.stopSoundObject(context);
                soundObject.stopVibrate();
                random.stopSoundObject(context);
                timerRunning = false;
                if (vibration) {
                    long pattern[] = {0, 100, 200, 300, 400};
                    soundObject.startVibrateEnd(pattern);
                }
                ImageButton button = ((Activity) context).findViewById(R.id.play_and_pause_group_button);
                button.setImageResource(R.drawable.ic_baseline_stop_24);

            }
        };

    }

    public void stopVibration() {
        soundObject.stopVibrate();
    }

    public void startTimer() {
        countDownTimerTimerObject.start();
        timerRunning = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pauseUnpauseTimer(boolean isPlaying, Context context) {
        if (isPlaying) {
            countDownTimerTimerObject.cancel();
            timerRunning = false;
        } else {
            createTimer(currentTimerValue, timerCountdownInterval,context);
            //countDownTimerTimerObject.start();
            startTimer();
        }

    }

    public void stopSound(Context context) {
        soundObject.stopSoundObject(context);
    }

    public boolean canStopSound() {
        return soundObject.isPlaying();
    }


    public long getTimerInitialValue() {
        return timerInitialValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setTimerInitialValue(long timerInitialValue) {
        this.timerInitialValue = timerInitialValue;
        intervalArrayRandom = notifications.randomIntervals(randomIntervalsMax, randomIntervalsMin, timerInitialValue);
    }

    public int getTimerCountdownInterval() {
        return timerCountdownInterval;
    }

    public void setTimerCountdownInterval(int timerCountdownInterval) {
        this.timerCountdownInterval = timerCountdownInterval;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

    public long getCurrentTimerValue() {
        return currentTimerValue;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCurrentTimerValue(long currentTimerValue) {
        intervalArrayRandom = notifications.randomIntervals(randomIntervalsMin,randomIntervalsMax, timerInitialValue);
        this.currentTimerValue = currentTimerValue;
    }

    public void setViews(TextView small, TextView medium, TextView big) {
        this.small = small;
        this.medium = medium;
        this.big = big;
    }

    public boolean isTimerCreated() {
        return timerCreated;
    }

    public void setTimerCreated(boolean timerCreated) {
        this.timerCreated = timerCreated;
    }

    public NotificationsClass getNotifications() {
        return this.notifications;
    }

    public void setNotifications(NotificationsClass noti) {
        this.notifications = noti;
    }

    public int getRandomIntervalsMax() {
        return randomIntervalsMax;
    }

    public void setRandomIntervalsMax(int randomIntervalsMax) {
        this.randomIntervalsMax = randomIntervalsMax;
    }

    public int getRandomIntervalsMin() {
        return randomIntervalsMin;
    }

    public void setRandomIntervalsMin(int randomIntervalsMin) {
        this.randomIntervalsMin = randomIntervalsMin;
    }

    public void setUpload(Upload upload) {
        this.image = upload;
    }

    public Upload getUpload() {
        return this.image;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }
    public void setRingtone(Uri ringtone, Context context) throws IOException {
        soundObject.setRingtone(ringtone, context);
    }

    ;


}
