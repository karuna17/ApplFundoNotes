package com.example.fundonotesapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.viewmodel.LoginViewModel;
import com.example.fundonotesapp.viewmodel.LoginViewModelFactory;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolBar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle toggle;
    private Context context = this;
    LoginViewModel loginViewModel;
    SharedViewModel sharedViewModel;
    CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        // This is for Hamburger menu icon on toolbar
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        loginViewModel = new ViewModelProvider(MainActivity.this, new LoginViewModelFactory(new AuthService())).get(LoginViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.set_gotoLoginPageStatus(true);

        observeAppNav();
    }

    private void observeAppNav() {
        sharedViewModel.gotoHomePageStatus.observe(MainActivity.this, status -> {
            if (status) {
                gotoHomePage();
            }
        });

        sharedViewModel.gotoLoginPageStatus.observe(MainActivity.this, status -> {
            if (status) {
                gotoLoginPage();
            }
        });

        sharedViewModel.gotoRegisterPageStatus.observe(MainActivity.this, status -> {
            if (status) {
                gotoRegisterPage();
            }
        });

        sharedViewModel.gotoAddNotesStatus.observe(MainActivity.this, status -> {
            if (status) {
                gotoNotePage();
            }
        });

        loginViewModel.checkUserExistence();
        loginViewModel.isUserLoggedIn.observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status) {
                    sharedViewModel.set_gotoHomePageStatus(true);
                } else {
                    sharedViewModel.set_gotoLoginPageStatus(true);
                }
            }
        });
    }

    private void gotoHomePage() {
        toolBar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
    }

    private void gotoLoginPage() {
        toolBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Login()).commit();
    }

    private void gotoRegisterPage() {
        toolBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Register()).commit();
    }

    private void gotoNotePage() {
        toolBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddNotes()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sharedViewModel.setQueryText(newText);
                return false;
            }
        });

        MenuItem menuItem = menu.findItem(R.id.profile_img);
        View view = MenuItemCompat.getActionView(menuItem);

        image = view.findViewById(R.id.toolbar_profile_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
                Profile showDetails = Profile.createNewInstace();
                showDetails.show(getFragmentManager(), "Dialouge Fragment");
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.profile_img);
//        View view = MenuItemCompat.getActionView(menuItem);
//
//        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);
//
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // custom dialog
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.fragment_profile);
//                dialog.setTitle("User Profile");

    // set the custom dialog components - text, image and button
//                ImageView image = dialog.findViewById(R.id.user_picture);
//                TextView name = dialog.findViewById(R.id.user_name);
//                TextView email = dialog.findViewById(R.id.user_email);
//
//                mAuth = FirebaseAuth.getInstance();
//                currentUser = mAuth.getCurrentUser();
//                fstore = FirebaseFirestore.getInstance();
//
//                userID = currentUser.getUid();
//                documentReference = fstore.collection("users").document(userID);
//                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.getResult().exists()) {
//                            image.setImageResource(R.drawable.iconimg);
//                            String profile_name = task.getResult().getString("fName");
//                            String profile_email = task.getResult().getString("u_email");
//                            name.setText(profile_name);
//                            email.setText(profile_email);
//                        }
//                    }
//                });
//
//                Button dialogButton = (Button) dialog.findViewById(R.id.profile_logout);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        loginViewModel.normalLogout();
//                        sharedViewModel.set_gotoLoginPageStatus(true);
//                    }
//                });
//                // if button is clicked, close the custom dialog
//                ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.btncancel);
//                closeButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
    //  }


}