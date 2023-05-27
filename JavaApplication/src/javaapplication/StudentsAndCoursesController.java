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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;

public class StudentsAndCoursesController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> student_number;
    @FXML
    private TableColumn<ObservableList<String>, String> student_name;
    @FXML
    private TableColumn<ObservableList<String>, String> course_code;
    @FXML
    private TableColumn<ObservableList<String>, String> course_name;
    @FXML
    private TableColumn<ObservableList<String>, String> status;
    @FXML
    private Pane insertPane;
    @FXML
    private TextField course_code0;
    @FXML
    private TextField student_number0;
    @FXML
    private Pane updatePane;
    @FXML
    private TextField course_code1;
    @FXML
    private TextField student_number1;
    @FXML
    private RadioButton registered1;
    @FXML
    private RadioButton withdrawn1;
    @FXML
    private RadioButton disconnected1;
    @FXML
    private ToggleGroup statusGroub;
    @FXML
    private Pane deletePane;
    @FXML
    private TextField course_code2;
    @FXML
    private TextField student_number2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setVisible(false);
        insertPane.setVisible(false);
        updatePane.setVisible(false);
        deletePane.setVisible(false);
    }

    public void allStudentsWithCourses() throws ClassNotFoundException {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        tableView.setVisible(true);
        insertPane.setVisible(false);

        data.clear();
        tableView.getItems().clear();
        student_number.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        student_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        course_code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        course_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        ResultSet rs;
        Connection conn;
        PreparedStatement pst;

        String sel = "select\n"
                + "	student_number,\n"
                + "	(\n"
                + "	select\n"
                + "		full_name\n"
                + "	from\n"
                + "		managment.students s\n"
                + "	where\n"
                + "		s.student_number = sc.student_number) ,\n"
                + "		sc.course_code ,\n"
                + "	(\n"
                + "	select\n"
                + "		c.course_name\n"
                + "	from\n"
                + "		managment.courses c\n"
                + "	where\n"
                + "		c.course_code = sc.course_code),\n"
                + "	status\n"
                + "from\n"
                + "	managment.students_courses sc;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("student_number"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("course_code"));
                row.add(rs.getString("course_name"));
                row.add(rs.getString("status"));
                tableView.getItems().add(row);
                data.add(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void addCourseToStudent() {
        tableView.setVisible(false);
        insertPane.setVisible(true);
        updatePane.setVisible(false);
        deletePane.setVisible(false);
    }

    public void inserStudentWithCourse() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "insert\n"
                + "	into\n"
                + "	managment.students_courses (student_number,\n"
                + "	course_code,\n"
                + "	status)\n"
                + "values(?,\n"
                + "?,\n"
                + "'registered');";

        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, student_number0.getText());
            pst.setString(2, course_code0.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "The course inserted with student");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void editCourseToStudent() {
        tableView.setVisible(false);
        insertPane.setVisible(false);
        updatePane.setVisible(true);
        deletePane.setVisible(false);
    }

    public void UpdateStudentWithCourse() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "update\n"
                + "	managment.students_courses\n"
                + "set\n"
                + "	status = ?\n"
                + "where\n"
                + "	student_number = ?\n"
                + "	and \n"
                + "course_code = ?;";
        try {
            String status_option = "";
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            RadioButton selectedRadioButton = (RadioButton) statusGroub.getSelectedToggle();
            if (selectedRadioButton != null) {
                if (selectedRadioButton == registered1) {
                    status_option = "Registered";
                } else if (selectedRadioButton == withdrawn1) {
                    status_option = "Withdrawn";
                } else if (selectedRadioButton == disconnected1) {
                    status_option = "Disconnected1";
                }
            }
            pst.setString(1, status_option);
            pst.setString(2, student_number1.getText());
            pst.setString(3, course_code1.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void deleteStudent() {
        tableView.setVisible(false);
        insertPane.setVisible(false);
        updatePane.setVisible(false);
        deletePane.setVisible(true);
    }

    public void deleteStudentWithCourse() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "delete\n"
                + "from\n"
                + "	managment.students_courses\n"
                + "where\n"
                + "	student_number = ?\n"
                + "	and course_code = ?;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, student_number2.getText());
            pst.setString(2, course_code2.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student Deleted");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }
}
