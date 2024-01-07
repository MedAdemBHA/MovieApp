package com.example.movieapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button Btngetin;
    Button Btngetin3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Btngetin= findViewById(R.id.btn_getin);
        Btngetin.setOnClickListener(this);
        Btngetin3= findViewById(R.id.btn_getin3);
        Btngetin3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_getin) {
            Intent myIntent = new Intent(MainActivity.this, Login.class);
            startActivity(myIntent);
        }

        if (view.getId() == R.id.btn_getin3) {
            Intent myIntent = new Intent(MainActivity.this, Home.class);
            startActivity(myIntent);
        }
    }
}