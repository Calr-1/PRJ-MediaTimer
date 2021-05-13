package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ImageButton addButton;
    private TimersWrapper wrapper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapper = TimersWrapper.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        registerForContextMenu(addButton);
        addButton.setOnClickListener(v -> addButton.showContextMenu());
        addButton.setOnLongClickListener(v -> true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.change_theme_option) {

            Intent intent = new Intent(this, ChangeAppThemeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTimerFragment() {

        TimerFragment frag = new TimerFragment(1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        wrapper.addTimerFragment(frag);


    }

    private void addGroupFragment() {

        TimerGroupFragment frag = new TimerGroupFragment(1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.timerLayout, frag)
                .commit();
        wrapper.addTimerGroupFragment(frag);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
