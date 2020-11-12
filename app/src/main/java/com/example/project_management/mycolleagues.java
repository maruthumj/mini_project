package com.example.project_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.*;


public class mycolleagues extends AppCompatActivity {
    FirebaseAuth fauth;
    FirebaseUser fuser;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    ListView listView1;
  List<String> namelist1=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycolleagues);

        fauth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        storagereference=FirebaseStorage.getInstance().getReference();
       listView1=(ListView)findViewById(R.id.listview1);


        Objects.requireNonNull(getSupportActionBar()).setTitle("My Colleagues");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     fstore.collection(Objects.requireNonNull(fauth.getCurrentUser().getEmail())).addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
             for(DocumentSnapshot documentSnapshot : value)
             {
                 namelist1.add(documentSnapshot.getString("colleagues"));

             }
             ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,namelist1);
             adapter1.notifyDataSetChanged();
             listView1.setAdapter(adapter1);


         }

     });
     }

}