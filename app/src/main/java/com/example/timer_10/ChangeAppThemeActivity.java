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
        findViewById(R.id.chooseBlueGreyColor).setOnClickListener(this);
        findViewById(R.id.chooseBlackColor).setOnClickListener(this);
        findViewById(R.id.chooseGreenColor).setOnClickListener(this);
        findViewById(R.id.chooseOrangeColor).setOnClickListener(this);
        findViewById(R.id.chooseRedBlackColor).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chooseBlueGreyColor:
                editor.putInt("currentTheme", 0);
                editor.commit();
                break;

            case R.id.chooseOrangeColor:
                editor.putInt("currentTheme", 1);
                editor.commit();
                break;
            case R.id.chooseGreenColor:
                editor.putInt("currentTheme", 2);
                editor.commit();
                break;

            case R.id.chooseRedBlackColor:
                editor.putInt("currentTheme", 3);
                editor.commit();
                break;

            case R.id.chooseBlackColor:
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