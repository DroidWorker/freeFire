package com.kwork.freefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EarningActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences mSettings;
    int coins = 0;
    int userID;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);

        mSettings = getSharedPreferences("appcfg", this.MODE_PRIVATE);
        userID = mSettings.getInt("userID", -1);

        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference();
        DatabaseReference refUID = myRef.child("users").child(String.valueOf(userID));
        refUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coins = Integer.parseInt(snapshot.child("coins").getValue().toString());
                TextView coinValue = findViewById(R.id.coinValue);
                coinValue.setText(String.valueOf(coins));
                TextView tvAvailable = findViewById(R.id.textViewAvailable);
                tvAvailable.setText("доступно для вывода "+coins+" монет");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BuildPriceBlock();
    }

    public void onWithdrawClick(View view){
        EditText etAccountID = findViewById(R.id.editTextAccountID);
        EditText etCoinNumber = findViewById(R.id.editTextCoinNumber);

        DatabaseReference loID = myRef.child("lastOrderID");
        loID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flag) return;
                flag = true;
                int lastOrderId = Integer.parseInt(snapshot.getValue().toString());
                Order order = new Order();
                order.id =lastOrderId;
                order.accountID = etAccountID.getText().toString();
                order.summ = etCoinNumber.getText().toString();
                order.userID = String.valueOf(userID);
                order.setStatus(1);
                order.publish(database.getReference("orders"));
                database.getReference("lastOrderID").setValue(lastOrderId+1);
                Snackbar.make(view, "заявка сформирована", BaseTransientBottomBar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception ex){
                            Log.e("err", ex.getMessage());
                        }
                        EarningActivity.this.finish();
                    };
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void BuildPriceBlock(){
        DatabaseReference exc = myRef.child("exchange");
        TextView defaultPrice = findViewById(R.id.defaultPrice);
        TextView sellPrice = findViewById(R.id.sellPrice);

        exc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sellString = "Выгодный обмен\n";
                String defNum = snapshot.child("default").getValue().toString();
                String sale1diamond = snapshot.child("sale1").child("diamond").getValue().toString();
                String sale1coin = snapshot.child("sale1").child("coin").getValue().toString();
                String sale2diamond = snapshot.child("sale2").child("diamond").getValue().toString();
                String sale2coin = snapshot.child("sale2").child("coin").getValue().toString();
                String sale3diamond = snapshot.child("sale3").child("diamond").getValue().toString();
                String sale3coin = snapshot.child("sale3").child("coin").getValue().toString();
                String waucher = snapshot.child("waucher").getValue().toString();
                defaultPrice.setText("1 алмаз = "+defNum+" монет");
                if (!sale1diamond.equals("0")){
                    sellString+=sale1diamond+" алмазов = "+sale1coin+" монет\n";
                }
                if (!sale2diamond.equals("0")){
                    sellString+=sale2diamond+" алмазов = "+sale2coin+" монет\n";
                }
                if (!sale3diamond.equals("0")){
                    sellString+=sale3diamond+" алмазов = "+sale3coin+" монет\n";
                }
                if (!waucher.equals("0")){
                    sellString+="недельный ваучер "+waucher;
                }
                if (!sale1diamond.equals("0")||!sale2diamond.equals("0")||!sale3diamond.equals("0")||!waucher.equals("0"))
                    sellPrice.setText(sellString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}