package com.example.timer_10;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TimerAudioActivity extends AppCompatActivity {

    private ArrayList<String> ringtoneNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_audio);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 5);
        ringtoneNames = new ArrayList<>();
        getRingtoneNames();

    }

    private void addToUi(ArrayList<String> ringtoneNames) {

        for (String name : ringtoneNames) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(this);
            textView.setText(name);
            linearLayout.addView(textView);

            LinearLayout f = findViewById(R.id.ringtoneLayout);
            f.addView(linearLayout);
        }

    }

    private void getRingtoneNames() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("alarm-ringtones");

        listRef.listAll()
                .addOnSuccessListener(listResult -> {

                    for (StorageReference item : listResult.getItems()) {
                        String name = item.getName();
                        this.ringtoneNames.add(name);
                    }
                    addToUi(this.ringtoneNames);
                })
                .addOnFailureListener(e -> {
                });

    }
}