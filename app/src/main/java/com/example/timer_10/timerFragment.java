package com.example.timer_10;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class timerFragment extends Fragment {

    private static final int IMAGE_REQUEST = 2;
    private Button startTimerButton, uploadButton, uploadTimerButton, pauseUnpauseButton, stopAlarmButton;
    private EditText hoursValue, minutesValue, secondsValue;
    private Timer timer;

    private boolean timerRunning, timerCreated;
    private long timerInitialValue, timerValue;
    private int timerCountdownInterval;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.timer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startTimerButton = getView().findViewById(R.id.startTimerButton);
        pauseUnpauseButton = getView().findViewById(R.id.pauseUnpauseButton);
        hoursValue = getView().findViewById(R.id.hoursEditView);
        minutesValue = getView().findViewById(R.id.minutesEditView);
        secondsValue = getView().findViewById(R.id.secondsEditView);
        uploadTimerButton = getView().findViewById(R.id.uploadTimerButton);
        stopAlarmButton = getView().findViewById(R.id.stopAlarmButton);

        timerRunning = false;
        timerCreated = false;
        timerInitialValue = 0;
        timerValue = 0;
        timerCountdownInterval = 100;
        startTimerButton.setOnClickListener(v -> {
            if(!timerCreated){
                startStopTimer();
                timerInitialValue=getTimerValue();
                timer = new Timer(timerInitialValue, timerCountdownInterval, "Default name", (MainActivity)getActivity(), R.raw.sound, this);
                timer.startTimer();
                Toast.makeText((MainActivity)getActivity(), "Timer started!", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText((MainActivity)getActivity(), "Timer already running...", Toast.LENGTH_LONG).show();
            }
        });

        stopAlarmButton.setOnClickListener(v -> {
            if(!timerCreated){
                stopAlarm();
                Toast.makeText((MainActivity)getActivity(), "Alarm Stopped!", Toast.LENGTH_LONG).show();
            }
        });

        pauseUnpauseButton.setOnClickListener(v -> {
            pauseUnpauseTimer();
        });


    }

    private void pauseUnpauseTimer() {
        if(timerRunning){
            pauseUnpauseButton.setText(R.string.unpauseTimer_string);
        }
        else{
            pauseUnpauseButton.setText(R.string.pauseTimer_string);
        }

        timer.pauseUnpauseTimer(timerRunning);
        timerRunning = !timerRunning;



    }

    private long getTimerValue(){
        int seconds=0;
        int minutes=0;
        int hours=0;

        if(!secondsValue.getText().toString().equals("")){
            seconds =  Integer.parseInt(secondsValue.getText().toString());
        }
        if(!minutesValue.getText().toString().equals("")){
            minutes =  Integer.parseInt(minutesValue.getText().toString());
        }
        if(!hoursValue.getText().toString().equals("")){
            hours =  Integer.parseInt(hoursValue.getText().toString());
        }
        return (hours * (60*60) + minutes * 60 + seconds) * 1000;


    }
    public void updateTimer(long timerLeft){
        timerValue=timerLeft;
        HashMap<String, Object> timeLeft = timer.millisToCommonTime(timerLeft);

        secondsValue.setText(String.valueOf(timeLeft.get("Seconds")));
        minutesValue.setText(String.valueOf(timeLeft.get("Minutes")));
        hoursValue.setText(String.valueOf(timeLeft.get("Hours")));

    }

    public void startStopTimer(){
        if (timerCreated) {
            startTimerButton.setVisibility(View.VISIBLE);
            pauseUnpauseButton.setVisibility(View.INVISIBLE);
            stopAlarmButton.setVisibility(View.VISIBLE);

        }
        else{
            startTimerButton.setVisibility(View.INVISIBLE);
            pauseUnpauseButton.setVisibility(View.VISIBLE);

        }

        hoursValue.setEnabled(timerCreated);
        minutesValue.setEnabled(timerCreated);
        secondsValue.setEnabled(timerCreated);
        timerRunning=!timerCreated;
        timerCreated=!timerCreated;
    }

    private void stopAlarm(){
        timer.stopSound();
        stopAlarmButton.setVisibility(View.INVISIBLE);


    }

}