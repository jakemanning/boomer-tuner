import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import menu.MenuController;
import menu.MenuModel;
import menu.MenuView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		MenuModel menuModel = new MenuModel();
		MenuController menuController = new MenuController(menuModel);
		MenuView menuView = new MenuView(menuModel, menuController);
		menuView.initializeMenuBar(primaryStage);

		Scene scene = new Scene(menuView);
		primaryStage.setTitle("Boomer Tuner");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
