package com.kwork.freefire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kwork.freefire.model.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{

    private final LayoutInflater inflater;
     final List<Message> users;
    Context ctx;

    MessagesAdapter(Context context, List<Message> users) {
        this.ctx = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder holder, int position) {
        int pos = position;
        Message user = users.get(position);
        holder.userid.setText(user.getUserID());
        holder.message.setText(user.getMessage());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView userid, message, email;
        ConstraintLayout root = itemView.findViewById(R.id.root);
        ViewHolder(View view){
            super(view);
            userid = view.findViewById(R.id.textView4);
            message = view.findViewById(R.id.textView5);
            email = view.findViewById(R.id.textView6);
        }
    }
}
