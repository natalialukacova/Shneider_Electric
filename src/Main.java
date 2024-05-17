import gui.controller.MainViewController;
import gui.controller.employee.EditEmployeeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            String css = getClass().getResource("/gui/css/MainView.css").toExternalForm();
            scene.getStylesheets().add(css);

            MainViewController mainViewController = loader.getController();
            mainViewController.setDependencies(new EditEmployeeController());

            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
                primaryStage.setOpacity(.8);
            });

            root.setOnMouseReleased(event -> {
                primaryStage.setOpacity(1);
            });

            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
