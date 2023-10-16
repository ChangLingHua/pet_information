package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    //建立所需的全域變數
    Context mContext;
    ArrayList<Customer> customerList;

    //透過建構子，傳入參數資訊
    public CustomerAdapter(Context mContext, ArrayList<Customer> customerList) {
        this.mContext = mContext;
        this.customerList = customerList;
    }

    //取得排版資訊，建立排版元件與變數的連結
    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).
                inflate(R.layout.customer_layout,parent,false);

        return new ViewHolder(view);
    }

    //顯示對應的資料
    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {

        Customer customer = customerList.get(position);

        holder.textEmail.setText(customer.email);
        holder.textName.setText(customer.name);
        holder.textPhone.setText(customer.phone);
        holder.textPet.setText(customer.pet);
        Glide.with(mContext)
                .load(customer.uri)
                .into(holder.imageView);

    }

    //顯示ArrayList裡的資料筆數
    @Override
    public int getItemCount() {
        return customerList.size();
    }

    //建立畫面上顯示元件的變數
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textEmail,textName,textPhone,textPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textEmail = itemView.findViewById(R.id.textEmail);
            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            textPet = itemView.findViewById(R.id.textPet);
        }
    }
}
