package com.example.timer_10;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button addButton;

    private ArrayList<timerFragment> fragmentsTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> addFragment());

    }

    private void addFragment() {

        if (fragmentsTimer.size() < 4) {

            timerFragment frag = new timerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timerLayout, frag)
                    .commit();
            fragmentsTimer.add(frag);
            System.out.println("Confirm x123");
        }
    }

}