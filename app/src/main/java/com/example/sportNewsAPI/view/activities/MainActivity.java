package com.example.sportNewsAPI.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sportNewsAPI.R;
import com.example.sportNewsAPI.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private AppBarConfiguration mAppBarConfiguration;
    NavHostFragment navHostFragment;
    NavController navController;

    ActivityMainBinding activityMainBinding;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);
        setSupportActionBar(activityMainBinding.appBarMainId.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_category, R.id.nav_bookmarks, R.id.nav_feedback)
                .setOpenableLayout(activityMainBinding.drawerLayout)
                .build();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.nav_category) {

                Toast.makeText(getApplicationContext(), "Category", Toast.LENGTH_LONG).show();

            } else if (id == R.id.nav_bookmarks) {

                Toast.makeText(getApplicationContext(), "Bookmarks", Toast.LENGTH_LONG).show();

            } else if (id == R.id.nav_feedback) {

                Toast.makeText(getApplicationContext(), "Feedback", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to quit from this app?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("YES",
                (dialog, which) -> {
                    moveTaskToBack(true);
                    finish();
                });
        alert = builder.create();
        alert.setTitle("AlertDialogExample");
        builder.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    protected void onStop() {
        super.onStop();
        deleteCache(getApplicationContext());
        Toast.makeText(MainActivity.this, " MainActivity onStop()", Toast.LENGTH_LONG).show();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);

        } catch (Exception e) {
            Log.d("MainActivity", "Deleted");
        }
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if(children!=null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (alert != null) {
            alert.dismiss();
            alert = null;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityMainBinding = null;
         setSupportActionBar(null);
        if (mAppBarConfiguration != null) {
            mAppBarConfiguration = null;
        }

        navHostFragment = null;
        navController = null;

        deleteCache(getApplicationContext());
    }
}
