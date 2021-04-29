package com.example.timer_10;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;

public class Timer {

    private long currentTimerValue, timerInitialValue;
    private int timerCountdownInterval;
    private String timerName;
    private CountDownTimer countDownTimerTimerObject;
    private final AlarmPlayer soundObject, random;

    private boolean intervals;
    private int numberOfIntervals;
    private String mode;
    private ArrayList<Long> intervalArray;

    private boolean timerRunning;

    private TextView small;
    private TextView medium;
    private TextView big;

    private timerFragment fragment;

    public Timer(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, TextView small, TextView medium, TextView big, timerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        random = new AlarmPlayer(context, R.raw.notification);

        intervals = false;
        numberOfIntervals = 1;
        mode = "HH/MM/SS";
        intervalArray = createIntervals(numberOfIntervals);

        timerRunning = false;

        this.small = small;
        this.medium = medium;
        this.big = big;

        this.fragment = fragment;
        soundObject = new AlarmPlayer(context, soundID);

    }

    public void createTimer(long timerInitialValue, int timerCountdownInterval) {

        countDownTimerTimerObject = new CountDownTimer(timerInitialValue, timerCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimerValue = millisUntilFinished;
                TimersWrapper.updateViews(currentTimerValue, mode, small, medium, big);
                for (Long l : intervalArray) {
                    if (Math.abs(l - currentTimerValue) < 110 && intervals) {
                        random.playNotification();
                    }
                }
            }

            @Override
            public void onFinish() {
                fragment.startStopTimer();
                soundObject.startSoundObject();

            }
        };
    }

    public void startTimer() {
        countDownTimerTimerObject.start();

    }

    public void pauseUnpauseTimer(boolean isPlaying) {
        if (isPlaying)
            countDownTimerTimerObject.cancel();
        else {
            countDownTimerTimerObject = new CountDownTimer(currentTimerValue, timerCountdownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    currentTimerValue = millisUntilFinished;
                    TimersWrapper.updateViews(currentTimerValue, mode, small, medium, big);
                    for (Long l : intervalArray) {
                        if (Math.abs(l - currentTimerValue) < 110 && intervals) {
                            random.playNotification();
                        }
                    }
                }

                @Override
                public void onFinish() {
                    fragment.startStopTimer();
                    soundObject.startSoundObject();

                }
            };
            countDownTimerTimerObject.start();
        }

    }

    public void stopSound() {
        soundObject.stopSoundObject();
    }

    public boolean canStopSound() {
        return soundObject.isPlaying();
    }

    private ArrayList<Long> createIntervals(int numberOfIntervals) {
        ArrayList<Long> array = new ArrayList<Long>();
        if (intervals) {
            if (numberOfIntervals == 0) {
                array.add((long) 0);
            } else if (numberOfIntervals == 1) {
                long l = timerInitialValue / (numberOfIntervals + 1);
                array.add(l);
            } else {
                int n = numberOfIntervals + 1;
                long l = 0;
                if (n % 2 == 0) {
                    l = timerInitialValue / n;
                } else {
                    l = (timerInitialValue / n) + 1;
                }
                long total = timerInitialValue;
                while (total > 0) {
                    total -= l;
                    if (total > 0) {
                        array.add(total);
                    }
                }
            }
        } else {
            array.add((long) 0);
        }
        return array;
    }

    public long getTimerInitialValue() {
        return timerInitialValue;
    }

    public void setTimerInitialValue(long timerInitialValue) {
        this.timerInitialValue = timerInitialValue;
    }

    public int getTimerCountdownInterval() {
        return timerCountdownInterval;
    }

    public void setTimerCountdownInterval(int timerCountdownInterval) {
        this.timerCountdownInterval = timerCountdownInterval;
    }

    public boolean isIntervals() {
        return intervals;
    }

    public int getNumberOfIntervals() {
        return numberOfIntervals;
    }

    public String getMode() {
        return mode;
    }

    public void setIntervals(boolean intervals) {
        this.intervals = intervals;
    }

    public void setNumberOfIntervals(int numberOfIntervals) {
        this.numberOfIntervals = numberOfIntervals;
        intervalArray = createIntervals(numberOfIntervals);
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
}
