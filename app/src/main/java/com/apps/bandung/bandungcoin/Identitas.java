package com.apps.bandung.bandungcoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


public class Identitas extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private EditText editTextNama;
    private ImageView imageProfile;

    private static final int GALLERY_PICK = 1;

    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identitas);

        editTextNama = (EditText) findViewById(R.id.editTextNama);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);

        findViewById(R.id.button_image).setOnClickListener(this);
        findViewById(R.id.button_goHome2).setOnClickListener(this);
        findViewById(R.id.button_password).setOnClickListener(this);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nama = dataSnapshot.child("nama").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                editTextNama.setText(nama);
                if(!image.equals("default")) {
                    Picasso.with(Identitas.this).load(image).into(imageProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void userSave(){
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        String nama = editTextNama.getEditableText().toString();

        mUserDatabase.child("nama").setValue(nama);


    }

    private void userImage(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){

                mProgressDialog = new ProgressDialog(Identitas.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Mohon Tunggu Selagi Upload Image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                String current_user_id = mCurrentUser.getUid();

                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            String download_url = task.getResult().getDownloadUrl().toString();

                            mUserDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        mProgressDialog.dismiss();
                                        Toast.makeText(Identitas.this, "Upload Berhasil", Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                        } else{
                            Toast.makeText(Identitas.this, "Error in Uploading", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_goHome2:
                userSave();
                Intent intent = new Intent(Identitas.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_image:
                userImage();
                break;

            case R.id.button_password:
                Intent x = new Intent(Identitas.this, ChangePassword.class);
                startActivity(x);
                break;

        }
    }



}
