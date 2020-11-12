package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class create extends AppCompatActivity {
    private static final String TAG = "create";

    FirebaseAuth fauth;
    TextView startdate, finishdate;
    FirebaseUser fuser;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener1;
    private List<String> namelist = new ArrayList<>();
    EditText pname,cname,ptask,prole,pdesc;

    Button pbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        TextView lview=(TextView)findViewById(R.id.lview1);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        pname=(EditText)findViewById(R.id.pname);
        pdesc=(EditText)findViewById(R.id.pdesc);
        ptask=(EditText)findViewById(R.id.ptask);
        prole=(EditText)findViewById(R.id.prole);
        cname=(EditText)findViewById(R.id.cname);
        pbtn=(Button)findViewById(R.id.pbtn);
        storageReference = FirebaseStorage.getInstance().getReference();
        startdate = (TextView) findViewById(R.id.startdate);
        finishdate=(TextView)findViewById(R.id.finishdate);
        Calendar cal = Calendar.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String creator="Create: "+fauth.getCurrentUser().getEmail().trim();
                String name="Project Name: "+pname.getText().toString().trim();
                String description="Project Description: "+pdesc.getText().toString().trim();
                String role="Role: "+prole.getText().toString().trim();
                String colleaguename="Colleague's Name: "+cname.getText().toString().trim();
                String task="Task Name: "+ptask.getText().toString().trim();
                String starttime="Starting Time: "+startdate.getText().toString().trim();
                String finisingtime="End Time: "+finishdate.getText().toString().trim();
                String status="Status: active";
                if(TextUtils.isEmpty(name))
                {
                    pname.setError("Field is Required");
                    return;
                } if(TextUtils.isEmpty(description))
                {
                    pdesc.setError(" Field is Required");
                    return;
                }if(TextUtils.isEmpty(role))
                {
                    prole.setError("Field is Required");
                    return;
                }if(TextUtils.isEmpty(colleaguename))
                {
                    cname.setError("Field is Required");
                    return;
                }if(TextUtils.isEmpty(task))
                {
                    ptask.setError("Email is Required");
                    return;
                }

                final Map<String,Object> user=new HashMap<>();
                user.put("name",name);
                user.put("desc",description);
                user.put("role",role);
                user.put("starttime",starttime);
                user.put("task",task);
                user.put("colleaguename",colleaguename);
                user.put("finishingtime",finisingtime);
                user.put("status",status);


                final Map<String, Object> user1=new HashMap<>();
                user1.put("name",name);
                user1.put("desc",description);
                user1.put("role",role);
                user1.put("starttime",starttime);
                user1.put("task",task);
                user1.put("colleaguename",colleaguename);
                user1.put("finishingtime",finisingtime);
                user1.put("status",status);
                user1.put("creator",creator);

                fstore.collection(cname.getText().toString()+"active").document().set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

                 fstore.collection(fauth.getCurrentUser().getEmail()+"projects").document().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(create.this,"Task Assigned Successfully",Toast.LENGTH_LONG).show();
                             prole.getText().clear();
                             cname.getText().clear();
                             ptask.getText().clear();
                         }
                     }
                 });

                fstore.collection(Objects.requireNonNull(fauth.getCurrentUser().getEmail())).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot documentSnapshot : value) {
                            String s = documentSnapshot.getString("colleagues");

                        }


                    }

                });
                           }
        });

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datepicker = new DatePickerDialog(create.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datepicker.show();

            }

        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDataSet: date: " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                startdate.setText(date);
            }
        };

        finishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datepicker = new DatePickerDialog(create.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener1, year, month, day);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datepicker.show();

            }

        });
        onDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDataSet: date: " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                finishdate.setText(date);
            }
        };

       fstore.collection(Objects.requireNonNull(fauth.getCurrentUser().getEmail())).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot documentSnapshot : value)
                {
                   String s= documentSnapshot.getString("colleagues");
                    lview.setText(s);
                }

            }
        });


    }
}
