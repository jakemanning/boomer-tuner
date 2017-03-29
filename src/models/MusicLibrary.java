package models;

import java.util.List;

public class MusicLibrary {
	private static MusicLibrary instance = new MusicLibrary();
	private List<Song> songs;
	private List<Album> albums;
	private List<Artist> artists;
	
	public static MusicLibrary instance() {
		return instance;
	}
	
	private MusicLibrary() {
	}
	
	private static void initializeSongs() {
		
	}
}
