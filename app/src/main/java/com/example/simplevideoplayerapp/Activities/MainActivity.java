package com.example.simplevideoplayerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simplevideoplayerapp.Fragments.VideosFragment;
import com.example.simplevideoplayerapp.R;
import com.example.simplevideoplayerapp.Utils.VideoPlayerConfig;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SELECTED_ITEM = "selected_item";
    //private CardView btnPlayUrlVideo, btnPlayDefaultVideo;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private VideosFragment videosFragment;

    MenuItem mMenuItem;
    int mMenuItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setSupportActionBar((Toolbar)findViewById(R.id.mToolbar));
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //btnPlayUrlVideo = findViewById(R.id.btnPlayUrlVideo);
        //btnPlayDefaultVideo = findViewById(R.id.btnPlayDefaultVideo);
        drawerLayout = findViewById(R.id.drawer_layout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

//        btnPlayUrlVideo.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDialogPrompt();
//                    }
//                }
//        );
//
//        btnPlayDefaultVideo.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent playerIntent = ExoPlayerActivity.getStartIntent(
//                                MainActivity.this,
///                                VideoPlayerConfig.DEFAULT_VIDEO_URL
//                        );
//                        startActivity(playerIntent);
//                    }
//                }
//        );

        drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = findViewById(R.id.nav);

        if(savedInstanceState != null){
            mMenuItemSelected = savedInstanceState.getInt(SELECTED_ITEM, 0);
            mMenuItem = navigationView.getCheckedItem();
        }else{
            mMenuItem = navigationView.getMenu().getItem(0);
        }

        selectItem(mMenuItem);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectItem(menuItem);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }

    private void selectItem(MenuItem menuItem){

        switch(menuItem.getItemId()){
            case R.id.navHome:
                videosFragment = new VideosFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction
                        = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFrame, videosFragment)
                        .commitNow();
                break;

            case R.id.navURL:
                showDialogPrompt();
                break;

            case R.id.navAbout:
                Intent defaultIntent = ExoPlayerActivity.
                        getStartIntent(MainActivity.this, VideoPlayerConfig.DEFAULT_VIDEO_URL);
                startActivity(defaultIntent);
                break;
        }

    }

    private void showDialogPrompt(){

        // get dialog.prompt.xml view
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText userInputUrl = view.findViewById(R.id.editTextDialogUrlInput);

        // set dialog.prompt.xml to dialog
        builder.setView(view);

        builder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                boolean isURL = Patterns.WEB_URL.matcher(userInputUrl
                                .getText().toString().trim()).matches();

                                if(isURL){
                                    Intent playerIntent =
                                            ExoPlayerActivity.getStartIntent(
                                                    MainActivity.this,
                                                    userInputUrl.getText().toString().trim()
                                            );
                                    startActivity(playerIntent);
                                }else{
                                    Snackbar.make(
                                            coordinatorLayout,
                                            "URL is not Valid",
                                            2000
                                    ).show();
                                }
                            }
                        })

                .setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM, mMenuItemSelected);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
