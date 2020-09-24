package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.*;

public class register extends AppCompatActivity {
EditText mname,mpassword1,mphone,memail,mconfpassword1;
Button mbtn;
ProgressBar mprogressBar;
FirebaseAuth mAuth;
TextView mtxtlogin;
FirebaseFirestore fstore;
String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         accountregister();
    }
    public void accountregister()
    {
        mname=findViewById(R.id.fname);
        mpassword1=findViewById(R.id.password1);
        mphone=findViewById(R.id.phonenum);
        memail=findViewById(R.id.emailid);
        mbtn=findViewById(R.id.btnsignup);
        mprogressBar=findViewById(R.id.progbar);
        mconfpassword1=findViewById(R.id.confpassword);
        mAuth=FirebaseAuth.getInstance();
        mtxtlogin=findViewById(R.id.txtlogin);
        fstore=FirebaseFirestore.getInstance();

        mtxtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=memail.getText().toString().trim();
                String password=mpassword1.getText().toString().trim();
                String cpassword=mconfpassword1.getText().toString().trim();
                String fullname=mname.getText().toString();
                String phonenum=mphone.getText().toString();
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
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                }
                            });
                           // startActivity(new Intent(getApplicationContext(),confirm_mail.class));

                        }
                        else
                        {
                            Toast.makeText(register.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}