package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {


    private EditText editTextTextEmailAddress;

    FirebaseAuth firebaseAuth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        //ActionBar actionBar = getActionBar();

        //actionBar.setDisplayHomeAsUpEnabled(true);
        Button resetPasswordSendEmailButton = (Button) findViewById(R.id.sendemail);
        editTextTextEmailAddress = (EditText) findViewById(R.id.et);


        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(editTextTextEmailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),login.class));
                            Toast.makeText(forgotpassword.this, "password has been sent to your email",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(forgotpassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }
}


