package com.apps.bandung.bandungcoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    public String user_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kampus");

        TabLayout tab = findViewById(R.id.tab);
        final ViewPager viewPager = findViewById(R.id.pager);


        //Set Text untuk setiap Tab
        tab.addTab(tab.newTab().setText("Negeri"));
        tab.addTab(tab.newTab().setText("Swasta"));
        tab.setTabGravity(tab.GRAVITY_FILL);

        //Memangil PagerAdapter
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tab.getTabCount());

        //Set ViewPager Adapater
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        //Ketika Tab Di pilih
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //set viewpager sesuai get posisi tab
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_menu);

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.nav_Login).setVisible(false);
        menu.findItem(R.id.nav_Logout).setVisible(false);
        menu.findItem(R.id.nav_account).setVisible(false);

        //jika user blm login
        // misal dia blm login makan tulisannya login. dan jika sudah login maka dia tuluisannya logout
        //dan juga kita disini ngebuat my account muncul ketika sudah login.sm memanggil method check user role untuk
        // role admin

        if(mAuth.getCurrentUser()==null){
            menu.findItem( R.id.nav_Login ).setVisible( true );

        }else {
            menu.findItem( R.id.nav_Logout ).setVisible( true );
            menu.findItem( R.id.nav_account ).setVisible( true );
            checkUserRole();

        }

        // method disini itu fungsinya untuk memberikan aksi di side bar. misal kita klik my account maka
        // akan ke aktivity my account dsb
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case(R.id.nav_account):
                        startActivity(new Intent(MainActivity.this, Identitas.class));
                        break;

                    case R.id.nav_announcement:
                        startActivity(new Intent(MainActivity.this, Announcement.class));
                        break;

                    case R.id.nav_Login:
                        startActivity(new Intent(MainActivity.this, Login.class));
                        break;

                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, about.class));
                        break;

                    case R.id.nav_weather:
                        startActivity(new Intent(MainActivity.this, Weather.class));
                        break;

                    case R.id.nav_news:
                        startActivity(new Intent(MainActivity.this, News.class));
                        break;


                    case R.id.nav_Logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent x = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(x);
                        break;
                }
                return true;
            }
        });

        //method dibawah ini itu gunanya kita memanggil sharedpreference yg telah kita buat dibawah.
        pref = getApplicationContext().getSharedPreferences( "session_role",0 );
        prefEdit = pref.edit();

        user_role="user";

    }

    //method dibawah ini itu gunanya kita deklarasiin adminnya itu bisa apa saja
    public void isIfAdmin(){
        String role = pref.getString( "user_role","user" );
        if(role.equals( "admin" )){
        }
    }



    //method dibawah ini itu kita deklarasiin user role. disini kita mengecek role skrg itu apa. misal
    // kita ada 2 role. satu user biasa satu lagi admin.
    // nah kita ccara menentukkannya itu menggunakan sharedpreference. dan mencocokannya itu dengan mengambil data dari database
    public void checkUserRole(){
        String uuid = mAuth.getCurrentUser().getUid();
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uuid);

        mUserDatabase.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String role = dataSnapshot.child( "role" ).getValue(String.class);
                if(role!=null){
                    Log.d("SESSION:ROLE",role);
                    user_role=role;
                }else user_role="user";


                Log.d("SESSION:ROLEID",user_role);
                prefEdit.putString( "user_role",role );
                prefEdit.commit();

                isIfAdmin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
