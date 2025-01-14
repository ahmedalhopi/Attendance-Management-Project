package javaapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class PresenceAndAbsenceController implements Initializable {

    @FXML
    private TextField lecture_id;
    @FXML
    private TextField student_number;
    @FXML
    private TextField student_name;
    @FXML
    private TextField student_mobile;
    @FXML
    private RadioButton present_status;
    @FXML
    private RadioButton absent_status;
    @FXML
    private ToggleGroup radioSelect;
    @FXML
    private Button btnGet;

    ObservableList<ObservableList<String>> dataTable = FXCollections.observableArrayList();
    ObservableList<String> row;
    private int i = 0;
    private int j = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void getAllStudents() throws ClassNotFoundException {
        ResultSet rs;
        Connection conn;
        PreparedStatement pst;

        String sel = "insert into managment.attendance (lecture_id, student_number, student_name, student_mobile)\n"
                + "select ?, s.student_number, s.full_name, s.mobile from managment.students s where student_number in (\n"
                + "select st.student_number from managment.students_courses st where st.status = 'Registered' and course_code = (select\n"
                + "l.course_code from managment.lectures l where id = ?));";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            int no_lect = Integer.parseInt(lecture_id.getText());
            pst.setInt(1, no_lect);
            pst.setInt(2, no_lect);
            pst.executeQuery();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        sel = "SELECT  lecture_id, student_number, student_name, student_mobile, status FROM managment.attendance where lecture_id = ? order by student_number;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            int no_lect = Integer.parseInt(lecture_id.getText());
            pst.setInt(1, no_lect);
            rs = pst.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                ObservableList<String> record = FXCollections.observableArrayList();
                record.add(rs.getString("lecture_id"));
                record.add(rs.getString("student_number"));
                record.add(rs.getString("student_name"));
                record.add(rs.getString("student_mobile"));
                record.add(rs.getString("status"));
                dataTable.add(record);
                System.out.println(record);
            }
            row = dataTable.get(0);
            String[] rowData = row.toArray(new String[0]);

            student_number.setText(rowData[1]);
            student_name.setText(rowData[2]);
            student_mobile.setText(rowData[3]);
            present_status.setSelected(false);
            absent_status.setSelected(false);
            lecture_id.setEditable(false);
            btnGet.setDisable(true);
            System.out.println(dataTable.size());
        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void nextStudent() throws ClassNotFoundException {

        if (i < dataTable.size()) {

            String status_option = "";

            PreparedStatement pst;
            Connection conn;
            String sel = "UPDATE managment.attendance SET  status = ? WHERE student_number = ? and lecture_id = ?;";
            try {
                conn = DatabaseConnect.connDB();
                pst = conn.prepareStatement(sel);

                RadioButton selectedRadioButton = (RadioButton) radioSelect.getSelectedToggle();
                if (selectedRadioButton != null) {
                    if (selectedRadioButton == present_status) {
                        status_option = "Present";
                    } else if (selectedRadioButton == absent_status) {
                        status_option = "Absent";
                    }
                }

                pst.setString(1, status_option);
                pst.setString(2, student_number.getText());
                int no_lect = Integer.parseInt(lecture_id.getText());
                pst.setInt(3, no_lect);
                pst.executeUpdate();
                row.set(4, status_option);

                ++i;
                row = dataTable.get(i);
                String[] rowData = row.toArray(new String[i]);
                student_number.setText(rowData[1]);
                student_name.setText(rowData[2]);
                student_mobile.setText(rowData[3]);
                if ("Present".equals(row.get(4))) {
                    present_status.setSelected(true);
                } else if ("Absent".equals(row.get(4))) {
                    absent_status.setSelected(true);
                } else {
                    present_status.setSelected(false);
                    absent_status.setSelected(false);
                }
                System.out.println(row.get(4));
                System.out.println(dataTable);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.out.println(ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Finsed all students");
            lecture_id.setEditable(true);

        }
    }

    public void previousStudent() throws ClassNotFoundException {
        if (i >= 0) {

                --i;
                row = dataTable.get(i);
                String[] rowData = row.toArray(new String[i]);
                student_number.setText(rowData[1]);
                student_name.setText(rowData[2]);
                student_mobile.setText(rowData[3]);
                if ("Present".equals(row.get(4))) {
                    present_status.setSelected(true);
                } else if ("Absent".equals(row.get(4))) {
                    absent_status.setSelected(true);
                } else {
                    present_status.setSelected(false);
                    absent_status.setSelected(false);
                }
                System.out.println(row.get(4));
                System.out.println(dataTable);


        } else {
            JOptionPane.showMessageDialog(null, "This is first student");
            lecture_id.setEditable(true);
            btnGet.setDisable(false);
        }
    }

    public void goToUpdate() throws IOException{
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePresenceAndAbsence.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.iconifiedProperty();
        primaryStage.show();
    }
}
