package com.example.timer_10;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationsClass {

    private List<Long> numberIntervals;
    private List<Long> timeIntervals;
    private List<Long> randomIntervals;


    private String typeOfInterval;

    private long intervalTime;
    private int intervalNumber;

    public NotificationsClass() {
        numberIntervals = new ArrayList<Long>();
        timeIntervals = new ArrayList<Long>();
        randomIntervals = new ArrayList<Long>();

        intervalTime = 0L;
        intervalNumber = 1;
        typeOfInterval = "No intervals";
    }

    public void setNumberIntervals(long timerInitialValue) {
        System.out.println("numero de intervalos definidos pelo user: " + intervalNumber);
        numberIntervals.clear();
        if (intervalNumber == 0) {
            numberIntervals.add((long) 0);
        } else if (intervalNumber == 1) {
            long l = timerInitialValue / (intervalNumber + 1);
            numberIntervals.add(l);
        } else {
            int n = intervalNumber + 1;
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
                    numberIntervals.add(total);
                }
            }
        }
    }

    public void setTimeIntervals(long timerInitialValue) {
        System.out.println("intervalo de tempo definido pelo user: " + intervalTime);
        timeIntervals.clear();
        long total = timerInitialValue;
        while (total > 0) {
            total -= intervalTime;
            if (total > 0) {
                timeIntervals.add(total);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRandomIntervals(List<Long> intervals, float numberOfIntervalsProb, long timerInitialValue) {
        randomIntervals.clear();
        for (int i = 0; i < intervals.size(); i++) {
            long interval = 0L;
            double b = Math.random();
            if (i == 0) {
                if (b <= numberOfIntervalsProb) {
                    long low = intervals.get(i) + 2000;
                    long high = timerInitialValue - 2000;
                    interval = ThreadLocalRandom.current().nextLong(low, high);
                }
            } else {
                if (b <= numberOfIntervalsProb) {
                    long low = intervals.get(i) + 2000;
                    long high = intervals.get(i - 1) - 2000;
                    interval = ThreadLocalRandom.current().nextLong(low, high);
                }
            }
            randomIntervals.add(interval);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Long> randomIntervals(int maxIntervals, int minIntervals, long timerInitialValue) {
        ArrayList<Long> intervals = new ArrayList<Long>();
        long time = timerInitialValue;
        long interval = timerInitialValue / (maxIntervals + 1);
        int numberOfIntervals = 0;
        if(maxIntervals > minIntervals){
            Random r = new Random();
            numberOfIntervals = r.nextInt((maxIntervals+1)-minIntervals) + minIntervals;
        }
        else numberOfIntervals = maxIntervals;
        for (int i = 0; i < numberOfIntervals; i++) {
            Random random = new Random();
            int low = -40;
            int high = 40;
            int r = random.nextInt(high - low) + low;
            float percentage = (float) ((100.0 + r) / 100.0);
            long alarm = (long) (interval * percentage);
            if (i == (maxIntervals - 1)) intervals.add(Math.abs(time - alarm));
            else intervals.add(time - alarm);
            time -= interval;
        }
        return intervals;
        /*Random r = new Random();
        //int result = r.nextInt(maxIntervals);
        int result = maxIntervals;
        ArrayList<Long> intervals = new ArrayList<Long>();
        long gama = 0L;
        if (result == 0) {
            gama = timerInitialValue;
        } else {
            gama = timerInitialValue / result;
        }
        for (int i = 0; i < result; i++) {
            long x = 0L;
            if (i == 0) x = timerInitialValue;
            else x = intervals.get(i - 1);
            long y = x - gama;
            if (y > 0) intervals.add(y);
        }
        intervals.add(0L);
        ArrayList<Long> random = new ArrayList<Long>();
        for (int i = 0; i < intervals.size(); i++) {
            long interval = 0L;
            long low = intervals.get(i);
            long high;
            if (i == 0) {
                high = timerInitialValue;
            } else {
                high = intervals.get(i - 1);
            }
            interval = ThreadLocalRandom.current().nextLong(low, high);
            random.add(interval);
            return random;
        }*/
    }


    public List<Long> getNumberIntervals() {
        return numberIntervals;
    }

    public List<Long> getTimeIntervals() {
        return timeIntervals;
    }

    public List<Long> getRandomIntervals() {
        return randomIntervals;
    }

    public String getTypeOfInterval() {
        return typeOfInterval;
    }

    public void setTypeOfInterval(String typeOfInterval) {
        this.typeOfInterval = typeOfInterval;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getNumberOfIntervals() {
        return intervalNumber;
    }

    public void setNumberOfIntervals(int numberOfIntervals) {
        this.intervalNumber = numberOfIntervals;
    }
}