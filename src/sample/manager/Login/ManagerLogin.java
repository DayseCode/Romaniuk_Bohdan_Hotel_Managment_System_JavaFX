package sample.manager.Login;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagerLogin implements Initializable {
    public ImageView closeWindow;
    public JFXTextField employeeNIDField;
    public JFXPasswordField employeePassField;
    public static String currentEmployeeNID;
    public StackPane rootPane;
    public AnchorPane rootAnchorPane;

    // Обробник події для кнопки входу менеджера
    public void ManagerLogin(ActionEvent actionEvent) throws IOException {
        String employeeNID = employeeNIDField.getText();
        currentEmployeeNID = employeeNID;
        String employeePass = employeePassField.getText();
        try {
            Connection connection = DBConnection.getConnections();
            if (employeeNID.isEmpty() || employeePass.isEmpty()) {
                // Виведення сповіщення про невірне заповнення полів
                CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Failed!", "Field can not be empty!", JFXDialog.DialogTransition.CENTER);
            } else {
                // Перевірка введених даних та входу користувача
                String sql = "SELECT * FROM EMPLOYEEINFO WHERE NID = ? AND PASSWORD = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, employeeNID);
                preparedStatement.setString(2, employeePass);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // Перехід на сторінку головного меню менеджера
                    CommonTask.pageNavigation("/sample/manager/ManagerPages/ManagerMain.fxml", Main.stage,this.getClass(),"Manager Dashboard", 800, 600);
                } else {
                    // Виведення сповіщення про невірний NID або пароль
                    CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Failed!", "Incorrect NID or Password!", JFXDialog.DialogTransition.CENTER);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBConnection.closeConnections();
        }
    }

    // Обробник події для кнопки повернення на головну сторінку
    public void BackToMain(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("/sample/sample.fxml", Main.stage,this.getClass(),"Hotel Management System", 600, 400);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Обробник події для кнопки закриття вікна
        closeWindow.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }
}
