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

public class UsersController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> number;
    @FXML
    private TableColumn<ObservableList<String>, String> name;
    @FXML
    private TableColumn<ObservableList<String>, String> mobile;
    @FXML
    private TableColumn<ObservableList<String>, String> living;
    @FXML
    private TableColumn<ObservableList<String>, String> department;
    @FXML
    private TableColumn<ObservableList<String>, String> password;
    /////////////////////////////////////////////////////////
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
    private TextField password0;

    /////////////////////////////////////////////////////////////////
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
    private TextField password1;
    @FXML
    private Button update1;
    ////////////////////////////////////////////////////////////////
    @FXML
    private Pane paneContainerDelete;
    @FXML
    private TextField number2;

    ///////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
    }


    public void get_users() throws ClassNotFoundException {
        tableView.getItems().clear();
        tableView.setVisible(true);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);
        number.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mobile.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        living.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        department.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        password.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        PreparedStatement pst;
        ResultSet rs ;
        Connection conn;

        String sel = "select * from managment.users";

        try {

            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            rs = pst.executeQuery();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("user_id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("mobile"));
                row.add(rs.getString("living"));
                row.add(rs.getString("department"));
                row.add(rs.getString("password"));
                tableView.getItems().add(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void add_user() {
        tableView.setVisible(false);
        paneContainer.setVisible(true);
        paneContainerUpdate.setVisible(false);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void saveDate() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "INSERT INTO managment.users (user_id, full_name, living, department, mobile, password) VALUES(?, ?, ?, ?, ?,? );";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, number0.getText());
            pst.setString(2, name0.getText());
            pst.setString(3, living0.getText());
            pst.setString(4, department0.getText());
            pst.setString(5, mobile0.getText());
            pst.setString(6, password0.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Inserted");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.err.println(ex);
        }
    }

    public void update_user() {
        tableView.setVisible(false);
        paneContainer.setVisible(false);
        paneContainerUpdate.setVisible(true);
        paneContainerDelete.setVisible(false);
        update1.setDisable(true);

    }

    public void getDataForUser() throws ClassNotFoundException {
        Connection conn = DatabaseConnect.connDB();
        System.out.println(conn);
        PreparedStatement pst;
        ResultSet rs;
        String log = "select * from managment.users where user_id = ? ";
        try {
            pst = conn.prepareStatement(log);
            pst.setString(1, number1.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                name1.setText(rs.getString("full_name"));
                living1.setText(rs.getString("living"));
                department1.setText(rs.getString("department"));
                mobile1.setText(rs.getString("mobile"));   
                password1.setText(rs.getString("password"));
                update1.setDisable(false);
                System.out.println(rs);
            } else {
                JOptionPane.showMessageDialog(null, "User not found");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void updateDate() throws ClassNotFoundException {
        PreparedStatement pst;
        Connection conn;
        String sel = "UPDATE managment.users SET full_name=?, living=?, department=?, mobile=?, password=? WHERE user_id=?;";
        try {
            conn = DatabaseConnect.connDB();
            pst = conn.prepareStatement(sel);
            pst.setString(1, name1.getText());
            pst.setString(2, living1.getText());
            pst.setString(3, department1.getText());
            pst.setString(4, mobile1.getText());
            pst.setString(5, password1.getText());
            pst.setString(6, number1.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.out.println(ex);
        }
    }

    public void delete_user() {
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
        alert.setContentText("Do you want to delete user?");

        ButtonType confirmButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("No");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            PreparedStatement pst;
            Connection conn;
            String sel = "DELETE from managment.users WHERE user_id=?;";
            try {
                conn = DatabaseConnect.connDB();
                pst = conn.prepareStatement(sel);
                pst.setString(1, number2.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "User Deleted");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex);
            }
        }
    }

}
