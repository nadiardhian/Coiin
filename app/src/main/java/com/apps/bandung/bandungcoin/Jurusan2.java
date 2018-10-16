package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Jurusan2 extends AppCompatActivity {

    private DatabaseReference  fDatabase;
    private TextView fNama, fJenis;

    private ListView mJurusanList;
    private ArrayList<String> mJurusan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurusan2);

        // Button Back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fNama = (TextView) findViewById(R.id.fNama);
        fJenis = (TextView) findViewById(R.id.fjenis);

        Intent i = getIntent();
        final String kampus = i.getStringExtra("nama");
        final String jenis = i.getStringExtra("fakultas");

        fNama.setText(kampus);
        fJenis.setText(jenis);

        fDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus").child(kampus).child("fakultas");

        mJurusanList = (ListView) findViewById(R.id.list_f);




        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mJurusan);

        mJurusanList.setAdapter(arrayAdapter);

        fDatabase.child(jenis).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class);

                mJurusan.add(value);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
