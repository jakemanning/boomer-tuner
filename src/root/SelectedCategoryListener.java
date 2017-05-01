package root;

import models.Playlist;
import utils.CategoryType;

public interface SelectedCategoryListener {
    void selectedCategoryChanged(final CategoryType value);
    void playlistCreated(final Playlist playlist);
}
