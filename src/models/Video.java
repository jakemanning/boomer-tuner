package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Video implements Category, Playable, Serializable {
	private final String title;
	private transient StringProperty duration = new SimpleStringProperty(Playable.format(Duration.ZERO));

	public String getTitle() {
		return title;
	}

	public StringProperty durationProperty() {
		if (duration == null) {
			duration = Video.from(this.uri).duration;
		}
		return duration;
	}

	public URI getUri() {
		return uri;
	}

	private final URI uri;

	private Video(final String title, final URI uri) {
		this.title = title;
		this.uri = uri;
	}

	public static Video from(final URI path) {
		Media media = new Media(path.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		Video video = new Video(Paths.get(path).getFileName().toString(), path);
		mediaPlayer.setOnReady(() -> video.duration.set(Playable.format(media.getDuration())));
		return video;
	}

	public static boolean accepts(final Path path) {
		final String normalized = path.toString().toLowerCase();
		return normalized.endsWith(".mov") || normalized.endsWith(".mp4") || normalized.endsWith(".m3u8")
				|| normalized.endsWith(".m4v");
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Video video = (Video) o;
		return Objects.equals(title, video.title) &&
				Objects.equals(uri, video.uri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, uri);
	}

	@Override
	public String toString() {
		return title;
	}
}
