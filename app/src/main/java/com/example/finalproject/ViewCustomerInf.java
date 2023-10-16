package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finalproject.databinding.ActivityAdminAddInfBinding;
import com.example.finalproject.databinding.ActivityViewCustomerInfBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewCustomerInf extends AppCompatActivity {

    private ActivityViewCustomerInfBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef;
    private ArrayList<Customer> customerList;
    CustomerAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_customer_inf);

        binding = ActivityViewCustomerInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("顧客資訊");

        //recyclerView主程式
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerList = new ArrayList<>();

        customerAdapter = new CustomerAdapter(ViewCustomerInf.this,customerList);
        binding.recyclerView.setAdapter(customerAdapter);

        EventChangeListener();


    }

    private void EventChangeListener() {

        db.collection("customer")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){

                                customerList.add(dc.getDocument().toObject(Customer.class));
                            }

                            customerAdapter.notifyDataSetChanged();
                        }
                    }
                });
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
                Intent signout = new Intent(
                        ViewCustomerInf.this,MainActivity.class);

                startActivity(signout);
                break;

            case R.id.itemPre:
                Intent itPre = new Intent(ViewCustomerInf.this,Admin.class);
                startActivity(itPre);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}