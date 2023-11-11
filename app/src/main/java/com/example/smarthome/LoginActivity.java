package com.example.smarthome;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class LoginActivity extends Activity {
    EditText ETUsername, ETPassword;
    String ValueUsername, ValuePassword;
    Firebase FbUsername, FbPassword;

    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Firebase.setAndroidContext(this);

        FbUsername = new Firebase("https://smarthomezonaphoto-default-rtdb.firebaseio.com/Login/Username");
        FbUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ValueUsername = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: " + ValueUsername);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        FbPassword = new Firebase("https://smarthomezonaphoto-default-rtdb.firebaseio.com/Login/Password");
        FbPassword.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ValuePassword = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: " + ValuePassword);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        ETUsername = findViewById(R.id.username);
        ETPassword = findViewById(R.id.password);

        btnlogin = findViewById(R.id.btn_1);
        btnlogin.setOnClickListener(v -> {
            if(ETUsername.getText().toString().equals(ValueUsername)){
                if(ETPassword.getText().toString().equals(ValuePassword)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(LoginActivity.this, "Password Salah, Periksa kembali...", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(LoginActivity.this, "Username Salah, Periksa kembali...", Toast.LENGTH_SHORT).show();
        });
    }
}