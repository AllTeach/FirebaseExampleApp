package com.example.firebaseexampleapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PostsActivity extends AppCompatActivity {


    private EditText etTitle,etBody;
    private ImageView ivPhoto;
    private ActivityResultLauncher<Void> mGetThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initViews();
        registerCameraLauncher();

    }

    private void registerCameraLauncher() {
        mGetThumb = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                ivPhoto.setImageBitmap(result);
            }
        });
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


    // Triggered when image is clicked
    public void takePhoto(View view)
    {
        mGetThumb.launch(null);

    }
    public void gotoAllPostsActivity(View view)
    {
        Intent i = new Intent(this,AllPostsActivity.class);
        startActivity(i);
    }
}