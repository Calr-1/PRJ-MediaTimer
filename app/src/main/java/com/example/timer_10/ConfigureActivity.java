package com.example.timer_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigureActivity extends AppCompatActivity {
    private boolean intervals;
    private int numberOfIntervals;
    private String mode;
    private CheckBox cb;
    private EditText et;
    private Spinner sp;
    private Intent intent;
    private boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        sp = findViewById(R.id.modes_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.modes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(adapter);
        et = findViewById(R.id.inputIntervals);
        cb = findViewById(R.id.notificationsCheckBox);
        intervals = getIntent().getBooleanExtra("intervals", false);
        numberOfIntervals = getIntent().getIntExtra("numberOfIntervals", 1);
        mode = getIntent().getStringExtra("mode");
        sp.setSelection(getIndex(sp, mode));
        cb.setChecked(intervals);
        et.setText("" + numberOfIntervals);
        ActionBar actionBar = getSupportActionBar();
        Button bt1 = findViewById(R.id.saveButton);
        bt1.setOnClickListener(v -> save());
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        saved = false;
        intent = new Intent(this, MainActivity.class);

    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void save() {
        intervals = cb.isChecked();
        numberOfIntervals = Integer.parseInt(et.getText().toString());
        mode = sp.getSelectedItem().toString();


        saved = true;
    }

    public void onBackPressed() {
        intent.putExtra("intervals", intervals);
        intent.putExtra("numberOfIntervals", numberOfIntervals);
        intent.putExtra("mode", mode);
        if (saved) setResult(RESULT_OK, intent);
        else setResult(RESULT_CANCELED, intent);// or setResult(RESULT_CANCELED);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                intent.putExtra("intervals", intervals);
                intent.putExtra("numberOfIntervals", numberOfIntervals);
                intent.putExtra("mode", mode);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}