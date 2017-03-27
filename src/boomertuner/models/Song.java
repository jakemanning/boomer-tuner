package boomertuner.models;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by bryancapps on 3/26/17.
 */
public class Song {
    public final String title;
    public final String artist;
    public final String album;
    public final int track;
    public final String uri;

    public Song(String title, String artist, String album, int track, String uri) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.uri = uri;
    }

    public static CompletableFuture<Song> from(String uri) {
        Media media = new Media(uri);

        ObservableMap<String, Object> metadata = media.getMetadata();
        CompletableFuture<String> title = new CompletableFuture<>();
        CompletableFuture<String> artist = new CompletableFuture<>();
        CompletableFuture<String> album = new CompletableFuture<>();
        CompletableFuture<Integer> track = new CompletableFuture<>();

        metadata.addListener((MapChangeListener<String, Object>) change -> {
            if (change.getKey().equals("title")) title.complete((String) change.getValueAdded());
            if (change.getKey().equals("artist")) artist.complete((String) change.getValueAdded());
            if (change.getKey().equals("album")) album.complete((String) change.getValueAdded());
            if (change.getKey().equals("track number")) track.complete((Integer) change.getValueAdded());
        });

        return CompletableFuture.allOf(title, artist, album, track)
                .thenApply(aVoid -> {
                    try {
                        return new Song(title.get(), artist.get(), album.get(), track.get(), uri);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public String getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (track != song.track) return false;
        if (!title.equals(song.title)) return false;
        if (!artist.equals(song.artist)) return false;
        if (!album.equals(song.album)) return false;
        return uri.equals(song.uri);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + artist.hashCode();
        result = 31 * result + album.hashCode();
        result = 31 * result + track;
        result = 31 * result + uri.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return title;
    }
}