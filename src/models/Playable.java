package models;

import javafx.util.Duration;

import java.net.URI;

public interface Playable extends Category {
	URI getUri();

	static String format(final Duration elapsed) {
		int secondsElapsed = (int) Math.floor(elapsed.toSeconds());
		if (secondsElapsed >= 3600) {
			return String.format("%d:%02d:%02d", secondsElapsed / 3600, (secondsElapsed % 3600) / 60,
					secondsElapsed % 60);
		} else if (secondsElapsed >= 600) {
			return String.format("%02d:%02d", (secondsElapsed % 3600) / 60, secondsElapsed % 60);
		} else {
			return String.format("%2d:%02d", (secondsElapsed % 3600) / 60, secondsElapsed % 60);
		}
	}
}
