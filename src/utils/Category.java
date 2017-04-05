package utils;

public enum Category {
	Songs,
	Playlists,
	Albums,
    Artists,
    Videos;

    @Override
    public String toString() {
        return this.name();
	}
}
