package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.model.User;
import com.example.fundonotesapp.viewmodel.LoginViewModel;
import com.example.fundonotesapp.viewmodel.LoginViewModelFactory;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;

public class Login extends Fragment {
    private static final String TAG = "LoginFragment";
    private LoginViewModel loginViewModel;
    private SharedViewModel sharedViewModel;
    private EditText email, password;
    private TextView createNewAcc, forgotPass;
    private Button loginButton;
    private ProgressBar progBar;
    private CheckBox rememberMe;

    public Login() {
    }

    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        email = v.findViewById(R.id.inputEmail);
        password = v.findViewById(R.id.inputPassword);
        loginButton = v.findViewById(R.id.loginBtn);
        createNewAcc = v.findViewById(R.id.create_new_acc);
        forgotPass = v.findViewById(R.id.forgot_password);
        progBar = v.findViewById(R.id.progress_bar);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(new AuthService())).get(LoginViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        // call All the methods
        login();
        creatingNewAcc();
        resetingNewPassword();
    }

    private void login() {
        loginButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String _email = email.getText().toString().trim();
                String _password = password.getText().toString().trim();
                if (TextUtils.isEmpty(_email)) {
                    email.setError("Email is required");
                    Log.d("Result", "onClick: " + _email);
                    return;
                }
                if (TextUtils.isEmpty(_password)) {
                    password.setError("Password is required");
                    return;
                }
                if (_password.length() < 6) {
                    password.setError("Password must be greater than 6 characters");
                    return;
                }
                progBar.setVisibility(View.VISIBLE);
                //Authenticate the user
                User user = new User(_email, _password);
                loginViewModel.loginToFundoNotes(user);
                loginViewModel.userLoginStatus.observe(Login.this, status -> {

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

    private void creatingNewAcc() {
        createNewAcc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                sharedViewModel.set_gotoRegisterPageStatus(true);
            }
        });
    }

    private void resetingNewPassword() {
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialouge = new AlertDialog.Builder(v.getContext());
                passwordResetDialouge.setTitle("Reset Password? ");
                passwordResetDialouge.setMessage("Enter Your Email To Reset The Password Link");
                passwordResetDialouge.setView(resetMail);
                passwordResetDialouge.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("FragmentLiveDataObserve")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        //reset password
                        loginViewModel.resettingPasswordToFundoNotes(mail);
                        loginViewModel.resetPasswordStatus.observe(Login.this, status -> {
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
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                passwordResetDialouge.create().show();
            }
        });
    }

}