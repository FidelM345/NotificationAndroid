package com.example.notificationtest;


import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;



public class UserAdapter extends RecyclerView.Adapter<UserAdapter.OrdersViewHolder> {


    private Context mCtx;
    private List<User> userList;

    public UserAdapter(Context mCtx, List<User> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @Override
    public UserAdapter.OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.userlayout, null);
        return new UserAdapter.OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.OrdersViewHolder holder, final int position) {
       final String email = userList.get(position).getEmail();
        final String token = userList.get(position).getToken();


        holder.usermail.setText(email);
        holder.id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mCtx,DisplayUsers.class);
                i.putExtra("email",email);
                i.putExtra("token",token);
                mCtx.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {
        TextView usermail;
        ConstraintLayout constraintLayout;
        Button id_button;

        public OrdersViewHolder(View itemView) {
            super(itemView);

            usermail = itemView.findViewById(R.id.id_email);
            id_button=itemView.findViewById(R.id.button);

        }
    }
}