package utils;

public enum Category {
	Songs,
	Playlists,
	Albums,
	Artists;
	
	@Override
	public String toString() {
		return this.name();
	}
}
