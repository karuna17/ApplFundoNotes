package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.model.User;
import com.example.fundonotesapp.viewmodel.LoginViewModel;
import com.example.fundonotesapp.viewmodel.RegisterViewModel;
import com.example.fundonotesapp.viewmodel.RegisterViewModelFactory;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;

public class Register extends Fragment {
    private static final String TAG = "LoginFragment";
    private RegisterViewModel registerViewModel;
    private SharedViewModel sharedViewModel;
    private EditText mName, mEmail, mPassword;
    private TextView alreadyAcc;
    private Button registerButton;
    private ProgressBar progBar;

    public Register() {
    }

    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        mName = v.findViewById(R.id.inputName);
        mEmail = v.findViewById(R.id.inputEmail);
        mPassword = v.findViewById(R.id.inputPassword);
        alreadyAcc = v.findViewById(R.id.already_acc);
        registerButton = v.findViewById(R.id.registerBtn);
        progBar = v.findViewById(R.id.progress_bar);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory(new AuthService())).get(RegisterViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        registeringNewUser();
        loginIfAlreadyHaveAcc();
    }

    private void registeringNewUser() {
       registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String uri = "";
                String fullName = mName.getText().toString();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be greater than 6 characters");
                    return;
                }
                progBar.setVisibility(View.VISIBLE);
                //register the user in firebase
                User user = new User(uri,fullName,email,password);
                registerViewModel.registerToFundoNotes(user);
                registerViewModel.userRegisterStatus.observe(Register.this, status -> {
                    if (status.getStatus()) {
                        Toast.makeText(getContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        sharedViewModel.set_gotoHomePageStatus(true);
                        progBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        progBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void loginIfAlreadyHaveAcc() {
        alreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.set_gotoLoginPageStatus(true);
            }
        });
    }
}