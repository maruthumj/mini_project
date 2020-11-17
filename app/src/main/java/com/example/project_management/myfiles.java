package com.example.project_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.mbms.DownloadRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class myfiles extends AppCompatActivity {
    FirebaseAuth fauth;

    String sts;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser fuser;

    FirebaseFirestore fstore;

    StorageReference storagereference;



    RecyclerView recyclerView11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfiles);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Files");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fauth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        storagereference = FirebaseStorage.getInstance().getReference();

        recyclerView11 = (RecyclerView) findViewById(R.id.finalreview);


        Query query = fstore.collection("Creator: "+fauth.getCurrentUser().getEmail()+"files");
        FirestoreRecyclerOptions<filelists> options = new FirestoreRecyclerOptions.Builder<filelists>().setQuery(query, filelists.class).build();
        adapter = new FirestoreRecyclerAdapter<filelists, myfiles.projectviewholder>(options) {
            @NonNull
            @Override
            public myfiles.projectviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelists_item, parent, false);

                return new myfiles.projectviewholder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull myfiles.projectviewholder holder, int position, @NonNull filelists model) {

                holder.list_creator.setText(model.getSender()+" has sent a file");
                holder.list_task.setText(model.getTask());


                holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Downloadfile(myfiles.this,model.getTask(),".pdf", DIRECTORY_DOWNLOADS,model.getLink());
                    }
                });


            }
        };
        recyclerView11.setHasFixedSize(true);
        recyclerView11.setLayoutManager(new LinearLayoutManager(this));
        recyclerView11.setAdapter(adapter);


    }

    private class projectviewholder extends RecyclerView.ViewHolder {
        TextView list_creator, list_task;
        Button downloadbtn;

        public projectviewholder(@NonNull View itemView) {
            super(itemView);
            list_creator = itemView.findViewById(R.id.nametxt);
            list_task = itemView.findViewById(R.id.tasktxt);
            downloadbtn = itemView.findViewById(R.id.downloadbtn);
        }

    }

   public void Downloadfile(Context context,String filename,String fileextention,String DestinationDir,String url){
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,DestinationDir,filename+fileextention);
        downloadManager.enqueue(request);

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
