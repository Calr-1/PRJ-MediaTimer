package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    private TimersWrapper wrapper;
    private TimerClass timer;

    private EditText hoursView, minutesView, secondsView, numberOfIntervals;

    private long time;
    private String typeOfInterval;
    private int indexOfTimer;
    private NotificationsClass noti;

    private EditText randomMax, randomMin;
    private CheckBox vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_notification);

        indexOfTimer = getIntent().getIntExtra("timerIndex", -1);
        wrapper = TimersWrapper.getInstance();
        timer = wrapper.getSpecificIndividualTimerByIndex(indexOfTimer);

        Spinner format = findViewById(R.id.typeOfIntervalSpinner);
        hoursView = findViewById(R.id.hoursEditInterval);
        minutesView = findViewById(R.id.minutesEditInterval);
        secondsView = findViewById(R.id.secondsEditInterval);
        numberOfIntervals = findViewById(R.id.inputNumberIntervals);
        randomMax = findViewById(R.id.inputNumberIntervalsMaxRandom);
        randomMin = findViewById(R.id.inputNumberIntervalsMinRandom);
        vibration = findViewById(R.id.enableVibrationsCB);

        randomMax.setText(timer.getRandomIntervalsMax() + "");
        randomMin.setText(timer.getRandomIntervalsMin() + "");
        vibration.setChecked(timer.isVibration());

        time = timer.getNotifications().getIntervalTime();
        typeOfInterval = timer.getNotifications().getTypeOfInterval();

        TimersWrapper.updateViews(time, "HH/MM/SS", secondsView, minutesView, hoursView);
        int nIntervals = timer.getNotifications().getNumberOfIntervals();
        numberOfIntervals.setText(nIntervals + "");

        noti = new NotificationsClass();

        format.setSelection(getIndex(format, typeOfInterval));

        format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfInterval = parent.getItemAtPosition(position).toString();
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
        Intent intent = new Intent(this.getApplicationContext(), TimerActivity.class);
        intent.putExtra("timerIndex", indexOfTimer);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void save() {
        time = TimersWrapper.getTimerValue("HH/MM/SS", secondsView, minutesView, hoursView);
        noti.setIntervalTime(time);
        noti.setNumberOfIntervals(Integer.parseInt(numberOfIntervals.getText().toString()));
        timer.setNotifications(noti);
        timer.setRandomIntervalsMax(Integer.parseInt(randomMax.getText().toString()));
        timer.setRandomIntervalsMin(Integer.parseInt(randomMin.getText().toString()));
        timer.setVibration(vibration.isChecked());
    }
}