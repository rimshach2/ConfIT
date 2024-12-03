package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class NoUnityRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_unity_record);
    }

    public void onClick(View v)
    {
        super.onBackPressed();
    }
}