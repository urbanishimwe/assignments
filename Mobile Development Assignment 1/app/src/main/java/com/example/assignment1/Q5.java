package com.example.assignment1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Q5 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q5);

        RadioGroup rg = findViewById(R.id.q5_radioButton);
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            TextView t = findViewById(R.id.q5_answer);
            RadioButton b = radioGroup.findViewById(i);
            if (b.getText() == getString(R.string.chicken)) t.setText(R.string.a_carnivore);
            else t.setText(R.string.you_are_a_vegan);
        });
    }
}