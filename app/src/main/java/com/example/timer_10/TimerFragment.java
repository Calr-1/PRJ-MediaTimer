package com.example.timer_10;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class TimerFragment extends Fragment implements Timer{

    private ImageButton playPauseButton;
    private TextView hoursValue, minutesValue, secondsValue;

    private TimerClass timerClass;
    private boolean timerRunning, timerCreated;
    private long timerInitialValue;
    private int timerCountdownInterval;
    private TimersWrapper wrapper;

    private final int typeId;
    private TimerGroupClass timerGroupClass;

    private static final int configure = 1;

    private LinearLayout parent;

    public TimerFragment(int typeId) {
        this.typeId = typeId;
    }

    public TimerFragment(int typeId, TimerGroupClass timerGroupClass) {
        this.typeId = typeId;
        this.timerGroupClass = timerGroupClass;
    }

    public TimerFragment(int typeId, TimerGroupClass timerGroupClass, TimerClass timerClass) {
        this.typeId = typeId;
        this.timerGroupClass = timerGroupClass;
        this.timerClass = timerClass;
    }

    public TimerFragment(int typeId, TimerClass timerClass) {
        this.typeId = typeId;
        this.timerClass = timerClass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wrapper = TimersWrapper.getInstance();

        hoursValue = getView().findViewById(R.id.hoursEditView);
        minutesValue = getView().findViewById(R.id.minutesEditView);
        secondsValue = getView().findViewById(R.id.secondsEditView);

        if (typeId == 1) {
            timerClass = new TimerClass(0, 0, "Timer " + (wrapper.getIndividualTimerList().size() + 1), getActivity(), R.raw.sound, secondsValue, minutesValue, hoursValue, this);
            wrapper.addIndividualTimerToList(timerClass);
        } else if (typeId == 2) {
            timerClass = new TimerClass(0, 0, "Timer " + (timerGroupClass.getNumberOfTimers() + 1), getActivity(), R.raw.sound, secondsValue, minutesValue, hoursValue, this);
            timerGroupClass.addTimer(timerClass);
        }

        timerClass.setFragment(this);

        playPauseButton = getView().findViewById(R.id.play_and_pause_group_button);
        //ImageButton stopTimerButton = getView().findViewById(R.id.play_and_pause_group_button);


        TextView tv = getView().findViewById(R.id.timerFragName);
        tv.setText(timerClass.getTimerName());

        timerRunning = false;
        timerCreated = false;
        timerInitialValue = 0;
        timerCountdownInterval = 100;


        if (typeId == 3 || typeId == 4) {
            timerRunning = timerClass.isTimerRunning();
            timerInitialValue = timerClass.getTimerInitialValue();
            timerCountdownInterval = timerClass.getTimerCountdownInterval();
            timerCreated = timerClass.isTimerCreated();
        }

        playPauseButton.setOnClickListener(v -> {
            if (!timerCreated) {
                timerInitialValue = TimersWrapper.getTimerValue(timerClass.getMode(), secondsValue, minutesValue, hoursValue);
                if (timerInitialValue != 0) {
                    startStopTimer();
                    timerClass.setTimerInitialValue(timerInitialValue);
                    Log.d("TIMER initial value: ", String.valueOf(timerInitialValue));
                    timerClass.setTimerCountdownInterval(timerCountdownInterval);
                    timerClass.createTimer(timerInitialValue, timerCountdownInterval);
                    timerClass.startTimer();
                    playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    Toast.makeText(getActivity(), "Timer started!", Toast.LENGTH_SHORT).show();
                    Log.e("CREATE TIMER"," YES");

                } else {
                    Toast.makeText(getActivity(), "Timer can't start!", Toast.LENGTH_SHORT).show();

                }

            } else {
                if (timerClass.canStopSound()) {
                    Log.e("STOP TIMER"," YES");
                    startStopTimer();
                    stopAlarm();
                    Toast.makeText(getActivity(), "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                    playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                } else {
                    Log.e("PAUSE TIMER"," YES");
                    pauseUnpauseTimer();}
            }
            Log.e("BUTTON PRESSED",timerClass.getTimerName());
        });


        ConstraintLayout layout = getView().findViewById(R.id.frameLayout);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TimerActivity.class);

            if (typeId == 2 || typeId == 3) {
                intent.putExtra("typeID", 2);
                intent.putExtra("indexGroup", wrapper.getIndexOfGroupOfTimers(timerGroupClass));
                intent.putExtra("indexTimer", timerGroupClass.getIndexOfTimer(timerClass));

            } else if (typeId == 1 || typeId == 4) {
                intent.putExtra("typeID", typeId);
                intent.putExtra("indexTimer", wrapper.getIndexOfIndividualTimer(timerClass));
            }
            startActivityForResult(intent, configure);
        });
        TimersWrapper.updateViews(timerClass.getCurrentTimerValue(), timerClass.getMode(), secondsValue, minutesValue, hoursValue);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pauseUnpauseTimer() {
        if (timerRunning) {
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
        }

        timerClass.pauseUnpauseTimer(timerRunning);
        timerRunning = !timerRunning;
        timerClass.setTimerRunning(timerRunning);

    }

    public void startStopTimer() {
        hoursValue.setEnabled(timerCreated);
        minutesValue.setEnabled(timerCreated);
        secondsValue.setEnabled(timerCreated);
        timerRunning = !timerCreated;
        timerCreated = !timerCreated;
        timerClass.setTimerCreated(timerCreated);
        timerClass.setTimerRunning(timerRunning);

    }


    private void stopAlarm() {
        timerClass.stopSound();
    }

    public TimerGroupClass getTimerGroup() {
        return timerGroupClass;
    }

    public int getTypeId() {
        return typeId;
    }

    public void start() {
        playPauseButton.performClick();
    }

    public ConstraintLayout getLayout(){
        ConstraintLayout layout = getView().findViewById(R.id.frameLayout);
        return layout;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == configure) {
            if (resultCode == RESULT_OK) {
                timerClass.setViews(secondsValue, minutesValue, hoursValue);
                TimersWrapper.updateViews(timerClass.getCurrentTimerValue(), timerClass.getMode(), secondsValue, minutesValue, hoursValue);
                TextView tv = getView().findViewById(R.id.timerFragName);
                tv.setText(timerClass.getTimerName());
            }
        }
    }

    @Override
    public int getType() {
        return 1;
    }

    public TimerClass getTimerClass() {
        return timerClass;
    }
}