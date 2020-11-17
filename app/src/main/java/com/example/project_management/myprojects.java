package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.Gravity.*;

public class myprojects extends AppCompatActivity {
    FirebaseAuth fauth;
    private static String TAG="myproject";
    String s;
    //TextView task1,desc1,name1,role1,starttime1,finishtime1,colleague1;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    List<String> projectlist=new ArrayList<>();
    ListView listView;
    EditText projectemail,projecttask;
    TextView updatetxt;
    Button projectarchivebutton,closePopupBtn;
    String cname1,task1,sts1,arch;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;


    List<String> mycolleagues1=new ArrayList<>();

     FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprojects);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayout1=(LinearLayout)findViewById(R.id.linearlayout1);


      //  projectemail=(EditText)findViewById(R.id.proemail);
        //projecttask=(EditText)findViewById(R.id.protask);
        fauth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        storagereference= FirebaseStorage.getInstance().getReference();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);





        Query query=fstore.collection("Creator: "+fauth.getCurrentUser().getEmail()+"projects");
        FirestoreRecyclerOptions<projectlists> options=new FirestoreRecyclerOptions.Builder<projectlists>().setQuery(query,projectlists.class).build();
         adapter= new FirestoreRecyclerAdapter<projectlists, projectviewholder1>(options) {
            @NonNull
            @Override
            public projectviewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.projectlists_items,parent,false);

                return new projectviewholder1(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull projectviewholder1 holder, int position, @NonNull projectlists model) {
            String name,desc,colleaguename,task,starttime,finishingtime,status,role,creator;


            name=model.getName();
            desc=model.getDesc();
            colleaguename=model.getColleaguename();
            task=model.getTask();
            starttime=model.getStarttime();
            finishingtime=model.getfinishingtime();
            status=model.getStatus();
            role=model.getRole();
            creator="Creator: "+fauth.getCurrentUser().getEmail();
            holder.list_name.setText(name);
            holder.list_desc.setText(desc);
            holder.list_colleaguename.setText(colleaguename);
            holder.list_task.setText(task);
            holder.list_starttime.setText(starttime);
            holder.list_endtime.setText(finishingtime);
            holder.list_status.setText(status);
            holder.list_role.setText(role);
            cname1=holder.list_colleaguename.getText().toString();
            task1=holder.list_task.getText().toString();
            sts1=holder.list_status.getText().toString();

            holder.viewupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = (LayoutInflater) myprojects.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = layoutInflater.inflate(R.layout.popup,null);

                    closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                    updatetxt=(TextView) customView.findViewById(R.id.updatetxt);
                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                   popupWindow.showAtLocation(customView,Gravity.CENTER,0,0);

                  updatetxt.setText(model.getUpdate());
                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });

                holder.archivebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference docref3=fstore.collection(cname1+"active").document(task1);
                        docref3.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });


                        DocumentReference docref2=fstore.collection("Creator: "+fauth.getCurrentUser().getEmail()+"projects").document(task1);


                        docref2.update("status","Status: archived").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        DocumentReference docref4=fstore.collection(cname1+"archived").document(task1);
                        final Map<String, Object> m3=new HashMap<>();

                        m3.put("name",name);
                        m3.put("desc",desc);
                        m3.put("creator",creator);
                        m3.put("task",task);
                        m3.put("starttime",starttime);
                        m3.put("finishingtime",finishingtime);
                        m3.put("status",status);
                        m3.put("role",role);
                        docref4.set(m3).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });









                    }
                });

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


}

    private class projectviewholder1 extends RecyclerView.ViewHolder {
        TextView list_name,list_desc,list_colleaguename,list_task,list_starttime,list_endtime,list_status,list_role;
        Button archivebtn,viewupdate;
        public projectviewholder1(@NonNull View itemView) {
            super(itemView);
            list_name=itemView.findViewById(R.id.list_name);
            list_colleaguename=itemView.findViewById(R.id.list_colleaguename);
            list_desc=itemView.findViewById(R.id.list_desc);
            list_starttime=itemView.findViewById(R.id.list_starttime);
            list_endtime=itemView.findViewById(R.id.list_endtime);
            list_task=itemView.findViewById(R.id.list_task);
            list_status=itemView.findViewById(R.id.list_status);
            list_role=itemView.findViewById(R.id.list_role);
            archivebtn=itemView.findViewById(R.id.archivebtn);
            viewupdate=itemView.findViewById(R.id.updateview);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}