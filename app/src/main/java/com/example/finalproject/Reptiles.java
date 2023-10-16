package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityBirdsBinding;
import com.example.finalproject.databinding.ActivityReptilesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Reptiles extends AppCompatActivity {

    private ActivityReptilesBinding binding;
    private StorageReference storageRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reptiles);

        binding = ActivityReptilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageRef = FirebaseStorage.getInstance().getReference();

        setTitle("爬行類");

        binding.linearRep.removeAllViews();

        //land的集合按類型排序
        db.collection("land")
                .orderBy("type")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot doc : task.getResult()){

                                String introd = doc.getString("introd");
                                String name = doc.getString("name");

                                //新增一個 imageButton 與 textView
                                ImageButton imageButton = new ImageButton(Reptiles.this);
                                TextView textView = new TextView(Reptiles.this);

                                LinearLayout.LayoutParams params =
                                        new LinearLayout.LayoutParams(600, 600);
                                imageButton.setLayoutParams(params);
                                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

                                //將資料庫儲存的 uri 匯入 imageButton 裡
                                Glide.with(Reptiles.this)
                                        .load(doc.getString("uri"))
                                        .into(imageButton);

                                binding.linearRep.addView(textView);;
                                binding.linearRep.addView(imageButton);

                                //按下 imageButton 會出現寵物的名稱與資訊
                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Toast.makeText(Reptiles.this,name,Toast.LENGTH_LONG).show();

                                        new AlertDialog.Builder(Reptiles.this)
                                                .setTitle(name)
                                                .setMessage(introd)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                    { }
                                                }).show();
                                    }
                                });

                            }

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                firebaseAuth.signOut();
                Intent signout = new Intent(Reptiles.this,MainActivity.class);
                startActivity(signout);
                break;

            case R.id.itemOne:
                Intent itA = new Intent(Reptiles.this,One.class);
                startActivity(itA);
                break;

            case R.id.itemPre:
                Intent itPre = new Intent(Reptiles.this,A.class);
                startActivity(itPre);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}