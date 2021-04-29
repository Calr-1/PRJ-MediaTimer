package com.example.timer_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.HashMap;

public class TimerActivity extends AppCompatActivity {

    private boolean intervals, timerRunning;
    private int numberOfIntervals;
    private Timer timer;
    private long time;
    private String mode, name;
    private CheckBox cb;
    private EditText et;
    private Spinner sp;
    private Intent intent;
    public static EditText hoursView;
    public static EditText minutesView;
    public static EditText secondsView;
    public EditText nameView;
    private TextView sp1, sp2, sp3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        sp = findViewById(R.id.modes_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.modes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("HH/MM/SS")) {
                    sp1.setText("H");
                    sp2.setText("M");
                    sp3.setText("S");
                } else {
                    sp1.setText("D");
                    sp2.setText("H");
                    sp3.setText("M");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et = findViewById(R.id.inputIntervals);
        cb = findViewById(R.id.notificationsCheckBox);
        hoursView = findViewById(R.id.hoursEditView);
        minutesView = findViewById(R.id.minutesEditView);
        secondsView = findViewById(R.id.secondsEditView);
        nameView = findViewById(R.id.timerName);
        sp1 = findViewById(R.id.hoursIndicator);
        sp2 = findViewById(R.id.minutesIndicator);
        sp3 = findViewById(R.id.minutesIndicator);


        intervals = getIntent().getBooleanExtra("intervals", false);
        numberOfIntervals = getIntent().getIntExtra("numberOfIntervals", 1);
        mode = getIntent().getStringExtra("mode");
        name = getIntent().getStringExtra("name");
        time = getIntent().getLongExtra("time", 0);
        timerRunning = getIntent().getBooleanExtra("paused", false);
        if (!timerRunning) {
            updateViews(time, mode);
        }
        sp.setSelection(getIndex(sp, mode));
        cb.setChecked(intervals);
        et.setText("" + numberOfIntervals);
        nameView.setText(name);
        editable(time);

        intent = new Intent(this.getApplicationContext(), timerFragment.class);
    }

    private void editable(long time) {
        if (time != 0) {
            hoursView.setEnabled(false);
            minutesView.setEnabled(false);
            secondsView.setEnabled(false);
        } else {
            hoursView.setEnabled(true);
            minutesView.setEnabled(true);
            secondsView.setEnabled(true);
        }
    }

    public void updateViews(long time, String mode) {
        HashMap<String, Object> timeLeft = Timer.millisToCommonTime(time, mode);

        if (mode.equals("HH/MM/SS")) {
            hoursView.setText(String.valueOf(timeLeft.get("Hours")));
            minutesView.setText(String.valueOf(timeLeft.get("Minutes")));
            secondsView.setText(String.valueOf(timeLeft.get("Seconds")));
            sp1.setText("H");
            sp2.setText("M");
            sp3.setText("S");
        } else {
            hoursView.setText(String.valueOf(timeLeft.get("Days")));
            minutesView.setText(String.valueOf(timeLeft.get("Hours")));
            secondsView.setText(String.valueOf(timeLeft.get("Minutes")));
            sp1.setText("D");
            sp2.setText("H");
            sp3.setText("M");
        }
    }

    public void onBackPressed() {
        save();
        intent.putExtra("intervals", intervals);
        intent.putExtra("numberOfIntervals", numberOfIntervals);
        intent.putExtra("mode", mode);
        intent.putExtra("time", time);
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        finish();

    }

    private long getTimerValue() {
        int seconds = 0;
        int minutes = 0;
        int hours = 0;

        if (!secondsView.getText().toString().equals("")) {
            seconds = Integer.parseInt(secondsView.getText().toString());
        }
        if (!minutesView.getText().toString().equals("")) {
            minutes = Integer.parseInt(minutesView.getText().toString());
        }
        if (!hoursView.getText().toString().equals("")) {
            hours = Integer.parseInt(hoursView.getText().toString());
        }
        if (mode.equals("HH/MM/SS")) return (hours * (60 * 60) + minutes * 60 + seconds) * 1000;
        else return ((hours * (60 * 60 * 24)) + minutes * (60 * 60) + seconds * 60) * 1000;


    }


    private void save() {
        intervals = cb.isChecked();
        numberOfIntervals = Integer.parseInt(et.getText().toString());
        mode = sp.getSelectedItem().toString();
        time = getTimerValue();
        name = nameView.getText().toString();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }
}