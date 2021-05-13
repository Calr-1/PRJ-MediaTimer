package com.example.timer_10;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeAppThemeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_change_app_theme);
    }
}