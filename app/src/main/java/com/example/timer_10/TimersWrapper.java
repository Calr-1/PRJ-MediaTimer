package com.example.timer_10;

import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton class that contains all the timers created
 * Serializable to facilitate saving user data
 */
public class TimersWrapper implements Serializable {

    /**
     * Single instance of TimersWrapper
     */
    private static TimersWrapper instance = null;

    /**
     * Array containing the individual timers
     */
    private ArrayList<Timer> individualTimerList;

    /**
     * Array containing the groups of timers
     */
    private ArrayList<TimerGroup> groupsOfTimers;

    /**
     * Array containing all the fragments that represent the individual timers
     */
    private ArrayList<Fragment> timerFragments;

    /**
     * Private constructor so it cant be instantiated outside
     * Initializes the arrays that will hold the necessary timers
     */
    private TimersWrapper() {
        individualTimerList = new ArrayList<>();
        groupsOfTimers = new ArrayList<>();
        timerFragments = new ArrayList<>();
    }

    /**
     * Static method to create and get an instance of this singleton
     */
    public static TimersWrapper getInstance() {
        if (instance == null)
            instance = new TimersWrapper();

        return instance;
    }

    /**
     * Static method to transform time from milliseconds to another format depending on the given mode
     *
     * @param millis Time in milliseconds to be reformatted
     * @param mode   String that dictates the mode returned
     * @return the time formatted
     */
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
            Log.d("M ", String.valueOf(minutes));
            Log.d("H ", String.valueOf(hours));
            Log.d("D ", String.valueOf(days));
            timerInHoursMinutesSeconds.put("Days", days);
            timerInHoursMinutesSeconds.put("Hours", hours);
            timerInHoursMinutesSeconds.put("Minutes", minutes);
        }
        return timerInHoursMinutesSeconds;
    }

    /**
     * Static method to transform the time in a given format to milliseconds
     *
     * @param mode   String that dictates the format of the given time
     * @param small  TextView that holds the time in its smallest value, either seconds or minutes
     * @param medium TextView that holds the time in its medium value, either minutes or hours
     * @param big    TextView that holds the time in its smallest value, either hours or days
     * @return the time in milliseconds
     */
    public static long getTimerValue(String mode, TextView small, TextView medium, TextView big) {
        long seconds = 0;
        long minutes = 0;
        long hours = 0;

        if (!small.getText().toString().equals("")) {
            seconds = Long.parseLong(small.getText().toString());
        }
        if (!medium.getText().toString().equals("")) {
            minutes = Long.parseLong(medium.getText().toString());
        }
        if (!big.getText().toString().equals("")) {
            hours = Long.parseLong(big.getText().toString());
        }
        long value = 0;
        if (mode.equals("HH/MM/SS")) value = (hours * (60 * 60) + minutes * 60 + seconds) * 1000;
        else value = ((hours * (60 * 60 * 24)) + minutes * (60 * 60) + seconds * 60) * 1000;
        return value;
    }

    /**
     * Static method to update the time in given TextViews
     *
     * @param time   current time of the timer
     * @param mode   String that dictates the format of the given time
     * @param small  TextView that holds the time in its smallest value, either seconds or minutes
     * @param medium TextView that holds the time in its medium value, either minutes or hours
     * @param big    TextView that holds the time in its smallest value, either hours or days
     */
    public static void updateViews(long time, String mode, TextView small, TextView medium, TextView big) {
        HashMap<String, Object> timeLeft = millisToCommonTime(time, mode);

        if (mode.equals("HH/MM/SS")) {
            big.setText(String.valueOf(timeLeft.get("Hours")));
            medium.setText(String.valueOf(timeLeft.get("Minutes")));
            small.setText(String.valueOf(timeLeft.get("Seconds")));

        } else {
            big.setText(String.valueOf(timeLeft.get("Days")));
            medium.setText(String.valueOf(timeLeft.get("Hours")));
            small.setText(String.valueOf(timeLeft.get("Minutes")));

        }
    }


    /**
     * SETTERS and GETTERS for the arrays of individual timers and groups of timers
     */

    public ArrayList<Timer> getIndividualTimerList() {
        return individualTimerList;
    }

    public void setIndividualTimerList(ArrayList<Timer> individualTimerList) {
        this.individualTimerList = individualTimerList;
    }

    public ArrayList<TimerGroup> getGroupsOfTimers() {
        return groupsOfTimers;
    }

    public void setGroupsOfTimers(ArrayList<TimerGroup> groupsOfTimers) {
        this.groupsOfTimers = groupsOfTimers;
    }

    public ArrayList<Fragment> getTimerFragments() {
        return timerFragments;
    }

    public void setTimerFragments(ArrayList<Fragment> timerFragments) {
        this.timerFragments = timerFragments;
    }

    /**
     * Methods to add objects to their respective arrays
     */

    /**
     * Adds individual timers to its specific array
     *
     * @param individualTimer Timer object to be added to the array
     */
    public void addIndividualTimerToList(Timer individualTimer) {
        individualTimerList.add(individualTimer);
    }

    /**
     * Adds group of timers to its specific array
     *
     * @param group TimerGroup object to be added to the array
     */
    public void addGroupOfTimers(TimerGroup group) {
        groupsOfTimers.add(group);
    }

    /**
     * Adds group of timers to its specific array
     *
     * @param fragment TimerGroup object to be added to the array
     */
    public void addTimerFragment(Fragment fragment) {
        timerFragments.add(fragment);
    }

    /**
     * Gets a specific individual timer from the array given an index
     *
     * @param index Index of the timer wanted
     */
    public Timer getSpecificIndividualTimerByIndex(int index) {
        return individualTimerList.get(index);
    }

    /**
     * Gets the index of the wanted individual timer from the array
     *
     * @param timer Index of timer wanted
     */
    public int getIndexOfIndividualTimer(Timer timer) {
        return individualTimerList.indexOf(timer);
    }

    /**
     * Gets a specific group of timers from the array given an index
     *
     * @param index Index of the timer wanted
     */
    public TimerGroup getSpecificGroupOfTimersByIndex(int index) {
        return groupsOfTimers.get(index);
    }

    /**
     * Gets the index of the wanted group of timers from the array
     *
     * @param group Index of group wanted
     */
    public int getIndexOfGroupOfTimers(TimerGroup group) {
        return groupsOfTimers.indexOf(group);
    }

    /**
     * Gets a timer fragment from the array given an index
     *
     * @param index Index of the timer wanted
     */
    public Fragment getTimerFragmentByIndex(int index) {
        return timerFragments.get(index);
    }

    /**
     * Gets the index of the wanted timer fragment from the array
     *
     * @param fragment Index of timer wanted
     */
    public int getIndexOfTimerFragment(Fragment fragment) {
        return timerFragments.indexOf(fragment);
    }

}
