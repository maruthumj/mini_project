package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class myprojects extends AppCompatActivity {
    FirebaseAuth fauth;
    private static String TAG="myproject";
    String s;
    TextView task1,desc1,name1,role1,starttime1,finishtime1,colleague1;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    List<String> projectlist=new ArrayList<>();
    ListView listView;
    EditText projectemail,projecttask;
    Button projectarchivebutton;
    String cname1,s1,docid,docid1,name,desc,tasks,role,starttime,finishtime;

    List<String> mycolleagues1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprojects);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectemail=(EditText)findViewById(R.id.proemail);
        projecttask=(EditText)findViewById(R.id.protask);
        fauth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        storagereference= FirebaseStorage.getInstance().getReference();
        listView=(ListView)findViewById(R.id.lview1);

        s1="Status: Archived";

        fstore.collection(fauth.getCurrentUser().getEmail()+"projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot documentSnapshot : value){
                    name=documentSnapshot.getString("name");desc=documentSnapshot.getString("desc");tasks=documentSnapshot.getString("task");role=documentSnapshot.getString("role");starttime=documentSnapshot.getString("starttime");finishtime=documentSnapshot.getString("finishingtime");
                    projectlist.add(name);
                    projectlist.add(desc);
                    projectlist.add(documentSnapshot.getString("colleaguename"));
                    projectlist.add(tasks);
                    projectlist.add(role);
                    projectlist.add(starttime);
                    projectlist.add(finishtime);
                    projectlist.add(documentSnapshot.getString("status"));

                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,projectlist);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });

     projectarchivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              CollectionReference colref=  fstore.collection(projectemail.getText().toString()+"active");

                Query query=colref.whereEqualTo("task",projecttask.getText().toString());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docid=document.getId();
                            }
                        }
                    }
                });

                final Map<String, Object> archived = new HashMap<>();
                archived.put("Project Name: ",name);
                archived.put("Project Description: ",desc);
                archived.put("Task Name: ",tasks);
                archived.put("Role: ",role);
                archived.put("Start Time: ",starttime);
                archived.put("Finishing Time: ",finishtime);


                fstore.collection(projectemail.getText().toString()+"archived").document().set(archived);

                fstore.collection(projectemail.getText().toString()+"active").document(docid).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                CollectionReference colref1=  fstore.collection(fauth.getCurrentUser().getEmail()+"projects");

                Query query1=colref1.whereEqualTo("task",projecttask.getText().toString());
                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                docid1=document.getId();
                            }
                        }
                    }
                });

                DocumentReference docref3=fstore.collection(fauth.getCurrentUser().getEmail()+"projects").document(docid1);

                final Map<String, String> map3=new HashMap<>();
                map3.put("status",s);

                docref3.set(map3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });



            }
        });




    }
}