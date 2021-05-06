package com.heavycoders.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class VolunteerActivity extends AppCompatActivity {
    Toolbar toolbar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);

        Bundle bd = getIntent().getExtras();
        String ss = bd.getString("state");
        Log.i("state_name", ss);
    }
}