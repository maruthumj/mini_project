package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    ImageView profilepic,searchbtn;
    TextView username,userphone,usermail;
    EditText searchuser;
    CardView cardView;
    FirebaseUser fuser;
    FirebaseAuth fauth;
    FirebaseStorage fstorage;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    String id,typedemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search_bar);


        fstore=FirebaseFirestore.getInstance();
        fauth=FirebaseAuth.getInstance();
        profilepic=(ImageView)findViewById(R.id.profilepic);
        username=(TextView) findViewById(R.id.username);
        userphone=(TextView) findViewById(R.id.userphonenumber);
        searchbtn=(ImageView) findViewById(R.id.searchbtn);
        searchuser=(EditText)findViewById(R.id.searchemail);
        cardView=(CardView) findViewById(R.id.cardview);
        usermail=(TextView) findViewById(R.id.useremail);
        typedemail=searchuser.getText().toString();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.fetchSignInMethodsForEmail(typedemail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check=!task.getResult().getSignInMethods().isEmpty();
                        if(!check)
                        {
                            Toast.makeText(getApplicationContext(),"User not found or Invalid Email",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            cardView.setVisibility(View.VISIBLE);
                            DocumentReference docref=fstore.collection("users").document(typedemail);
                            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                  username.setText(documentSnapshot.getString("fname"));
                                  userphone.setText(documentSnapshot.getString("phone"));
                                  usermail.setText(documentSnapshot.getString("email"));

                                }
                            });

                        }
                    }
                });


            }


        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),useractivity.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.search_bar:
                        return true;
                    case R.id.main:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
    }
