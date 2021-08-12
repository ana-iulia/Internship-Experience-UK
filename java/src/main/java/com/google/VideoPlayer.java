package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;
    private final PlaylistLibrary playlistLibrary;
    private HashMap<Video, String> flaggedVideos;
    private Video playingVideo;
    private boolean paused;


    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        this.playingVideo = null;
        this.paused = false;
        this.playlistLibrary = new PlaylistLibrary();
        this.flaggedVideos = new HashMap<>();
    }

    private void showVideos(List<Video> videos) {
        for (Video video : videos) {
            String tags = "";
            List<String> tagsList = video.getTags();
            for (int i = 0; i < tagsList.size(); i++) {
                if (i < tagsList.size() - 1) {
                    tags += tagsList.get(i) + " ";
                } else {
                    tags += tagsList.get(i);
                }

            }
            String flagged = "";
            if (this.flaggedVideos.containsKey(video)) {
                flagged = " - FLAGGED (reason: " + this.flaggedVideos.get(video) + ")";
            }
            System.out.println(video.getTitle() + " (" + video.getVideoId() + ") [" + tags + "]" + flagged);
        }
    }

    private void showVideosNumbered(List<Video> videos) {
        int nr = 0;
        for (Video video : videos) {
            nr++;
            String tags = "";
            List<String> tagsList = video.getTags();
            for (int i = 0; i < tagsList.size(); i++) {
                if (i < tagsList.size() - 1) {
                    tags += tagsList.get(i) + " ";
                } else {
                    tags += tagsList.get(i);
                }

            }
            System.out.println(nr + ") " + video.getTitle() + " (" + video.getVideoId() + ") [" + tags + "]");
        }
    }


    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos() {
        System.out.println("Here's a list of all available videos:");
        List<Video> videos = videoLibrary.getVideos()
                .stream()
                .sorted((a, b) -> a.getTitle().compareTo(b.getTitle()))
                .collect(Collectors.toList());

        showVideos(videos);

    }

    public void playVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null) {
            System.out.println("Cannot play video: Video does not exist");
        } else {
            if (this.flaggedVideos.containsKey(video)) {
                System.out.printf("Cannot play video: Video is currently flagged (reason: %s)\n", this.flaggedVideos.get(video));
            } else {
                if (this.playingVideo == null) {
                    System.out.printf("Playing video: %s\n", video.getTitle());
                    this.playingVideo = video;
                    this.paused = false;
                } else {
                    System.out.printf("Stopping video: %s\n", this.playingVideo.getTitle());
                    System.out.printf("Playing video: %s\n", video.getTitle());
                    this.playingVideo = video;
                    this.paused = false;
                }
            }

        }

    }

    public void stopVideo() {
        if (this.playingVideo == null) {
            System.out.println("Cannot stop video: No video is currently playing");
        } else {
            System.out.printf("Stopping video: %s\n", this.playingVideo.getTitle());
            this.playingVideo = null;
        }

    }

    public void playRandomVideo() {
        List<Video> availableVideos = new ArrayList<>();
        for (Video video : videoLibrary.getVideos()) {
            if (!this.flaggedVideos.containsKey(video)) {
                availableVideos.add(video);
            }
        }
        if (availableVideos.size() == 0) {
            System.out.println("No videos available");
        } else {
            int rnd = new Random().nextInt(availableVideos.size());
            Video randomVideoToPlay = availableVideos.get(rnd);
            playVideo(randomVideoToPlay.getVideoId());
        }
    }

    public void pauseVideo() {
        if (this.playingVideo == null) {
            System.out.println("Cannot pause video: No video is currently playing");
        } else {
            if (this.paused) {
                System.out.printf("Video already paused: %s\n", this.playingVideo.getTitle());
            } else {
                System.out.printf("Pausing video: %s\n", this.playingVideo.getTitle());
                this.paused = true;

            }
        }

    }

    public void continueVideo() {
        if (this.playingVideo == null) {
            System.out.println("Cannot continue video: No video is currently playing");
        } else {
            if (!this.paused) {
                System.out.println("Cannot continue video: Video is not paused");
            } else {
                System.out.printf("Continuing video: %s\n", this.playingVideo.getTitle());
                this.paused = false;
            }
        }
    }

    public void showPlaying() {
        if (this.playingVideo == null) {
            System.out.println("No video is currently playing");
        } else {
            String status = "";
            if (this.paused) {
                status = " - PAUSED";
            }
            String tags = "";
            List<String> tagsList = this.playingVideo.getTags();
            for (int i = 0; i < tagsList.size(); i++) {
                if (i < tagsList.size() - 1) {
                    tags += tagsList.get(i) + " ";
                } else {
                    tags += tagsList.get(i);
                }

            }
            String information = "Currently playing: %s (%s) [%s]" + status + "\n";
            System.out.printf(information, this.playingVideo.getTitle(), this.playingVideo.getVideoId(), tags);
        }
    }

    public void createPlaylist(String playlistName) {

        if (this.playlistLibrary.getPlaylistByTitle(playlistName) != null) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");

        } else {
            VideoPlaylist videoPlaylist = new VideoPlaylist(playlistName);
            this.playlistLibrary.addPlaylist(videoPlaylist);
            System.out.printf("Successfully created new playlist: %s\n", playlistName);
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        if (this.playlistLibrary.getPlaylistByTitle(playlistName) == null) {
            System.out.printf("Cannot add video to %s: Playlist does not exist\n", playlistName);
        } else {
            Video video = this.videoLibrary.getVideo(videoId);
            if (video == null) {
                System.out.printf("Cannot add video to %s: Video does not exist\n", playlistName);
            } else {
                if (this.flaggedVideos.containsKey(video)) {
                    System.out.printf("Cannot add video to %s: Video is currently flagged (reason: %s)\n", playlistName, this.flaggedVideos.get(video));
                } else if (this.playlistLibrary.playlistContainsVideo(playlistName, videoId)) {
                    System.out.printf("Cannot add video to %s: Video already added\n", playlistName);
                } else {
                    this.playlistLibrary.addVideoToPlaylist(playlistName, video);
                    System.out.printf("Added video to %s: %s\n", playlistName, video.getTitle());
                }
            }
        }
    }

    public void showAllPlaylists() {
        List<String> playList = this.playlistLibrary.getPlayListNames();
        if (playList.size() == 0) {
            System.out.println("No playlists exist yet");
        } else {
            System.out.println("Showing all playlists:");
            playList.forEach(x -> System.out.println(x));
        }

    }

    public void showPlaylist(String playlistName) {
        if (this.playlistLibrary.getPlaylistByTitle(playlistName) == null) {
            System.out.printf("Cannot show playlist %s: Playlist does not exist\n", playlistName);
        } else {
            System.out.printf("Showing playlist: %s\n", playlistName);
            List<Video> playListVideos = this.playlistLibrary.getPlaylistVideos(playlistName);
            if (playListVideos.size() == 0) {
                System.out.println("No videos here yet");
            } else {
                showVideos(playListVideos);
            }

        }
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        if (this.playlistLibrary.getPlaylistByTitle(playlistName) == null) {
            System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);
        } else {
            if (this.videoLibrary.getVideo(videoId) == null) {
                System.out.printf("Cannot remove video from %s: Video does not exist\n", playlistName);
            } else {
                if (!this.playlistLibrary.playlistContainsVideo(playlistName, videoId)) {
                    System.out.printf("Cannot remove video from %s: Video is not in playlist\n", playlistName);
                } else {
                    Video video = this.videoLibrary.getVideo(videoId);
                    this.playlistLibrary.removeVideoFromPlaylist(playlistName, video);
                    System.out.printf("Removed video from %s: %s\n", playlistName, video.getTitle());
                }
            }

        }
    }

    public void clearPlaylist(String playlistName) {
        if (this.playlistLibrary.getPlaylistByTitle(playlistName) == null) {
            System.out.printf("Cannot clear playlist %s: Playlist does not exist\n", playlistName);
        } else {
            this.playlistLibrary.clearPlaylist(playlistName);
            System.out.printf("Successfully removed all videos from %s\n", playlistName);
        }
    }

    public void deletePlaylist(String playlistName) {
        if (this.playlistLibrary.getPlaylistByTitle(playlistName) == null) {
            System.out.printf("Cannot delete playlist %s: Playlist does not exist\n", playlistName);
        } else {
            this.playlistLibrary.deletePlaylist(playlistName);
            System.out.printf("Deleted playlist: %s\n", playlistName);
        }
    }

    public void searchVideos(String searchTerm) {
        List<Video> videos = new ArrayList<>();
        for (Video video : this.videoLibrary.getVideosByTerm(searchTerm)) {
            if (!this.flaggedVideos.containsKey(video)) {
                videos.add(video);
            }
        }
        videos = videos
                .stream()
                .sorted((a, b) -> a.getTitle().compareTo(b.getTitle()))
                .collect(Collectors.toList());
        if (videos.size() == 0) {
            System.out.printf("No search results for %s\n", searchTerm);
        } else {
            System.out.printf("Here are the results for %s:\n", searchTerm);
            showVideosNumbered(videos);
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
            System.out.println("If your answer is not a valid number, we will assume it's a no.");
            var scanner = new Scanner(System.in);
            var input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                if (number >= 1 && number <= videos.size()) {
                    playVideo(videos.get(number - 1).getVideoId());
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    public void searchVideosWithTag(String videoTag) {
        List<Video> videos = new ArrayList<>();
        for (Video video : this.videoLibrary.getVideosByTag(videoTag)) {
            if (!this.flaggedVideos.containsKey(video)) {
                videos.add(video);
            }
        }
        videos = videos
                .stream()
                .sorted((a, b) -> a.getTitle().compareTo(b.getTitle()))
                .collect(Collectors.toList());
        if (videos.size() == 0) {
            System.out.printf("No search results for %s\n", videoTag);
        } else {
            System.out.printf("Here are the results for %s:\n", videoTag);
            showVideosNumbered(videos);
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
            System.out.println("If your answer is not a valid number, we will assume it's a no.");
            var scanner = new Scanner(System.in);
            var input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                if (number >= 1 && number <= videos.size()) {
                    playVideo(videos.get(number - 1).getVideoId());
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    public void flagVideo(String videoId) {
        flagVideo(videoId, "Not supplied");
    }

    public void flagVideo(String videoId, String reason) {
        Video video = this.videoLibrary.getVideo(videoId);
        if (video == null) {
            System.out.println("Cannot flag video: Video does not exist");
        } else {
            if (this.flaggedVideos.containsKey(video)) {
                System.out.println("Cannot flag video: Video is already flagged");
            } else {
                this.flaggedVideos.put(video, reason);
                if (this.playingVideo != null && this.playingVideo.equals(video)) {
                    this.playingVideo = null;
                    this.paused = false;
                    System.out.printf("Stopping video: %s\n", video.getTitle());
                }
                System.out.printf("Successfully flagged video: %s (reason: %s)\n", video.getTitle(), reason);
            }
        }
    }

    public void allowVideo(String videoId) {
        Video video = this.videoLibrary.getVideo(videoId);
        if (video == null) {
            System.out.println("Cannot remove flag from video: Video does not exist");
        } else {
            if (!this.flaggedVideos.containsKey(video)) {
                System.out.println("Cannot remove flag from video: Video is not flagged");
            } else {
                System.out.printf("Successfully removed flag from video: %s\n", video.getTitle());
            }
        }
    }
}