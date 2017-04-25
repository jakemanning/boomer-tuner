package models;

import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class Song implements Category, Playable, Serializable {
	private final String title;
	private final Artist artist;
	private final int track;
	private final int length;
	private final URI uri;
	private final Album album;

	public Song(final String title, final Artist artist, final Album album, final int track,
				final int seconds, final URI uri) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.track = track;
		this.length = seconds;
		this.uri = uri;

		if (artist != null) {
			artist.addSong(this);
		}
		if (album != null) {
			album.addSong(this);
		}
	}

	public static Song from(final URI uri) {
		try {
			AudioFile f = AudioFileIO.read(new File(uri));
			Tag tag = f.getTag();
			Artist artist = new Artist(tag.getFirst(FieldKey.ARTIST));
			Artwork artwork = tag.getFirstArtwork();
			Album album = new Album(tag.getFirst(FieldKey.ALBUM), artwork);
			String title = tag.getFirst(FieldKey.TITLE);
			String trackStr = tag.getFirst(FieldKey.TRACK);
			Integer seconds = f.getAudioHeader().getTrackLength() + 1;

			int track = trackStr.isEmpty() ? -1 : Integer.valueOf(trackStr);
			return new Song(title, artist, album, track, seconds, uri);
		} catch (CannotReadException | IOException | TagException | InvalidAudioFrameException
				| ReadOnlyFileException e) {
			e.printStackTrace();
			return null;
		}
	}

	Artwork getArtwork() {
		try {
			AudioFile f = AudioFileIO.read(new File(uri));
			Tag tag = f.getTag();
			return tag.getFirstArtwork();
		} catch (CannotReadException | IOException | TagException | InvalidAudioFrameException
				| ReadOnlyFileException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".mp3") || normalized.endsWith(".m4a");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Song song = (Song) o;
		return track == song.track && length == song.length && Objects.equals(title, song.title)
				&& Objects.equals(artist, song.artist) && Objects.equals(uri, song.uri)
				&& Objects.equals(album, song.album);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, artist.getName(), track, length, uri, album.getName());
	}

	@Override
	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public Artist getArtist() {
		return artist;
	}

	public Album getAlbum() {
		return album;
	}

	public int getTrack() {
		return track;
	}

	public String getDuration() {
		return Playable.format(Duration.seconds(length));
	}

	public URI getUri() {
		return uri;
	}
}