package images;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import models.Image;
import root.RootModel;
import utils.CategoryView;
import utils.MediaLibrary;
import utils.TaskRunner;

import java.io.IOException;
import java.net.MalformedURLException;

public class ImagesView extends ScrollPane implements CategoryView{
	private ImagesController imagesController;
	private TilePane tilePane = new TilePane();
	
	public ImagesView(final ImagesController ic){
		imagesController  = ic;
		initView();

		ObservableList<Image> images = MediaLibrary.instance().getImages();
		addImages(images);
	}

	private void initView() {
		prefHeight(575);
		prefWidth(668);

		tilePane.setVgap(10);
		tilePane.setHgap(10);
		tilePane.setPadding(new Insets(10,10,10,10));
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Horizontal scroll bar
		setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
		setFitToHeight(true);
		setFitToWidth(true);
		setStyle("-fx-background-color: transparent");
		setContent(tilePane);
	}

	private void addImages(ObservableList<Image> images) {
		Task<Void> importTask = new Task<Void>() {
			@Override
			protected Void call() throws InterruptedException, IOException {
				final int size = images.size();
				for (int i = 0; i < size; i++) {
					if (Thread.currentThread().isInterrupted()) return null;
					addImageView(images.get(i));
					updateProgress(i, size);
				}
				return null;
			}
		};

		TaskRunner.run(importTask, "Loading Images...", null);
	}

	private void addImageView(final Image i) {
		try {
			final String imageName = i.getUri().toURL().toString();
			ImageView imageView = new ImageView(new javafx.scene.image.Image(imageName));
			imageView.setSmooth(true);
			imageView.setCache(true);
			imageView.setCacheHint(CacheHint.QUALITY);
			imageView.setPreserveRatio(true);
			imageView.setFitWidth(200);

			Platform.runLater(() -> {
				tilePane.getChildren().add(imageView);
			});

		} catch (MalformedURLException e) {
			System.out.println("URL Conversion didn't work");
			e.printStackTrace();
		}
	}

	@Override
	public void setListeners(final RootModel rootModel) {
		rootModel.setPlaylistModeListener(newValue -> {

        });
//		rootModel.setSearchListener(searchText -> {
//			getChildren().clear();
//			for (Image i : images) {
//				if (imagesController.searchFilter(searchText).test(i)) {
//					addImageView(i);
//				}
//			}
//		});
	}
}
