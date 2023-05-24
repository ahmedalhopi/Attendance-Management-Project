package javaapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class LoginController implements Initializable {
    
    public static String USER_ID;
    @FXML
    private TextField id_number_txt;
    @FXML
    private PasswordField password_txt;
    @FXML
    private Button login_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = DatabaseConnect.connDB();
            System.out.println(conn);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        id_number_txt.setText("2020");
        password_txt.setText("123");
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws ClassNotFoundException, IOException {
        Connection conn = DatabaseConnect.connDB();
        System.out.println(conn);
        PreparedStatement pst;
        ResultSet rs;
        boolean found = false;
        boolean admin = false;
        String sql = "select * from managment.users where user_id = ? and password = ?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, id_number_txt.getText());
                pst.setString(2, password_txt.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    USER_ID = id_number_txt.getText();
                    if("1".equals(rs.getString("is_admin")))
                        admin = true;
                    found = true;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            
        if (found && admin) {
            System.out.println("Login Don Admin");
            Stage primaryStage = new Stage();
            Stage currentStage = (Stage) login_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Attendance Management");
            primaryStage.setResizable(false);
            primaryStage.iconifiedProperty();
            currentStage.close();
            primaryStage.show();

        } else if (found && !admin) {
            System.out.println("Login Don User");
            System.out.println("Login Don Admin");
            Stage primaryStage = new Stage();
            Stage currentStage = (Stage) login_btn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssistantsHome.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Attendance Management");
            primaryStage.setResizable(false);
            primaryStage.iconifiedProperty();
            currentStage.close();
            primaryStage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Id Number or Password is not corrcet!");
        }
    }
;

}
