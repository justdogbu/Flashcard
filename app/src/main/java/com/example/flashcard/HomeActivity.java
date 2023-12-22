package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flashcard.model.user.User;
import com.example.flashcard.utils.Constant;
import com.example.flashcard.utils.Utils;
import com.example.flashcard.viewmodel.HomeDataViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

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
    private ActivityResultLauncher<Intent> launchSettingsForResult;
    private SharedPreferences sharedPref;
    private HomeDataViewModel userViewModel;
    private BroadcastReceiver connectivityReceiver;
    private final int REQUEST_INTERNET_PERMISSION = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        launchSettingsForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == RESULT_OK){
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        sharedPref = getSharedPreferences(Constant.SHARE_PREF, Context.MODE_PRIVATE);
        if (sharedPref.getString(Constant.USER_DATA, null) == null) {
            backtoIntro();
            return;
        } else {
            userViewModel = new ViewModelProvider(this).get(HomeDataViewModel.class);
            userViewModel.setUser(new Gson().fromJson(sharedPref.getString(Constant.USER_DATA, null), User.class));
        }

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
                    case R.id.nav_logout:
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(Constant.USER_DATA);
                        editor.apply();
                        backtoIntro();
                        return true;
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

        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    checkInternetConnection();
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);

        if (checkSelfPermission(android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.INTERNET}, REQUEST_INTERNET_PERMISSION
            );
        } else {
            checkInternetConnection();
        }
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

    private class StartActivityForResult {
    }

    private void backtoIntro() {
        Intent intro = new Intent(this, MainActivity.class);
        intro.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intro);
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            fetchData();
        } else {
            Utils.showSnackBar(getCurrentFocus(), "No Internet Connection");
        }
    }

    private void fetchData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_INTERNET_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchData();
            } else {
                Utils.showSnackBar(getCurrentFocus(), "Permission Denied");
            }
        }
    }
}