package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.linearSend.setVisibility(View.GONE);

        setTitle("請登入");

        firebaseAuth = FirebaseAuth.getInstance();

        // 如果使用者登入後，會記住email並顯示在editTextEmail欄位
        if(firebaseAuth.getCurrentUser() != null){

            binding.editTextEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        }

        // 登入按鈕
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 如果帳號或密碼其中一個欄位是空值，會顯示"帳號或密碼不得為空"的訊息
                if(binding.editTextEmail.getText().toString().isEmpty() ||
                        binding.editTextPassword.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"帳號或密碼不得為空",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    String email = binding.editTextEmail.getText().toString();
                    String password = binding.editTextPassword.getText().toString();

                    //如果完成認證後，會顯示"登入成功"、"完成認證"的訊息
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                            Toast.makeText(MainActivity.this, "登入成功：" +
                                                            task.getResult().getUser().getEmail(),
                                                    Toast.LENGTH_SHORT).show();

                                            Toast.makeText(MainActivity.this, "完成認證",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent one = new Intent(MainActivity.this,One.class);
                                            startActivity(one);
                                        }
                                        //不然就會請使用者進行認證
                                        else {
                                            Toast.makeText(MainActivity.this, "請完成認證",
                                                    Toast.LENGTH_SHORT).show();

                                            binding.linearSend.setVisibility(View.VISIBLE);

                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            String emailvef = user.getEmail();
                                            binding.textViewCheck.setText("請至" + emailvef +
                                                    "信箱中接收認證信，並點選連以完成認證");
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(MainActivity.this, "登入失敗：" +
                                            e.getMessage(),Toast.LENGTH_SHORT).show();

                                    firebaseAuth.signOut();
                                }
                            });
                }
            }
        });

        //管理者的登入按鈕
        binding.btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 如果帳號或密碼其中一個欄位是空值，會顯示"帳號或密碼不得為空"的訊息
                if(binding.editTextEmail.getText().toString().isEmpty() ||
                        binding.editTextPassword.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"帳號或密碼不得為空",
                            Toast.LENGTH_SHORT).show();}

                //透過email檢查是否為管理者
                if(binding.editTextEmail.getText().toString().equals("admin@gmail.com")){

                    Toast.makeText(MainActivity.this, "管理者登入成功",
                            Toast.LENGTH_SHORT).show();

                    Intent admin = new Intent(MainActivity.this,Admin.class);
                    startActivity(admin);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "您不是管理者",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        //註冊按鈕
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 如果帳號或密碼其中一個欄位是空值，會顯示"帳號或密碼不得為空"的訊息
                if(binding.editTextEmail.getText().toString().isEmpty() ||
                        binding.editTextPassword.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"帳號或密碼不得為空",
                            Toast.LENGTH_SHORT).show();
                }

                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                //檢查使用者的email是否有註冊過
                if(firebaseAuth.getUid() == null){

                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Toast.makeText(MainActivity.this, "註冊成功" ,
                                            Toast.LENGTH_SHORT).show();

                                    send();
                                }
                            });
                }else {

                    Toast.makeText(MainActivity.this, "此帳號已使用過" ,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        //重新寄送認證信按鈕
        binding.btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String emailvef = user.getEmail();


                firebaseAuth.getCurrentUser().sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.textViewCheck.setText("請至" + emailvef +
                                        "信箱中接收認證信，並點選連以完成認證");
                            }
                        });
            }
        });
    }

    //寄送認證信至使用者的信箱內
    void send(){

        firebaseAuth.getCurrentUser().sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(MainActivity.this,"已送出email",
                                Toast.LENGTH_SHORT).show();

                        binding.linearSend.setVisibility(View.VISIBLE);

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String emailvef = user.getEmail();
                        binding.textViewCheck.setText("請至" + emailvef +
                                "信箱中接收認證信，並點選連以完成認證");
                    }
                });
    }



}