package com.example.timer_10;

import android.os.CountDownTimer;

import java.util.HashMap;

public class Timer {

    private long currentTimerValue, timerInitialValue;
    private final int timerCountdownInterval;
    private final String timerName;
    private CountDownTimer countDownTimerTimerObject;
    private final AlarmPlayer soundObject;
    private final timerFragment fragment;

    public Timer(long timerInitialValue, int timerCountdownInterval, String timerName, android.content.Context context, int soundID, timerFragment fragment) {
        currentTimerValue = timerInitialValue;
        this.timerInitialValue = timerInitialValue;
        this.timerCountdownInterval = timerCountdownInterval;
        this.timerName = timerName;
        this.fragment = fragment;

        soundObject = new AlarmPlayer(context, soundID);
        createTimer(timerInitialValue, timerCountdownInterval);

    }

    public void createTimer(long timerInitialValue, int timerCountdownInterval) {

        countDownTimerTimerObject = new CountDownTimer(timerInitialValue, timerCountdownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimerValue = millisUntilFinished;
                fragment.updateTimer(currentTimerValue);
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
                    fragment.updateTimer(currentTimerValue);
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

    public HashMap<String, Object> millisToCommonTime(Long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
        HashMap<String, Object> timerInHoursMinutesSeconds = new HashMap<>();
        timerInHoursMinutesSeconds.put("Hours", hours);
        timerInHoursMinutesSeconds.put("Minutes", minutes);
        timerInHoursMinutesSeconds.put("Seconds", seconds);

        return timerInHoursMinutesSeconds;
    }

    public void stopSound() {
        soundObject.stopSoundObject();
    }

}
