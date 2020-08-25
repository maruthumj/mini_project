package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
EditText lemailid,lpassword;
Button lsigninbtn;
TextView lsignupbtn;
ProgressBar lprogbar2;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      lemailid=findViewById(R.id.emailid);
      lpassword=findViewById(R.id.password);
      lsigninbtn=findViewById(R.id.loginbtn);
      lprogbar2=findViewById(R.id.progbar2);
      lsignupbtn=findViewById(R.id.txtlogin2);
fAuth=FirebaseAuth.getInstance();
      lsignupbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(),register.class));
          }
      });
      lsigninbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String email1=lemailid.getText().toString().trim();
              String password1=lpassword.getText().toString().trim();
              if(TextUtils.isEmpty(email1))
              {
                  lemailid.setError("Email is Required");
                  return;
              }
              if(TextUtils.isEmpty(password1))
              {
                  lpassword.setError("Password is required");
                  return;
              }
              if(password1.length()<6)
              {

                  lpassword.setError("Password must be greater than 6 characters");
                  return;
              }
                 lprogbar2.setVisibility(View.VISIBLE);
              fAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful())
                           {
                               Toast.makeText(login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           }
                           else
                           {
                               Toast.makeText(login.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                           }
                  }
              });
          }
      });
    }
}