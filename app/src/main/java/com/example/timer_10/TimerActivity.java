package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class TimerActivity extends AppCompatActivity {

    private boolean intervals;
    private long time;
    private String mode, name;

    private Spinner sp;
    private Intent intent;
    public EditText hoursView;
    public EditText minutesView;
    public EditText secondsView;
    public EditText nameView;
    private TextView sp1, sp2, sp3;

    private TimerClass timerClass;
    private int indexTimer;


    private static final int images = 3;
    private static final int notifications = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimersWrapper.loadTheme(this);
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
                time = TimersWrapper.getTimerValue(mode, secondsView, minutesView, hoursView);
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
                timerClass.setMode(mode);
                TimersWrapper.updateViews(time, mode, secondsView, minutesView, hoursView);


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button addImages = findViewById(R.id.addImagesButton);
        hoursView = findViewById(R.id.hoursEditView);
        minutesView = findViewById(R.id.minutesEditView);
        secondsView = findViewById(R.id.secondsEditView);
        nameView = findViewById(R.id.groupName);
        sp1 = findViewById(R.id.hoursIndicator);
        sp2 = findViewById(R.id.minutesIndicator);
        sp3 = findViewById(R.id.secondsIndicator);
        TimersWrapper wrapper = TimersWrapper.getInstance();
        int typeID = getIntent().getIntExtra("typeID", -1);

        if (typeID == 2 || typeID == 3) {
            int indexGroup = getIntent().getIntExtra("indexGroup", -1);
            indexTimer = getIntent().getIntExtra("indexTimer", -1);
            TimerGroupClass group = wrapper.getSpecificGroupOfTimersByIndex(indexGroup);
            timerClass = group.getTimerByIndex(indexTimer);
        } else if (typeID == 1 || typeID == 4) {
            indexTimer = getIntent().getIntExtra("indexTimer", -1);
            timerClass = wrapper.getSpecificIndividualTimerByIndex(indexTimer);

        }

        timerClass.setViews(secondsView, minutesView, hoursView);

        mode = timerClass.getMode();
        name = timerClass.getTimerName();
        time = timerClass.getCurrentTimerValue();
        boolean timerRunning = timerClass.isTimerRunning();
        if (!timerRunning) {
            TimersWrapper.updateViews(time, mode, secondsView, minutesView, hoursView);
        }
        sp.setSelection(getIndex(sp, mode));

        nameView.setText(name);
        editable(time);

        intent = new Intent(this.getApplicationContext(), TimerFragment.class);

        addImages.setOnClickListener(v -> {
            intent = new Intent(this.getApplicationContext(), AddImagesActivity.class);
            intent.putExtra("timerIndex", indexTimer);
            startActivityForResult(intent, images);
        });

        TextView editNotifications = findViewById(R.id.enableIntervalText);
        editNotifications.setOnClickListener(v -> {
            intent = new Intent(this.getApplicationContext(), NotificationActivity.class);
            intent.putExtra("timerIndex", indexTimer);
            startActivityForResult(intent, notifications);
        });

        Upload upload = timerClass.getUpload();
        ImageView image = findViewById(R.id.timerMedia);
        if (upload != null) {
            Picasso.get()
                    .load(upload.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(image);
        } else {
            image.setImageResource(R.drawable.no_image);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Upload upload = timerClass.getUpload();
        ImageView image = findViewById(R.id.timerMedia);
        if (upload != null) {
            Picasso.get()
                    .load(upload.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(image);
        } else {
            image.setImageResource(R.drawable.no_image);
        }
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

        mode = sp.getSelectedItem().toString();
        time = TimersWrapper.getTimerValue(mode, secondsView, minutesView, hoursView);
        name = nameView.getText().toString();

        timerClass.setMode(mode);
        timerClass.setTimerName(name);
        timerClass.setCurrentTimerValue(time);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            save();
            setResult(RESULT_OK);
            finish();

            return true;
        }
        return false;
    }
}