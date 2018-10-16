package com.apps.bandung.bandungcoin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.apps.bandung.bandungcoin.Model.Post;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class Announcement extends AppCompatActivity {

    private RecyclerView mPostList;
    private Query mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        mPostList = (RecyclerView) findViewById(R.id.post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = (Query) FirebaseDatabase.getInstance().getReference().child("Post").orderByChild("timestamp");
        //mQuery = mDatabase.child()orderByChild("timestamp");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

                Post.class,
                R.layout.post_list,
                PostViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                viewHolder.setNama(model.getNama());
                viewHolder.setDesc(model.getDeskripsi());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setTime(model.getTimestamp());

            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);



    }



    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setNama(String nama){

            TextView post_nama = (TextView) mView.findViewById(R.id.post_nama);
            post_nama.setText(nama);

        }



        public void setDesc(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setImage(Context ctx, String image){

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }

        public void setTime(Long time){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_time);

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(time/(-1));
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            post_desc.setText(String.valueOf(date));

        }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //code dibawah ini itu kita deklarasikan user role. disini aku buat role yang
        //jika role tidak sama dengan admin maka tombol + yang ada diannouncement itu hilang.
        //jd yang ada tanda tombol + itu cuma admin aja.

        getMenuInflater().inflate(R.menu.announcement_menu, menu);
        SharedPreferences pref = getApplicationContext().getSharedPreferences( "session_role",0 );
        String role = pref.getString( "user_role","user");
        if(!role.equals( "admin" )){
            MenuItem menuadd = menu.findItem(R.id.add_button) ;
            menuadd.setVisible( false );
        }
        Log.d("SESSION::ISADMIN",role);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_button){
            startActivity(new Intent(Announcement.this, PostActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void isIfAdmin(){

    }
}
