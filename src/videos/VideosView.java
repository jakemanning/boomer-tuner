package videos;

import base.CategoryView;
import javafx.scene.control.ListView;
import models.Video;
import root.RootModel;

/**
 * Created by bryancapps on 4/4/17.
 */
public class VideosView extends ListView<Video> implements CategoryView {
    @Override
    public void setMenuModel(RootModel rootModel) {
        rootModel.addPlaylistModeListener(newValue -> {

        });
    }
}
