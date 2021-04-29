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

    private static final int configure = 1;


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
        timer = new Timer(0, 0, "Default", getActivity(), R.raw.sound, secondsValue, minutesValue, hoursValue, this);
        wrapper.addIndividualTimerToList(timer);



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

        playPauseButton.setOnClickListener(v -> {
            if (!timerCreated) {
                startStopTimer();
                timerInitialValue = wrapper.getTimerValue(timer.getMode(), secondsValue, minutesValue, hoursValue);
                timer.setTimerInitialValue(timerInitialValue);
                Log.d("TIMER initial value: ", String.valueOf(timerInitialValue));
                timer.setTimerCountdownInterval(timerCountdownInterval);
                timer.createTimer(timerInitialValue, timerCountdownInterval);
                timer.startTimer();
                Toast.makeText(getActivity(), "Timer started!", Toast.LENGTH_SHORT).show();
                playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);

            } else {
                pauseUnpauseTimer();
            }
        });

        optionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConfigureActivity.class);
            intent.putExtra("timerIndex", wrapper.getIndexOfIndividualTimer(timer));
            startActivity(intent);

        });

        layout = getView().findViewById(R.id.frameLayout);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TimerActivity.class);
            intent.putExtra("timerIndex", wrapper.getIndexOfIndividualTimer(timer));
            startActivityForResult(intent, configure);
        });

    }

    private void pauseUnpauseTimer() {
        if (timerRunning) {
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
        }

        timer.pauseUnpauseTimer(timerRunning);
        timerRunning = !timerRunning;


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == configure) {
            if (resultCode == RESULT_OK) {
                timer.setViews(secondsValue, minutesValue, hoursValue);
                wrapper.updateViews(timer.getCurrentTimerValue(), timer.getMode(), secondsValue, minutesValue, hoursValue);
            }
        }
    }
}