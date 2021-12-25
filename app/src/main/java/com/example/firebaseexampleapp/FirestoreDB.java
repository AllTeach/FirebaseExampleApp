package com.example.firebaseexampleapp;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FirestoreDB {
    private static final String TAG = "Firestore DB";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private PostQueryResult postQueryResult;


    public interface PostQueryResult {
        void postsReturned(ArrayList<Post> arr);
        void postsChanged(Post p,int oldIndex,int newIndex);
        void postRemoved(int index);
        void postAdded(Post p,int index);
    }



    public FirestoreDB() {
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void insertUser(String email, String password, String phone, String nickName) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(email, password, firebaseUser.getUid(), phone, nickName);
        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        // when reaching a specific document use set
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onComplete: success");
            }
        });
    }


    // this method simulates an update of a specific field for a user
    public void updatePhoneNumber(String phone) {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        documentReference.update("phone", phone);

    }

    // post here include image that will be stored in the Firebase Storage
    // in the firestore database we keep only the storage ref
    public void insertPost(String title, String body, Bitmap bitmap) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // add the photo to the firebase storage

        // hold the reference for the storage
        DocumentReference ref = firebaseFirestore.collection("posts").document();
        FBStorage fbStorage = new FBStorage();
        // set a unique name in the storage
        // set the reference as follows:
        // "folder" named entryname which is the id of the post
        // unique image name in case we have more than one image in the post...future
        String uniqueString = UUID.randomUUID().toString() + ".jpg";
        String path = ref.getId().concat("/" + uniqueString);
        fbStorage.uploadImageToStorage(bitmap, path);
        // update the storage reference in the post entry
        Post post = new Post(title, body, path, firebaseUser.getEmail());
       

        // upload to storage and then to firestore
                ref.set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: post loaded successfully ");

                            }
                        });
             
                
    }


    public void setPostQueryResult(PostQueryResult pqr) {
        postQueryResult = pqr;

    }


    public void getDataFromListener(Task<QuerySnapshot> task) {
        ArrayList<Post> arr = new ArrayList<>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                arr.add(doc.toObject(Post.class));
                // get all images

            }
            if (postQueryResult != null)
                postQueryResult.postsReturned(arr);
        } else {
            Log.d(TAG, "onComplete: failed");
           // postQueryResult.postsReturned(null);
        }

    }


    public void getPostsOrderByWithLimit(String field, int limit) {

        firebaseFirestore.collection("posts").orderBy(field).limit(limit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        getDataFromListener(task);
                    }
                });
    }

    public void getAllPosts() {

        firebaseFirestore.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        getDataFromListener(task);
                    }

                });

    }
    public void listenForPostChanges()
    {
        firebaseFirestore.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.d(TAG, "onEvent: FAILED UPDATE");
                }
                for (DocumentChange change:value.getDocumentChanges()) {
                    if(change.getType() == DocumentChange.Type.MODIFIED)
                    {
                        Post p = change.getDocument().toObject(Post.class);
                        Log.d(TAG, "onEvent:  changed" + p.getTitle());
                        if (postQueryResult != null)
                            postQueryResult.postsChanged(p,change.getOldIndex(),change.getNewIndex());

                    }
                    else if(change.getType() == DocumentChange.Type.REMOVED)
                    {
                      //  Post p = change.getDocument().toObject(Post.class);

                        Log.d(TAG, "onEvent:  removed" + change.getOldIndex());
                        if (postQueryResult != null)
                            postQueryResult.postRemoved(change.getOldIndex());
                    }

                    else if(change.getType() == DocumentChange.Type.ADDED)
                    {
                        if(change.getOldIndex() != change.getNewIndex()) {
                            Post p = change.getDocument().toObject(Post.class);
                            Log.d(TAG, "onEvent:  added" + p.getTitle());
                            if (postQueryResult != null)
                                postQueryResult.postAdded(p, change.getNewIndex());
                        }
                    }



                }

            }
        });
    }

    public void removeListenForPostChanges()
    {
        //firebaseFirestore.collection("posts").

        }
}