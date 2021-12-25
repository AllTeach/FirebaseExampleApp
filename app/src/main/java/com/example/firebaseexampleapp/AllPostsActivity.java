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

        // not required since we have listener
      //  firestoreDB.getAllPosts();


    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvAllPosts);
        firestoreDB = new FirestoreDB();
        postsArray = new ArrayList<>();

        firestoreDB.setPostQueryResult(this);

        // testing 123
    //    postsArray = arr;
        adapter = new AllPostsAdapter(postsArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestoreDB.listenForPostChanges();

    }


    @Override
    public void postsReturned(ArrayList<Post> arr) {

        if(arr!=null) {

            postsArray.clear();
            for (Post p:arr) {
                postsArray.add(p);
            }

            adapter.notifyDataSetChanged();
        }
/*
          adapter = new AllPostsAdapter(arr);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


           firestoreDB.listenForPostChanges();
        }
        */


    }

    @Override
    public void postsChanged(Post p,int oldIndex,int newIndex) {
        if(oldIndex == newIndex) {
            postsArray.set(newIndex, p);
            adapter.notifyItemChanged(newIndex);
        }
        else {
            postsArray.remove(oldIndex);
            postsArray.set(newIndex,p);
            adapter.notifyItemMoved(oldIndex,newIndex);

        }


    }

    @Override
    public void postRemoved(int index) {
        postsArray.remove(index);
        adapter.notifyItemRemoved(index);

    }

    @Override
    public void postAdded(Post p, int index) {
        postsArray.add(index,p);
        adapter.notifyItemInserted(index);

    }

    public void showFilteredPosts(View view)
    {
        firestoreDB.getPostsOrderByWithLimit("title",3);
    }
}