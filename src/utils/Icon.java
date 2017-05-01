package utils;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Icon {
	SHUFFLE("res/playercontrols/shuffle.png"),
	SHUFFLE_PRESSED("res/playercontrols/shuffle_pressed.png"),
	PLAY ("res/playercontrols/play.png"),
	MUTE ("res/playercontrols/mute.png"),
	STOP ("res/playercontrols/stop.png"),
	PAUSE ("res/playercontrols/pause.png"),
	VOLDOWN ("res/playercontrols/voldown.png"),
	LOOP ("res/playercontrols/loop.png"),
	LOOP_PRESSED ("res/playercontrols/loop_pressed.png"),
	VOLUP ("res/playercontrols/volup.png"), 
	PREVIOUS ("res/playercontrols/rewind.png"),
	NEXT("res/playercontrols/fastforward.png"),
	DEFAULT_ARTWORK("res/artwork.png"),

	SHUFFLE_DARK("res/playercontrols-dark/shuffle.png"),
	SHUFFLE_PRESSED_DARK("res/playercontrols-dark/shuffle_pressed.png"),
	PLAY_DARK("res/playercontrols-dark/play.png"),
	MUTE_DARK("res/playercontrols-dark/mute.png"),
	STOP_DARK("res/playercontrols-dark/stop.png"),
	PAUSE_DARK("res/playercontrols-dark/pause.png"),
	VOLDOWN_DARK("res/playercontrols-dark/voldown.png"),
	LOOP_DARK("res/playercontrols-dark/loop.png"),
	LOOP_PRESSED_DARK("res/playercontrols-dark/loop_pressed.png"),
	VOLUP_DARK("res/playercontrols-dark/volup.png"),
	PREVIOUS_DARK("res/playercontrols-dark/rewind.png"),
	NEXT_DARK("res/playercontrols-dark/fastforward.png");
	
	private Image image;
	
	public Image image() {
		return image;
	}
	
	private Icon(final String path) {
		try {
			this.image = new Image(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
