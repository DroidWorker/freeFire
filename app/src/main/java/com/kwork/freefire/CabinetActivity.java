package com.kwork.freefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.freefire.model.Message;
import com.kwork.freefire.model.Order;

import java.util.ArrayList;

public class CabinetActivity extends AppCompatActivity {
    private String password = "qwerty";
    ArrayList<Order> orders = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;

    OrderAdapter oAdapter;
    SharedPreferences mSettings;
    int userID;

    boolean mode = false;//false- user| true- admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        mSettings = getSharedPreferences("appcfg", this.MODE_PRIVATE);
        userID = mSettings.getInt("userID", -1);

        Order o = new Order();
        o.id = -1;
        o.accountID = "ID аккаунта";
        o.setStatus(0);
        o.summ = "сумма";
        orders.add(o);

        TextView tvCabinet = findViewById(R.id.cabinet);
        tvCabinet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CabinetActivity.this);

                alert.setTitle("пароль администратора");

                final EditText input = new EditText(CabinetActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if (value.equals(password))
                            setAdmin();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                return true;
            }
        });
        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference();
        DatabaseReference refUID = myRef.child("users").child(String.valueOf(userID));
        refUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int coins = Integer.parseInt(snapshot.child("coins").getValue().toString());
                TextView coinValue = findViewById(R.id.coinValue);
                coinValue.setText(String.valueOf(coins));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView rv = findViewById(R.id.recyclerView);
        oAdapter = new OrderAdapter(this, orders);
        rv.setAdapter(oAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        updateRV(oAdapter);
    }

    public void onBackClick(View view){
        this.finish();
    }


    void setAdmin(){
        oAdapter.isAdmin = true;
        TextView tvCabinet = findViewById(R.id.cabinet);
        tvCabinet.setText("АДМИНИСТРАТОР");
        LinearLayout cash = findViewById(R.id.linearLayout);
        cash.setVisibility(View.GONE);
        TextView tvBottom = findViewById(R.id.BottomTextView);
        tvBottom.setVisibility(View.GONE);
        TextView tvEdit = findViewById(R.id.Edit);
        tvEdit.setVisibility(View.VISIBLE);
        TextView tvErr = findViewById(R.id.textView10);
        tvErr.setVisibility(View.GONE);
        ImageButton messages = findViewById(R.id.messages);
        messages.setVisibility(View.VISIBLE);
        mode = true;
        orders = new ArrayList<>();
        Order o = new Order();
        o.id = -1;
        o.accountID = "ID аккаунта";
        o.setStatus(0);
        o.summ = "сумма";
        orders.add(o);
        updateRV(oAdapter);
    }

    public void showDialog(View view) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View linearlayout = getLayoutInflater().inflate(R.layout.menu, null);
        dialog.setView(linearlayout);
        //Button acceptButton = linearlayout.findViewById(R.id.buttonAccept);
        EditText s1c = linearlayout.findViewById(R.id.s1c);
        EditText a1 = linearlayout.findViewById(R.id.a1);
        EditText a2 = linearlayout.findViewById(R.id.a2);
        EditText a3 = linearlayout.findViewById(R.id.a3);
        EditText c1 = linearlayout.findViewById(R.id.c1);
        EditText c2 = linearlayout.findViewById(R.id.c2);
        EditText c3 = linearlayout.findViewById(R.id.c3);
        EditText wv1 = linearlayout.findViewById(R.id.wv1);
        dialog.setPositiveButton("ПРИМЕНИТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
                myRef = database.getReference().child("exchange");
                if (s1c.getText().length()>0)
                    myRef.child("default").setValue(s1c.getText().toString());
                if (a1.getText().length()>0&&c1.getText().length()>0){
                    myRef.child("sale1").child("coin").setValue(c1.getText().toString());
                    myRef.child("sale1").child("diamond").setValue(a1.getText().toString());
                }
                if (a2.getText().length()>0&&c2.getText().length()>0){
                    myRef.child("sale2").child("coin").setValue(c2.getText().toString());
                    myRef.child("sale2").child("diamond").setValue(a2.getText().toString());
                }
                if (a3.getText().length()>0&&c3.getText().length()>0){
                    myRef.child("sale3").child("coin").setValue(c3.getText().toString());
                    myRef.child("sale3").child("diamond").setValue(a3.getText().toString());
                }
                if (wv1.getText().length()>0)
                    myRef.child("waucher").setValue(wv1.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }

    public void onMessagesClick(View view){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        ArrayList<com.kwork.freefire.model.Message> messages = new ArrayList<>();

        View linearlayout = getLayoutInflater().inflate(R.layout.messages_menu, null);
        dialog.setView(linearlayout);
        RecyclerView rv = linearlayout.findViewById(R.id.rvMessages);
        MessagesAdapter ma = new MessagesAdapter(this, messages);
        rv.setAdapter(ma);
        rv.setLayoutManager(new LinearLayoutManager(this));
        messages = loadMessages(ma);
        dialog.setPositiveButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
    public void onsendFeedbackClick(View view){
        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference().child("feedback");

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View linearlayout = getLayoutInflater().inflate(R.layout.input_dialog, null);
        dialog.setView(linearlayout);
        EditText etInput = linearlayout.findViewById(R.id.feedbackField);
        EditText etEmail = linearlayout.findViewById(R.id.inputEmail);
        dialog.setPositiveButton("ОТПРАВИТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!etEmail.getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    Toast.makeText(CabinetActivity.this, "некорректный EMAIl", Toast.LENGTH_SHORT).show();
                    return;
                }
                myRef.child(String.valueOf(userID)).child("message").setValue(etInput.getText().toString());
                myRef.child(String.valueOf(userID)).child("email").setValue(etEmail.getText().toString());
                Snackbar.make(view, "сообщение отправлено", BaseTransientBottomBar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.create();
        dialog.show();
    }

    void updateRV(OrderAdapter oAdapter){
        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference().child("orders");
        if (!mode){//если пользователь
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()
                    ) {
                        Log.i("aaaaa", snap.getKey()+"|||"+String.valueOf(userID));
                        if (snap.child("userID").getValue().toString().equals(String.valueOf(userID))){
                            Order o = new Order();
                            o.id = Integer.parseInt(snap.getKey().toString());
                            o.accountID = snap.child("accountID").getValue().toString();
                            o.setStatus(Integer.parseInt(snap.child("status").getValue().toString()));
                            o.summ = snap.child("summ").getValue().toString();
                            orders.add(o);
                        }
                    }
                    oAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()
                    ) {
                        Order o = new Order();
                        o.id = Integer.parseInt(snap.getKey().toString());
                        o.accountID = snap.child("accountID").getValue().toString();
                        o.setStatus(Integer.parseInt(snap.child("status").getValue().toString()));
                        o.summ = snap.child("summ").getValue().toString();
                        orders.add(o);
                    }
                    oAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    ArrayList<Message> loadMessages(MessagesAdapter ma){
        ArrayList<Message> messages = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference();
        DatabaseReference reffedbk = myRef.child("feedback");
        reffedbk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    Message m = new Message();
                    m.setUserID(snap.getKey());
                    m.setMessage(snap.child("message").getValue().toString());
                    m.setEmail(snap.child("email").getValue().toString());
                    messages.add(m);
                    ma.users.add(m);
                }
                ma.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messages;
    }
}