package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Video implements Category, Playable {
	private final String title;
	private final StringProperty duration = new SimpleStringProperty(Playable.format(Duration.ZERO));

	public String getTitle() {
		return title;
	}

	public StringProperty durationProperty() {
		return duration;
	}

	public URI getUri() {
		return uri;
	}

	private final URI uri;

	private Video(String title, URI uri) {
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Video video = (Video) o;
		return Objects.equals(title, video.title) && Objects.equals(duration.get(), video.duration.get())
				&& Objects.equals(uri, video.uri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, duration.get(), uri);
	}

	@Override
	public String toString() {
		return title;
	}
}
