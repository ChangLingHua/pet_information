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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityAdminAddInfBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminAddInf extends AppCompatActivity {

    private ActivityAdminAddInfBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_admin_add_inf);

        binding = ActivityAdminAddInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageRef = FirebaseStorage.getInstance().getReference();

        setTitle("新增寵物資訊");


        //將資料匯入spinner裡
        String[] item = {"哺乳類＿犬","哺乳類＿貓","哺乳類＿鼠","爬行類","鳥類"};
        ArrayAdapter adapter = new ArrayAdapter(AdminAddInf.this,
                R.layout.support_simple_spinner_dropdown_item,item);

        adapter.setDropDownViewResource(R.layout.myspinner);
        binding.spinnerAdd.setAdapter(adapter);

        //spinner選項
        binding.spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0){
                    binding.editTextType.setText("dog");
                }
                else if(i == 1){
                    binding.editTextType.setText("cat");
                }
                else if(i ==2){
                    binding.editTextType.setText("mouse");
                }
                else if(i ==3){
                    binding.editTextType.setText("land");
                }
                else if(i ==4){
                    binding.editTextType.setText("bird");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //確認按鈕
        binding.fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 抓取 動應類型 資料夾裡以 輸入的名稱 為圖片的名稱的 uri
                storageRef.child(binding.editTextType.getText().toString())
                        .child(binding.editTextName.getText().toString())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //將資料儲存至 Firebase Firestore 中 動應類型 的集合裡，以 輸入的名稱 當文件的名稱
                                Map<String,Object> pet = new HashMap<>();
                                pet.put("introd",binding.editTextIntrod.getText().toString());
                                pet.put("name",binding.editTextName.getText().toString());
                                pet.put("type",binding.editTextName.getText().toString());
                                pet.put("uri",uri.toString());

                                db.collection(binding.editTextType.getText().toString())
                                        .document(binding.editTextName.getText().toString())
                                        .set(pet)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AdminAddInf.this,
                                                        "資料上傳成功",Toast.LENGTH_LONG).show();

                                                binding.editTextName.setText("");
                                                binding.editTextIntrod.setText("");
                                                binding.btnAddPhoto.setImageResource(R.drawable.icons_photo);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminAddInf.this,e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        });


            }
        });

        //刪除按鈕
        binding.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storageRef.child(binding.editTextType.getText().toString())
                        .child(binding.editTextName.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminAddInf.this, "刪除成功",
                                        Toast.LENGTH_SHORT).show();

                                binding.btnAddPhoto.setImageResource(R.drawable.icons_photo);



                            }
                        });

                //資料庫裡的資料都會刪除，欄位也都會清空
                db.collection(binding.editTextType.getText().toString())
                        .document(binding.editTextName.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                binding.editTextName.setText("");
                                binding.editTextIntrod.setText("");

                            }
                        });

            }
        });

        //上傳圖片按鈕
        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                startActivityForResult(picker,1);
            }
        });


    }

    //將圖片上傳至 Storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1){

            Uri uri = data.getData();

            Glide.with(AdminAddInf.this)
                    .load(uri)
                    .into(binding.btnAddPhoto);

            String type = binding.editTextType.getText().toString();
            String name = binding.editTextName.getText().toString();

            storageRef.child(type).child(name)
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminAddInf.this, "照片上傳成功",
                                    Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminAddInf.this,e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
                Intent signout = new Intent(AdminAddInf.this,MainActivity.class);
                startActivity(signout);
                break;

            case R.id.itemPre:
                Intent itPre = new Intent(AdminAddInf.this,Admin.class);
                startActivity(itPre);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}