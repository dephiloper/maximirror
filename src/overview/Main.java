package overview;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    private OverviewController controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("overview.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root,300,275);
        scene.getStylesheets().add("overview/style.css");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        controller.updateClock();
    }
    @Override
    public void stop(){
        controller.stopRunning();
        System.out.println("muh");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
