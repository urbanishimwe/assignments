package com.example.assignment1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class Q4 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q4);

        CheckBox c = findViewById(R.id.e4_checkbox);
        // disable checkbox on startup
        c.setChecked(false);

        c.setOnCheckedChangeListener((compoundButton, b) -> {
            TextView t = findViewById(R.id.e4_text);
            if (b) t.setText(R.string.a_student);
            else t.setText(R.string.not_a_student);
        });
    }
}