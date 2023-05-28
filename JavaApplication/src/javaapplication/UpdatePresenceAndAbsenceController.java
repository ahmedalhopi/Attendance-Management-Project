package javaapplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class UpdatePresenceAndAbsenceController implements Initializable {

    @FXML
    private Pane updatePane;
    @FXML
    private TextField lecture_id;
    @FXML
    private TextField student_number;
    @FXML
    private RadioButton present;
    @FXML
    private RadioButton absent;
    @FXML
    private ToggleGroup status;
    @FXML
    private Pane reportPane;
    @FXML
    private TextField lecture_title;
    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> course0;
    @FXML
    private TableColumn<ObservableList<String>, String> student_name0;
    @FXML
    private TableColumn<ObservableList<String>, String> status0;

    ObservableList<String[]> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updatePane.setVisible(false);
        reportPane.setVisible(false);
    }

    public void updateStatus() {
        updatePane.setVisible(true);
        reportPane.setVisible(false);
    }

    public void updateStatusForStudent() throws ClassNotFoundException {
        RadioButton selectedRadioButton = (RadioButton) status.getSelectedToggle();
        String status_option = "";
        if (selectedRadioButton != null) {
            if (selectedRadioButton == present) {
                status_option = "Present";
            } else if (selectedRadioButton == absent) {
                status_option = "Absent";
            }
        }
        PreparedStatement pst;
        Connection conn;
        String sel;
        System.out.println(status_option);

        sel = "update\n"
                + " managment.attendance\n"
                + " set\n"
                + " status = ?\n"
                + " where\n"
                + " lecture_id = ?\n"
                + " and student_number = ?;";
        System.out.println(sel);
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, status_option);
            int no_lect = Integer.parseInt(lecture_id.getText());
            pst.setInt(2, no_lect);
            pst.setString(3, student_number.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Status Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }

    }

    public void getReportPane() {
        updatePane.setVisible(false);
        reportPane.setVisible(true);
    }

    public void getStudentsInLecture() throws ClassNotFoundException {

        tableView.getItems().clear();
        course0.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        student_name0.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        status0.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	(\n"
                + "	select\n"
                + "		c.course_name\n"
                + "	from\n"
                + "		managment.courses c\n"
                + "	where\n"
                + "		c.course_code = (\n"
                + "		select\n"
                + "			distinct l.course_code\n"
                + "		from\n"
                + "			managment.lectures l\n"
                + "		where\n"
                + "			l.title = ?)),\n"
                + "	a.student_name,\n"
                + "		a.status\n"
                + "	from\n"
                + "		managment.attendance a;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, lecture_title.getText());
            rs = pst.executeQuery();

            while (rs.next()) {
                String[] row = new String[6];
                ObservableList<String> row_data = FXCollections.observableArrayList();
                row_data.add("course_name");
                row_data.add("student_name");
                row_data.add("attendance");
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                tableView.getItems().add(row_data);
                data.add(row);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void repoerExcleExoprt() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "students-in-lecture.xls";

        Path directory = Paths.get(path);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("Failed to create directory: " + e);
                return;
            }
        }

        Path filePath = Paths.get(fileName);
        int count = 1;
        while (Files.exists(filePath)) {
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            fileName = baseName + "_" + count + extension;
            filePath = Paths.get(fileName);
            count++;
        }

        try {

            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            int rowIndex = 0;
            for (String[] row : data) {
                Row excelRow = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (String cellData : row) {
                    Cell cell = excelRow.createCell(cellIndex++);
                    cell.setCellValue(cellData);
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                workbook.write(outputStream);
            }

            System.out.println("Excel file exported successfully to: " + filePath.toAbsolutePath());
            JOptionPane.showMessageDialog(null, "Excel file exported successfully to: " + filePath.toAbsolutePath());

        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

}
