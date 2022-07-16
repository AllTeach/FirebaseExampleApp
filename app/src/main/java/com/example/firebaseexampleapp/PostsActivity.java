package com.example.firebaseexampleapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PostsActivity extends AppCompatActivity {


    private EditText etTitle,etBody;
    private ImageView ivPhoto;
    private ActivityResultLauncher<Void> mGetThumb;
    // Launcher
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {

                // just an example of extracting an image
                // Handle the returned Uri
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

    private BatteryReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initViews();
        receiver = new BatteryReceiver();
        registerCameraLauncher();

    }


    // Display Menu
    // 1 Create menu Folder - DONE
    // 2 Create menu XML FILE - DONE
    // 3 Inflate -> ?  and Bind to MENU Object
    // 4 Handle Event when Item Selected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();

        if(id==R.id.itemMenuAllPosts) {

            Intent i = new Intent(this,AllPostsUsingFirebaseUIActivity.class);
            startActivity(i);        }
        else
            if(id==R.id.itemMenuOnePost)
                Toast.makeText(this,"One Post Selected in menu",Toast.LENGTH_SHORT).show();




        return true;

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
        if(TextUtils.isEmpty(etTitle.getText()) ||
                TextUtils.isEmpty(etBody.getText()))
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap =((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
        FirestoreDB firestoreDB = new FirestoreDB();
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        if(title.equals("found") || title.equals("lost"))
            firestoreDB.insertPost(title,body,bitmap);
        else
        {
            Toast.makeText(this, "title can be only found or lost", Toast.LENGTH_SHORT).show();

        }
    }


    // Triggered when image is clicked
    public void takePhoto(View view)
    {
        mGetThumb.launch(null);

    }
    public void gotoAllPostsActivity(View view)
    {
     //   Intent i = new Intent(this,AllPostsActivity.class);
       // startActivity(i);
    }

    public void gotoAllPostsActivityUsingFirebaseUI(View view)
    {
        Intent i = new Intent(this,AllPostsUsingFirebaseUIActivity.class);
        startActivity(i);
    }

    public void selectPictureGallery(View view) {
        // Pass the Mime as Image
        // // this will filter the results provided only images
        mGetContent.launch("image/*");
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(receiver,filter);
    }
}