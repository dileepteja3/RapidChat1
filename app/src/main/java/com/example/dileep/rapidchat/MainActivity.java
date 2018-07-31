package com.example.dileep.rapidchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolBar;

    private DatabaseReference mUserRef;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ONCREATE","onCreate Top");
        mAuth = FirebaseAuth.getInstance();

        Log.d("ONCREATE","onCreate Bottom");
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("RapidChat");


        if(mAuth.getCurrentUser()!= null){
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }else {

        }

        //tabs
        mViewPager=(ViewPager)findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        Toast.makeText(MainActivity.this,"ON START",Toast.LENGTH_LONG).show();
        if (currentUser == null){
            sendToStart();
        }else {

            mUserRef.child("online").setValue("true");

        }

    }

    public void onResume(){
        super.onResume();
//        Toast.makeText(MainActivity.this,"ON RESUME",Toast.LENGTH_LONG).show();
        mUserRef.child("online").setValue("true");
    }

    public void onPause(){
        super.onPause();
//        Toast.makeText(MainActivity.this,"ON PAUSE",Toast.LENGTH_LONG).show();
        mUserRef.child("online").setValue("true");
    }

    public void onStop(){
        super.onStop();

//        Toast.makeText(MainActivity.this,"ON STOP",Toast.LENGTH_LONG).show();
        if(mAuth.getCurrentUser()!= null){


            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }


    }


    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()== R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            sendToStart();
        }

        if(item.getItemId()== R.id.main_settings_btn){
            Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId()== R.id.main_all_btn){
            Intent settingsIntent = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(settingsIntent);
        }


        return true;
    }
}
