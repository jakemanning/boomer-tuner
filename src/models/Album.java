package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jaudiotagger.tag.images.Artwork;

public class Album {
	private final String name;
	private final Artwork artwork;
	private Artist artist;
	private List<Song> songs = new ArrayList<>();
	private int year;

	public Album(final String name, final Artwork artwork) {
		this.name = name;
		this.artwork = artwork;
	}

	public void addSong(final Song song) {
		artist = song.getArtist();
		songs.add(song);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Album album = (Album) o;

		if (!Objects.equals(this.name, album.getName()))
			return false;
		if (!Objects.equals(this.artist, album.getArtist()))
			return false;
		if (!Objects.equals(this.songs, album.getSongs()))
			return false;
		if (this.year != album.getYear())
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + artist.hashCode();
		result = 31 * result + songs.hashCode();
		result = 31 * result + year;
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public Artwork getArtwork() {
		return artwork;
	}

	public String getName() {
		return name;
	}

	public Artist getArtist() {
		return artist;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public int getYear() {
		return year;
	}
}
