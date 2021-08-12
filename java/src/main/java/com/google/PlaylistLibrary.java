package com.google;

import java.util.ArrayList;
import java.util.List;

public class PlaylistLibrary {
    private List<VideoPlaylist> playList;

    public PlaylistLibrary() {
        this.playList = new ArrayList<>();
    }

    public VideoPlaylist getPlaylistByTitle(String title) {
        for (VideoPlaylist video : this.playList) {
            if (video.getTitle().equalsIgnoreCase(title)) {
                return video;
            }
        }
        return null;
    }

    public void addPlaylist(VideoPlaylist videoPlaylist) {
        this.playList.add(videoPlaylist);
    }

    public void addVideoToPlaylist(String playlistName, Video video) {
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        videoPlaylist.getVideoList().add(video);
    }

    public void removeVideoFromPlaylist(String playlistName, Video video) {
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        videoPlaylist.getVideoList().remove(video);
    }

    public boolean playlistContainsVideo(String playlistName, String videoId) {
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        for (Video video : videoPlaylist.getVideoList()) {
            if (video.getVideoId().equalsIgnoreCase(videoId)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPlayListNames() {
        List<String> playListTitles = new ArrayList<>();
        this.playList
                .stream()
                .sorted((a, b) -> a.getTitle().compareTo(b.getTitle()))
                .forEach(x -> playListTitles.add(x.getTitle()));
        return playListTitles;
    }

    public List<Video> getPlaylistVideos(String playlistName) {
        List<Video> videos = new ArrayList<>();
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        videoPlaylist.getVideoList().forEach(x -> videos.add(x));
        return videos;
    }

    public void clearPlaylist(String playlistName) {
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        videoPlaylist.getVideoList().clear();
    }

    public void deletePlaylist(String playlistName) {
        VideoPlaylist videoPlaylist = getPlaylistByTitle(playlistName);
        this.playList.remove(videoPlaylist);
    }
}
