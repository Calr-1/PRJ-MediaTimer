package com.example.timer_10;

import android.os.Build;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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


    private TextView small;
    private TextView medium;
    private TextView big;

    private final TimerFragment fragment;

    private NotificationsClass notifications;

    private int randomIntervals;

    private Upload image;

    public TimerClass(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, TextView small, TextView medium, TextView big, TimerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        notificationSound = new AlarmPlayerClass(context, R.raw.notification);
        random = new AlarmPlayerClass(context, R.raw.random);


        mode = "HH/MM/SS";

        timerRunning = false;
        timerCreated = false;

        this.small = small;
        this.medium = medium;
        this.big = big;

        this.fragment = fragment;
        soundObject = new AlarmPlayerClass(context, soundID);
        notifications = new NotificationsClass();

        randomIntervals = 0;

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
                                notificationSound.stopSoundObject();
                                notificationSound.playNotification();
                                intervalArrayNumber.remove(l);
                                break;
                            }
                        }
                    /*if(enableRandom) {
                        for (Long l : intervalArrayRandomNumber) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                random.stopSoundObject();
                                random.playNotification();
                                intervalArrayRandomNumber.remove(l);
                                break;
                            }
                        }
                    }*/
                        break;
                    case "Time":
                        notifications.setTimeIntervals(timerInitialValue);
                        intervalArrayTime = notifications.getTimeIntervals();
                        for (Long l : intervalArrayTime) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                notificationSound.stopSoundObject();
                                notificationSound.playNotification();
                                intervalArrayTime.remove(l);
                                break;
                            }
                        }
                    /*if(enableRandom) {
                        for (Long l : intervalArrayRandomTime) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                random.stopSoundObject();
                                random.playNotification();
                                intervalArrayRandomTime.remove(l);
                                break;
                            }
                        }
                    }*/
                        break;
                    case "Random":
                        for (Long l : intervalArrayRandom) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                random.stopSoundObject();
                                random.playNotification();
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
                fragment.startStopTimer();
                soundObject.startSoundObject();
                notificationSound.stopSoundObject();
                random.stopSoundObject();

            }
        };

    }

    public void startTimer() {
        countDownTimerTimerObject.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pauseUnpauseTimer(boolean isPlaying) {
        if (isPlaying)
            countDownTimerTimerObject.cancel();
        else {
            createTimer(currentTimerValue, timerCountdownInterval);
            countDownTimerTimerObject.start();
        }

    }

    public void stopSound() {
        soundObject.stopSoundObject();
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
        intervalArrayRandom = notifications.randomIntervals(randomIntervals, timerInitialValue);
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

    public void setCurrentTimerValue(long currentTimerValue) {
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

    public int getRandomIntervals() {
        return randomIntervals;
    }

    public void setRandomIntervals(int randomIntervals) {
        this.randomIntervals = randomIntervals;
    }

    public void setUpload(Upload upload) {
        this.image = upload;
    }

    public Upload getUpload() {
        return this.image;
    }
}
