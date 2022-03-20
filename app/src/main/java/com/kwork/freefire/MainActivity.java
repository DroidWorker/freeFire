package com.kwork.freefire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEarningClick(View view){
        startActivity(new Intent(MainActivity.this, EarningActivity.class));
    }
    public void onCabinetClick(View view){
        startActivity(new Intent(MainActivity.this, CabinetActivity.class));
    }
}