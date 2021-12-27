package com.example.firebaseexampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                Log.d(TAG, "onViewClicked: " + p.getTitle() + " id " + id);
                Toast.makeText(AllPostsUsingFirebaseUIActivity.this,"clicked: item FB id " + id + " postion " + position
                ,Toast.LENGTH_SHORT).show();

            }
        });
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