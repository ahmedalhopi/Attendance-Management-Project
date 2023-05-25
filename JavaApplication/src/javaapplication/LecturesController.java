package javaapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;

public class LecturesController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> lecture_id;
    @FXML
    private TableColumn<ObservableList<String>, String> course_code;
    @FXML
    private TableColumn<ObservableList<String>, String> course_name;
    @FXML
    private TableColumn<ObservableList<String>, String> title;
    @FXML
    private TableColumn<ObservableList<String>, String> place;
    @FXML
    private TableColumn<ObservableList<String>, String> day;
    @FXML
    private TableColumn<ObservableList<String>, String> date;
    @FXML
    private Pane paneContainer;
    @FXML
    private TextField course_code0;
    @FXML
    private TextField title0;
    @FXML
    private TextField place0;
    @FXML
    private TextField day0;
    @FXML
    private DatePicker date0;
    @FXML
    private Pane paneContainerUpdate;
    @FXML
    private TextField lecture_id1;
    @FXML
    private TextField course_code1;
    @FXML
    private TextField title1;
    @FXML
    private TextField place1;
    @FXML
    private TextField day1;
    @FXML
    private DatePicker date1;
    @FXML
    private Button update1;
    @FXML
    private Pane paneContainerDelete;
    @FXML
    private TextField lecture_id2;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
    }

    public void getAllLectures() throws ClassNotFoundException {
        tableView.getItems().clear();
        tableView.setVisible(true);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
        lecture_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        course_code.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        course_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        title.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        place.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        day.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "SELECT id, course_code,(select course_name  from managment.courses c where c.course_code = r.course_code) ,title, place, day, date FROM managment.lectures r;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("id"));
                row.add(rs.getString("course_code"));
                row.add(rs.getString("course_name"));
                row.add(rs.getString("title"));
                row.add(rs.getString("place"));
                row.add(rs.getString("day"));
                row.add(rs.getString("date"));
                tableView.getItems().add(row);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void insert_lecture() {
        tableView.setVisible(false);
        paneContainer.setVisible(true);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void insertLecture() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "INSERT INTO managment.lectures (course_code, title, place, day,date) VALUES (?, ?, ?, ?, to_date(?,'yyyy/mm/dd'));";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, course_code0.getText());
            pst.setString(2, title0.getText());
            pst.setString(3, place0.getText());
            pst.setString(4, day0.getText());
            pst.setString(5, date0.getValue().toString());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lecture Inserted");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.err.println(ex);
        }
    }

    public void update_lectuer() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(true);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }


    public void getDataForLectuers() throws ClassNotFoundException {
        Connection conn = DatabaseConnect.connDB();
        System.out.println(conn);
        PreparedStatement pst;
        ResultSet rs;
        String log = "SELECT * FROM managment.lectures WHERE id = ?";
        try {
            pst = conn.prepareStatement(log);
            int lectureId = Integer.parseInt(lecture_id1.getText());
            pst.setInt(1, lectureId);
            rs = pst.executeQuery();
            if (rs.next()) {
                course_code1.setText(rs.getString("course_code"));
                title1.setText(rs.getString("title"));
                place1.setText(rs.getString("place"));
                day1.setText(rs.getString("day"));
                LocalDate dateVirable = rs.getDate("date").toLocalDate();
                date1.setValue(dateVirable);

                update1.setDisable(false);
            } else {
                JOptionPane.showMessageDialog(null, "The lecture not found");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void updateLecture() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "UPDATE managment.lectures SET course_code=?, title=?, place=?, day=?, date=to_date(?,'yyyy/mm/dd') WHERE id=?;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, course_code1.getText());
            pst.setString(2, title1.getText());
            pst.setString(3, place1.getText());
            pst.setString(4, day1.getText());
            pst.setString(5, date1.getValue().toString());
            int lec_id = Integer.parseInt(lecture_id1.getText());
            pst.setInt(6, lec_id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lecture Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void delete_lecture() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(true);
        update1.setDisable(true);
    }

    public void deleteLecture() throws ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete lecture?");

        ButtonType confirmButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("No");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            PreparedStatement pst;
            Connection conn;
            String sel = "DELETE from managment.lectures WHERE id=?;";
            try {
                conn = DatabaseConnect.connDB();
                pst = conn.prepareStatement(sel);
                int lec_id = Integer.parseInt(lecture_id2.getText());
                pst.setInt(1, lec_id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Lectur Deleted");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.out.println(ex);
            }
        }
    }

}
