package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class TimerActivity extends AppCompatActivity {

    private boolean intervals, timerRunning;
    private int numberOfIntervals;
    private long time;
    private String mode, name;
    //private CheckBox cb;
    private EditText et;
    private Spinner sp;
    private Intent intent;
    public static EditText hoursView;
    public static EditText minutesView;
    public static EditText secondsView;
    public EditText nameView;
    private TextView sp1, sp2, sp3;
    private TextView editNotifications;

    private Timer timer;
    private TimersWrapper wrapper;

    private Button addImages;

    private static final int images = 3;
    private static final int notifications = 4;


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
                time = wrapper.getTimerValue(mode, secondsView, minutesView, hoursView);
                mode = selectedItem;
                if (selectedItem.equals("HH/MM/SS")) {
                    sp1.setText("H");
                    sp2.setText("M");
                    sp3.setText("S");
                } else {
                    sp1.setText("D");
                    sp2.setText("H");
                    sp3.setText("M");
                }
                timer.setMode(mode);
                TimersWrapper.updateViews(time, mode, secondsView, minutesView, hoursView);


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et = findViewById(R.id.inputNumberIntervals);
        //cb = findViewById(R.id.notificationsCheckBox);
        hoursView = findViewById(R.id.hoursEditView);
        minutesView = findViewById(R.id.minutesEditView);
        secondsView = findViewById(R.id.secondsEditView);
        nameView = findViewById(R.id.timerName);
        sp1 = findViewById(R.id.hoursIndicator);
        sp2 = findViewById(R.id.minutesIndicator);
        sp3 = findViewById(R.id.secondsIndicator);
        addImages = findViewById(R.id.addImagesButton);

        int indexOfTimer = getIntent().getIntExtra("timerIndex", -1);
        wrapper = TimersWrapper.getInstance();
        timer = wrapper.getSpecificIndividualTimerByIndex(indexOfTimer);

        timer.setViews(secondsView, minutesView, hoursView);
        //intervals = timer.isIntervals();
        numberOfIntervals = timer.getNotifications().getNumberOfIntervals();
        mode = timer.getMode();
        name = timer.getTimerName();
        time = timer.getCurrentTimerValue();
        timerRunning = timer.isTimerRunning();
        if (!timerRunning) {
            wrapper.updateViews(time, mode, secondsView, minutesView, hoursView);
        }
        sp.setSelection(getIndex(sp, mode));
        //cb.setChecked(intervals);
        et.setText("" + numberOfIntervals);
        nameView.setText(name);
        editable(time);

        addImages.setOnClickListener(v -> {
            intent = new Intent(this.getApplicationContext(), AddImagesActivity.class);
            startActivityForResult(intent, images);
        });

        editNotifications = findViewById(R.id.enableIntervalText);
        editNotifications.setOnClickListener(v -> {
            intent = new Intent(this.getApplicationContext(), NotificationActivity.class);
            intent.putExtra("timerIndex", indexOfTimer);
            startActivityForResult(intent, notifications);
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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


    public void onBackPressed() {
        save();
        setResult(RESULT_OK);
        finish();

    }



    private void save() {
        //intervals = cb.isChecked();
        numberOfIntervals = Integer.parseInt(et.getText().toString());
        mode = sp.getSelectedItem().toString();
        time = wrapper.getTimerValue(mode, secondsView, minutesView, hoursView);
        name = nameView.getText().toString();

        //timer.setIntervals(intervals);
        timer.getNotifications().setNumberOfIntervals(numberOfIntervals);
        timer.setMode(mode);
        timer.setTimerName(name);
        timer.setCurrentTimerValue(time);

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            save();
            setResult(RESULT_OK);
            finish();

            return true;
        }
        return false;
    }
}