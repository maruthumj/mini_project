package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class notification extends AppCompatActivity {
TextView notification;
FirebaseFirestore fstore;
FirebaseAuth fauth;
Button accept,reject;
String response,sender;
private static String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notification=(TextView) findViewById(R.id.notification1);
        fstore=FirebaseFirestore.getInstance();
        fauth=FirebaseAuth.getInstance();
        accept=(Button) findViewById(R.id.accept);
        reject=(Button) findViewById(R.id.reject);
        DocumentReference docref=fstore.collection("request").document(fauth.getCurrentUser().getEmail());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    response=  documentSnapshot.getString("response1");
                    sender=documentSnapshot.getString("sender");
                    if(response.equals("yes"))
                    {
                        accept.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                        notification.setText(sender+" has requested you to be a Colleague");

                    }

                                   }



            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> friends=new HashMap<>();

                  friends.put("colleagues",sender);

                  fstore.collection(fauth.getCurrentUser().getEmail()).document().set(friends).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Log.d(TAG,"Colleague request is accepted");
                          Toast.makeText(getApplicationContext(),"request accepted",Toast.LENGTH_LONG).show();
                      }


                  });

                  Map<String,Object> friends1=new HashMap<>();

                  friends1.put("colleagues",fauth.getCurrentUser().getEmail());
                fstore.collection(sender).document().set(friends1).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {

                      }
                  });
                DocumentReference docref3=fstore.collection("request").document(fauth.getCurrentUser().getEmail());
                Map<String,Object> updates = new HashMap<>();
                updates.put("response1", FieldValue.delete());
                        docref3.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                notification.setVisibility(View.GONE);
                                accept.setVisibility(View.GONE);
                                reject.setVisibility(View.GONE);
                            }
                        });

// Remove the 'capital' field from the document


            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Colleague request rejected!");
                        Toast.makeText(getApplicationContext(), "Colleague request rejected", Toast.LENGTH_SHORT).show();
                        notification.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error rejecting!", e);

                    }
                });
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.notification);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.search_bar:
                        startActivity(new Intent(getApplicationContext(),search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.main:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:

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
