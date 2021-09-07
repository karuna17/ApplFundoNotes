package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
    private static final String TAG = Login.class.getName();
    private LoginViewModel loginViewModel;
    private SharedViewModel sharedViewModel;
    private EditText email, password;
    private TextView createNewAcc, forgotPass;
    private Button loginButton;
    private ProgressBar progBar;
    private LayoutInflater inflater;
    private ViewGroup container;

    public Login() {
    }

//    public void onConfigurationChanged(Configuration newConfig) {
//        View view = null;
//
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            view = inflater.inflate(R.layout.fragment_login, container, false);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            view = inflater.inflate(R.layout.fragment_login_horizontal, container, false);
//
//        }
//        super.onConfigurationChanged(newConfig);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.inputEmail);
        password = view.findViewById(R.id.inputPassword);
        loginButton = view.findViewById(R.id.loginBtn);
        createNewAcc = view.findViewById(R.id.create_new_acc);
        forgotPass = view.findViewById(R.id.forgot_password);
        progBar = view.findViewById(R.id.progress_bar);

        return view;
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
                User user = new User(_email, _password);
                loginViewModel.loginWithApi(user);
                loginViewModel.userLoginStatus.observe(Login.this, status -> {
                    Log.d(TAG, "onClick: LoginStatus: " + status.getStatus());
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