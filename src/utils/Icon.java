package utils;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Icon {
	SHUFFLE("res/playercontrols/shuffle.png"),
	PLAY ("res/playercontrols/play.png"),
	MUTE ("res/playercontrols/mute.png"),
	STOP ("res/playercontrols/stop.png"),
	PAUSE ("res/playercontrols/pause.png"),
	VOLDOWN ("res/playercontrols/voldown.png"),
	LOOP ("res/playercontrols/loop.png"),
	VOLUP ("res/playercontrols/volup.png"), 
	PREVIOUS ("res/playercontrols/rewind.png"),
	NEXT ("res/playercontrols/fastforward.png");
	
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
