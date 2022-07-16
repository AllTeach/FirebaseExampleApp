package com.example.firebaseexampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllPostsUsingFirebaseUIActivity extends AppCompatActivity {

    private static final String TAG = "Posts using Firebase UI";
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private CollectionReference postsRef =firebaseFirestore.collection("posts");

    private FirebaseUIPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts_using_firebase_uiactivity);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = postsRef.orderBy("title");


        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query,Post.class)
                .build();
        adapter = new FirebaseUIPostAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.rvFirebaseUI);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // swipte left will delete the item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                adapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setViewClickedListener(new FirebaseUIPostAdapter.onViewClickListener() {
            @Override
            public void onViewClicked(DocumentSnapshot documentSnapshot, int position) {
                Post p = documentSnapshot.toObject(Post.class);
                String id = documentSnapshot.getId();

                String email = p.getOwnerMail();
                String title = "";
                if(p.getTitle().equals("found"))
                     title="Your dog?\n Do you know the owner?";
                else
                    title="Have you seen this dog?";
                String data = "please contact via mail " + email;
                showAlertDialog(title,data);

                // create Alert Dialog requesting whether want to contact


            }
        });
    }

    private void showAlertDialog(String title,String data)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(data);
        alertDialog.setIcon(R.drawable.ic_launcher_background);
        alertDialog.setCancelable(true);

        // in case clicked YES
        // remove from list and then from database
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog= alertDialog.create();
        dialog.show();
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