package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.finalproject.databinding.ActivityABinding;
import com.google.firebase.auth.FirebaseAuth;

public class A extends AppCompatActivity {

    private ActivityABinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_a);

        binding = ActivityABinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("請選擇...");

        firebaseAuth = FirebaseAuth.getInstance();

        //哺乳類按鈕
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mammals = new Intent(A.this,Mammals.class);
                startActivity(mammals);
            }
        });

        //爬行類按鈕
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reptiles = new Intent(A.this,Reptiles.class);
                startActivity(reptiles);
            }
        });

        //鳥類按鈕
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent birds = new Intent(A.this,Birds.class);
                startActivity(birds);
            }
        });
    }

    //建立OptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            //登出
            case R.id.itemlogout:
                firebaseAuth.signOut();
                Intent signout = new Intent(A.this,MainActivity.class);
                startActivity(signout);
                break;

            //上一頁
            case R.id.itemPre:
                Intent itA = new Intent(A.this,One.class);
                startActivity(itA);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}