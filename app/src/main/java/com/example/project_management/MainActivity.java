package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
//private FirebaseAuth fauth;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userid;
    FirebaseUser fuser;
    PagerAdapter adapter;
    ImageView create,ongoing,archived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fauth=FirebaseAuth.getInstance();
        //FirestoreProfileData();
        fstore=FirebaseFirestore.getInstance();
        create=(ImageView)findViewById(R.id.create);
        ongoing=(ImageView)findViewById(R.id.ongoing);
        archived=(ImageView)findViewById(R.id.archived);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),create.class));
                finish();
            }
        });
        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ongoing.class));
                finish();
            }
        });
        archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),archived.class));
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
          bottomNavigationView.setSelectedItemId(R.id.main);
          bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                  switch(menuItem.getItemId()){
                      case R.id.main:
                          return true;
                      case R.id.search_bar:
                          startActivity(new Intent(getApplicationContext(),search.class));
                            overridePendingTransition(0,0);
                            return true;
                      case R.id.notification:
                          startActivity(new Intent(getApplicationContext(),notification.class));
                          overridePendingTransition(0,0);
                          return true;
                      case R.id.profile:
                          startActivity(new Intent(getApplicationContext(),profile.class));
                          overridePendingTransition(0,0);
                          return true;
                  }
return false;
              }
          });
        }
    @Override
    protected void onStart()
    {
       super.onStart();
        FirebaseUser muser=fauth.getCurrentUser();

        if(muser!= null)
        {
            
        }
        else
        {
            startActivity(new Intent(this,login.class));
            finish();
        }
    }
}