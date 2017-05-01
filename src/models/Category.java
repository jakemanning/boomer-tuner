package models;

import java.nio.file.Path;

public interface Category {
	public static Category from(final Path path) {
		if (Song.accepts(path)) {
			return Song.from(path.toUri());
		} else if (Video.accepts(path)) {
			return Video.from(path.toUri());
		} else if (Image.accepts(path)){
			return Image.from(path.toUri());
		} else {
			return null;
		}
	}
}
