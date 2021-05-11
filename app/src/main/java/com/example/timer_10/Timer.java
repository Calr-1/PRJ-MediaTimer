package com.example.timer_10;

import android.os.Build;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class Timer {

    private long currentTimerValue;
    private long timerInitialValue;

    private int timerCountdownInterval;
    private String timerName;
    private CountDownTimer countDownTimerTimerObject;
    private final AlarmPlayer soundObject, notificationSound, random;

    private String mode;
    private List<Long> intervalArrayNumber;
    private List<Long> intervalArrayTime;
    private List<Long> intervalArrayRandomNumber, intervalArrayRandomTime, intervalArrayRandom;

    private boolean timerRunning;

    private TextView small;
    private TextView medium;
    private TextView big;

    private timerFragment fragment;

    private Notifications notifications;

    private int randomIntervals;

    public Timer(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, TextView small, TextView medium, TextView big, timerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        notificationSound = new AlarmPlayer(context, R.raw.notification);
        random = new AlarmPlayer(context, R.raw.random);

        mode = "HH/MM/SS";

        timerRunning = false;

        this.small = small;
        this.medium = medium;
        this.big = big;

        this.fragment = fragment;
        soundObject = new AlarmPlayer(context, soundID);
        notifications = new Notifications();

        randomIntervals = 0;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createTimer(long timerInitialValue, int timerCountdownInterval) {
        //System.out.println(intervalArrayNumber.toString());
        //System.out.println(intervalArrayTime.toString());
        //notifications.setRandomIntervals(intervalArrayNumber, 0.33f, timerInitialValue);
        //intervalArrayRandomNumber = notifications.getRandomIntervals();
        //System.out.println(intervalArrayRandomNumber.toString());
        //notifications.setRandomIntervals(intervalArrayTime, 0.33f, timerInitialValue);
        //intervalArrayRandomTime = notifications.getRandomIntervals();
        //System.out.println(intervalArrayRandomTime.toString());
        intervalArrayRandom = notifications.randomIntervals(randomIntervals, timerInitialValue);
        System.out.println(intervalArrayRandom.toString());
        countDownTimerTimerObject = new CountDownTimer(timerInitialValue, timerCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimerValue = millisUntilFinished;
                if (notifications.getTypeOfInterval().equals("Number")) {
                    notifications.setNumberIntervals(timerInitialValue);
                    intervalArrayNumber = notifications.getNumberIntervals();
                    for (Long l :intervalArrayNumber) {
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
                } else if (notifications.getTypeOfInterval().equals("Time")) {
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
                }
                else if (notifications.getTypeOfInterval().equals("Random")) {
                    for (Long l : intervalArrayRandom) {
                        if (Math.abs(l - currentTimerValue) < 120) {
                            random.stopSoundObject();
                            random.playNotification();
                            intervalArrayRandom.remove(l);
                            break;
                        }
                    }
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
            /*notifications.setNumberIntervals(timerInitialValue);
            intervalArrayNumber = notifications.getNumberIntervals();
            //System.out.println(intervalArrayNumber.toString());
            notifications.setTimeIntervals(timerInitialValue);
            intervalArrayTime = notifications.getTimeIntervals();
            //System.out.println(intervalArrayTime.toString());
            notifications.setRandomIntervals(intervalArrayNumber, 0.33f, timerInitialValue);
            intervalArrayRandomNumber = notifications.getRandomIntervals();
            //System.out.println(intervalArrayRandomNumber.toString());
            notifications.setRandomIntervals(intervalArrayTime, 0.33f, timerInitialValue);
            intervalArrayRandomTime = notifications.getRandomIntervals();
            //System.out.println(intervalArrayRandomTime.toString());
            intervalArrayRandom = notifications.randomIntervals(5, 0.33f, timerInitialValue);
            System.out.println(intervalArrayRandom.toString());
            countDownTimerTimerObject = new CountDownTimer(currentTimerValue, timerCountdownInterval) {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTick(long millisUntilFinished) {
                    currentTimerValue = millisUntilFinished;
                    if (notifications.getTypeOfInterval().equals("Number")) {
                        for (Long l :intervalArrayNumber) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                notificationSound.playNotification();
                                notificationSound.stopSoundObject();
                                intervalArrayNumber.remove(l);
                                break;
                            }
                        }
                        if(enableRandom) {
                            for (Long l : intervalArrayRandomNumber) {
                                if (Math.abs(l - currentTimerValue) < 120) {
                                    random.stopSoundObject();
                                    random.playNotification();
                                    intervalArrayRandomNumber.remove(l);
                                    break;
                                }
                            }
                        }
                    } else if (notifications.getTypeOfInterval().equals("Time")) {
                        for (Long l : intervalArrayTime) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                notificationSound.stopSoundObject();
                                notificationSound.playNotification();
                                intervalArrayTime.remove(l);
                                break;
                            }
                        }
                        if(enableRandom) {
                            for (Long l : intervalArrayRandomTime) {
                                if (Math.abs(l - currentTimerValue) < 120) {
                                    random.stopSoundObject();
                                    random.playNotification();
                                    intervalArrayRandomTime.remove(l);
                                    break;
                                }
                            }
                        }
                    }
                    else if (notifications.getTypeOfInterval().equals("Random")) {
                        for (Long l : intervalArrayRandom) {
                            if (Math.abs(l - currentTimerValue) < 120) {
                                random.stopSoundObject();
                                random.playNotification();
                                intervalArrayRandom.remove(l);
                                break;
                            }
                        }
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
            countDownTimerTimerObject.start();

             */
            createTimer(currentTimerValue, timerCountdownInterval);
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

    public void setTimerInitialValue(long timerInitialValue) {
        this.timerInitialValue = timerInitialValue;
        //notifications = new Notifications(timerInitialValue);
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

    public Notifications getNotifications(){
        return this.notifications;
    }

    public void setNotifications(Notifications noti){
        this.notifications = noti;
    }

    public int getRandomIntervals() {
        return randomIntervals;
    }

    public void setRandomIntervals(int randomIntervals) {
        this.randomIntervals = randomIntervals;
    }
}
