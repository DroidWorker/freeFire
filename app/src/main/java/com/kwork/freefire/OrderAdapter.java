package com.kwork.freefire;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwork.freefire.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Order> orders;
    boolean isAdmin = false;
    Context ctx;

    OrderAdapter(Context context, List<Order> states) {
        this.ctx = context;
        this.orders = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        int pos = position;
        Order order = orders.get(position);
        if (order.id>=0)
            holder.id.setText(String.valueOf(order.id));
        else holder.id.setText("№");
        holder.accountID.setText(order.accountID);
        holder.status.setText(order.getStatus());
        holder.summ.setText(order.summ);

        if (isAdmin)
            holder.root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    startCheckboxMenu(orders.get(pos).accountID, orders.get(pos).userID, String.valueOf(orders.get(pos).id));
                    return true;
                }
            });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView id, accountID, status, summ;
        ConstraintLayout root = itemView.findViewById(R.id.root);
        ViewHolder(View view){
            super(view);
            id = view.findViewById(R.id.id);
            accountID = view.findViewById(R.id.accountID);
            status = view.findViewById(R.id.status);
            summ = view.findViewById(R.id.summ);
        }
    }

    void startCheckboxMenu(String accID, String userID, String orderID){
        View checkBoxView = View.inflate(ctx, R.layout.checkbox_menu, null);
        RadioButton r1 = (RadioButton) checkBoxView.findViewById(R.id.radioButton1);
        RadioButton r2 = (RadioButton) checkBoxView.findViewById(R.id.radioButton2);
        RadioButton r3 = (RadioButton) checkBoxView.findViewById(R.id.radioButton3);
        RadioButton r4 = (RadioButton) checkBoxView.findViewById(R.id.radioButton4);

        r1.setText("обрабатывается");
        r2.setText("выплата");
        r3.setText("оплачено");
        r4.setText("отменено");

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("аккаунт "+accID);
        builder.setMessage("Пользователь "+userID)
                .setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://freefire-7e250-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference().child("orders").child(orderID).child("status");
                            if (r1.isChecked())
                                myRef.setValue("1");
                            else if (r2.isChecked())
                                myRef.setValue("2");
                            else if (r3.isChecked())
                                myRef.setValue("3");
                            else if (r4.isChecked())
                                myRef.setValue("4");
                        }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}
