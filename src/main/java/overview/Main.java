package overview;

import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timetable.Train;
import timetable.TrainHelper;

import java.util.Arrays;


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
        primaryStage.setFullScreen(true);
        primaryStage.show();
        overviewController.init();

    }

    @Override
    public void stop(){
        overviewController.stopRunning();
    }

    public static void main(String[] args) {
        if (args.length == 1)
            TrainHelper.TRAIN_LOCATION_ID = args[0];
        else
            TrainHelper.TRAIN_LOCATION_ID = "9110001";

        launch(args);
    }
}
