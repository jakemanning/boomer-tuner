package albums;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import models.Album;
import models.Song;
import root.RootModel;
import songs.SongsController;
import songs.SongsView;
import utils.CategoryView;
import utils.MediaLibrary;

public class AlbumsView extends SplitPane implements CategoryView {
	private final AlbumsModel model;
	private final AlbumsController controller;
	private ListView<Album> albums;
	private SongsView detail;

	public AlbumsView(AlbumsModel model, AlbumsController controller) {
		this.model = model;
		this.controller = controller;

		initializeViews();

		initializeAlbums();
		initializeDetailView();
	}

	private void initializeAlbums() {
		albums.setItems(MediaLibrary.instance().getAlbums());
		albums.getSelectionModel().selectedItemProperty().addListener(controller.albumSelectionListener());
	}

	private void initializeDetailView() {
		if (model.isDirectorySelected()){
			detail.setPlaceholder(new Label("Select an album from the list"));
		}else{
			detail.setPlaceholder(new Label("Choose a directory to view albums"));
		}

		detail.getColumns().remove(3); // remove album column
		model.selectedAlbumProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Song> items = MediaLibrary.instance().getSongs()
					.filtered(song -> song.getAlbum().equals(newValue));
			detail.setItems(items);
		});
		if (model.getSelectedAlbum() == null) {
			detail.setItems(null);
		}
	}

	private void initializeViews() {
		setPrefHeight(575);
		setPrefWidth(668);
		albums = new ListView<>();
		detail = new SongsView(new SongsController());
		getItems().addAll(albums, detail);
		setDividerPositions(0.28f);
	}

	@Override
	public void setRootModel(RootModel rootModel) {
		rootModel.setPlaylistModeListener(this::playlistModeChanged);
	}

	private void playlistModeChanged(boolean playlistMode) {
		detail.playlistModeChanged(playlistMode);
	}
}
