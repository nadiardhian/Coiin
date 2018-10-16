package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    private  EditText et_newPassword, et_newRePassword;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        findViewById(R.id.button_goProfile).setOnClickListener(this);
        findViewById(R.id.button_save).setOnClickListener(this);

        et_newPassword = (EditText) findViewById(R.id.newPassword);
        et_newRePassword = (EditText) findViewById(R.id.newRePassword);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                userSave();
                break;

            case R.id.button_goProfile:
                startActivity(new Intent(this, Identitas.class));
                break;

        }
    }

    private void userCek(){


    }

    private void userSave() {
        String newPassword = et_newPassword.getText().toString().trim();
        String newRePassword = et_newRePassword.getText().toString().trim();



        if(newPassword.isEmpty()) {
            et_newPassword.setError("Input New Password");
            et_newPassword.requestFocus();
            return;

        }

        if(newPassword.length()<6){
            et_newPassword.setError("Password Minimal 6 Huruf / Angka");
            et_newPassword.requestFocus();
            return;
        }

        if(newRePassword.isEmpty()) {
            et_newRePassword.setError("Input New Password anda kembali");
            et_newRePassword.requestFocus();
            return;

        }

        if(newRePassword.length()<6){
            et_newRePassword.setError("Password Minimal 6 Huruf / Angka");
            et_newRePassword.requestFocus();
            return;
        }

        if ((!newRePassword.equals(newPassword))) {
            et_newRePassword.setError("Masukkan Password yang Sama");
            et_newRePassword.requestFocus();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null){
            //dialog.setMessage("Changing Password, Mohon Tunggu");
            //dialog.show();
            user.updatePassword(et_newPassword.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent x = new Intent(ChangePassword.this, Identitas.class);
                                startActivity(x);
                                Toast.makeText(getApplicationContext(), "Berhasil Ganti Password", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ganti Password Gagal", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }
}
