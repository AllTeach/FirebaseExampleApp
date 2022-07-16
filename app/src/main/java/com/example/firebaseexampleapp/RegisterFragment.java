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

public class RegisterFragment extends Fragment {

    private RegisterFragment.RegisterResult registerResult;
    private Button b;
    private EditText etMail,etPassword,etPhoneHome,etNickname;

    // using liveData
    ItemViewModel viewModel;

    public  interface RegisterResult
    {
        void dataFromRegister(String mail,String password,String phoneNumber, String nickName);
    }

    public RegisterFragment() {
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
        final View view =  inflater.inflate(R.layout.fragment_register, container, false);
        b = view.findViewById(R.id.login);
        etMail = view.findViewById(R.id.usernameReg);
        etPassword = view.findViewById(R.id.passwordReg);
        etPhoneHome = view.findViewById(R.id.phoneNumber);
        etNickname = view.findViewById(R.id.nickname);


        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhoneHome.getText().toString();
                String nickName = etNickname.getText().toString();

                viewModel = viewModel = new ViewModelProvider(getActivity()).get(ItemViewModel.class);
                // live data
                User u = new User(email,password);
                viewModel.selectItem(u);


                // interface

                if(registerResult!=null)
                    registerResult.dataFromRegister(email,password,phone,nickName);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof RegisterResult)
            registerResult = (RegisterResult) getActivity();
    }



    }