package com.example.simplevideoplayerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simplevideoplayerapp.R;
import com.example.simplevideoplayerapp.Utils.VideoPlayerConfig;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private CardView btnPlayUrlVideo, btnPlayDefaultVideo;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayUrlVideo = findViewById(R.id.btnPlayUrlVideo);
        btnPlayDefaultVideo = findViewById(R.id.btnPlayDefaultVideo);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        btnPlayUrlVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogPrompt();
                    }
                }
        );

        btnPlayDefaultVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent playerIntent = ExoPlayerActivity.getStartIntent(
                                MainActivity.this,
                                VideoPlayerConfig.DEFAULT_VIDEO_URL
                        );
                        startActivity(playerIntent);
                    }
                }
        );
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
}
