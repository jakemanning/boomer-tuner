package videos;

import base.CategoryView;
import javafx.scene.control.ListView;
import menu.MenuModel;
import models.Video;

/**
 * Created by bryancapps on 4/4/17.
 */
public class VideosView extends ListView<Video> implements CategoryView {
    @Override
    public void setMenuModel(MenuModel menuModel) {
        menuModel.addPlaylistModeListener(newValue -> {

        });
    }
}
