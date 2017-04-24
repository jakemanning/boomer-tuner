package root;

import models.Playlist;
import utils.CategoryType;

public interface SelectedCategoryListener {
    void selectedCategoryChanged(CategoryType value);
    void playlistCreated(Playlist playlist);
}
