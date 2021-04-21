package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class timerFragment extends Fragment {

    private ImageButton playPauseButton, stopTimerButton, optionsButton;
    private EditText hoursValue, minutesValue, secondsValue;
    private Timer timer;
    private boolean timerRunning, timerCreated;
    private long timerInitialValue;
    private int timerCountdownInterval;

    private boolean intervals;
    private int numberOfIntervals;
    private String mode;
    private Spinner sp;
    private EditText et;
    private CheckBox cb;

    private static final int configure = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.timer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hoursValue = getView().findViewById(R.id.hoursEditView);
        minutesValue = getView().findViewById(R.id.minutesEditView);
        secondsValue = getView().findViewById(R.id.secondsEditView);

        playPauseButton = getView().findViewById(R.id.play_and_pause_button);
        stopTimerButton = getView().findViewById(R.id.stop_playing_button);
        optionsButton = getView().findViewById(R.id.timer_options_button);


        timerRunning = false;
        timerCreated = false;
        timerInitialValue = 0;
        timerCountdownInterval = 100;

        sp = getView().findViewById(R.id.modes_spinner);
        et = getView().findViewById(R.id.inputIntervals);
        cb = getView().findViewById(R.id.notificationsCheckBox);
        intervals = false;
        numberOfIntervals = 1;
        mode = "HH/MM/SS";

        playPauseButton.setOnClickListener(v -> {
            if (!timerCreated) {
                startStopTimer();
                timerInitialValue = getTimerValue();
                timer = new Timer(timerInitialValue, timerCountdownInterval, "Default name", getActivity(), R.raw.sound, this);
                timer.setIntervals(intervals);
                timer.setMode(mode);
                timer.setNumberOfIntervals(numberOfIntervals);
                timer.startTimer();
                Toast.makeText(getActivity(), "Timer started!", Toast.LENGTH_SHORT).show();

            } else {
                pauseUnpauseTimer();
            }
        });

        stopTimerButton.setOnClickListener(v -> {
            if (timer.canStopSound()) {
                stopAlarm();
                Toast.makeText(getActivity(), "Alarm Stopped!", Toast.LENGTH_SHORT).show();
            }
        });
        optionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConfigureActivity.class);
            intent.putExtra("intervals", intervals);
            intent.putExtra("numberOfIntervals", numberOfIntervals);
            intent.putExtra("mode", mode);
            startActivityForResult(intent, configure);

        });

    }

    private void pauseUnpauseTimer() {
        if (timerRunning) {
            playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        timer.pauseUnpauseTimer(timerRunning);
        timerRunning = !timerRunning;


    }

    private long getTimerValue() {
        int seconds = 0;
        int minutes = 0;
        int hours = 0;

        if (!secondsValue.getText().toString().equals("")) {
            seconds = Integer.parseInt(secondsValue.getText().toString());
        }
        if (!minutesValue.getText().toString().equals("")) {
            minutes = Integer.parseInt(minutesValue.getText().toString());
        }
        if (!hoursValue.getText().toString().equals("")) {
            hours = Integer.parseInt(hoursValue.getText().toString());
        }
        if (mode.equals("HH/MM/SS")) return (hours * (60 * 60) + minutes * 60 + seconds) * 1000;
        else return ((hours * (60 * 60 * 24)) + minutes * (60 * 60) + seconds * 60) * 1000;


    }

    public void updateTimer(long timerLeft) {
        HashMap<String, Object> timeLeft = timer.millisToCommonTime(timerLeft);

        if (mode.equals("HH/MM/SS")) {
            secondsValue.setText(String.valueOf(timeLeft.get("Seconds")));
            minutesValue.setText(String.valueOf(timeLeft.get("Minutes")));
            hoursValue.setText(String.valueOf(timeLeft.get("Hours")));
        } else {
            secondsValue.setText(String.valueOf(timeLeft.get("Minutes")));
            minutesValue.setText(String.valueOf(timeLeft.get("Hours")));
            hoursValue.setText(String.valueOf(timeLeft.get("Days")));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == configure) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                intervals = data.getBooleanExtra("intervals", false);
                numberOfIntervals = data.getIntExtra("numberOfIntervals", 1);
                mode = data.getStringExtra("mode");
                if (timer != null) {
                    timer.setMode(mode);
                }
            }
        }
    }

    public void startStopTimer() {
        hoursValue.setEnabled(timerCreated);
        minutesValue.setEnabled(timerCreated);
        secondsValue.setEnabled(timerCreated);
        timerRunning = !timerCreated;
        timerCreated = !timerCreated;
    }

    private void stopAlarm() {
        timer.stopSound();
    }

}