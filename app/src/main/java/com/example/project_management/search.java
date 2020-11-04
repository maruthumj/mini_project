package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class search extends AppCompatActivity {
    private static String FIRE_LOG = "FIRE_LOG";
    ImageView profilepic;
    Button searchbtn;
    TextView username, userphone, usermail, cardView;
    EditText searchuser;

    FirebaseUser fuser;
    FirebaseAuth fauth;
    FirebaseUser currentuser;
    FirebaseStorage fstorage;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    String id, typedemail;
    Button cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search_bar);

        storagereference = FirebaseStorage.getInstance().getReference();
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();

        String currentuser=fauth.getCurrentUser().getEmail();
         profilepic=(ImageView)findViewById(R.id.profilepic);

        searchbtn = (Button) findViewById(R.id.searchbtn);
        searchuser = (EditText) findViewById(R.id.searchemail);
        cardView = (Button) findViewById(R.id.cardview);

        typedemail = searchuser.getText().toString();
       username=(TextView)findViewById(R.id.username);
       userphone=(TextView)findViewById(R.id.userphone);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkemail(v);
           DocumentReference docref1=fstore.collection("users").document(searchuser.getText().toString());
                     docref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                         @Override
                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                             usermail.setText(documentSnapshot.getString("fname"));
                             userphone.setText(documentSnapshot.getString("phone"));
                         }
                     });


            }


        });
          cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DocumentReference docref=fstore.collection("request").document(searchuser.getText().toString());
                  String send="yes";
                  String typedemail=searchuser.getText().toString();
                  Map<String, String> request=new HashMap<>();
                  request.put("response1",send);
                  request.put("sender",currentuser);


                  docref.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Log.d("TAG","request send successfully to"+searchuser.getText().toString());
                          Toast.makeText(getApplicationContext(),"Colleague request is sent",Toast.LENGTH_LONG).show();

                      }
                  });
              }
          });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search_bar:
                        return true;
                    case R.id.main:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), notification.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });
    }

    public void checkemail(View v) {

        fauth=FirebaseAuth.getInstance();
        fauth.fetchSignInMethodsForEmail(searchuser.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = !task.getResult().getSignInMethods().isEmpty();
                if (!check) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                } else {
                    cardView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
