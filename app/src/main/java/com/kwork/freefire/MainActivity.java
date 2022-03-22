package com.kwork.freefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    int lastUserID = 0;
    int coins = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSettings = getSharedPreferences("appcfg", this.MODE_PRIVATE);
        int userID = mSettings.getInt("userID", -1);

        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        if (userID!=-1) {//если юзер существут
            myRef = database.getReference("users").child(String.valueOf(userID));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    coins = Integer.parseInt(snapshot.child("coins").getValue().toString());
                    TextView coinValue = findViewById(R.id.coinValue);
                    coinValue.setText(String.valueOf(coins));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            myRef = database.getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lastUserID = Integer.parseInt(snapshot.child("lastUserID").getValue().toString());
                    myRef.child("users").child(String.valueOf(lastUserID+1)).child("coins").setValue(0);
                    myRef.child("lastUserID").setValue(lastUserID+1);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt("userID", lastUserID+1);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void onEarningClick(View view){
        startActivity(new Intent(MainActivity.this, EarningActivity.class));
    }
    public void onCabinetClick(View view){
        startActivity(new Intent(MainActivity.this, CabinetActivity.class));
    }
}