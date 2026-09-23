package com.example.assignment1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

public class Q6 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q6);

        ArrayAdapter<String> dp = new ArrayAdapter<>(this, R.layout.q6_list_item);
        ListView lv = findViewById(R.id.q6_listView);
        lv.setAdapter(dp);

        Button b = findViewById(R.id.q6_button);
        b.setOnClickListener(view -> {
            TextInputEditText e = findViewById(R.id.q6_textInput);
            if (e.getText() == null) return;
            String text = e.getText().toString();
            if (text.isEmpty()) return;
            dp.add(text);
            e.setText(null);
        });
    }
}