package com.example.simplevideoplayerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.simplevideoplayerapp.Activities.ExoPlayerActivity;
import com.example.simplevideoplayerapp.Models.Video;
import com.example.simplevideoplayerapp.R;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{

    private Context mContext;
    private List<Video> videos;

    public VideosAdapter(Context context, List<Video> list){
        this.videos = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(mContext)
                        .inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        final Video video = videos.get(position);

        Glide.with(mContext)
                .load(video.getVideoThumbnail())
                .into(holder.videoThumbnail);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent playerIntent =
                                ExoPlayerActivity.getStartIntent(mContext,
                                        video.getmFilePath());
                        mContext.startActivity(playerIntent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoThumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
        }
    }
}
