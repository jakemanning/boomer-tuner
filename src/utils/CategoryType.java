package utils;

import models.*;

public enum CategoryType {
	Songs (Song.class),
	Playlists (Playlist.class),
	Albums(Album.class),
	Artists(Artist.class),
	Videos(Video.class),
	Images(Image.class);

	Class<? extends Category> aClass;

	CategoryType(Class<? extends Category> aClass) {
		this.aClass = aClass;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

	public static CategoryType of(Category category) {
		for (CategoryType categoryType : CategoryType.values()) {
			if (categoryType.aClass.equals(category.getClass())) {
				return categoryType;
			}
		}
		return null;
	}
}
