import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;
import java.util.stream.Collectors;

public class UFV extends Application {

    static List<Node> nodes = new ArrayList<>();
    static int N = 0;
    enum implsU {byRank, bySize};
    enum implsF {pathCompr, pathHalf, pathSplit};
    static implsU implU = implsU.byRank;
    static implsF implF = implsF.pathCompr;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("app/sample.fxml"));

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Union-Find Visualizer");
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Launch a standalone application. This method is typically called from the main method().
     * It must not be called more than once or an exception will be thrown.
     * The launch method does not return until the application has exited,
     * either via a call to Platform.exit or all of the application windows have been closed.
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
