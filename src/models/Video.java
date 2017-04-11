package models;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

import utils.Category;

/**
 * Created by bryancapps on 4/3/17.
 */
public class Video implements Category {
    public final String title;
    public final Duration duration;
    public final URI uri;

    public Video(String title, Duration duration, URI uri) {
        this.title = title;
        this.duration = duration;
        this.uri = uri;
    }
    
    public static Video from(final URI path) {
        // TODO: Get Duration
        File file = new File(path);
        return new Video(file.getName(), null, path);
    }
    
    public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".mov") || normalized.endsWith(".mp4") || normalized.endsWith(".avi")
				|| normalized.endsWith(".m4v");
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(title, video.title) &&
                Objects.equals(duration, video.duration) &&
                Objects.equals(uri, video.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, duration, uri);
    }

    @Override
    public String toString() {
        return title;
    }
}

	