package com.example.timer_10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class NotificationActivity extends AppCompatActivity {

    private TimersWrapper wrapper;
    private Timer timer;

    private EditText hoursView, minutesView, secondsView, numberOfIntervals;
    private Spinner format;

    private long time;
    private String typeOfInterval;
    private int indexOfTimer;
    private int nIntervals;
    private Notifications noti;

    private EditText random;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        indexOfTimer = getIntent().getIntExtra("timerIndex", -1);
        wrapper = TimersWrapper.getInstance();
        timer = wrapper.getSpecificIndividualTimerByIndex(indexOfTimer);

        format = findViewById(R.id.typeOfIntervalSpinner);
        hoursView = findViewById(R.id.hoursEditInterval);
        minutesView = findViewById(R.id.minutesEditInterval);
        secondsView = findViewById(R.id.secondsEditInterval);
        numberOfIntervals = findViewById(R.id.inputNumberIntervals);
        random = findViewById(R.id.inputNumberIntervalsRandom);
        random.setText(timer.getRandomIntervals()+"");

        time = timer.getNotifications().getIntervalTime();
        typeOfInterval = timer.getNotifications().getTypeOfInterval();

        wrapper.updateViews(time,"HH/MM/SS",secondsView, minutesView, hoursView);
        nIntervals = timer.getNotifications().getNumberOfIntervals();
        numberOfIntervals.setText(nIntervals + "");

        noti = new Notifications();

        format.setSelection(getIndex(format, typeOfInterval));

        format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                typeOfInterval = selectedItem;
                noti.setTypeOfInterval(typeOfInterval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public void onBackPressed() {
        save();
        Intent intent = new Intent( this.getApplicationContext(), TimerActivity.class);
        intent.putExtra("timerIndex", indexOfTimer);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void save(){
        time = wrapper.getTimerValue("HH/MM/SS", secondsView, minutesView, hoursView);
        noti.setIntervalTime(time);
        noti.setNumberOfIntervals(Integer.valueOf(numberOfIntervals.getText().toString()));
        timer.setNotifications(noti);
        timer.setRandomIntervals(Integer.valueOf(random.getText().toString()));
    }
}