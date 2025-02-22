package javaapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import javafx.scene.layout.Pane;

public class CoursesController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> code;
    @FXML
    private TableColumn<ObservableList<String>, String> course;
    @FXML
    private TableColumn<ObservableList<String>, String> subject;
    @FXML
    private TableColumn<ObservableList<String>, String> book;
    @FXML
    private TableColumn<ObservableList<String>, String> teacher;
    @FXML
    private TableColumn<ObservableList<String>, String> place;
    @FXML
    private Pane paneContainer;
    @FXML
    private TextField code0;
    @FXML
    private TextField name0;
    @FXML
    private TextField subject0;
    @FXML
    private TextField book0;
    @FXML
    private TextField teacher0;
    @FXML
    private TextField place0;
    @FXML
    private Pane paneContainerUpdate;
    @FXML
    private TextField code1;
    @FXML
    private TextField name1;
    @FXML
    private TextField subject1;
    @FXML
    private TextField book1;
    @FXML
    private TextField teacher1;
    @FXML
    private TextField place1;
    @FXML
    private Button update1;
    @FXML
    private Pane paneContainerDelete;
    @FXML
    private TextField code2;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
    }

    public void get_courses() throws ClassNotFoundException {
        tableView.getItems().clear();
        tableView.setVisible(true);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
        code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        course.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        subject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        book.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        teacher.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        place.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        PreparedStatement pst;
        ResultSet rs = null;
        Connection conn;

        String sel = "select * from managment.courses";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("course_code"));
                row.add(rs.getString("course_name"));
                row.add(rs.getString("subject"));
                row.add(rs.getString("book"));
                row.add(rs.getString("teacher"));
                row.add(rs.getString("place"));
                tableView.getItems().add(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void add_course() {
        tableView.setVisible(false);
        paneContainer.setVisible(true);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void saveDate() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "INSERT INTO managment.courses (course_code, course_name, subject, book, teacher, place) VALUES(?, ?, ?, ?, ?, ?);";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, code0.getText());
            pst.setString(2, name0.getText());
            pst.setString(3, subject0.getText());
            pst.setString(4, book0.getText());
            pst.setString(5, teacher0.getText());
            pst.setString(6, place0.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Course Inserted");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.err.println(ex);
        }
    }

    public void update_course() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(true);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void getDataForCourse() throws ClassNotFoundException {
        Connection conn = DatabaseConnect.connDB();
        System.out.println(conn);
        PreparedStatement pst;
        ResultSet rs;
        String log = "select * from managment.courses where course_code = ? ";
        try {
            pst = conn.prepareStatement(log);
            pst.setString(1, code1.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                name1.setText(rs.getString("course_name"));
                subject1.setText(rs.getString("subject"));
                book1.setText(rs.getString("book"));
                teacher1.setText(rs.getString("teacher"));
                place1.setText(rs.getString("place"));
                update1.setDisable(false);
            } else {
                JOptionPane.showMessageDialog(null, "No course found");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void updateDate() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "UPDATE managment.courses SET course_name=? , subject= ?, book= ?, teacher=? , place=? WHERE course_code=? ;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, name1.getText());
            pst.setString(2, subject1.getText());
            pst.setString(3, book1.getText());
            pst.setString(4, teacher1.getText());
            pst.setString(5, place1.getText());
            pst.setString(6, code1.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Course Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.err.println(ex);
        }
    }

    public void delete_course() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(true);
        update1.setDisable(true);
    }

    public void deleteData() throws ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want delete corse?");

        ButtonType confirmButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("No");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            PreparedStatement pst;
            Connection conn;
            String sel = "DELETE from managment.courses WHERE course_code=?;";
            try {
                conn = DatabaseConnect.connDB();
                pst = conn.prepareStatement(sel);
                pst.setString(1, code2.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Course Deleted");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex);
            }
        }
    }

}
