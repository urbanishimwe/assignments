package com.example.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.util.Map;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<Integer, Runnable> actions = Map.of(R.id.button, () -> {
            Intent i = new Intent(this, Q1.class);
            startActivity(i);
        }, R.id.button2, () -> {
            Intent i = new Intent(this, Q2.class);
            startActivity(i);
        }, R.id.button3, () -> {
            Intent i = new Intent(this, Q3.class);
            startActivity(i);
        }, R.id.button4, () -> {
            Intent i = new Intent(this, Q4.class);
            startActivity(i);
        }, R.id.button5, () -> {
            Intent i = new Intent(this, Q5.class);
            startActivity(i);
        }, R.id.button6, () -> {
            Intent i = new Intent(this, Q6.class);
            startActivity(i);
        }, R.id.button7, () -> {
            new AlertDialog.Builder(this)
                    .setTitle("An alert Dialog")
                    .setMessage("Hello World!")
                    .show();
        }, R.id.button8, () -> {
            Intent i = new Intent(this, Q8.class);
            startActivity(i);
        });

        actions.forEach((id, action) -> {
            Button b = findViewById(id);
            b.setOnClickListener(view -> action.run());
        });
    }
}
