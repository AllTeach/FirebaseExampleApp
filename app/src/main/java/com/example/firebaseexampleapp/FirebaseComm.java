package com.example.firebaseexampleapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class FirebaseComm
{
    private final static FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private static final String TAG = "Firebase Comm";
    private User fbUser;


    public boolean isUserSignedIn()
    {
        return mAuth.getCurrentUser() != null;

    }

    public String authUserEmail()
    {
        return mAuth.getCurrentUser().getEmail();
    }
/*
    public void createFbUser(String email, String password)
    {
        fbUser = new User(email,password,"");
    }

 */
    public void registerUser(String email,String password)
    {
      //  createFbUser(email,password);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: success ");
                       //     fbUser.setUser_id(mAuth.getUid());


                        }
                        else {
                            Log.d(TAG, "onComplete: failed ");
              //              onFirebaseResult.firebaseResult(ResultType.REGISTER,false);
                        }
                    }
                });
    }

    public void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {

                                    // if logged in success without registering
                                    // create User if required
                           //         if(fbUser==null)
                           //             createFbUser(email,password);

                                    Log.d(TAG, "onComplete: success ");
                  //                  onFirebaseResult.firebaseResult(ResultType.LOGIN,true);
                                }
                                else {
                                    Log.d(TAG, "onComplete: failed ");
                     //               onFirebaseResult.firebaseResult(ResultType.LOGIN,false);
                                }
                            }
                        });
    }





}
