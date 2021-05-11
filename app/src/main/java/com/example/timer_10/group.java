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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class group extends AppCompatActivity {

    private ImageButton addButton;
    private TimerGroup timerGroup;
    private TimersWrapper wrapper;

    private EditText et;

    SharedPreferences app_preferences;

    boolean appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getBoolean("color", false);
        if (!appColor) {
            setTheme(R.style.Theme_Timer_10);
        } else if (appColor) {
            setTheme(R.style.Theme_ORANGE_THEME);
        }
        setContentView(R.layout.activity_group);

        wrapper = TimersWrapper.getInstance();
        timerGroup = wrapper.getSpecificGroupOfTimersByIndex(getIntent().getIntExtra("groupIndex", -1));

        et = findViewById(R.id.groupName);
        et.setText(timerGroup.getName());
        addButton = findViewById(R.id.addTimerButton);
        registerForContextMenu(addButton);
        addButton.setOnClickListener(v -> addButton.showContextMenu());
        addButton.setOnLongClickListener(v -> true);
        loadExistingTimers();

    }

    private void loadExistingTimers() {
        for (int index = 0; index < timerGroup.getNumberOfTimers(); index++) {

            timerFragment frag = new timerFragment(3, timerGroup, timerGroup.getTimerByIndex(index));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            timerGroup.addTimerFragment(frag);
        }
        for (int index = 0; index < timerGroup.getNumberOfGroups(); index++) {

            timer_group frag = new timer_group(3, timerGroup.getGroupByIndex(index), timerGroup);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            timerGroup.addTimerGroupFragment(frag);
        }

    }


    private void addTimerFragment() {
        timerFragment frag = new timerFragment(2, timerGroup);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        timerGroup.addTimerFragment(frag);
    }

    private void addGroupFragment() {

        timer_group frag = new timer_group(2, timerGroup);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        timerGroup.addTimerGroupFragment(frag);

    }

    public void onBackPressed() {
        String name = et.getText().toString();
        timerGroup.setName(name);

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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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