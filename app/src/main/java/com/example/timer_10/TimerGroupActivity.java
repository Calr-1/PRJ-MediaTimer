package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TimerGroupActivity extends AppCompatActivity {

    private ImageButton addButton;
    private TimerGroupClass timerGroupClass;

    private EditText et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_group);
        registerForContextMenu(findViewById(R.id.addButton));

        TimersWrapper wrapper = TimersWrapper.getInstance();
        timerGroupClass = wrapper.getSpecificGroupOfTimersByIndex(getIntent().getIntExtra("groupIndex", -1));

        et = findViewById(R.id.groupName);
        et.setText(timerGroupClass.getName());
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Log.d("ENTROU 1",String.valueOf(item.getItemId()));

                        switch (item.getItemId()) {
                            case R.id.favouriteButton:
                                break;
                            case R.id.addButton:
                                findViewById(R.id.addButton).showContextMenu();
                                break;
                            case R.id.optionsButton:
                                Intent intent = new Intent(TimerGroupActivity.this, ChangeAppThemeActivity.class);
                                startActivityForResult(intent, 1);
                                break;
                        }
                        return false;
                    }
                });

        bottomNav.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        Log.d("ENTROU 2",String.valueOf(item.getItemId()));

                        switch (item.getItemId()) {
                            case R.id.favouriteButton:
                                break;
                            case R.id.addButton:
                                findViewById(R.id.addButton).showContextMenu();
                                break;
                            case R.id.optionsButton:
                                Intent intent = new Intent(TimerGroupActivity.this, ChangeAppThemeActivity.class);
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                });
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