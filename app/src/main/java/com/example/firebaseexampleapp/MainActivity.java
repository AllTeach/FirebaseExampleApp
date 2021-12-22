package com.example.firebaseexampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends FragmentActivity implements LoginFragment.LoginResult{

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Toast.makeText(MainActivity.this, "received " + user.getEmail() + " " + user.getPassword(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViews() {
        // initiating the tabhost
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);




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
        Toast.makeText(MainActivity.this, "received interface" + mail + " " + password,Toast.LENGTH_LONG).show();

    }
}
