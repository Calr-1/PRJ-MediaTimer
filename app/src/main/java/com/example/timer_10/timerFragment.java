package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class timerFragment extends Fragment {

    private ImageButton playPauseButton, stopTimerButton, optionsButton;
    private TextView hoursValue, minutesValue, secondsValue;
    private Timer timer;
    private ConstraintLayout layout;
    private boolean timerRunning, timerCreated;
    private long timerInitialValue;
    private int timerCountdownInterval;
    private TimersWrapper wrapper;

    private Spinner sp;
    private EditText et;
    private CheckBox cb;

    private int typeId;
    private TimerGroup timerGroup;

    private static final int configure = 1;

    public timerFragment(int typeId) {
        this.typeId = typeId;
    }

    public timerFragment(int typeId, TimerGroup timerGroup) {
        this.typeId = typeId;
        this.timerGroup = timerGroup;
    }

    public timerFragment(int typeId, TimerGroup timerGroup, Timer timer) {
        this.typeId = typeId;
        this.timerGroup = timerGroup;
        this.timer = timer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.timer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wrapper = TimersWrapper.getInstance();

        hoursValue = getView().findViewById(R.id.hoursEditView);
        minutesValue = getView().findViewById(R.id.minutesEditView);
        secondsValue = getView().findViewById(R.id.secondsEditView);
        if (typeId == 1) {
            timer = new Timer(0, 0, "Timer " + (wrapper.getIndividualTimerList().size() + 1), getActivity(), R.raw.sound, secondsValue, minutesValue, hoursValue, this);
            wrapper.addIndividualTimerToList(timer);
        } else if (typeId == 2) {
            timer = new Timer(0, 0, "Timer " + (timerGroup.getNumberOfTimers() + 1), getActivity(), R.raw.sound, secondsValue, minutesValue, hoursValue, this);
            timerGroup.addTimer(timer);
        }


        playPauseButton = getView().findViewById(R.id.play_and_pause_group_button);
        stopTimerButton = getView().findViewById(R.id.stop_playing_group_button);
        optionsButton = getView().findViewById(R.id.timer_options_group_button);

        TextView tv = getView().findViewById(R.id.timerFragName);
        tv.setText(timer.getTimerName());

        timerRunning = false;
        timerCreated = false;
        timerInitialValue = 0;
        timerCountdownInterval = 100;

        sp = getView().findViewById(R.id.modes_spinner);
        et = getView().findViewById(R.id.inputIntervals);
        cb = getView().findViewById(R.id.notificationsCheckBox);
        if (typeId == 3) {
            timerRunning = timer.isTimerRunning();
            timerInitialValue = timer.getTimerInitialValue();
            timerCountdownInterval = timer.getTimerCountdownInterval();
            timerCreated = timer.isTimerCreated();
        }

        playPauseButton.setOnClickListener(v -> {
            if (!timerCreated) {
                timerInitialValue = wrapper.getTimerValue(timer.getMode(), secondsValue, minutesValue, hoursValue);
                if (timerInitialValue != 0) {
                    startStopTimer();
                    timer.setTimerInitialValue(timerInitialValue);
                    Log.d("TIMER initial value: ", String.valueOf(timerInitialValue));
                    timer.setTimerCountdownInterval(timerCountdownInterval);
                    timer.createTimer(timerInitialValue, timerCountdownInterval);
                    timer.startTimer();
                    Toast.makeText(getActivity(), "Timer started!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Timer can't start!", Toast.LENGTH_SHORT).show();

                }

            } else {
                pauseUnpauseTimer();
            }
        });

        stopTimerButton.setOnClickListener(v -> {
            if (timer.canStopSound()) {
                stopAlarm();
                Toast.makeText(getActivity(), "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);

            }
        });
        layout = getView().findViewById(R.id.frameLayout);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TimerActivity.class);

            if (typeId == 2 || typeId == 3) {
                intent.putExtra("typeID", 2);
                intent.putExtra("indexGroup", wrapper.getIndexOfGroupOfTimers(timerGroup));
                intent.putExtra("indexTimer", timerGroup.getIndexOfTimer(timer));

            } else if (typeId == 1) {
                intent.putExtra("typeID", 1);
                intent.putExtra("indexTimer", wrapper.getIndexOfIndividualTimer(timer));
            }
            startActivityForResult(intent, configure);
        });
        timer.setViews(secondsValue, minutesValue, hoursValue);
        TimersWrapper.updateViews(timer.getCurrentTimerValue(), timer.getMode(), secondsValue, minutesValue, hoursValue);
    }

    private void pauseUnpauseTimer() {
        if (timerRunning) {
            playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        timer.pauseUnpauseTimer(timerRunning);
        timerRunning = !timerRunning;
        timer.setTimerRunning(timerRunning);


    }

    public void startStopTimer() {
        hoursValue.setEnabled(timerCreated);
        minutesValue.setEnabled(timerCreated);
        secondsValue.setEnabled(timerCreated);
        timerRunning = !timerCreated;
        timerCreated = !timerCreated;
        timer.setTimerCreated(timerCreated);
        timer.setTimerRunning(timerRunning);

    }


    private void stopAlarm() {
        timer.stopSound();
    }

    public TimerGroup getTimerGroup() {
        return timerGroup;
    }

    public int getTypeId() {
        return typeId;
    }

    public void start() {
        playPauseButton.performClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == configure) {
            if (resultCode == RESULT_OK) {
                timer.setViews(secondsValue, minutesValue, hoursValue);
                TimersWrapper.updateViews(timer.getCurrentTimerValue(), timer.getMode(), secondsValue, minutesValue, hoursValue);
                TextView tv = getView().findViewById(R.id.timerFragName);
                tv.setText(timer.getTimerName());
            }
        }
    }
}