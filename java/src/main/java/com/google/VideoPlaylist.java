package com.google;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to represent a Playlist
 */
class VideoPlaylist {

    private final String title;
    private final List<Video> videoList;

    VideoPlaylist(String title) {
        this.title = title;
        this.videoList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Video> getVideoList() {
        return videoList;
    }
}
