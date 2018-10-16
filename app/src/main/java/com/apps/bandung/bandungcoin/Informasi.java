package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Informasi extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ImageView kImage;
    private TextView kNama, kSekilas, kAlamat, kWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        // Button Back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kImage = (ImageView) findViewById(R.id.kampusImage);
        kNama = (TextView) findViewById(R.id.kampusNama);
        kSekilas = (TextView) findViewById(R.id.kampusSekilas);
        kAlamat = (TextView) findViewById(R.id.kampusAlamat);
        kWebsite = (TextView) findViewById(R.id.kampusWebsite);


        Intent i = getIntent();
        String kampus = i.getStringExtra("nama");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");


        mDatabase.child(kampus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String main_nama = (String) dataSnapshot.child("nama").getValue();
                String main_image = (String) dataSnapshot.child("gambar").getValue();
                String main_sekilas = (String) dataSnapshot.child("sekilas").getValue();
                String main_alamat = (String) dataSnapshot.child("alamat").getValue();
                String main_website = (String) dataSnapshot.child("website").getValue();


                kNama.setText(main_nama);
                Picasso.with(Informasi.this).load(main_image).into(kImage);
                kSekilas.setText(main_sekilas);
                kAlamat.setText(main_alamat);
                kWebsite.setText(main_website);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
