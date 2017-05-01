package models;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Image implements Category, Serializable{
	private final String name;
	private final URI uri;
	
	public Image(final String name, final URI uri){
		this.uri = uri;
		this.name = name;
	}
	
	public static Image from(final URI uri){
		return new Image(Paths.get(uri).getFileName().toString() ,uri);
	}
	
	public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".jpg") || normalized.endsWith(".png") || normalized.endsWith(".jpeg");
	}
	
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Image image = (Image) o;
		return Objects.equals(name, image.getName()) && Objects.equals(uri, image.getUri());
	}

	public String getName() {
		return name;
	}

	public URI getUri() {
		return uri;
	}

	@Override
	public String toString() {
		return name;
	}
}
