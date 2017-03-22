package overview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import weather.WeatherController;

public class Main extends Application {
    private OverviewController overviewController;
    private WeatherController weatherController;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/overview.fxml"));
        Parent root = loader.load();
        overviewController = loader.getController();
        Scene scene = new Scene(root,300,275);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
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
