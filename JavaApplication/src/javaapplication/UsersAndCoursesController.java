package javaapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;

public class UsersAndCoursesController implements Initializable {

    @FXML
    private TableView tableview;
    @FXML
    private TableColumn<ObservableList<String>, String> name;
    @FXML
    private TableColumn<ObservableList<String>, String> course;
    @FXML
    private Pane linkPane;
    @FXML
    private TextField number0;
    @FXML
    private TextField code0;
    @FXML
    private Pane unlinkPane;
    @FXML
    private TextField number1;
    @FXML
    private TextField code1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void getUsersWithCourses() throws ClassNotFoundException {
        tableview.setVisible(true);
        linkPane.setVisible(false);
        unlinkPane.setVisible(false);
        tableview.getItems().clear();
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        course.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        PreparedStatement pst;
        ResultSet rs ;
        Connection conn;

        String sel = "select(select u.full_name from managment.users u where u.user_id = r.user_id),(select c.course_name from managment.courses c where c.course_code = r.code) from managment.user_courses r;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("full_name"));
                row.add(rs.getString("course_name"));
                tableview.getItems().add(row);
                System.out.println(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void insertData() {
        tableview.setVisible(false);
        linkPane.setVisible(true);
        unlinkPane.setVisible(false);
    }

    public void deleteData() {
        tableview.setVisible(false);
        linkPane.setVisible(false);
        unlinkPane.setVisible(true);
    }

    public void insertUserWithCourse() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "INSERT INTO managment.user_courses (user_id, code) VALUES(?, ?);";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, number0.getText());
            pst.setString(2, code0.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insert User With Course");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void deletetUserWithCourse() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "DELETE FROM managment.user_courses WHERE user_id = ? and code = ?;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, number1.getText());
            pst.setString(2, code1.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Delete User With Course");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
    }
}
