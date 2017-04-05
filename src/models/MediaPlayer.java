package models;

import java.util.Collections;
import java.util.List;

/**
 * Created by bryancapps on 4/3/17.
 */
public class MediaPlayer {
    private static MediaPlayer ourInstance = new MediaPlayer();

    public static MediaPlayer instance() {
        return ourInstance;
    }

    private javafx.scene.media.MediaPlayer currentPlayer;
    private Song currentSong;

    private MediaPlayer() {
    }

    public void play(List<Song> songs) {
//        currentSong = songs.get(0);
//        currentPlayer = new javafx.scene.media.MediaPlayer(new Media(currentSong.uri.toString()));
//        currentPlayer.setOnEndOfMedia(() -> {
//            int nextIndex = songs.indexOf(currentSong) + 1;
//            if (nextIndex >= songs.size()) {
//                return;
//            }
//            currentSong = songs.get(nextIndex);
//            currentPlayer = new javafx.scene.media.MediaPlayer(new Media(currentSong.uri.toString()));
//        });
    }

    public void play(Song song) {
        play(Collections.singletonList(song));
    }

    public void play() {

    }

    public boolean isStopped() {
        return currentPlayer == null || currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.STOPPED);
    }

    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING);
    }

    public void pause() {
//        if (currentPlayer != null) {
//            currentPlayer.pause();
//        }
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

    }

    public void next() {

    }

    //TODO: make this observable so play icon, song title, and artwork can change accordingly
}
