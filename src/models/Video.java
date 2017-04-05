package models;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

/**
 * Created by bryancapps on 4/3/17.
 */
public class Video {
    public final String title;
    public final Duration duration;
    public final URI uri;

    public Video(String title, Duration duration, URI uri) {
        this.title = title;
        this.duration = duration;
        this.uri = uri;
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
