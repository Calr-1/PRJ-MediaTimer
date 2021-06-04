package com.example.timer_10;

import java.util.ArrayList;

public class TimerGroupClass{


    private ArrayList<TimerClass> timerClassArray;
    private ArrayList<TimerFragment> timerFragment;
    private ArrayList<TimerGroupClass> timerGroupClassArray;
    private ArrayList<TimerGroupFragment> timerGroupFragment;


    private String name;
    private boolean runSequential;
    private int currentlyRunning;
    private final TimersWrapper wrapper;

    public TimerGroupClass(String name) {
        wrapper = TimersWrapper.getInstance();
        timerClassArray = new ArrayList<TimerClass>();
        timerFragment = new ArrayList<TimerFragment>();
        timerGroupClassArray = new ArrayList<TimerGroupClass>();
        timerGroupFragment = new ArrayList<TimerGroupFragment>();

        this.name = name;
        runSequential = true;
        currentlyRunning = 0;
    }

    public void addTimer(TimerClass timerClass) {

        timerClassArray.add(timerClass);
    }

    public void addTimerGroup(TimerGroupClass timer) {

        timerGroupClassArray.add(timer);
        wrapper.addGroupOfTimers(timer);
    }

    public boolean removeTimer(TimerClass timerClass) {
        return timerClassArray.remove(timerClass);
    }

    public void removeTimerByIndex(int index) {
        timerClassArray.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfTimers() {
        return timerClassArray.size();
    }

    public int getNumberOfGroups() {
        return timerGroupClassArray.size();
    }


    public TimerClass getTimerByIndex(int index) {
        return timerClassArray.get(index);
    }

    public TimerGroupClass getGroupByIndex(int index) {
        return timerGroupClassArray.get(index);
    }


    public ArrayList<TimerFragment> getTimerFragment() {
        return timerFragment;
    }

    public void setTimerFragment(ArrayList<TimerFragment> timerFragment) {
        this.timerFragment = timerFragment;
    }

    public void addTimerFragment(TimerFragment fragment) {
        this.timerFragment.add(fragment);
    }

    public void addTimerGroupFragment(TimerGroupFragment fragment) {
        this.timerGroupFragment.add(fragment);
    }


    public int getIndexOfTimer(TimerClass timerClass) {
        return timerClassArray.indexOf(timerClass);
    }

    public void runNextTimer() {

        if (runSequential) {
            currentlyRunning++;
            if (currentlyRunning < timerClassArray.size()) {

                if (getTimerByIndex(currentlyRunning).getCurrentTimerValue() != 0) {
                    timerFragment.get(currentlyRunning).start();
                } else currentlyRunning--;
            }
        }
    }

    public void setCurrentlyRunning(TimerClass timerClass) {
        currentlyRunning = getIndexOfTimer(timerClass);
    }
}
