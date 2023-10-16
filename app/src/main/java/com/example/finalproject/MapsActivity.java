package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalproject.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //將資料匯入spinner裡
        String[] item = {"永康區","安平區","中西區","南區"};
        ArrayAdapter adapter = new ArrayAdapter(MapsActivity.this,
                R.layout.support_simple_spinner_dropdown_item,item);
        adapter.setDropDownViewResource(R.layout.myspinner);
        binding.spinner.setAdapter(adapter);

        //回首頁的按鈕
        binding.buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MapsActivity.this,One.class);
                startActivity(it);
            }
        });

        //spinner選項
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i ==0)
                    region();
                else if(i == 1)
                    region();
                else if(i == 2)
                    region();
                else if(i == 3)
                    region();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //根據 type 分類
    void region(){
        String name = binding.spinner.getSelectedItem().toString();

        mMap.clear();

        db.collection("region")
                .whereEqualTo("type",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            LatLng lastLatLng = null;

                            for (QueryDocumentSnapshot doc:task.getResult()){

                                GeoPoint gps = doc.getGeoPoint("GPS");
                                String title = doc.getString("title");
                                String snippet = doc.getString("snippet");
                                LatLng Attractions =
                                         new LatLng(gps.getLatitude(),gps.getLongitude());
                                mMap.addMarker(
                                        new MarkerOptions().position(Attractions).title(title).snippet(snippet));

                                lastLatLng = Attractions;
                            }

                            CameraPosition cameraPosition =
                                    new CameraPosition.Builder().target(lastLatLng).zoom(14).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.getUiSettings().setZoomControlsEnabled(true);

                        }

                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}