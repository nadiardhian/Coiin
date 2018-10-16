package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        findViewById(R.id.button_reset).setOnClickListener(this);
        findViewById(R.id.button_kembali).setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        mAuth = FirebaseAuth.getInstance();
    }


    private void resetPassword(final String email) {

        if (email.isEmpty()) {
            editTextEmail.setError("Input Email Anda");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Input Email yang Valid");
            editTextEmail.requestFocus();
            return;
        }


        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Forget.this, "Kami telah mengirim link untuk mengubah password ke email:" + email, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Forget.this, "Gagal mengirimkan password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_reset:
                userReset();
                break;

            case R.id.button_kembali:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }

    private void userReset() {
        resetPassword(editTextEmail.getText().toString());
    }


}
