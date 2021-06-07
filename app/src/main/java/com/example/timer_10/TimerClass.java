package com.example.timer_10;

import android.net.Uri;

import android.os.Build;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import java.util.List;

public class TimerClass {

    private long currentTimerValue, timerInitialValue;
    private int timerCountdownInterval;
    private String timerName;
    private CountDownTimer countDownTimerTimerObject;
    private final AlarmPlayerClass soundObject, notificationSound, random;

    private String mode;

    private List<Long> intervalArrayNumber;
    private List<Long> intervalArrayTime;
    private List<Long> intervalArrayRandom;

    private boolean timerRunning;

    private boolean timerCreated;

    private boolean vibration;

    private TextView small;
    private TextView medium;
    private TextView big;

    private TimerFragment fragment;

    private NotificationsClass notifications;

    private int randomIntervalsMax, randomIntervalsMin;

    private Upload image;

    private ProgressBar mProgressBar;

    public TimerClass(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, TextView small, TextView medium, TextView big, TimerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        notificationSound = new AlarmPlayerClass(context, R.raw.notification);
        random = new AlarmPlayerClass(context, R.raw.random);
        mProgressBar = fragment.getView().findViewById(R.id.progress_bar);

        mode = "HH/MM/SS";

        timerRunning = false;
        timerCreated = false;
        vibration = false;

        this.small = small;
        this.medium = medium;
        this.big = big;

        this.fragment = fragment;
        soundObject = new AlarmPlayerClass(context, soundID);
        notifications = new NotificationsClass();

        randomIntervalsMax = 1;
        randomIntervalsMin = 1;

        mProgressBar.setProgress(0);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createTimer(long timerInitialValue, int timerCountdownInterval) {
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
                                notificationSound.stopSoundObject(fragment.getContext());
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
                                notificationSound.stopSoundObject(fragment.getContext());
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
                                random.stopSoundObject(fragment.getContext());
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
                double progress = (100.0 * currentTimerValue / getTimerInitialValue());
                mProgressBar.setProgress((int) progress);
                TimersWrapper.updateViews(currentTimerValue, mode, small, medium, big);
            }

            @Override
            public void onFinish() {
                currentTimerValue = 0;
                soundObject.startSoundObject();
                notificationSound.stopSoundObject(fragment.getContext());
                soundObject.stopVibrate();
                random.stopSoundObject(fragment.getContext());
                timerRunning = false;
                if (vibration) {
                    long pattern[] = {0, 100, 200, 300, 400};
                    soundObject.startVibrateEnd(pattern);
                }
                mProgressBar.setProgress(100);
                ImageButton button = fragment.getView().findViewById(R.id.play_and_pause_group_button);
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
    public void pauseUnpauseTimer(boolean isPlaying) {
        if (isPlaying) {
            countDownTimerTimerObject.cancel();
            timerRunning = false;
        } else {
            createTimer(currentTimerValue, timerCountdownInterval);
            //countDownTimerTimerObject.start();
            startTimer();
        }

    }

    public void stopSound() {
        mProgressBar.setProgress(0);
        soundObject.stopSoundObject(fragment.getContext());
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

    public TimerFragment getFragment() {
        return fragment;
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

    public void setRingtone(Uri ringtone) throws IOException {
        soundObject.setRingtone(ringtone, fragment.getContext());
    }

    public void setFragment(TimerFragment frag){
        fragment =frag;
        setViews();
    }

    public void setViews(){

        this.big = fragment.getView().findViewById(R.id.hoursEditView);
        this.medium = fragment.getView().findViewById(R.id.minutesEditView);
        this.small = fragment.getView().findViewById(R.id.secondsEditView);
        this.mProgressBar = fragment.getView().findViewById(R.id.progress_bar);

    }


}
