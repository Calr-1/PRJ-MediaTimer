package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class NotificationActivity extends AppCompatActivity {

    private TimersWrapper wrapper;
    private TimerClass timer;

    private EditText hoursView, minutesView, secondsView, numberOfIntervals;

    private long time;
    private String typeOfInterval;
    private int indexOfTimer;
    private NotificationsClass noti;

    private EditText random;

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
        random = findViewById(R.id.inputNumberIntervalsRandom);
        random.setText(timer.getRandomIntervals() + "");

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

        TextView tv = findViewById(R.id.chooseSoundText);
        tv.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            this.startActivityForResult(intent, 5);
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
        timer.setRandomIntervals(Integer.parseInt(random.getText().toString()));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(" RESULT CODE ", String.valueOf(resultCode));
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK && requestCode == 5) {
                Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(uri!=null) {
                    try {
                        timer.setRingtone(uri,this.getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}