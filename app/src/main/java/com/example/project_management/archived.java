package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class archived extends AppCompatActivity {
    FirebaseAuth fauth;
    private static String TAG = "archived";
    String sts;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    FirebaseFirestore fstore;
    StorageReference storagereference;

    RecyclerView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Archived");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fauth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        storagereference = FirebaseStorage.getInstance().getReference();
        review = (RecyclerView) findViewById(R.id.review);

        Query query=fstore.collection("Colleague's Name: "+fauth.getCurrentUser().getEmail()+"archived");
        FirestoreRecyclerOptions<archivedprojectlists> options=new FirestoreRecyclerOptions.Builder<archivedprojectlists>().setQuery(query,archivedprojectlists.class).build();
        adapter= new FirestoreRecyclerAdapter<archivedprojectlists, archived.projectviewholder>(options) {
            @NonNull
            @Override
            public archived.projectviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.archivedprojectlists_items,parent,false);

                return new archived.projectviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull archived.projectviewholder holder, int position, @NonNull archivedprojectlists model) {
                holder.list_name.setText(model.getName());
                holder.list_desc.setText(model.getDesc());
                holder.list_creator.setText(model.getCreator());
                holder.list_task.setText(model.getTask());
                holder.list_starttime.setText(model.getStarttime());
                holder.list_endtime.setText(model.getFinishingtime());
                holder.list_status.setText(model.getStatus());
                holder.list_role.setText(model.getRole());


            }
        };
        review.setHasFixedSize(true);
        review.setLayoutManager(new LinearLayoutManager(this));
        review.setAdapter(adapter);



    }
    private class projectviewholder extends RecyclerView.ViewHolder {
        TextView list_name,list_desc,list_creator,list_task,list_starttime,list_endtime,list_status,list_role;
        public projectviewholder(@NonNull View itemView) {
            super(itemView);
            list_name=itemView.findViewById(R.id.list_name2);
            list_creator=itemView.findViewById(R.id.list_creator2);
            list_desc=itemView.findViewById(R.id.list_desc2);
            list_starttime=itemView.findViewById(R.id.list_starttime2);
            list_endtime=itemView.findViewById(R.id.list_endtime2);
            list_task=itemView.findViewById(R.id.list_task2);
            list_status=itemView.findViewById(R.id.list_status2);
            list_role=itemView.findViewById(R.id.list_role2);
        }

    }
    @Override
    protected void onStart () {

        adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop () {
        adapter.stopListening();
        super.onStop();
    }
}