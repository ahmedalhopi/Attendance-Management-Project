package javaapplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class JavaApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();


        javaapplication.LoginController controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Attendance Management");
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
