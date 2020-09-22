package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import io.perfmark.Tag;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class confirm_mail extends AppCompatActivity {
TextView maildisplay;
FirebaseAuth fauth;
String userid;
ProgressBar probar;
Button btn;

    FirebaseUser fuser=fauth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mail);
        fauth=FirebaseAuth.getInstance();
        maildisplay=(TextView) findViewById(R.id.emailtext);
         probar=(ProgressBar) findViewById(R.id.progressBar);
        btn=(Button) findViewById(R.id.confbtn);
        Intent intent=getIntent();
        String value=intent.getStringExtra("emailid");
        maildisplay.setText(value);

         userid=fauth.getCurrentUser().getUid();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FirebaseUser user=fauth.getCurrentUser();
                fuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(confirm_mail.this,"Verification Email has been sent",Toast.LENGTH_LONG).show();
                                            }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(confirm_mail.this,"Verification Email has not been sent "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }

        });
emailverified();

}
private void emailverified()
{

        boolean isEmailverified=fauth.getCurrentUser().isEmailVerified();
        if(isEmailverified)
        {
            btn.setEnabled(true);
            sendtologin();
        }
          else {
              btn.setEnabled(true);
              Toast.makeText(this,"please verify your email address first",Toast.LENGTH_LONG).show();

        }
}
  private void sendtologin()
  {
      Intent intent=new Intent(getApplicationContext(),login.class);
      startActivity(intent);
      finish();
  }
}