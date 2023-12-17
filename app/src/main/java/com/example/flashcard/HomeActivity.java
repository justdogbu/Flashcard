package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment homeFragment;
    Fragment searchFragment;
    Fragment libraryFragment;
    Fragment profileFragment;
    Fragment recentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavbar);

        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        libraryFragment = new LibraryFragment();
        profileFragment = new ProfileFragment();
        recentFragment = homeFragment;

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, homeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, searchFragment).hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, libraryFragment).hide(libraryFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, profileFragment).hide(profileFragment).commit();

        NavigationView navigationView = findViewById(R.id.drawerNavigation);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.nav_home:
                        fragmentManager.beginTransaction().hide(recentFragment).show(homeFragment).commit();
                        recentFragment = homeFragment;
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case R.id.nav_search:
                        fragmentManager.beginTransaction().hide(recentFragment).show(searchFragment).commit();
                        recentFragment = searchFragment;
                        bottomNavigationView.setSelectedItemId(R.id.search);
                        break;
                    case R.id.nav_library:
                        fragmentManager.beginTransaction().hide(recentFragment).show(libraryFragment).commit();
                        recentFragment = libraryFragment;
                        bottomNavigationView.setSelectedItemId(R.id.library);
                        break;
                    case R.id.nav_profile:
                        fragmentManager.beginTransaction().hide(recentFragment).show(profileFragment).commit();
                        recentFragment = profileFragment;
                        bottomNavigationView.setSelectedItemId(R.id.profile);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    fragmentManager.beginTransaction().hide(recentFragment).show(homeFragment).commit();
                    recentFragment = homeFragment;
                    navigationView.setCheckedItem(R.id.nav_home);
                    break;
                case R.id.search:
                    fragmentManager.beginTransaction().hide(recentFragment).show(searchFragment).commit();
                    recentFragment = searchFragment;
                    navigationView.setCheckedItem(R.id.nav_search);
                    break;
                case R.id.library:
                    fragmentManager.beginTransaction().hide(recentFragment).show(libraryFragment).commit();
                    recentFragment = libraryFragment;
                    navigationView.setCheckedItem(R.id.nav_library);
                    break;
                case R.id.profile:
                    fragmentManager.beginTransaction().hide(recentFragment).show(profileFragment).commit();
                    recentFragment = profileFragment;
                    navigationView.setCheckedItem(R.id.nav_profile);
                    break;
            }

            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        MaterialButton addTopicBtn = dialog.findViewById(R.id.addTopicBtn);
        MaterialButton addFolderBtn = dialog.findViewById(R.id.addFolderBtn);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        addTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this,"Create a topic is clicked",Toast.LENGTH_SHORT).show();
            }
        });

        addFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this,"Create a folder is Clicked",Toast.LENGTH_SHORT).show();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}