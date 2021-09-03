package com.example.fundonotesapp.view;

import android.app.Dialog;
import android.app.DialogFragment;
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
import androidx.fragment.app.Fragment;
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
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public class Profile extends DialogFragment {

    ImageButton cancelButton;
    FloatingActionButton cameraButton;
    TextView name, email;
    Button logoutButton;
    ImageView profile_image;

    LoginViewModel loginViewModel;
    SharedViewModel sharedViewModel;

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

        profile_image = v.findViewById(R.id.user_picture);
        name = v.findViewById(R.id.user_name);
        email = v.findViewById(R.id.user_email);
        cameraButton = v.findViewById(R.id.camera_btn);
        cancelButton = v.findViewById(R.id.cancel_btn);
        logoutButton = v.findViewById(R.id.profile_logout);

//       loginViewModel = new ViewModelProvider(getContext(), new LoginViewModelFactory(new AuthService())).get(LoginViewModel.class);
//        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createDialougeBox();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createDialougeBox() {
        Dialog dialouge = new Dialog(getContext());
        dialouge.setTitle("User Profile");
        dialouge.setContentView(R.layout.fragment_profile);

        profile_image.setImageResource(R.drawable.iconimg);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialouge.dismiss();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.normalLogout();
                sharedViewModel.set_gotoLoginPageStatus(true);
            }
        });

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ImagePicker.Companion.with(getActivity()).crop().cropSquare().maxResultSize(1080,1080,true);
//                ImagePicker.Companion.with(getActivity())
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .cropOval()                //Allow dimmed layer to have a circle inside
//                        .cropFreeStyle()        //Let the user to resize crop bounds
//                        .galleryOnly()          //We have to define what image provider we want to use
//                        .maxResultSize(1080, 1080, true)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .createIntent();
//            }
//        });
        dialouge.show();
    }

    public static Profile createNewInstace() {
        Profile profile = new Profile();
        return profile;
    }

    private void forSelctingImage() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profile_image.setImageURI(uri);
    }
}