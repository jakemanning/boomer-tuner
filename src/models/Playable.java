package models;

import java.net.URI;

import javafx.util.Duration;

public interface Playable extends Category {
	URI getUri();
	String format(Duration elapsed);
}
