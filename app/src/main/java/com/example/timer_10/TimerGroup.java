package com.example.timer_10;

import java.util.ArrayList;

public class TimerGroup {

    private ArrayList<Timer> timerArray;
    private ArrayList<timerFragment> timerFragment;

    private String name;
    private boolean runSequential;
    private int currentlyRunning;

    public TimerGroup(String name) {
        timerArray = new ArrayList<Timer>();
        timerFragment = new ArrayList<timerFragment>();

        this.name = name;
        runSequential = true;
        currentlyRunning = 0;
    }

    public void addTimer(Timer timer) {

        timerArray.add(timer);
    }

    public boolean removeTimer(Timer timer) {
        if (timerArray.remove(timer)) return true;
        return false;
    }

    public void removeTimerByIndex(int index) {
        timerArray.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfTimers() {
        return timerArray.size();
    }

    public Timer getTimerByIndex(int index) {
        return timerArray.get(index);
    }


    public ArrayList<timerFragment> getTimerFragment() {
        return timerFragment;
    }

    public void setTimerFragment(ArrayList<timerFragment> timerFragment) {
        this.timerFragment = timerFragment;
    }

    public void addTimerFragment(timerFragment fragment) {
        this.timerFragment.add(fragment);
    }

    public int getIndexOfTimer(Timer timer) {
        return timerArray.indexOf(timer);
    }

    public void runNextTimer() {

        if (runSequential) {
            currentlyRunning++;
            if (currentlyRunning < timerArray.size()) {

                if (getTimerByIndex(currentlyRunning).getCurrentTimerValue() != 0) {
                    getTimerByIndex(currentlyRunning).getFragment().start();
                } else currentlyRunning--;
            }
        }
    }

    public void setCurrentlyRunning(Timer timer) {
        currentlyRunning = getIndexOfTimer(timer);
    }
}
