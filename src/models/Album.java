package models;

import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.tag.images.Artwork;

public class Album {
	public final String name;
	public final Artwork artwork;
	public Artist artist;
	private List<Song> songs = new ArrayList<>();
	private int year;

	public Album(final String name, final Artwork artwork) {
		this.name = name;
		this.artwork = artwork;
	}

	public void addSong(final Song song) {
		artist = song.artist;
		songs.add(song);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Album album = (Album) o;
		// System.out.println(album.artist);
		if (name != album.name)
			return false;
		if (!artist.equals(album.artist))
			return false;
		if (!songs.equals(album.songs))
			return false;
		return year != album.year;
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
}
