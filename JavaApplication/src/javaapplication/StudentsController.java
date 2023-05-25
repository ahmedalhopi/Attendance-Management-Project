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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;

public class StudentsController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> number;
    @FXML
    private TableColumn<ObservableList<String>, String> name;
    @FXML
    private TableColumn<ObservableList<String>, String> gender;
    @FXML
    private TableColumn<ObservableList<String>, String> mobile;
    @FXML
    private TableColumn<ObservableList<String>, String> living;
    @FXML
    private TableColumn<ObservableList<String>, String> department;
    @FXML
    private TableColumn<ObservableList<String>, String> major;
    @FXML
    private Pane paneContainer;
    @FXML
    private TextField number0;
    @FXML
    private TextField name0;
    @FXML
    private TextField department0;
    @FXML
    private TextField mobile0;
    @FXML
    private TextField living0;
    @FXML
    private TextField major0;
    @FXML
    private RadioButton male0;
    @FXML
    private RadioButton female0;
    @FXML
    private ToggleGroup gender0;
    @FXML
    private Pane paneContainerUpdate;
    @FXML
    private TextField number1;
    @FXML
    private TextField name1;
    @FXML
    private TextField department1;
    @FXML
    private TextField mobile1;
    @FXML
    private TextField living1;
    @FXML
    private TextField major1;
    @FXML
    private RadioButton male1;
    @FXML
    private RadioButton female1;
    @FXML
    private Button update1;
    @FXML
    private ToggleGroup gender1;
    @FXML
    private Pane paneContainerDelete;
    @FXML
    private TextField number2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void get_all_students() throws ClassNotFoundException {
        tableView.getItems().clear();
        tableView.setVisible(true);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
        number.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mobile.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        gender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        living.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        department.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        major.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select * from managment.students";

        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("student_number"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("mobile"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("living"));
                row.add(rs.getString("department"));
                row.add(rs.getString("major"));
                tableView.getItems().add(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void insert_student() {
        tableView.setVisible(false);
        paneContainer.setVisible(true);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void saveStudent() throws ClassNotFoundException {
        String sel = "INSERT INTO managment.students (student_number, full_name,mobile ,gender, living, department, major) VALUES(?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst;
        Connection conn;
        try {
            String gender_select = "";
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, number0.getText());
            pst.setString(2, name0.getText());
            RadioButton genderVariable = (RadioButton) gender0.getSelectedToggle();
            if (genderVariable == male0) {
                gender_select = "male";
            } else if (genderVariable == female0) {
                gender_select = "female";
            }
            pst.setString(3, mobile0.getText());
            pst.setString(4, gender_select);
            pst.setString(5, living0.getText());
            pst.setString(6, department0.getText());
            pst.setString(7, major0.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student Inserted");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void update_student() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(true);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void getDataForStudent() throws ClassNotFoundException {
        Connection conn = DatabaseConnect.connDB();
        System.out.println(conn);
        PreparedStatement pst;
        ResultSet rs;
        String log = "select * from managment.students where student_number = ? ";
        try {
            pst = conn.prepareStatement(log);
            pst.setString(1, number1.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                name1.setText(rs.getString("full_name"));
                if ("male".equals(rs.getString("gender"))) {
                    male1.setSelected(true);
                } else {
                    female1.setSelected(true);
                }
                living1.setText(rs.getString("mobile"));
                living1.setText(rs.getString("living"));
                department1.setText(rs.getString("department"));
                major1.setText(rs.getString("major"));
                mobile1.setText(rs.getString("mobile"));
                update1.setDisable(false);
                System.out.println(rs);
            } else {
                JOptionPane.showMessageDialog(null, "Student not found");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void updateStudent() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "UPDATE managment.students SET full_name=?, gender=?, mobile=? ,living=?, department=?, major=? WHERE student_number=?;";
        try {
            String gender_select = "";
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, name1.getText());
            RadioButton genderVariable = (RadioButton) gender1.getSelectedToggle();
            if (genderVariable == male1) {
                gender_select = "male";
            } else if (genderVariable == female1) {
                gender_select = "female";
            }
            pst.setString(2, gender_select);
            pst.setString(3, mobile1.getText());
            pst.setString(4, living1.getText());
            pst.setString(5, department1.getText());
            pst.setString(6, major1.getText());
            pst.setString(7, number1.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void delete_student() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(true);
        update1.setDisable(true);
    }

    public void deleteStudent() throws ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete student?");

        ButtonType confirmButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("No");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            PreparedStatement pst;
            Connection conn;
            String sel = "DELETE FROM managment.students WHERE student_number=?;";
            try {
                conn = DatabaseConnect.connDB();
                pst = conn.prepareStatement(sel);
                pst.setString(1, number2.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Student Deleted");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex);
            }
        }
    }

}
