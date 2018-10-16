package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Profile(View view) {
        finish();
        Intent intent = new Intent(this, Identitas.class);
        startActivity(intent);

    }

    public void Announcement(View view) {
        Intent intent = new Intent(this, Announcement.class);
        startActivity(intent);

    }

    public void Main(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void userLogOut(View view) {

        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }



}
