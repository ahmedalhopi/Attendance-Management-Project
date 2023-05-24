/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IT
 */
public class AssistantsHomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void goStudents() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Attendance Management");
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        Image icon = new Image(getClass().getResourceAsStream("img/icon.jpg"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    public void goAttendance() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Attendance.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Attendance Management");
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        Image icon = new Image(getClass().getResourceAsStream("img/icon.jpg"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    public void goLectures() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Lectures.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Attendance Management");
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        Image icon = new Image(getClass().getResourceAsStream("img/icon.jpg"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    public void goReports() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reports.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Attendance Management");
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        Image icon = new Image(getClass().getResourceAsStream("img/icon.jpg"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }
}
