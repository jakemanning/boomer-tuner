package models;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by bryancapps on 3/26/17.
 */
public class Song {
    public final String title;
    public final String artist;
    public final String album;
    public final int track;
    public final URI uri;

    public Song(String title, String artist, String album, int track, URI uri) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.track = track;
        this.uri = uri;
    }

    public static Song from(URI uri) {
        try {
            AudioFile f = AudioFileIO.read(new File(uri));
            Tag tag = f.getTag();
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String title = tag.getFirst(FieldKey.TITLE);
            int track = Integer.valueOf(tag.getFirst(FieldKey.TRACK));
            return new Song(title, artist, album, track, uri);
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            e.printStackTrace();
            return null;
        }
    }

    public URI getUri() {
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