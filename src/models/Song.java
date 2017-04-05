package models;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

/**
 * Created by bryancapps on 3/26/17.
 */
public class Song {
	private final String title;
	private final Artist artist;
	private final Album album;
	private final int track;
	private final URI uri;

	public Song(final String title, final Artist artist, final Album album, final int track, final Artwork artwork,
			final URI uri) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.track = track;
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

			int track = trackStr.isEmpty() ? -1 : Integer.valueOf(trackStr);
			return new Song(title, artist, album, track, artwork, uri);
		} catch (CannotReadException | IOException | TagException | InvalidAudioFrameException
				| ReadOnlyFileException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Song song = (Song) o;

		if (track != song.track)
			return false;
		if (!Objects.equals(title, song.getTitle()))
			return false;
		if (!Objects.equals(artist, song.getArtist()))
			return false;
		if (!Objects.equals(album, song.getAlbum()))
			return false;
		if (!Objects.equals(uri, song.getUri()))
			return false;
		return true;
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

	public URI getUri() {
		return uri;
	}
}