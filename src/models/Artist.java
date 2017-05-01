package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Artist implements Category, Serializable {
	private final String name;
	private List<Album> albums = new ArrayList<>();

	public Artist(final String name) {
		this.name = name;
	}

	public void addSong(final Song song) {
		albums.add(song.getAlbum());
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Artist artist = (Artist) o;

		return Objects.equals(this.name, artist.getName());
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + albums.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Song> getSongs() {
		List<Song> songs = new ArrayList<>();
		albums.forEach(alb -> songs.addAll(alb.getSongs()));
		return songs;
	}
}
