package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity implements View.OnClickListener {
Button verify,sendotp,resendotp,phoneverify,emailverify;
TextView phonenum;
EditText otpnum;
FirebaseAuth fauth;
FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
FirebaseFirestore fstore;

    private static final String TAG = "otp";
     PhoneAuthProvider.ForceResendingToken mResendToken;
     PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        initsetup();
        checkverificationstatus();
    }
    private void initsetup(){

        verify=(Button) findViewById(R.id.checkotp);
        phonenum=(TextView) findViewById(R.id.phonenum);
        otpnum=(EditText) findViewById(R.id.otpnum);
        sendotp=(Button) findViewById(R.id.sendotp);
        resendotp=(Button) findViewById(R.id.resendotp);

        Intent intent=getIntent();
        String phoneval=intent.getStringExtra("phoneval");
        phonenum.setText(phoneval);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Verify Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        verify.setOnClickListener(this);
        sendotp.setOnClickListener(this);
        resendotp.setOnClickListener(this);

    }
    private void checkverificationstatus(){

        fauth=FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted( PhoneAuthCredential credential) {
                Log.d("", "onVerificationCompleted:" + credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("", "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(otp.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(@NotNull String verificationId, @NotNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d("", "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
               // otpnum.setText(String.valueOf(mVerificationId));
            }
        };
    }



    @Override
    public void onClick(@NotNull View view) {
        switch (view.getId()) {
            case R.id.sendotp:
                getOtp( phonenum.getText().toString());
                sendotp.setVisibility(View.GONE);
                otpnum.setVisibility(View.VISIBLE);
                verify.setVisibility(View.VISIBLE);
                resendotp.setVisibility(View.VISIBLE);
                break;
            case R.id.checkotp:
                String code = otpnum.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);
                if(otpnum.getText().toString().isEmpty() || otpnum.getText().toString().length()< 6 || otpnum.getText().toString().length()>6){
                    otpnum.setError("please enter a valid otp");
                }
                break;
            case R.id.resendotp:
                resendVerificationCode(phonenum.getText().toString(), mResendToken);
                Toast.makeText(otp.this, "OTP has been resent", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fauth=FirebaseAuth.getInstance();
        fauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            Toast.makeText(otp.this, "User Authenticated Successfully.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),login.class));
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpnum.setError("please Enter a valid OTP");
                                Toast.makeText(otp.this, "Invalid code.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getOtp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);
    }
}







