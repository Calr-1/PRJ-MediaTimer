package com.example.timer_10;

import java.util.ArrayList;

public class TimerGroup {


    private ArrayList<Timer> timerArray;
    private ArrayList<timerFragment> timerFragment;
    private ArrayList<TimerGroup> timerGroupArray;
    private ArrayList<timer_group> timerGroupFragment;


    private String name;
    private boolean runSequential;
    private int currentlyRunning;
    private TimersWrapper wrapper;

    public TimerGroup(String name) {
        wrapper = TimersWrapper.getInstance();
        timerArray = new ArrayList<Timer>();
        timerFragment = new ArrayList<timerFragment>();
        timerGroupArray = new ArrayList<TimerGroup>();
        timerGroupFragment = new ArrayList<timer_group>();

        this.name = name;
        runSequential = true;
        currentlyRunning = 0;
    }

    public void addTimer(Timer timer) {

        timerArray.add(timer);
    }

    public void addTimerGroup(TimerGroup timer) {

        timerGroupArray.add(timer);
        wrapper.addGroupOfTimers(timer);
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

    public int getNumberOfGroups() {
        return timerGroupArray.size();
    }


    public Timer getTimerByIndex(int index) {
        return timerArray.get(index);
    }

    public TimerGroup getGroupByIndex(int index) {
        return timerGroupArray.get(index);
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

    public void addTimerGroupFragment(timer_group fragment) {
        this.timerGroupFragment.add(fragment);
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
