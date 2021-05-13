package com.example.timer_10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeAppThemeActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();
        TimersWrapper.loadTheme(this);
        setContentView(R.layout.activity_change_app_theme);
        findViewById(R.id.BlueGreyLayout).setOnClickListener(this);
        findViewById(R.id.OrangeLayout).setOnClickListener(this);
        findViewById(R.id.GreenLayout).setOnClickListener(this);
        findViewById(R.id.RedBlackLayout).setOnClickListener(this);
        findViewById(R.id.BlackLayout).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BlueGreyLayout:
                editor.putInt("currentTheme", 0);
                editor.commit();
                break;

            case R.id.OrangeLayout:
                editor.putInt("currentTheme", 1);
                editor.commit();
                break;
            case R.id.GreenLayout:
                editor.putInt("currentTheme", 2);
                editor.commit();
                break;

            case R.id.RedBlackLayout:
                editor.putInt("currentTheme", 3);
                editor.commit();
                break;

            case R.id.BlackLayout:
                editor.putInt("currentTheme", 4);
                editor.commit();
                break;
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void onBackPressed() {
        finish();
    }
}