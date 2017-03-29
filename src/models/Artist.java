package models;

import java.util.ArrayList;
import java.util.List;

public class Artist {
	public final String name;
	private List<Song> songs = new ArrayList<>();
	
	public Artist(final String name) {
		this.name = name;
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
