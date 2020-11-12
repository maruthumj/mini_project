package com.example.project_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

public class archived extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Archived");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}