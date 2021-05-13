package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GroupActivity extends AppCompatActivity {

    private ImageButton addButton;
    private TimerGroupClass timerGroupClass;

    private EditText et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean appColor = app_preferences.getBoolean("color", false);
        if (!appColor) {
            setTheme(R.style.Theme_Timer_10);
        } else if (appColor) {
            setTheme(R.style.Theme_ORANGE_THEME);
        }
        setContentView(R.layout.activity_group);

        TimersWrapper wrapper = TimersWrapper.getInstance();
        timerGroupClass = wrapper.getSpecificGroupOfTimersByIndex(getIntent().getIntExtra("groupIndex", -1));

        et = findViewById(R.id.groupName);
        et.setText(timerGroupClass.getName());
        addButton = findViewById(R.id.addTimerButton);
        registerForContextMenu(addButton);
        addButton.setOnClickListener(v -> addButton.showContextMenu());
        addButton.setOnLongClickListener(v -> true);
        loadExistingTimers();

    }

    private void loadExistingTimers() {
        for (int index = 0; index < timerGroupClass.getNumberOfTimers(); index++) {

            TimerFragment frag = new TimerFragment(3, timerGroupClass, timerGroupClass.getTimerByIndex(index));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            timerGroupClass.addTimerFragment(frag);
        }
        for (int index = 0; index < timerGroupClass.getNumberOfGroups(); index++) {

            TimerGroupFragment frag = new TimerGroupFragment(3, timerGroupClass.getGroupByIndex(index), timerGroupClass);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            timerGroupClass.addTimerGroupFragment(frag);
        }

    }


    private void addTimerFragment() {
        TimerFragment frag = new TimerFragment(2, timerGroupClass);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        timerGroupClass.addTimerFragment(frag);
    }

    private void addGroupFragment() {

        TimerGroupFragment frag = new TimerGroupFragment(2, timerGroupClass);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        timerGroupClass.addTimerGroupFragment(frag);

    }

    public void onBackPressed() {
        String name = et.getText().toString();
        timerGroupClass.setName(name);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("groupIndex", getIntent().getIntExtra("groupIndex", -1));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.choose_type_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_timer_option:
                addTimerFragment();
                return true;
            case R.id.create_group_option:
                addGroupFragment();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}