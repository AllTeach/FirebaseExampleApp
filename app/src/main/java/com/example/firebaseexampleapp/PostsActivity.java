package com.example.firebaseexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PostsActivity extends AppCompatActivity {


    private EditText etTitle,etBody;
    ImageView ivPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initViews();

    }

    private void initViews() {
        etTitle = findViewById(R.id.etPostName);
        etBody = findViewById(R.id.etPostData);
        ivPhoto = findViewById(R.id.imageView);
    }


    public void postToFirebase(View view)
    {

        Bitmap bitmap =((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
        FirestoreDB firestoreDB = new FirestoreDB();
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();
        firestoreDB.insertPost(title,body,bitmap);

    }
}