package overview;

import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;


public class Main extends Application {
    private OverviewController overviewController;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Config.create();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/overview.fxml"));
        Parent root = loader.load();
        overviewController = loader.getController();
        Scene scene = new Scene(root,Config.instance.WINDOW_WIDTH,Config.instance.WINDOW_HEIGHT);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(Config.instance.ENABLE_FULLSCREEN);
        primaryStage.show();
        overviewController.init();

    }

    @Override
    public void stop(){
        overviewController.stopRunning();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
