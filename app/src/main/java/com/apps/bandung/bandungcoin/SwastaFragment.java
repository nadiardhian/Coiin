package com.apps.bandung.bandungcoin;


import com.apps.bandung.bandungcoin.Adapter.MainAdapter;
import com.apps.bandung.bandungcoin.Model.Main;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class SwastaFragment extends Fragment {

    //Deklarasi Variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Main> listMain;

    Query mDatabase;


    public SwastaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_swasta_fragment, container, false);

        //Inisialisasi Firebase (Auth,Database)
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.rv_swasta);
        recyclerView.setHasFixedSize(true);

        // Ambil Query dengan database child Post disusun berdasarkan idUser yang sedang login
        mDatabase = FirebaseDatabase.getInstance().getReference("Kampus").orderByChild("jenis").equalTo("swasta");
        listMain = new ArrayList<>();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMain.clear();
                //ambil post dari database child
                for (DataSnapshot posts : dataSnapshot.getChildren()){
                    Main main = posts.getValue(Main.class);
                        listMain.add(main);
                }
                //Ditampilkan ke recyclerView
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                MainAdapter mainAdapter = new MainAdapter(getContext(), listMain);
                recyclerView.setAdapter(mainAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
