package com.example.firebaseexampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends FragmentActivity implements LoginFragment.LoginResult, RegisterFragment.RegisterResult{

    private static final String TAG = "Main Activity";
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ItemViewModel viewModel;

    FirebaseComm comm;

    FirestoreDB firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();





        /*
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Toast.makeText(MainActivity.this, "received " + user.getEmail() + " " + user.getPassword(),Toast.LENGTH_SHORT).show();
            }
        });

         */

    }





    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if(comm.isUserSignedIn()) {
            Toast.makeText(this, "user signed in: " + comm.authUserEmail(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onStart: " + comm.authUserEmail());
        }

        //updateUI(currentUser);
    }

    private void initViews() {
        // initiating the tabhost
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        comm = new FirebaseComm();
        firestoreDB = new FirestoreDB();

        FragmentPagerAdapter
                adapter
                = new FragmentPagerAdapter(this);

        // Set the adapter onto
        // the view pager
        String[] arr = {"Login","Register"};
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> tab.setText( arr[position])//(position + 1))
        ).attach();
    }

    @Override
    public void dataFromLogin(String mail, String password) {
        comm.loginUser(mail,password);
    }

    @Override
    public void dataFromRegister(String mail, String password, String phoneNumber, String nickName) {
        comm.registerUser(mail,password);
        if(comm.isUserSignedIn())
            firestoreDB.insertUser(mail,password,phoneNumber,nickName);
    }

    public void gotoPosts(View view) {
        Intent i = new Intent(this,PostsActivity.class);
        startActivity(i);
    }
}
