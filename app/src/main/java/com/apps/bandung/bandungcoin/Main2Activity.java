package com.apps.bandung.bandungcoin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.apps.bandung.bandungcoin.Notification.Constants;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase, sDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ImageView mLogo;
    private TextView mNama,mJurusan, mAlamat;

    private Button subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mLogo = (ImageView) findViewById(R.id.main2_image);
        mNama = (TextView) findViewById(R.id.main2_nama);
        subscribe = (Button) findViewById(R.id.subscribe);

        findViewById(R.id.card_informasi).setOnClickListener(this);
        findViewById(R.id.card_jurusan).setOnClickListener(this);
        findViewById(R.id.card_testimoni).setOnClickListener(this);

        findViewById(R.id.subscribe).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        final String kampus = i.getStringExtra("nama");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");


        createNotificationChannel();

        //Untuk mengambil data dari database, kemudian menginputkan data ke textview dan imageview
        mDatabase.child(kampus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String main_nama = (String) dataSnapshot.child("nama").getValue();
                String main_logo = (String) dataSnapshot.child("logo").getValue();


                mNama.setText(main_nama);
                Picasso.with(Main2Activity.this).load(main_logo).into(mLogo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Melakukan pengecekan, jika user telah login
        // Selanjutnya akan dilakukan pengecekan terhadap subscribe yang telah dilakukan oleh user

        if(mAuth.getCurrentUser()!=null) {

            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String current_uid = mCurrentUser.getUid();
            sDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("subscribe").child(kampus).child("subscribe");
            sDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())  {
                            String sSubs = dataSnapshot.getValue().toString();
                            if (sSubs.equals("Yes")) {
                                subscribe.setText("Subscribed");
                            } else {
                                subscribe.setText("Subscribe");
                            }
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }




    private void userSubscribe(){
        if (subscribe.getText().equals("Subscribe")){
            subscribe.setText("Subscribed");
            String topic = mNama.getText().toString().replace(" ", "");
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            saveSubscribe1();

        } else if (subscribe.getText().equals("Subscribed")) {
            subscribe.setText("Subscribe");
            String topic = mNama.getText().toString().replace(" ", "");
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
            saveSubscribe2();
        }
    }

    private void saveSubscribe1(){
        String sub = mNama.getText().toString();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("subscribe").child(sub);

        HashMap<String, String > userMap = new HashMap<>();
        userMap.put("subscribe", "Yes");

        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Subscribed Berhasil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveSubscribe2(){
        String sub = mNama.getText().toString();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("subscribe").child(sub);

        HashMap<String, String > userMap = new HashMap<>();
        userMap.put("subscribe", "No");

        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "Subscribe Berhasil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
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
                    .setTitle("Tidak bisa melakukan subscribe")
                    .create();
            alertLogin.show();
        } else {
            userSubscribe();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subscribe:
                cekUser();
                break;
        }

        switch (view.getId()){
            case R.id.card_informasi:
                Intent x = new Intent(Main2Activity.this, Informasi.class);
                String xNama = mNama.getText().toString();
                x.putExtra("nama",xNama);
                startActivity(x);
                break;
        }

        switch (view.getId()){
            case R.id.card_jurusan:
                Intent x = new Intent(Main2Activity.this, Jurusan.class);
                String xNama = mNama.getText().toString();
                x.putExtra("nama",xNama);
                startActivity(x);
                break;
        }

        switch (view.getId()){
            case R.id.card_testimoni:
                Intent x = new Intent(Main2Activity.this, Testimoni.class);
                String xNama = mNama.getText().toString();
                x.putExtra("nama",xNama);
                startActivity(x);
                break;
        }

    }
}
