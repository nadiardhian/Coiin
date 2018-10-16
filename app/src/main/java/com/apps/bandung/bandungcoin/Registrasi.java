package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registrasi extends AppCompatActivity implements View.OnClickListener{


    private EditText editTextEmail, editTextPassword, editTextRePassword, editTextNama;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_gologin).setOnClickListener(this);
        findViewById(R.id.button_regis).setOnClickListener(this);


    }



    private void userRegistrasi() {
        final String nama = editTextNama.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repassword = editTextRePassword.getText().toString().trim();


        if (email.isEmpty()) {
            editTextNama.setError("Input Nama Anda");
            editTextNama.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Input Email Anda");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Input Email yang Valid");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Input Password Anda");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password Minimal 6 Huruf / Angka");
            editTextPassword.requestFocus();
            return;
        }

        if (repassword.isEmpty()) {
            editTextRePassword.setError("Input Password Anda Kembali");
            editTextRePassword.requestFocus();
            return;
        }

        if ((!repassword.equals(password))) {
            editTextRePassword.setError("Masukkan Password yang Sama");
            editTextRePassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String > userMap = new HashMap<>();
                    userMap.put("nama", nama);
                    userMap.put("image", "default");
                    userMap.put("email", email);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registrasi.this, Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Email Telah Digunakan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Terdapat Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_regis:
                userRegistrasi();
                break;

            case R.id.button_gologin:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }
}

