package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigureActivity extends AppCompatActivity {
    private boolean intervals;
    private int numberOfIntervals;
    private String mode;
    private CheckBox cb;
    private EditText et;
    private Spinner sp;
    private Intent intent;
    private boolean saved;

    private Timer timer;
    private TimersWrapper wrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        sp = findViewById(R.id.modes_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.modes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(adapter);
        et = findViewById(R.id.inputIntervals);
        cb = findViewById(R.id.notificationsCheckBox);
        int indexOfTimer = getIntent().getIntExtra("timerIndex", -1);
        wrapper = TimersWrapper.getInstance();
        timer = wrapper.getSpecificIndividualTimerByIndex(indexOfTimer);
        sp.setSelection(getIndex(sp, timer.getMode()));
        cb.setChecked(timer.isIntervals());
        et.setText("" + timer.getNumberOfIntervals());
        ActionBar actionBar = getSupportActionBar();
        Button bt1 = findViewById(R.id.saveButton);
        bt1.setOnClickListener(v -> save());
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        saved = false;
        intent = new Intent(this, MainActivity.class);

    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void save() {
        intervals = cb.isChecked();
        numberOfIntervals = Integer.parseInt(et.getText().toString());
        mode = sp.getSelectedItem().toString();

        timer.setIntervals(intervals);
        timer.setMode(mode);
        timer.setNumberOfIntervals(numberOfIntervals);


        saved = true;
    }

    public void onBackPressed() {
        timer.setIntervals(intervals);
        timer.setMode(mode);
        timer.setNumberOfIntervals(numberOfIntervals);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                timer.setIntervals(intervals);
                timer.setMode(mode);
                timer.setNumberOfIntervals(numberOfIntervals);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}