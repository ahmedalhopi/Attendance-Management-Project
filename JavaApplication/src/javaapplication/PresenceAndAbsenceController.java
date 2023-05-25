package javaapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

        sel = "SELECT  lecture_id, student_number, student_name, student_mobile,'0' as status FROM managment.attendance where lecture_id = ? ;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            int no_lect = Integer.parseInt(lecture_id.getText());
            pst.setInt(1, no_lect);
            rs = pst.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("lecture_id"));
                row.add(rs.getString("student_number"));
                row.add(rs.getString("student_name"));
                row.add(rs.getString("student_mobile"));
                row.add(rs.getString("status"));
                dataTable.add(row);
                System.out.println(row);
            }
            row = dataTable.get(0);
            String[] rowData = row.toArray(new String[0]);

            student_number.setText(rowData[1]);
            student_name.setText(rowData[2]);
            student_mobile.setText(rowData[3]);
            present_status.setSelected(false);
            absent_status.setSelected(false);
            lecture_id.setEditable(false);
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

                ++i;
                row = dataTable.get(i);
                String[] rowData = row.toArray(new String[0]);
                student_number.setText(rowData[1]);
                student_name.setText(rowData[2]);
                student_mobile.setText(rowData[3]);
                row.set(4, status_option);
                System.out.println(row);
                System.out.println(dataTable);
                if (!"0".equals(row.get(4))) {
                    if ("Present".equals(row.get(4))) {
                        present_status.setSelected(true);
                    } else {
                        absent_status.setSelected(true);
                    }
                } else {
                    present_status.setSelected(false);
                    absent_status.setSelected(false);
                }

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

                --i;
                row = dataTable.get(i);
                String[] rowData = row.toArray(new String[0]);
                student_number.setText(rowData[1]);
                student_name.setText(rowData[2]);
                student_mobile.setText(rowData[3]);
                row.set(4, status_option);
                System.out.println(row);
                System.out.println(dataTable);
                if (!"0".equals(row.get(4))) {
                    if ("Present".equals(row.get(4))) {
                        present_status.setSelected(true);
                    } else {
                        absent_status.setSelected(true);
                    }
                } else {
                    present_status.setSelected(false);
                    absent_status.setSelected(false);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.out.println(ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "This is first student");
            lecture_id.setEditable(true);
        }
    }

}
