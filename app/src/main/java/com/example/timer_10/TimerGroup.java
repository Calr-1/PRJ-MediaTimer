package com.example.timer_10;

import java.util.ArrayList;

public class TimerGroup {

    private ArrayList<Timer> timerArray;
    private String name;

    public TimerGroup(String name) {
        timerArray = new ArrayList<Timer>();
        this.name = name;
    }

    public void addTimer(Timer timer) {
        timerArray.add(timer);
    }

    public boolean removeTimer(Timer timer) {
        if (timerArray.remove(timer)) return true;
        return false;
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

}
