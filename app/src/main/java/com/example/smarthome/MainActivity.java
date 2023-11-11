package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnLogout;
    TextView TVLampu1, TVLampu2, TVPagar, TVMode, TVLPStudio, TVLPDepan;
    Firebase FbLampu1, FbPagar, FbLampu2, FbMode, FbLampuDepan, FbLampuStudio;
    SwitchCompat SwLampu1, SwLampu2, SwPagar, SwMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        Firebase.setAndroidContext(this);


        DatabaseReference DBModeLampu1 = database.getReference("LampuDepan");
        TVLampu1 = findViewById(R.id.tv_lampu1);
        SwLampu1 = findViewById(R.id.sw_lampu1);
        SwLampu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwLampu1.isChecked()) {
//                    TVLampu1.setText(R.string.switch_on);
                    DBModeLampu1.setValue(0);
                } else {
//                    TVLampu1.setText(R.string.switch_off);
                    DBModeLampu1.setValue(1);
                }
            }
        });

        DatabaseReference DBModeLampu2 = database.getReference("LampuStudio");
        TVLampu2 = findViewById(R.id.tv_lampu2);
        SwLampu2 = findViewById(R.id.sw_lampu2);
        SwLampu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwLampu2.isChecked()) {
//                    TVLampu2.setText(R.string.switch_on);
                    DBModeLampu2.setValue(0);
                } else {
//                    TVLampu2.setText(R.string.switch_off);
                    DBModeLampu2.setValue(1);
                }
            }
        });

        DatabaseReference DBModeKipas = database.getReference("Pagar");
        TVPagar = findViewById(R.id.tv_pagar);
        SwPagar = findViewById(R.id.sw_pagar);
        SwPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwPagar.isChecked()) {
                    TVPagar.setText(R.string.on);
                    DBModeKipas.setValue(180);
                } else {
                    TVPagar.setText(R.string.off);
                    DBModeKipas.setValue(0);
                }
            }
        });

        TVLPDepan = findViewById(R.id.lampu1);
        FbLampuDepan = new Firebase("https://smarthomezonaphoto-default-rtdb.firebaseio.com/LPDepan");
        FbLampuDepan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TVLPDepan.setText(getString(R.string.Mode, value));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        TVLPStudio = findViewById(R.id.lampu2);
        FbLampuStudio = new Firebase("https://smarthomezonaphoto-default-rtdb.firebaseio.com/LPStudio");
        FbLampuStudio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TVLPStudio.setText(getString(R.string.Mode, value));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Mode Otomatis
        DatabaseReference DBModeMode = database.getReference("Mode");
        SwMode = findViewById(R.id.mode);
        SwMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwMode.isChecked()) {
                    SwLampu1.setVisibility(View.INVISIBLE);
                    SwLampu2.setVisibility(View.INVISIBLE);
                    TVLPDepan.setVisibility(View.VISIBLE);
                    TVLPStudio.setVisibility(View.VISIBLE);
//                    TVLampu1.setText(R.string.Otomatis);
//                    TVLampu2.setText(R.string.Otomatis);
                    Toast.makeText(MainActivity.this, "Mode Otomatis Aktif", Toast.LENGTH_SHORT).show();
                    DBModeMode.setValue(0);
                } else {
                    SwLampu1.setVisibility(View.VISIBLE);
                    SwLampu2.setVisibility(View.VISIBLE);
                    TVLPDepan.setVisibility(View.INVISIBLE);
                    TVLPStudio.setVisibility(View.INVISIBLE);
//                    TVLampu1.setText(R.string.Manual);
//                    TVLampu2.setText(R.string.Manual);
                    Toast.makeText(MainActivity.this, "Mode Manual Aktif", Toast.LENGTH_SHORT).show();
                    DBModeMode.setValue(1);
                }
            }
        });



        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}