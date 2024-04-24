package com.example.moviereviewer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.moviereviewer.Adapters.ViewPagerAdapter;
import com.example.moviereviewer.Dialogs.AuthorizationDialog;
import com.example.moviereviewer.Fragments.ProfileFragment;
import com.example.moviereviewer.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements ProfileFragment.SignOutListener{

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    private static final String PREFS_FILE = "WatchStorm";
    private static final String PREF_LOGIN = "Login";
    private static final boolean PREF_SIGNED_IN = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSignedIn()){
            setupViews(getLogin());
        } else {
            showAuthorizationDialog();
        }
    }

    private void showAuthorizationDialog(){
        AuthorizationDialog authorizationDialog = new AuthorizationDialog();
        authorizationDialog.createDialog(this, false, R.layout.dialog_authorization);
        authorizationDialog.setOnSignInListener(new AuthorizationDialog.SignInListener() {
            @Override
            public void onSignIn(String login) {
                changeSignedStatus(true, login);
                setupViews(login);
            }
        });
    }

    private SharedPreferences.Editor getEditor(){
        sharedPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    private boolean checkSignedIn(){
        return getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
                .getBoolean(String.valueOf(PREF_SIGNED_IN), false);
    }

    private String getLogin(){
        return getSharedPreferences(PREFS_FILE, MODE_PRIVATE).getString(PREF_LOGIN, "Login");
    }

    private void setupViews(String login){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPagerAdapter = new ViewPagerAdapter(this, login);

        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setCurrentItem(1, false);
        viewPager2.setOffscreenPageLimit(5);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Add");
                        break;
                    case 1:
                        tab.setText("Movies");
                        break;
                    case 2:
                        tab.setText("Profile");
                        break;
                    default:
                        break;
                }
            }
        }).attach();
    }

    @Override
    public void onSignOut() {
        changeSignedStatus(false, "");
        showAuthorizationDialog();
    }

    private void changeSignedStatus(boolean isUserSignedIn, String login){
        editor = getEditor();
        editor.putString(PREF_LOGIN, login);
        editor.putBoolean(String.valueOf(PREF_SIGNED_IN), isUserSignedIn);
        editor.apply();
    }
}