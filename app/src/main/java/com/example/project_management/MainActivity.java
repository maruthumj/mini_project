package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
//private FirebaseAuth fauth;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fauth=FirebaseAuth.getInstance();
        //FirestoreProfileData();
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