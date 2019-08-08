package com.example.simplevideoplayerapp.Utils;

public class VideoPlayerConfig {

    // Minimum video you want to buffer while playing
    public final static int MIN_BUFFER_DURATION = 3000;
    //Maximum video you want to buffer during playback
    public final static int MAX_BUFFER_DURATION = 5000;
    //Min video you want to buffer before start playing
    public final static int MIN_PLAYBACK_START_BUFFER = 1500;
    //Min video you want to buffer when user resumes the video
    public final static int MIN_PLAYBACK_RESUME_BUFFER = 5000;

    public final static String DEFAULT_VIDEO_URL = "https://player.vimeo.com/external/347958130.sd.mp4?s=adf9c3b4908c7a4b3d2ce78fe1d3c6668721ef86&profile_id=165";
}
