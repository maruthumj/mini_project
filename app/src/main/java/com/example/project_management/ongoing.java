package com.example.project_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Objects;

public class ongoing extends AppCompatActivity {
    FirebaseAuth fauth;
    private static String TAG="ongoing";
    String s;
    TextView task1,desc1,name1,role1,starttime1,finishtime1,colleague1;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    FirebaseFirestore fstore;
    StorageReference storagereference;
    List<String> ongoinglist=new ArrayList<>();
    ListView lview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);

        fauth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        storagereference= FirebaseStorage.getInstance().getReference();
        lview2=(ListView)findViewById(R.id.lview1);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Ongoing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         fstore.collection(fauth.getCurrentUser().getEmail()+"active").addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                 for(DocumentSnapshot documentSnapshot : value)
                 {
                     ongoinglist.add(documentSnapshot.getString("name"));
                     ongoinglist.add(documentSnapshot.getString("desc"));
                     ongoinglist.add(documentSnapshot.getString("colleaguename"));
                     ongoinglist.add(documentSnapshot.getString("task"));
                     ongoinglist.add(documentSnapshot.getString("role"));
                     ongoinglist.add(documentSnapshot.getString("starttime"));
                     ongoinglist.add(documentSnapshot.getString("finishingtime"));
                     ongoinglist.add(documentSnapshot.getString("status"));
                     ongoinglist.add(documentSnapshot.getString("creator"));
                 }
                 ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,ongoinglist);
                 adapter.notifyDataSetChanged();
                 lview2.setAdapter(adapter);
             }
         });




    }
}