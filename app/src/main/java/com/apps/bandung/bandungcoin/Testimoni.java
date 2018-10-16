package com.apps.bandung.bandungcoin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.apps.bandung.bandungcoin.Adapter.TestiAdapter;
import com.apps.bandung.bandungcoin.Model.TModel;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Testimoni extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase, mUserDatabase, t2Database;
    private Query t1Database;
    private TextView kNama, kSekilas, kAlamat, kWebsite;
    private EditText tDeskripsi;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    private Dialog kDialog;
    private Button kKirim, kCancel;
    private EditText kKomentar;


    private EditText editTextNama, editTextImage;
    private ImageView imageProfile;
    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;


    private ArrayList<TModel> listTModel = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimoni);

        kNama = (TextView) findViewById(R.id.kampusNama);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextImage = (EditText) findViewById(R.id.editTextImage);

        // Button Back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.t_save).setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_testi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Testimoni.this));

        Intent i = getIntent();
        String kampus = i.getStringExtra("nama");

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");
        t1Database = FirebaseDatabase.getInstance().getReference().child("Komentar");



        // Untuk mengambil data dari kampus yang telah dipilih
        // Data yang diambil adalah data dari nama kampus yang dipilih sebelumnya
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


        t1Database.orderByChild("tkampus").equalTo(kampus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTModel.clear();
                //ambil post dari database child
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TModel tmodel = postSnapshot.getValue(TModel.class);
                    listTModel.add(tmodel);
                }

                //Ditampilkan ke recyclerView
                TestiAdapter testiAdapter = new TestiAdapter(Testimoni.this,listTModel);
                recyclerView.setAdapter(testiAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(mAuth.getCurrentUser()!=null) {
            showData();
        }

    }

    private void showData(){

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nama = dataSnapshot.child("nama").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                editTextNama.setText(nama);
                editTextImage.setText(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inputTestimoni(){

        String postUserNama = editTextNama.getText().toString();
        String postDeskripsi = tDeskripsi.getText().toString();
        String postKampus = kNama.getText().toString();
        String postFoto = editTextImage.getText().toString();
        long timestamp = System.currentTimeMillis();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        if(!postDeskripsi.isEmpty()) {
            t2Database = FirebaseDatabase.getInstance().getReference().child("Komentar").push();
            HashMap<String, Object> userMap3 = new HashMap<>();
            userMap3.put("uid", uid);
            userMap3.put("tnama", postUserNama);
            userMap3.put("tdeskripsi", postDeskripsi);
            userMap3.put("tkampus", postKampus);
            userMap3.put("tfoto", postFoto);
            userMap3.put("timestamp", timestamp);
            t2Database.setValue(userMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Testimoni.this, "Input Berhasil", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void cekUser(){
        if(mAuth.getCurrentUser()==null) {
            AlertDialog.Builder alertLogin = new AlertDialog.Builder(this);
            alertLogin.setMessage("Anda harus login terlebih dahulu")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Tidak bisa membuat komentar")
                    .create();
            alertLogin.show();
        } else{
            KomentarDialog();
        }
    }

    private void KomentarDialog(){
        kDialog = new Dialog(Testimoni.this);
        kDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        kDialog.setContentView(R.layout.komentardialog);

        kKomentar = (EditText)kDialog.findViewById(R.id.kKomentar);
        kKirim = (Button) kDialog.findViewById(R.id.kKirim);
        kCancel = (Button)kDialog.findViewById(R.id.kCancel);

        String komen = kKomentar.getText().toString();





        kKirim.setEnabled(true);
        kCancel.setEnabled(true);


        kKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postUserNama = editTextNama.getText().toString();
                String postDeskripsi = kKomentar.getText().toString();
                String postKampus = kNama.getText().toString();
                String postFoto = editTextImage.getText().toString();
                long timestamp = System.currentTimeMillis();

                if (postDeskripsi.isEmpty()) {
                    kKomentar.setError("Masukkan komentar anda");
                    kKomentar.requestFocus();
                    return;
                }

                if(postDeskripsi.length()<5){
                    kKomentar.setError("Komentar minimal 5 huruf / angka ");
                    kKomentar.requestFocus();
                    return;
                }

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();

                if(!postDeskripsi.isEmpty()) {
                    t2Database = FirebaseDatabase.getInstance().getReference().child("Komentar").push();
                    HashMap<String, Object> userMap3 = new HashMap<>();
                    userMap3.put("uid", uid);
                    userMap3.put("tnama", postUserNama);
                    userMap3.put("tdeskripsi", postDeskripsi);
                    userMap3.put("tkampus", postKampus);
                    userMap3.put("tfoto", postFoto);
                    userMap3.put("timestamp", timestamp);
                    t2Database.setValue(userMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Testimoni.this, "Input Berhasil", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                kDialog.dismiss();
            }
        });

        kCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kDialog.cancel();
            }
        });

        kDialog.show();
    }




    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t_save:
                cekUser();

                break;


        }
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
