package boomertuner.viewmodels;

import boomertuner.models.Song;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Created by bryancapps on 3/26/17.
 */
public class SongCellFactory implements Callback<ListView<Song>, ListCell<Song>> {
    @Override
    public ListCell<Song> call(ListView<Song> param) {
        return new SongCell();
    }
}
