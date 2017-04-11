package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 4/3/17.
 */
public class MusicPlayer {
    private static MusicPlayer instance = new MusicPlayer();

    public static MusicPlayer instance() {
        return instance;
    }

    private MediaPlayer currentPlayer;
    private List<Song> playQueue;
    private int currentIndex;

    private List<MusicPlayerListener> listeners = new ArrayList<>();

    private Runnable playNextSong = () -> {
        currentIndex = (currentIndex + 1) % playQueue.size();
        currentPlayer = new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString()));
        currentPlayer.play();
        newSong();
    };

    private MusicPlayer() {
    }

    public void play(List<Song> songs, int index) {
        if (currentPlayer != null && currentPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            currentPlayer.stop();
        }
        playQueue = songs;
        currentIndex = index;
        currentPlayer = new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString()));
        currentPlayer.setOnEndOfMedia(playNextSong);
        currentPlayer.play();
        newSong();
    }

    public void resume() {
        if (currentPlayer != null) {
            currentPlayer.play();
            playingStatusChanged(MediaPlayer.Status.PLAYING);
        }
    }

    public boolean isStopped() {
        return currentPlayer == null || currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.STOPPED);
    }

    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING);
    }

    public void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
            playingStatusChanged(MediaPlayer.Status.PAUSED);
        }
    }

    public void stop() {
//        if (currentPlayer != null) {
//            currentPlayer.stop();
//        }
    }

    public boolean isPaused() {
        return currentPlayer != null && currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PAUSED);
    }

    public void previous() {
        if (currentPlayer.getCurrentTime().greaterThan(Duration.seconds(7))) {
            currentPlayer.seek(Duration.ZERO);
        } else {
            int newIndex = ((currentIndex - 1) % playQueue.size());
            if (newIndex < 0) {
                newIndex = newIndex + playQueue.size();
            }
            play(playQueue, newIndex);
        }
    }

    public void next() {
        play(playQueue, (currentIndex + 1) % playQueue.size());
    }

    private void newSong() {
        for (MusicPlayerListener listener : listeners) {
            listener.newSong(playQueue.get(currentIndex));
        }
    }

    private void playingStatusChanged(MediaPlayer.Status status) {
        for (MusicPlayerListener listener : listeners) {
            listener.playingStatusChanged(status);
        }
    }

    public void addListener(MusicPlayerListener listener) {
        listeners.add(listener);
    }
}
