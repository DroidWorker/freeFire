package com.kwork.freefire;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CabinetActivity extends AppCompatActivity {
    private String password = "qwerty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

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
    }

    public void onBackClick(View view){
        this.finish();
    }


    void setAdmin(){
        TextView tvCabinet = findViewById(R.id.cabinet);
        tvCabinet.setText("АДМИНИСТРАТОР");
        LinearLayout cash = findViewById(R.id.linearLayout);
        cash.setVisibility(View.GONE);
        TextView tvBottom = findViewById(R.id.BottomTextView);
        tvBottom.setVisibility(View.GONE);
        TextView tvEdit = findViewById(R.id.Edit);
        tvEdit.setVisibility(View.VISIBLE);
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
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }

}