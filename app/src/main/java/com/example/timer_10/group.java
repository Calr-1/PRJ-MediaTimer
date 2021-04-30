package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class group extends AppCompatActivity {

    private Button addButton;
    private TimerGroup timerGroup;
    private TimersWrapper wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        wrapper = TimersWrapper.getInstance();
        timerGroup = wrapper.getSpecificGroupOfTimersByIndex(getIntent().getIntExtra("groupIndex", -1));

        TextView tv = findViewById(R.id.groupName);
        tv.setText(timerGroup.getName());
        addButton = findViewById(R.id.addTimerButton);
        addButton.setOnClickListener(v -> addFragment());
        if (timerGroup.getNumberOfTimers() > 0) loadExistingTimers();
    }

    private void loadExistingTimers() {
        for (int index = 0; index < timerGroup.getNumberOfTimers(); index++) {

            timerFragment frag = new timerFragment(3, timerGroup, timerGroup.getTimerByIndex(index));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            timerGroup.addTimerFragment(frag);
        }

    }

    private void addFragment() {
        timerFragment frag = new timerFragment(2, timerGroup);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        timerGroup.addTimerFragment(frag);

    }

    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("groupIndex", getIntent().getIntExtra("groupIndex", -1));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }
}