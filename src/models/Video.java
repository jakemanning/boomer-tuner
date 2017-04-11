package models;

import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class Video implements Category, Playable {
	private final String title;
	private final long seconds;

	public String getTitle() {
		return title;
	}

	public String getDuration() {
		return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
	}

	public URI getUri() {
		return uri;
	}

	private final URI uri;

	public Video(String title, long seconds, URI uri) {
		this.title = title;
		this.seconds = seconds;
		this.uri = uri;
    }
    
    public static Video from(final URI path) {
		File file = new File(path);
		long seconds;
		try {
			IsoFile isoFile = new IsoFile(file);
			MovieHeaderBox movieHeaderBox = isoFile.getMovieBox().getMovieHeaderBox();
			seconds = movieHeaderBox.getDuration() / 1000; // convert millis to seconds
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new Video(file.getName(), seconds, path);
	}
    
    public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".mov") || normalized.endsWith(".mp4") || normalized.endsWith(".m3u8")
				|| normalized.endsWith(".m4v");
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(title, video.title) &&
				Objects.equals(seconds, video.seconds) &&
				Objects.equals(uri, video.uri);
    }

    @Override
    public int hashCode() {
		return Objects.hash(title, seconds, uri);
	}

    @Override
    public String toString() {
        return title;
    }
}

	