package models;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

public class Image implements Category, Serializable{
	private final String name;
	private final URI uri;
	
	public Image(URI uri){
		this.uri = uri;
		name = uri.toString().substring(0, uri.toString().length()-3);
	}
	
	public static Image from(URI uri){
		return new Image(uri);
	}
	
	public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".jpg") || normalized.endsWith(".png");
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Image image = (Image) o;
		return name == image.getName() && uri == image.getUri();
	}

	public String getName() {
		return name;
	}

	public URI getUri() {
		return uri;
	}
}
