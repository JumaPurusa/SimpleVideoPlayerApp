package com.example.simplevideoplayerapp.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.simplevideoplayerapp.Adapters.VideosAdapter;
import com.example.simplevideoplayerapp.Models.Video;
import com.example.simplevideoplayerapp.R;
import com.google.android.exoplayer2.source.MediaSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment {

    private static final String TAG = VideosFragment.class.getSimpleName();
    private static final int MY_REQUEST_PERMISSION = 101;

    private RecyclerView videoListView;
    private VideosAdapter videosAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Video> videos;

    //private ProgressBar progressBar;
    
    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoListView = view.findViewById(R.id.staggered_recycler_view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        videoListView.setLayoutManager(staggeredGridLayoutManager);
        videos = new ArrayList<>();

//        progressBar = view.findViewById(R.id.progress_bar);
//
//        if(!progressBar.isShown())
//            progressBar.setVisibility(View.VISIBLE);
    }

    private void getAllVideosFromGallery(){
        Uri uri;
        Cursor mCursor;
        int COLUMN_INDEX_DATA, COLUMN_INDEX_NAME, COLUMN_ID, COLUMN_THUMB;
        String absolutePathOfFile = null;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        mCursor = getActivity().getApplicationContext()
                .getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        COLUMN_INDEX_DATA = mCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        COLUMN_INDEX_NAME = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        COLUMN_ID = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        COLUMN_THUMB = mCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while(mCursor.moveToNext()){
            absolutePathOfFile = mCursor.getString(COLUMN_INDEX_DATA);
            Log.d(TAG, "getAllVideosFromGallery: absolutePathOfFile : " + absolutePathOfFile);
            Log.d(TAG, "getAllVideosFromGallery: Folder: " + mCursor.getString(COLUMN_INDEX_NAME));
            Log.d(TAG, "getAllVideosFromGallery: column_id : " + mCursor.getString(COLUMN_ID));
            Log.d(TAG, "getAllVideosFromGallery: thumbnail : " + mCursor.getString(COLUMN_THUMB));

            Video video = new Video();
            video.setmFilePath(absolutePathOfFile);
            video.setVideoThumbnail(mCursor.getString(COLUMN_THUMB));
            video.setSelected(false);

            videos.add(video);
        }

//        if(progressBar.isShown())
//            progressBar.setVisibility(View.GONE);

        videosAdapter = new VideosAdapter(getActivity(), videos);
        videoListView.setAdapter(videosAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 23){

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){

                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity)getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                )){

                    ActivityCompat.requestPermissions((Activity)getContext(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_PERMISSION);
                }else{
                    ActivityCompat.requestPermissions((Activity)getContext(),
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, MY_REQUEST_PERMISSION);
                }

            }else{

                    getAllVideosFromGallery();
            }

        }else{
                getAllVideosFromGallery();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case MY_REQUEST_PERMISSION:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                    if(ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: permission granted");
                        getAllVideosFromGallery();
                    }
                }else{
                    Log.d(TAG, "onRequestPermissionsResult: No permission granted");
                    getActivity().finish();
                }

                return;
            }

        }
    }
}
