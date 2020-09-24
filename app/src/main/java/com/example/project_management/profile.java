package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
private Button logout;
CheckBox verifyemail,verifyphone;
FirebaseAuth fauth;
FirebaseFirestore fstore;
TextView name1,number1,email1;
ImageView profileimage,profilepicedit,done1,done2;

StorageReference storagereference;
String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        done1=(ImageView) findViewById(R.id.done1);
        done2=(ImageView) findViewById(R.id.done2);
        storagereference= FirebaseStorage.getInstance().getReference();
         fauth=FirebaseAuth.getInstance();
         fstore=FirebaseFirestore.getInstance();
         verifyphone=(CheckBox) findViewById(R.id.confirmphone);
         verifyemail=(CheckBox) findViewById(R.id.confirmemail);
         email1=(TextView) findViewById(R.id.email);
         number1=(TextView) findViewById(R.id.number);
         name1=(TextView) findViewById(R.id.name);
         profileimage=(ImageView) findViewById(R.id.profileimg);
         profilepicedit=(ImageView)findViewById(R.id.propicedit);
        logout=(Button) findViewById(R.id.btn);
        userid=fauth.getCurrentUser().getUid();
        FirebaseUser fuser=fauth.getCurrentUser();
        Intent intent1=getIntent();
        String value=intent1.getStringExtra("value");
        if(value.equals("success"))
        {
            verifyphone.setVisibility(View.GONE);
            done1.setVisibility(View.VISIBLE);
        }
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
        verifyemail.setVisibility(View.VISIBLE);
          verifyemail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Toast.makeText(profile.this,"Verification Email has been sent", Toast.LENGTH_SHORT).show();

                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(profile.this,"Email not sent"+e.getMessage(),Toast.LENGTH_LONG).show();
                      }
                  });
              }
          });
          verifyphone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  Intent intent=new Intent(getApplicationContext(),otp.class);
                  String number1val=number1.getText().toString();
                  intent.putExtra("emailval",number1val);
                  startActivity(intent);

              }
          });
          if(fuser.isEmailVerified())
          {
              Toast.makeText(profile.this,"Email verified!",Toast.LENGTH_SHORT).show();
              verifyemail.setVisibility(View.GONE);
              done2.setVisibility(View.VISIBLE);
          }

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
                //profileimage.setImageURI(imageuri);
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


