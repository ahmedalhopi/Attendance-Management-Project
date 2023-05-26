package javaapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcleReportsController implements Initializable {

    @FXML
    private Pane studentPane;
    @FXML
    private Pane coursePane;
    @FXML
    private TextField course_code1;
    @FXML
    private TextField student_number;
    @FXML
    private TextField course_code0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentPane.setVisible(false);
        coursePane.setVisible(false);
    }

    public void getTop10Lectures() throws ClassNotFoundException, IOException {
        String path = "../excle/";
        String fileName = path + "top10lectures.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	l.id,\n"
                + "	l.course_code,\n"
                + "	c.course_name ,\n"
                + "	l.title,\n"
                + "	l.date,\n"
                + "	COUNT(l.id)\n"
                + "from\n"
                + "	managment.lectures l\n"
                + "join managment.attendance a on\n"
                + "	l.id = a.lecture_id\n"
                + "join managment.courses c on\n"
                + "	c.course_code = l.course_code\n"
                + "where\n"
                + "	a.status = 'Present'\n"
                + "group by\n"
                + "	l.id,\n"
                + "	l.course_code,\n"
                + "	c.course_name,\n"
                + "	l.title,\n"
                + "	l.date\n"
                + "order by\n"
                + "	l.date desc,\n"
                + "	COUNT(l.id) desc\n"
                + "limit 10;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    public void exportCommitedStudents() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "commited-students.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	s.*,\n"
                + "	(a.totalPresent * 100 / a.totalLectures) as rate\n"
                + "from\n"
                + "	managment.students s\n"
                + "join (\n"
                + "	select\n"
                + "		student_number,\n"
                + "		COUNT(*) as totalLectures,\n"
                + "		(\n"
                + "		select\n"
                + "			COUNT(*)\n"
                + "		from\n"
                + "			managment.attendance ac2\n"
                + "		where\n"
                + "			ac2.student_number = ac.student_number\n"
                + "			and ac2.status = 'Present') as totalPresent\n"
                + "from\n"
                + "		managment.attendance ac\n"
                + "group by\n"
                + "		student_number\n"
                + "having\n"
                + "		COUNT(*) >= 0 \n"
                + "                ) a on\n"
                + "	s.student_number = a.student_number\n"
                + "order by\n"
                + "	rate desc;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[8];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void getLecturesCoursePane() {
        studentPane.setVisible(false);
        coursePane.setVisible(true);
    }

    public void exportLectursFromCourseCode() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "lectures-for-course.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	*\n"
                + "from\n"
                + "	managment.lectures l\n"
                + "where\n"
                + "	l.course_code = ?;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, course_code1.getText());
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void exportStudentsUp80() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "students-up-80.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	s.*,\n"
                + "	(a.totalPresent * 100 / a.totalLectures) as rate\n"
                + "from\n"
                + "	managment.students s\n"
                + "join (\n"
                + "	select\n"
                + "		student_number,\n"
                + "		COUNT(*) as totalLectures,\n"
                + "		(\n"
                + "		select\n"
                + "			COUNT(*)\n"
                + "		from\n"
                + "			managment.attendance ac2\n"
                + "		where\n"
                + "			ac2.student_number = ac.student_number\n"
                + "			and ac2.status = 'Present') as totalPresent\n"
                + "	from\n"
                + "		managment.attendance ac\n"
                + "	group by\n"
                + "		student_number\n"
                + "	having\n"
                + "		COUNT(*) >= 0\n"
                + "		and (\n"
                + "		select\n"
                + "			COUNT(*)\n"
                + "		from\n"
                + "			managment.attendance ac2\n"
                + "		where\n"
                + "			ac2.student_number = ac.student_number\n"
                + "			and ac2.status = 'Present') * 100 / COUNT(*) >= 80\n"
                + "                ) a on\n"
                + "	s.student_number = a.student_number\n"
                + "order by\n"
                + "	rate desc;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[8];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void getLecturesStudentPane() {
        studentPane.setVisible(true);
        coursePane.setVisible(false);
    }

    public void exportLectursForStudentsInCourse() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "lectures-for-student.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	a.student_number ,\n"
                + "	l.course_code, \n"
                + "	a.lecture_id ,\n"
                + "	l.title ,\n"
                + "	l.place ,\n"
                + "	l.day,\n"
                + "	l.date ,\n"
                + "	a.status\n"
                + "from\n"
                + "	managment.lectures l ,\n"
                + "	managment.attendance a\n"
                + "where\n"
                + "	l.id = a.lecture_id\n"
                + "	and l.course_code = ?\n"
                + "	and a.student_number = ? ;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, course_code0.getText());
            pst.setString(2, student_number.getText());
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[8];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void exportStudent25Absent() throws ClassNotFoundException {
        String path = "../excle/";
        String fileName = path + "student-up-25.xls";

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

        PreparedStatement pst;
        ResultSet rs;
        Connection conn;

        String sel = "select\n"
                + "	s.*,\n"
                + "	ac.course_code ,\n"
                + "	ac.course_name,\n"
                + "	ac.total_absences ,\n"
                + "	(ac.total_absences * 100 / ac.totalLectureCourse) as absence_rate\n"
                + "from\n"
                + "	managment.students s ,\n"
                + "	(\n"
                + "	select\n"
                + "		a.student_number,\n"
                + "		(\n"
                + "		select\n"
                + "			l.course_code\n"
                + "		from\n"
                + "			managment.lectures l\n"
                + "		where\n"
                + "			l.id = a.lecture_id) as course_code \n"
                + "               ,\n"
                + "		count(a.lecture_id)as total_absences,\n"
                + "		(\n"
                + "		select\n"
                + "			count(*)\n"
                + "		from\n"
                + "			managment.lectures l\n"
                + "		where\n"
                + "			l.course_code = (\n"
                + "			select\n"
                + "				l2.course_code\n"
                + "			from\n"
                + "				managment.lectures l2\n"
                + "			where\n"
                + "				l2.id = a.lecture_id)) as totalLectureCourse\n"
                + "               ,\n"
                + "		(\n"
                + "		select\n"
                + "			c.course_name \n"
                + "		from\n"
                + "			managment.courses c\n"
                + "		where\n"
                + "			c.course_code = (\n"
                + "			select\n"
                + "				l.course_code\n"
                + "			from\n"
                + "				managment.lectures l\n"
                + "			where\n"
                + "				l.id = a.lecture_id)) as course_name\n"
                + "	from\n"
                + "		managment.attendance a\n"
                + "	group by\n"
                + "		student_number,\n"
                + "		status,\n"
                + "		course_code,\n"
                + "		totalLectureCourse,\n"
                + "		course_name\n"
                + "	having\n"
                + "		(SUM(case when status = 'Absent' then 1 else 0 end) * 100 / COUNT(*)) > 25\n"
                + "               ) ac\n"
                + "where\n"
                + "	s.student_number = ac.student_number;";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();

            ObservableList<String[]> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String[] row = new String[11];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                row[9] = rs.getString(10);
                row[10] = rs.getString(11);
                data.add(row);
            }

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

        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void importDataFromExcle() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // Use HSSFWorkbook

            try {

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                PreparedStatement pst;
                Connection conn;
                conn = DatabaseConnect.connDB();

                String insertQuery = "insert\n"
                        + "	into\n"
                        + "	managment.attendance (lecture_id,\n"
                        + "	student_number,\n"
                        + "	student_name,\n"
                        + "	student_mobile,\n"
                        + "	status)\n"
                        + "values (?,\n"
                        + "?,\n"
                        + "?,\n"
                        + "?,\n"
                        + "?);";
                pst = conn.prepareStatement(insertQuery);

                // Iterate over the rows in the Excel sheet
                for (int i = 1; i <= rowCount; i++) { // Start from index 1 to skip the header row
                    Row row = sheet.getRow(i);

                    // Extract the cell values from the row
                    int lectureId = (int) row.getCell(0).getNumericCellValue();
                    String studentNumber = row.getCell(1).getStringCellValue();
                    String studentName = row.getCell(2).getStringCellValue();
                    String studentMobile = row.getCell(3).getStringCellValue();
                    String status = row.getCell(4).getStringCellValue();

                    pst.setInt(1, lectureId);
                    pst.setString(2, studentNumber);
                    pst.setString(3, studentName);
                    pst.setString(4, studentMobile);
                    pst.setString(5, status);

                    pst.executeQuery();
                }

                System.out.println("Data imported successfully!");
                JOptionPane.showMessageDialog(null, "Data imported successfully!");

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

}
