package overview;

import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;

import java.beans.EventHandler;


public class AssistantMirror extends Application {
    private final KeyCombination QUIT_KEYS = new KeyCodeCombination(KeyCode.Q,
            KeyCombination.CONTROL_DOWN);
    private final String OVERVIEW_FXML = "/overview.fxml";
    private final String STYLE = "style.css";

    private OverviewController overviewController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Config.create();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(OVERVIEW_FXML));
        Parent root = loader.load();
        overviewController = loader.getController();
        Scene scene = new Scene(root,Config.instance.WINDOW_WIDTH,Config.instance.WINDOW_HEIGHT);
        scene.getStylesheets().add(STYLE);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(Config.instance.ENABLE_FULLSCREEN);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (QUIT_KEYS.match(event)) {
                System.exit(0);
            }
        });

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
