package com.apps.bandung.bandungcoin;

import com.apps.bandung.bandungcoin.Adapter.TestiAdapter;
import com.apps.bandung.bandungcoin.Model.TModel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Rifqy on 24/04/2018.
 */

public class TestTestimoni extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<TModel> listTModel = new ArrayList<>();
    private DatabaseReference t1Database;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimoni);

        recyclerView = (RecyclerView) findViewById(R.id.rv_testi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestTestimoni.this));

        t1Database = FirebaseDatabase.getInstance().getReference().child("Testimoni");

        t1Database.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTModel.clear();
                //ambil post dari database child
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TModel tmodel = postSnapshot.getValue(TModel.class);
                    listTModel.add(tmodel);
                }

                //Ditampilkan ke recyclerView
                TestiAdapter testiAdapter = new TestiAdapter(TestTestimoni.this, listTModel);
                recyclerView.setAdapter(testiAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
