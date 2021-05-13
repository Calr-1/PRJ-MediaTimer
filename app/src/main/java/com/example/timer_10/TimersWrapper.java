package com.example.timer_10;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton class that contains all the timers created,
 * Serializable to facilitate saving user data
 */
public class TimersWrapper implements Serializable {

    /**
     * Single instance of TimersWrapper
     */
    private static TimersWrapper instance = null;

    /**
     * Reference for firebase storage
     */
    private StorageReference storageReference;

    /**
     * Array containing the individual timers
     */
    private ArrayList<TimerClass> individualTimerClassList;

    /**
     * Array containing the groups of timers
     */
    private ArrayList<TimerGroupClass> groupsOfTimers;
    /**
     * Array containing the groups of timers fragments
     */
    private ArrayList<Fragment> groupsOfTimersFragment;

    /**
     * Array containing all the fragments that represent the individual timers
     */
    private ArrayList<Fragment> timerFragments;

    /**
     * Private constructor so it cant be instantiated outside
     * Initializes the arrays that will hold the necessary timers
     */
    private TimersWrapper() {
        individualTimerClassList = new ArrayList<>();
        groupsOfTimers = new ArrayList<>();
        timerFragments = new ArrayList<>();
        groupsOfTimersFragment = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();

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
            int days = (int) (millis / (1000 * 60 * 60 * 24));
            timerInHoursMinutesSeconds.put("Hours", (hours + (days * 24)));
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

    /**
     * Static method to transform the time in a given format to milliseconds
     *
     * @param mode   String that dictates the format of the given time
     * @param small  TextView that holds the time in its smallest value, either seconds or minutes
     * @param medium TextView that holds the time in its medium value, either minutes or hours
     * @param big    TextView that holds the time in its biggest value, either hours or days
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
     * @param big    TextView that holds the time in its biggest value, either hours or days
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

    public ArrayList<TimerClass> getIndividualTimerList() {
        return individualTimerClassList;
    }

    public void setIndividualTimerList(ArrayList<TimerClass> individualTimerClassList) {
        this.individualTimerClassList = individualTimerClassList;
    }

    public ArrayList<TimerGroupClass> getGroupsOfTimers() {
        return groupsOfTimers;
    }

    public void setGroupsOfTimers(ArrayList<TimerGroupClass> groupsOfTimers) {
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
     * @param individualTimerClass TimerClass object to be added to the array
     */
    public void addIndividualTimerToList(TimerClass individualTimerClass) {
        individualTimerClassList.add(individualTimerClass);
    }

    /**
     * Adds GroupActivity of timers to its specific array
     *
     * @param group TimerGroupClass object to be added to the array
     */
    public void addGroupOfTimers(TimerGroupClass group) {
        groupsOfTimers.add(group);
    }

    /**
     * Adds fragment to its specific array
     *
     * @param fragment timer fragment object to be added to the array
     */
    public void addTimerFragment(Fragment fragment) {
        timerFragments.add(fragment);
    }

    /**
     * Adds a fragment to its specific array
     *
     * @param fragment GroupActivity of timers fragment object to be added to the array
     */
    public void addTimerGroupFragment(Fragment fragment) {
        groupsOfTimersFragment.add(fragment);
    }

    /**
     * Gets a specific individual timer from the array given an index
     *
     * @param index Index of the wanted timer
     */
    public TimerClass getSpecificIndividualTimerByIndex(int index) {
        return individualTimerClassList.get(index);
    }

    /**
     * Gets the index of the given individual timerClass from the array
     *
     * @param timerClass Index of wanted timerClass
     */
    public int getIndexOfIndividualTimer(TimerClass timerClass) {
        return individualTimerClassList.indexOf(timerClass);
    }

    /**
     * Gets a specific GroupActivity of timers from the array given an index
     *
     * @param index Index of the wanted timer
     */
    public TimerGroupClass getSpecificGroupOfTimersByIndex(int index) {
        return groupsOfTimers.get(index);
    }

    /**
     * Gets the index of the given GroupActivity of timers from the array
     *
     * @param group Index of wanted GroupActivity
     */
    public int getIndexOfGroupOfTimers(TimerGroupClass group) {
        return groupsOfTimers.indexOf(group);
    }

    /**
     * Gets a timer fragment from the array given an index
     *
     * @param index Index of the wanted timer
     */
    public Fragment getTimerFragmentByIndex(int index) {
        return timerFragments.get(index);
    }

    /**
     * Gets the index of the given timer fragment from the array
     *
     * @param fragment Index of wanted timer
     */
    public int getIndexOfTimerFragment(Fragment fragment) {
        return timerFragments.indexOf(fragment);
    }

    public void getAllRingtones() {


    }

}
