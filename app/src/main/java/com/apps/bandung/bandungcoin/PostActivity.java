package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class PostActivity extends AppCompatActivity {
    private ArrayList<String> items = new ArrayList<>();
    private SpinnerDialog spinnerDialog;
    private Button btnPilih, btnSumbmit;
    private TextView mKampus, mImages;
    private EditText mDeskripsi;
    private ImageView mLogo;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        mKampus = (TextView) findViewById(R.id.tv_kampus);
        mDeskripsi = (EditText) findViewById(R.id.et_deskripsi);
        mLogo = (ImageView) findViewById(R.id.img_logo);
        mImages = (TextView) findViewById(R.id.tv_images);


        btnSumbmit = (Button) findViewById(R.id.button_submit);

        DaftarItem();
        spinnerDialog = new SpinnerDialog(PostActivity.this, items, "Pilih Kampus");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                mKampus.setText(item);
                if (item.equals("Telkom University")) {
                    mLogo.setImageResource(R.drawable.logotelkom);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/profile_images%2Flogotelkom.png?alt=media&token=1908c917-6b12-44b8-b81a-75b558b7d4ae");
                } else if (item.equals("Universitas Padjajaran")) {
                    mLogo.setImageResource(R.drawable.logounpad);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/profile_images%2Flogounpad.png?alt=media&token=cf5af911-cb24-4350-9dda-7eab539a1746");
                } else if (item.equals("Institut Teknologi Bandung")) {
                    mLogo.setImageResource(R.drawable.logoitb);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_Institut%20Teknologi%20Bandung.png?alt=media&token=d48ef792-abf8-459f-b312-02e1c11d3f35");
                } else if (item.equals("Universitas Pendidikan Indonesia")) {
                    mLogo.setImageResource(R.drawable.logoupi);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_Universitas%20Pendidikan%20Indonesia.png?alt=media&token=da4fd6f4-4500-416b-b20a-6fc60adac905");
                } else if (item.equals("LP3I")) {
                    mLogo.setImageResource(R.drawable.logolp3i);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2FLogo_LP3I.png?alt=media&token=9a63c62d-1d6d-4a06-87e0-bb30fc1c97fe");
                } else if (item.equals("Politeknik Ganesha Bandung")) {
                    mLogo.setImageResource(R.drawable.logoganesha);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Fkampus_ganesha.jpg?alt=media&token=63d3c96b-cc98-4dac-879f-afdf783ca132");
                } else if (item.equals("Politeknik Negeri Bandung")) {
                    mLogo.setImageResource(R.drawable.logopoltekbandung);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Fkampus_Politeknik%20Negeri%20Bandung.jpg?alt=media&token=10127e5b-b057-4950-ae0f-7fd15332db78");
                } else if (item.equals("Politeknik Pos Indonesia")) {
                    mLogo.setImageResource(R.drawable.logopoliteknikposindonesia);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_Politeknik%20Pos%20Indonesia.png?alt=media&token=f481ccb8-7f10-4c53-9cff-e1aa5af3fa45");
                } else if (item.equals("Politeknik STTT Bandung")) {
                    mLogo.setImageResource(R.drawable.logo_politeknikstttbandung);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo%20sttt.png?alt=media&token=243d742b-446e-4fa5-97ad-b81fdf74b48d");
                } else if (item.equals("ST Pariwisata Bandung")) {
                    mLogo.setImageResource(R.drawable.logo_stpariwisata);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_ST%20Pariwisata.jpg?alt=media&token=3510f805-7169-4315-aff5-b3486073c93b");
                } else if (item.equals("Sekolah Tinggi Musik Bandung")) {
                    mLogo.setImageResource(R.drawable.logostinggimusik);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_tinggimusik.jpg?alt=media&token=5955031b-f02e-409c-a36a-bd4b4c664516");
                } else if (item.equals("Universitas Bandung Raya")) {
                    mLogo.setImageResource(R.drawable.logouniversitasbandungraya);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo-Universitas-Bandung-Raya.png?alt=media&token=5982017b-c7bd-40dd-9978-9ecde930badc");
                } else if (item.equals("Universitas Islam Bandung")) {
                    mLogo.setImageResource(R.drawable.logouniversitasislambandung);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo%20Universitas%20Islam%20Bandung.png?alt=media&token=8657294a-2814-4191-9f90-7a53c7d21c63");
                } else if (item.equals("Universitas Islam Negeri")) {
                    mLogo.setImageResource(R.drawable.logouin);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogouin.png?alt=media&token=32476b46-0456-48fa-a522-5b9c8822a830");
                } else if (item.equals("Universitas Jenderal Achmad Yani")) {
                    mLogo.setImageResource(R.drawable.logojendralachmadyani);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/profile_images%2Fjendralachmadyani.png?alt=media&token=3be80f25-441b-4086-b536-63609d9641d8");
                } else if (item.equals("Universitas Komputer Indonesia")) {
                    mLogo.setImageResource(R.drawable.logounikom);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_unikom_kuning.png?alt=media&token=2fc2a5e3-69b5-4db2-b366-57a6df4deefc");
                } else if (item.equals("Universitas Pasundan")) {
                    mLogo.setImageResource(R.drawable.logounpas);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo-universitas-pasundan.png?alt=media&token=0e438dac-e378-41a4-9a00-d016dc3d6a61");
                } else if (item.equals("Universitas Widyatama")) {
                    mLogo.setImageResource(R.drawable.logowidyatama);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo%20WidyatamaBandung.png?alt=media&token=c2d79016-c6fe-4206-a39c-bd9f407791a8");
                } else if (item.equals("Politeknik Manufaktur Bandung")) {
                    mLogo.setImageResource(R.drawable.logopoltekmanufaktur);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Fkampus-Politeknik%20Manufaktur.jpg?alt=media&token=5d003fe2-12f6-4155-9335-3c215817b2a4");
                } else if (item.equals("Institut Seni Budaya Bandung")) {
                    mLogo.setImageResource(R.drawable.logoisbi);
                    mImages.setText("https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Flogo_Institut%20Seni%20Budaya%20Bandung.png?alt=media&token=456c30a9-5ec3-437c-813a-ca1ecc115afd");



                }else{
                    mLogo.setImageResource(R.drawable.logo);
                    mImages.setText("R.drawable.logo");
                }
            }
        });

        btnPilih = (Button) findViewById(R.id.btn_pilih);
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });


        btnSumbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();

            }
        });
    }


    private void startPosting() {


        String nama_kampus = mKampus.getText().toString();
        String desc_news = mDeskripsi.getText().toString();
        String image_post = mImages.getText().toString();
        String time = String.valueOf(ServerValue.TIMESTAMP);


        long timestamp = System.currentTimeMillis();


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();


        String current_user_id = mCurrentUser.getUid();


        if(!TextUtils.isEmpty(desc_news) && !nama_kampus.equals("Kampus")){
            progressBar.setVisibility(View.VISIBLE);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Post").child(uid + timestamp);

            /*DatabaseReference newPost = mDatabase.push();

            newPost.child("title").setValue(nama_kampus);
            newPost.child("desc").setValue(desc_news);
            newPost.child("image").setValue(image_post);
            newPost.child("timestamp").setValue(timestamp);*/

            HashMap<String, Object > userMap2 = new HashMap<>();
            userMap2.put("nama", nama_kampus);
            userMap2.put("deskripsi", desc_news);
            userMap2.put("image", image_post);
            userMap2.put("timestamp", timestamp*(-1));

            mDatabase.setValue(userMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PostActivity.this, "Upload Berhasil", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostActivity.this, Announcement.class));
                        finish();
                    }
                }
            });



        }
    }

    private void DaftarItem() {
        items.add("Telkom University");
        items.add("Universitas Padjajaran");
        items.add("Institut Teknologi Bandung");
        items.add("Universitas Pendidikan Indonesia");
        items.add("Politeknik Manufaktur Bandung");
        items.add("LP3I");
        items.add("Politeknik Ganesha Bandung ");
        items.add("Politeknik Negeri Bandung");
        items.add("Politeknik Pos Indonesia");
        items.add("Politeknik STTT Bandung");
        items.add("ST Pariwisata Bandung");
        items.add("Sekolah Tinggi Musik Bandung");
        items.add("Universitas Bandung Raya");
        items.add("Universitas Islam Bandung");
        items.add("Universitas Islam Negeri");
        items.add("Universitas Jenderal Achmad Yani");
        items.add("Universitas Komputer Indonesia");
        items.add("Universitas Pasundan");
        items.add("Universitas Widyatama");
        items.add("Institut Seni Budaya Bandung");


    }
    /*private void logo() {
        if (items.equals("Universitas Telkom")) {
            mLogo.setImageResource(R.drawable.logotelkom);
        } else if (items.equals("Universitas Padjajaran")) {
            mLogo.setImageResource(R.drawable.logounpad);
        } else if (items.equals("Institut Teknologi Bandung")) {
            mLogo.setImageResource(R.drawable.logoitb);
        } else if (items.equals("Universitas Pendidikan Indonesia")) {
            mLogo.setImageResource(R.drawable.logoupi);
        } else if (items.equals("Politeknik Manufaktur Bandung")) {
            mLogo.setImageResource(R.drawable.logopoltekmanufaktur);
        } else if (items.equals("UIN Sunan Gunung Jati")) {
            mLogo.setImageResource(R.drawable.logouin);
        }
    }*/


}
