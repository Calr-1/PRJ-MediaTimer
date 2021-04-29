package com.example.timer_10;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Timer {

    private long currentTimerValue, timerInitialValue, lastValue;
    private final int timerCountdownInterval;
    private String timerName;
    private CountDownTimer countDownTimerTimerObject;
    private AlarmPlayer soundObject, random;
    private timerFragment fragment;

    private boolean intervals;
    private int numberOfIntervals;
    private String mode;
    private ArrayList<Long> intervalArray;

    public Timer(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, timerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        random = new AlarmPlayer(context, R.raw.notification);

        this.fragment = fragment;
        intervals = false;
        numberOfIntervals = 1;
        mode = "HH/MM/SS";
        intervalArray = createIntervals(numberOfIntervals);


        soundObject = new AlarmPlayer(context, soundID);
        createTimer(timerInitialValue, timerCountdownInterval);

        lastValue = 0;

    }

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

    public void createTimer(long timerInitialValue, int timerCountdownInterval) {

        countDownTimerTimerObject = new CountDownTimer(timerInitialValue, timerCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimerValue = millisUntilFinished;
                lastValue = millisUntilFinished;
                fragment.updateTimer(currentTimerValue);
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
        if (isPlaying) {
            fragment.setLastValue(lastValue);
            countDownTimerTimerObject.cancel();
        } else {
            countDownTimerTimerObject = new CountDownTimer(currentTimerValue, timerCountdownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    currentTimerValue = millisUntilFinished;
                    lastValue = millisUntilFinished;
                    fragment.updateTimer(currentTimerValue);
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

    public static HashMap<String, Object> millisToCommonTime(Long millis, String mode) {
        HashMap<String, Object> timerInHoursMinutesSeconds = new HashMap<>();
        if (mode.equals("HH/MM/SS")) {
            int seconds = (int) (millis / 1000) % 60;
            int minutes = (int) ((millis / (1000 * 60)) % 60);
            int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
            timerInHoursMinutesSeconds.put("Hours", hours);
            timerInHoursMinutesSeconds.put("Minutes", minutes);
            timerInHoursMinutesSeconds.put("Seconds", seconds);
        } else if (mode.equals("DD/HH/MM")) {
            int minutes = (int) ((millis / (1000 * 60)) % 60);
            int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
            int days = (int) (millis / (1000 * 60 * 60 * 24));
            timerInHoursMinutesSeconds.put("Days", days);
            timerInHoursMinutesSeconds.put("Hours", hours);
            timerInHoursMinutesSeconds.put("Minutes", minutes);
        }
        return timerInHoursMinutesSeconds;
    }

    public void stopSound() {
        soundObject.stopSoundObject();
    }

    public boolean canStopSound() {
        if (soundObject != null) return soundObject.isPlaying();
        return false;
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
}
