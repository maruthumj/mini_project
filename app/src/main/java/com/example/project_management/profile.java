package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class profile extends AppCompatActivity {
 Button logout;


FirebaseAuth fauth;
FirebaseFirestore fstore;
TextView name1,number1,email1;
ImageView profileimage,profilepicedit;
StorageReference storagereference;
String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        storagereference= FirebaseStorage.getInstance().getReference();
         fauth=FirebaseAuth.getInstance();
         fstore=FirebaseFirestore.getInstance();

         email1=(TextView) findViewById(R.id.email);
         number1=(TextView) findViewById(R.id.number);
         name1=(TextView) findViewById(R.id.name);
         profileimage=(ImageView) findViewById(R.id.profileimg);
         profilepicedit=(ImageView)findViewById(R.id.propicedit);
        logout=(Button) findViewById(R.id.btn);
        userid=fauth.getCurrentUser().getUid();


        FirebaseUser fuser=fauth.getCurrentUser();



        StorageReference profileref=storagereference.child("users"+fauth.getCurrentUser().getUid()+"/profile.jpg");

         profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
             @Override
             public void onSuccess(Uri uri) {
                 Picasso.get().load(uri).into(profileimage);
             }
         });
        profilepicedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);

            }
        });










        DocumentReference documentReference=fstore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                number1.setText(value.getString("phone"));
                name1.setText(value.getString("fname"));
                email1.setText(value.getString("email"));
            }
        });
             logout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   fauth.signOut();
                    startActivity(new Intent(getApplicationContext(),login.class));
                     finish();
                 }
             });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
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
                        startActivity(new Intent(getApplicationContext(),notification.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:

                        return true;


                }
                return false;
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK){
                Uri imageuri=data.getData();

                uploadImageToFirebase(imageuri);
            }
        }
    }
    private void uploadImageToFirebase(Uri imageuri)
    {
        StorageReference fileref=storagereference.child("users"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    }
                });
            Toast.makeText(profile.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
            fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileimage);

                }
            });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"Failed",Toast.LENGTH_SHORT).show();;

            }
        });
    }


    }



