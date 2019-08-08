package com.example.simplevideoplayerapp.Models;

public class Video {
    private String mFilePath, videoThumbnail;
    private boolean isSelected;

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
