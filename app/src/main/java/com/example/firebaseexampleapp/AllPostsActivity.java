package com.example.firebaseexampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class AllPostsActivity extends AppCompatActivity implements FirestoreDB.PostQueryResult{

    private RecyclerView  recyclerView;
    private FirestoreDB firestoreDB;
    private ArrayList<Post> postsArray;
    AllPostsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        initViews();

        firestoreDB.getAllPosts();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvAllPosts);
        firestoreDB = new FirestoreDB();
        postsArray = new ArrayList<>();
        firestoreDB.setPostQueryResult(this);
    }


    @Override
    public void postsReturned(ArrayList<Post> arr) {
        if(arr!=null) {
            adapter = new AllPostsAdapter(arr);


            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    public void showFilteredPosts(View view)
    {
        firestoreDB.getPostsOrderByWithLimit("title",3);


    }
}