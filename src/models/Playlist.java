package models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
	private String name;
	public List<Song> songs = new ArrayList<>();
	
	public Playlist(List<Song> songs) {
		this.songs.addAll(songs);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
