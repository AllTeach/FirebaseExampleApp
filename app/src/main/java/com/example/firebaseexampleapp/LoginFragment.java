package com.example.firebaseexampleapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private ItemViewModel viewModel;
    private  Button b;
    private EditText etMail,etPassword;

    private LoginResult loginResult;

    public  interface LoginResult
    {
        void dataFromLogin(String mail,String password);
    }

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_login, container, false);
         b = view.findViewById(R.id.login);
         etMail = view.findViewById(R.id.usernameLog);
         etPassword = view.findViewById(R.id.passwordLog);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"clicked in fragment",Toast.LENGTH_SHORT).show();
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String email = etMail.getText().toString();
                                     String password = etPassword.getText().toString();

                                     User u = new User(email,password);
                                     viewModel.selectItem(u);

                                    if(loginResult!=null)
                                        loginResult.dataFromLogin(email,password);

                                 }
                             });
                // Set a new item

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof  LoginResult)
            loginResult = (LoginResult) getActivity();
    }
}