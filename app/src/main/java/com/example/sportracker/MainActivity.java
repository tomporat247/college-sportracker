package com.example.sportracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sportracker.Database.AppDatabase;
import com.example.sportracker.Database.FirestoreCacheHandler;
import com.example.sportracker.Utils.CircleTransform;
import com.example.sportracker.Utils.DrawerLocker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements DrawerLocker {
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.drawer = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile)
                .setDrawerLayout(this.drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        AppDatabase.setupDatabase(getApplicationContext());
        FirestoreCacheHandler.cacheServerContests();
        this.showUserDataInDrawerHeader();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        this.drawer.setDrawerLockMode(lockMode);
    }

    private void showUserDataInDrawerHeader() {
        View headerView = this.navigationView.getHeaderView(0);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) headerView.findViewById(R.id.drawerUserEmail)).setText(user.getEmail());
        ((TextView) headerView.findViewById(R.id.drawerUserName)).setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).transform(new CircleTransform()).into((ImageView) headerView.findViewById(R.id.drawerUserImage));
    }
}