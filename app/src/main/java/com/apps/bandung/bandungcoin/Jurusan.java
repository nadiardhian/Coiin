package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Jurusan extends AppCompatActivity {

    private DatabaseReference jDatabase, mDatabase;
    private TextView kNama;

    private ListView mJurusanList;
    private ArrayList<String> mJurusan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurusan);

        Intent i = getIntent();
        String kampus = i.getStringExtra("nama");

        // Button Back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kNama = (TextView) findViewById(R.id.kampusNama);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");

        jDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus").child(kampus).child("ListF");
        mJurusanList = (ListView) findViewById(R.id.list_jurusan);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mJurusan);

        mJurusanList.setAdapter(arrayAdapter);

        jDatabase.addChildEventListener(new ChildEventListener() {
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

        mJurusanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String fNama = kNama.getText().toString();

                Intent i = new Intent(Jurusan.this, Jurusan2.class);
                i.putExtra("nama",fNama);
                i.putExtra("fakultas", mJurusanList.getItemAtPosition(position).toString());
                startActivity(i);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");


        mDatabase.child(kampus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String main_nama = (String) dataSnapshot.child("nama").getValue();

                kNama.setText(main_nama);

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
