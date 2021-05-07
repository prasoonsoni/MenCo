package com.heavycoders.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class VolunteerActivity extends AppCompatActivity {
    Toolbar toolbar2;
    EditText yourName, yourLocation, yourNumber;
    RadioButton oxygenButton, plasmaButton, bedButton, foodButton;
    Button submitButton;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        mAuth = FirebaseAuth.getInstance();

        toolbar2 = findViewById(R.id.toolbar2);
        yourName = findViewById(R.id.yourName);
        yourLocation = findViewById(R.id.yourLocation);
        yourName = findViewById(R.id.yourName);
        yourNumber = findViewById(R.id.yourNumber);
        oxygenButton = findViewById(R.id.oxygenButton);
        plasmaButton = findViewById(R.id.plasmaButton);
        bedButton = findViewById(R.id.bedButton);
        foodButton = findViewById(R.id.foodButton);
        submitButton = findViewById(R.id.submitButton);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Please Wait");
        pd.setMessage("Submitting your details...");
        pd.setCancelable(false);

        setSupportActionBar(toolbar2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);

        Bundle bd = getIntent().getExtras();
        String state = bd.getString("state");
        String city = bd.getString("city");
        String name = bd.getString("name");

        yourName.setText(name);
        yourLocation.setText(city+", "+state);

        submitButton.setOnClickListener(v -> {
            if(name.isEmpty() || city.isEmpty() || state.isEmpty() || yourNumber.getText().toString().isEmpty() || (
            !oxygenButton.isChecked() && !plasmaButton.isChecked() && !bedButton.isChecked() && !foodButton.isChecked())){
                Toast.makeText(this, "Fields cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                pd.show();
                String category = "";
                if(oxygenButton.isChecked()){
                    category = "oxygensupply";
                } else if(plasmaButton.isChecked()){
                    category = "plasmaassistance";
                } else if(bedButton.isChecked()){
                    category = "hospitalbed";
                } else if(foodButton.isChecked()){
                    category = "foodservice";
                }
                //Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
                String user = mAuth.getCurrentUser().getUid();
                DatabaseReference storeValue = FirebaseDatabase.getInstance().getReference()
                        .child(category)
                        .child(state)
                        .child(city)
                        .child(user);
                HashMap<String, String> info = new HashMap<>();
                info.put("name", name);
                info.put("city", city);
                info.put("state", state);
                info.put("number", yourNumber.getText().toString());
                storeValue.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VolunteerActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        startActivity(new Intent(VolunteerActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        });


    }
}