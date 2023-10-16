package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityAddpetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Addpet extends AppCompatActivity {

    private ActivityAddpetBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addpet);

        binding = ActivityAddpetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageRef = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        String email = firebaseAuth.getCurrentUser().getEmail();

        setTitle("寵物認領登記");

        //確認按鈕
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 抓取 customer 資料夾裡以 email 為圖片的名稱的 uri
                storageRef.child("customer")
                        .child(firebaseAuth.getCurrentUser().getEmail())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //將資料儲存至 Firebase Firestore 中 customer 的集合裡，以 email 當文件的名稱
                                Map<String ,String> customer = new HashMap<>();
                                customer.put("name",binding.editTextName.getText().toString());
                                customer.put("phone",binding.editTextPhone.getText().toString());
                                customer.put("pet",binding.editTextPet.getText().toString());
                                customer.put("email",email);
                                customer.put("uri",uri.toString());

                                db.collection("customer")
                                        .document(email)
                                        .set(customer)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(Addpet.this,
                                                        "資料傳送成功！若有可養寵物資訊！將會傳送訊息給您。",
                                                        Toast.LENGTH_SHORT).show();

                                                binding.editTextName.setText(binding.editTextName.getText().toString());
                                                binding.editTextPhone.setText(binding.editTextPhone.getText().toString());
                                                binding.editTextPet.setText(binding.editTextPet.getText().toString());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(Addpet.this,
                                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

            }
        });

        //上傳圖片按鈕
        binding.imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                startActivityForResult(picker,1);
            }
        });

        //刪除按鈕
        binding.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //storage裡動應的照片會刪除
                storageRef.child("customer").child(firebaseAuth.getCurrentUser().getEmail())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(Addpet.this, "刪除成功", Toast.LENGTH_SHORT).show();

                                binding.imageViewCamera.setImageResource(R.drawable.icons_photo);



                            }
                        });

                //資料庫裡的資料都會刪除，欄位也都會清空
                db.collection("customer")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                binding.editTextName.setText("");
                                binding.editTextPhone.setText("");
                                binding.editTextPet.setText("");
                            }
                        });

            }
        });



    }

    //將圖片上傳至 Storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1){

            Uri uri = data.getData();
            Glide.with(Addpet.this)
                    .load(uri)
                    .into(binding.imageViewCamera);

            storageRef.child("customer").child(firebaseAuth.getCurrentUser().getEmail())
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Addpet.this, "圖片上傳成功", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Addpet.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

            switch (id) {

                case R.id.itemlogout:
                    firebaseAuth.signOut();
                    Intent out = new Intent(Addpet.this,MainActivity.class);
                    startActivity(out);
                    break;

                case R.id.itemPre:
                    Intent itA = new Intent(Addpet.this,One.class);
                    startActivity(itA);
                    break;

            }
        return super.onOptionsItemSelected(item);
    }

    //儲存資料至onPause
    @Override
    protected void onPause() {
        super.onPause();

        storageRef.child("customer")
                .child(firebaseAuth.getCurrentUser().getEmail())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Map<String ,String> customer = new HashMap<>();
                        customer.put("name",binding.editTextName.getText().toString());
                        customer.put("phone",binding.editTextPhone.getText().toString());
                        customer.put("pet",binding.editTextPet.getText().toString());
                        customer.put("email",firebaseAuth.getCurrentUser().getEmail());
                        customer.put("uri",uri.toString());

                        db.collection("customer")
                                .document(firebaseAuth.getCurrentUser().getEmail().toString())
                                .set(customer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.editTextName.setText("name");
                                        binding.editTextPhone.setText("phone");
                                        binding.editTextPet.setText("pet");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(Addpet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });




    }

    //顯示資料
    @Override
    protected void onResume() {
        Addpet.super.onResume();

        db.collection("customer")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();

                            String name = doc.getString("name");
                            String phone = doc.getString("phone");
                            String pet = doc.getString("pet");

                            binding.editTextName.setText(name);
                            binding.editTextPhone.setText(phone);
                            binding.editTextPet.setText(pet);
                        }
                    }
                });

        storageRef.child("customer").child(firebaseAuth.getCurrentUser().getEmail())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(Addpet.this)
                                .load(uri)
                                .into(binding.imageViewCamera);

                    }
                });


    }
}