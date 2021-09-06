package com.example.fundonotesapp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.viewmodel.LoginViewModel;
import com.example.fundonotesapp.viewmodel.LoginViewModelFactory;
import com.example.fundonotesapp.viewmodel.ProfileViewModel;
import com.example.fundonotesapp.viewmodel.ProfileViewModelFactory;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public class Profile extends DialogFragment {
    SharedViewModel sharedViewModel;
    ProfileViewModel profileViewModel;

    private CircleImageView profile_image;
    private MaterialTextView profile_name, profile_email;
    private MaterialButton logoutButton;
    private ImageView closeIcon;

    public Profile() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = v.findViewById(R.id.profileImageView);
        profile_name = v.findViewById(R.id.profileName);
        profile_email = v.findViewById(R.id.profileEmail);
        closeIcon = v.findViewById(R.id.profileCloseIcon);
        logoutButton = v.findViewById(R.id.SignOut_Button);

        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(new AuthService())).get(ProfileViewModel.class);
        sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeIcon.setOnClickListener(v -> dismiss());
        signout();

    }

    private void signout() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileViewModel.logout();
                profileViewModel.logoutStatus.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        sharedViewModel.set_gotoLoginPageStatus(true);
                        sharedViewModel.set_gotoHomePageStatus(false);
                    }
                });
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profile_image.setImageURI(uri);
    }
}