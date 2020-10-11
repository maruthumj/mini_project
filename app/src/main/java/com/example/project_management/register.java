package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {
    private Handler mhandler=new Handler();
EditText mname,mpassword1,otpnum,mphone,memail,mconfpassword1;
Button mbtn,phoneverify,emailverify,nextbtn;
ProgressBar mprogressBar;
FirebaseAuth mAuth;
TextView mtxtlogin;
FirebaseUser fuser;
FirebaseFirestore fstore;
String userid;

CountryCodePicker codePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        fuser=mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        mname=findViewById(R.id.fname);
        mpassword1=findViewById(R.id.password1);
        mphone=findViewById(R.id.phonenum);
        memail=findViewById(R.id.emailid);
        mbtn=findViewById(R.id.btnsignup);
        mprogressBar=findViewById(R.id.progbar);
        mconfpassword1=findViewById(R.id.confpassword);
        mtxtlogin=findViewById(R.id.txtlogin);
        fstore=FirebaseFirestore.getInstance();
        codePicker=(CountryCodePicker) findViewById(R.id.ccp);
        emailverify=(Button) findViewById(R.id.emailverify);
        phoneverify=(Button) findViewById(R.id.verifyphone);






        accountregister();
    }



    public void accountregister()
    {

        mtxtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        emailverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(register.this,"Email Verification has been sent",Toast.LENGTH_SHORT).show();

                           startActivity(new Intent(getApplicationContext(),login.class));
                           finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(register.this,"Email not sent"+e.getMessage(),Toast.LENGTH_LONG).show();
                   }
               });
            }


        });

        phoneverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), otp.class);
                String phoneval="+"+codePicker.getSelectedCountryCode()+mphone.getText().toString();
                intent.putExtra("phoneval",phoneval);
                startActivity(intent);
                finish();



                }
        });


        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=memail.getText().toString().trim();
                String password=mpassword1.getText().toString().trim();
                String cpassword=mconfpassword1.getText().toString().trim();
                String fullname=mname.getText().toString();
                String phonenum="+"+codePicker.getSelectedCountryCode()+mphone.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    memail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mpassword1.setError("Password is required");
                    return;
                }
                if(password.length()<6)
                {

                    mpassword1.setError("Password must be greater than 6 characters");
                    return;
                }
                if(!cpassword.equals(password))
                {
                    mconfpassword1.setError("Passwords are not matching!");
                    return;
                }
                mprogressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(register.this,"User Created",Toast.LENGTH_LONG).show();

                            userid=mAuth.getCurrentUser().getUid();
                            DocumentReference docref=fstore.collection("users").document(userid);
                            final Map<String,Object> user=new HashMap<>();
                            user.put("fname",fullname);
                            user.put("email",email);
                            user.put("phone",phonenum);
                            docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","onSuccess: User Profile is created for"+userid);
                                    mbtn.setVisibility(View.GONE);
                                    phoneverify.setVisibility(View.VISIBLE);
                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(register.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mprogressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

    }




        }


        /*
          if(bundle != null)
                {
                    int data=bundle.getInt("data");
                    if(data == 1)
                    {
                        phoneverify.setVisibility(View.GONE);
                        emailverify.setVisibility(View.VISIBLE);
                    }
                    else {

                    }
                    }
         */