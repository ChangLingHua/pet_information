package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.finalproject.databinding.ActivityOneBinding;
import com.google.firebase.auth.FirebaseAuth;

public class One extends AppCompatActivity {

    private ActivityOneBinding binding;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);


        binding = ActivityOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        setTitle(firebaseAuth.getCurrentUser().getEmail() + "，您好");

        //寵物資訊的按鈕
        binding.btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent type = new Intent(One.this,A.class);
                startActivity(type);
            }
        });

        //寵物醫院地圖的按鈕
        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent map = new Intent(One.this,MapsActivity.class);
                startActivity(map);
            }
        });

        //寵物認領登記的按鈕
        binding.btnInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent inf = new Intent(One.this,Addpet.class);
                startActivity(inf);
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
                Intent signout = new Intent(One.this,MainActivity.class);
                startActivity(signout);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}