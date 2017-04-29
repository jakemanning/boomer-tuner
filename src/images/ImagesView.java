package images;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import models.Image;
import root.RootModel;
import utils.CategoryView;
import utils.MediaLibrary;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class ImagesView extends TilePane implements CategoryView{
	private ImagesController imagesController;
	private ArrayList<Image> images;
	
	public ImagesView(ImagesController ic){
		
		imagesController  = ic;
		this.prefHeight(575);
		this.prefWidth(668);
		
		this.setPadding(new Insets(10,10,10,10));
		this.setVgap(10);
		this.setHgap(10);
		
		this.getChildren().add(new Text("Choose Directory"));
		
		ObservableList<Image> ims = MediaLibrary.instance().getImages();
		System.out.println(ims);
		
		images = new ArrayList<Image>();

		for(Image i: ims){
			images.add(i);
		}
		
		for(Image i: images){
			addImageView(i);
		}

		System.out.println(images);
		this.setVisible(true);


	}

	private void addImageView(Image i) {
		try {
			this.getChildren().add(new ImageView(i.getUri().toURL().toString()));
		} catch (MalformedURLException e) {
			System.out.println("URL Conversion didn't work");
			e.printStackTrace();
		}
	}

	@Override
	public void setRootModel(RootModel rootModel) {
		rootModel.setPlaylistModeListener(newValue -> {

        });
		rootModel.setSearchListener(searchText -> {
			getChildren().clear();
			for (Image i : images) {
				if (imagesController.searchFilter(searchText).test(i)) {
					addImageView(i);
				}
			}
		});
	}
}
