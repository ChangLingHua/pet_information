package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.finalproject.databinding.ActivityAdminAddInfBinding;
import com.example.finalproject.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Admin extends AppCompatActivity {

    private ActivityAdminBinding binding;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_admin);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("請選擇...");

        //新增寵物資訊按鈕
        binding.btnAddInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(Admin.this,AdminAddInf.class);
                startActivity(it);


            }
        });

        //顧客資訊按鈕
        binding.btnViewCsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(Admin.this,ViewCustomerInf.class);
                startActivity(it);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menulogout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.itemlogout:
                firebaseAuth.signOut();
                Intent signout = new Intent(Admin.this,MainActivity.class);
                startActivity(signout);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}