package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ongoing extends AppCompatActivity {
    FirebaseAuth fauth;
    private static String TAG="ongoing";
    String sts;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    FirebaseFirestore fstore;
    String link,crname;
    String c1,c2;
    StorageReference storagereference;
    DocumentReference documentReference;
    List<String> ongoinglist=new ArrayList<>();
    ListView lview2;
    RecyclerView recyclerView1;
    final static int PICK_PDF_CODE = 2342;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);

        fauth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        storagereference= FirebaseStorage.getInstance().getReference();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Ongoing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Query query=fstore.collection("Colleague's Name: "+fauth.getCurrentUser().getEmail()+"active");
        recyclerView1=(RecyclerView)findViewById(R.id.recyclerview1);
        FirestoreRecyclerOptions<ongoingprojectlists> options=new FirestoreRecyclerOptions.Builder<ongoingprojectlists>().setQuery(query,ongoingprojectlists.class).build();
        adapter= new FirestoreRecyclerAdapter<ongoingprojectlists, ongoing.projectviewholder>(options) {
            @NonNull
            @Override
            public ongoing.projectviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoingprojectlists_item,parent,false);

                return new ongoing.projectviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ongoing.projectviewholder holder, int position, @NonNull ongoingprojectlists model) {
                holder.list_name.setText(model.getName());
                holder.list_desc.setText(model.getDesc());
                holder.list_creator.setText(model.getCreator());
                holder.list_task.setText(model.getTask());
                holder.list_starttime.setText(model.getStarttime());
                holder.list_endtime.setText(model.getFinishingtime());
                holder.list_status.setText(model.getStatus());
                holder.list_role.setText(model.getRole());
                String cname=model.getColleaguename();
                crname=holder.list_creator+"files";
                sts=holder.list_name.getText().toString();
                String update1=holder.editupdate.getText().toString();
                c1=holder.list_creator.getText().toString().trim()+"projects";
                c2=holder.list_task.getText().toString();

                holder.Uploadfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent();
                        intent.setType("application/pdf");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_PDF_CODE);
                        DocumentReference dref=fstore.collection(fauth.getCurrentUser().getUid()+"files").document("files");
                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    link=documentSnapshot.getString("link");
                                final Map<String, String> hashmap2=new HashMap<>();


                                hashmap2.put("task",c2);
                                hashmap2.put("sender",fauth.getCurrentUser().getEmail());
                                hashmap2.put("link",link);
                                DocumentReference dref2=fstore.collection(holder.list_creator.getText().toString()+"files").document(holder.list_task.getText().toString());


                                dref2.set(hashmap2);

                            }
                        });








                        }



                });

                holder.sendupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference dref=fstore.collection(c1).document(c2);

                        dref.update("update",holder.editupdate.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Status updated Successfully",Toast.LENGTH_LONG).show();
                                holder.editupdate.getText().clear();
                                finish();
                                }

                            }
                        });
                    }
                });
            }
        };
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(adapter);
    }
    public class projectviewholder extends RecyclerView.ViewHolder {
        TextView list_name,list_desc,list_creator,list_task,list_starttime,list_endtime,list_status,list_role;
        Button Uploadfile,sendupdate;
        EditText editupdate,filename;
        public projectviewholder(@NonNull View itemView) {
            super(itemView);
            list_name=itemView.findViewById(R.id.list_name1);
            list_creator=itemView.findViewById(R.id.list_creator);
            list_desc=itemView.findViewById(R.id.list_desc1);
            list_starttime=itemView.findViewById(R.id.list_starttime1);
            list_endtime=itemView.findViewById(R.id.list_endtime1);
            list_task=itemView.findViewById(R.id.list_task1);
            list_status=itemView.findViewById(R.id.list_status1);
            list_role=itemView.findViewById(R.id.list_role1);
            Uploadfile=itemView.findViewById(R.id.Uploadfile);
            sendupdate=itemView.findViewById(R.id.sendupdate);
            editupdate=itemView.findViewById(R.id.editupdate);

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PDF_CODE){
            if(resultCode== RESULT_OK){
                Uri Fileuri=data.getData();



                StorageReference Folder=FirebaseStorage.getInstance().getReference().child(fauth.getCurrentUser().getUid()+"files");
                StorageReference file_name=Folder.child("file"+Fileuri.getLastPathSegment());
                file_name.putFile(Fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                fstore=FirebaseFirestore.getInstance();
                          DocumentReference docref9=fstore.collection(fauth.getCurrentUser().getUid()+"files").document("files");
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("link", String.valueOf(uri));
                                hashMap.put("sender",fauth.getCurrentUser().getEmail());



                           docref9.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(getApplicationContext(),"File Uploaded Successfully",Toast.LENGTH_SHORT).show();
                               }
                               }
                           });



                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {

        adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        adapter.stopListening();
        super.onStop();
    }
}