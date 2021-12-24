package com.example.firebaseexampleapp;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import androidx.annotation.NonNull;

public class FirestoreDB
{
    private static final String TAG = "Firestore DB";
    private FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    public FirestoreDB()
    {
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
    public void insertUser(String email, String password, String phone,String nickName  )
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(email,password,firebaseUser.getUid(),phone,nickName);
        DocumentReference documentReference =firebaseFirestore.collection("users").document(firebaseUser.getUid());
        // when reaching a specific document use set
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onComplete: success");
            }
        });
    }


    // this method simulates an update of a specific field for a user
    public void updatePhoneNumber(String phone)
    {
        DocumentReference documentReference =firebaseFirestore.collection("users").document(firebaseUser.getUid());
        documentReference.update("phone",phone);

    }

    // post here include image that will be stored in the Firebase Storage
    // in the firestore database we keep only the storage ref
    public void insertPost(String title, String body, Bitmap bitmap)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // add the photo to the firebase storage
        Post post = new Post(title,body,"",firebaseUser.getEmail());

        firebaseFirestore.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess:  post added");
                        FBStorage fbStorage = new FBStorage();
                        // set a unique name in the storage
                        // set the reference as follows:
                        // "folder" named entryname which is the id of the post
                        // unique image name in case we have more than one image in the post...future
                        String uniqueString = UUID.randomUUID().toString() + ".jpg";
                        String path =  documentReference.getId().concat("/"+uniqueString );

                        fbStorage.uploadImageToStorage(bitmap,path);
                        // update the storage reference in the post entry
                        documentReference.update("bitmapUrl",path);
                    }
                });
    }










}
