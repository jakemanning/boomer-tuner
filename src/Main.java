import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import root.RootController;
import root.RootModel;
import root.RootView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		RootModel rootModel = new RootModel();
		RootController rootController = new RootController(rootModel);
		RootView rootView = new RootView(rootModel, rootController);
		rootView.initializeMenuBar(primaryStage);

		Scene scene = new Scene(rootView);
		primaryStage.setTitle("Boomer Tuner");
		primaryStage.setMinWidth(650);
		primaryStage.setMinHeight(300);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
