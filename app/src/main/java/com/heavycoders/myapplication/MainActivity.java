package com.heavycoders.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private FirebaseAuth mAuth;
    private TextView greeting, userName, slogan;
    private DatabaseReference db;
    private CardView volunteer, resources, recommendation;
    private EditText stateName;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        greeting = findViewById(R.id.greeting);
        userName = findViewById(R.id.userName);
        slogan = findViewById(R.id.slogan);
        stateName = findViewById(R.id.stateName);
        volunteer = findViewById(R.id.volunteer);
        resources = findViewById(R.id.resources);
        recommendation = findViewById(R.id.recommendation);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);


        volunteer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VolunteerActivity.class);
            intent.putExtra("state", stateName.getText().toString());
            startActivity(intent);
        });

        resources.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NeedyActivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            Date date = new Date();
            int time = date.getHours();
            if(time>=0 && time<=12){
                greeting.setText("Good Morning,");
            } else if(time>12 && time<=16){
                greeting.setText("Good Afternoon,");
            } else if(time>16 && time<=20){
                greeting.setText("Good Evening,");
            } else if(time>20 && time<=24){
                greeting.setText("Good Night,");
            }
            Task<DataSnapshot> name = db.child("users").child(mAuth.getCurrentUser().getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        userName.setText(task.getResult().getValue().toString());
                    }
                }
            });
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.i("lat", latitude+ "");
        Log.i("long", longitude+"");
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List <Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            Log.i("state", addresses.get(0).getAdminArea().toString());
            Log.i("city", addresses.get(0).getLocality());
            stateName.setText(addresses.get(0).getAdminArea());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}