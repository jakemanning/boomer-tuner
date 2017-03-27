package boomertuner.viewmodels;

import boomertuner.models.Song;
import javafx.scene.control.ListCell;

/**
 * Created by bryancapps on 3/26/17.
 */
public class SongCell extends ListCell<Song> {
    @Override
    protected void updateItem(Song item, boolean empty) {
        super.updateItem(item, empty);

        setText(item.toString());
    }
}
