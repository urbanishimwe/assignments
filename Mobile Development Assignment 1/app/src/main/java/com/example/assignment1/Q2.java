package com.example.assignment1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Q2 extends Activity {

    private int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q2);
    }

    public void handleClick(View v) {
        clicked++;
        Button b = (Button) v;
        b.setText(getString(R.string.clicked_times, clicked));
    }
}