import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jaudiotagger.tag.images.Artwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainAppController {
    @FXML
    private ListView center;
    @FXML
    private ImageView albumArt;
    @FXML
    private ImageView reverse;
    @FXML
    private ImageView playPause;
    @FXML
    private ImageView fastForward;

    public MainAppController() {
    }

    @FXML
    private void initialize() {
        try {
            reverse.setImage(new Image(new FileInputStream("res/playercontrols/rewind.png")));
            playPause.setImage(new Image(new FileInputStream("res/playercontrols/play.png")));
            fastForward.setImage(new Image(new FileInputStream("res/playercontrols/fastforward.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAlbumArt(Artwork artwork) {

    }

    public void playPause() {

    }
}
