package sample.yadmin.Login;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

public class AdminLogin implements Initializable {

    // Елементи інтерфейсу
    public TextField adminNIDField;
    public PasswordField adminPassField;
    public static String currentAdminNID;
    public ImageView closeWindow;
    public StackPane rootPane;
    public AnchorPane rootAnchorPane;

    // Метод для авторизації адміністратора
    public void LoginAdminHome(ActionEvent actionEvent) throws IOException {
        // Отримання введених даних
        String adminNID = adminNIDField.getText();
        currentAdminNID = adminNID;
        String adminPass = adminPassField.getText();

        try {
            // Встановлення з'єднання з базою даних
            Connection connection = DBConnection.getConnections();

            if (adminNID.isEmpty() || adminPass.isEmpty()) {
                // Перевірка на пусті поля та виведення повідомлення про помилку
                CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Failed!", "Field Can't be Empty!", JFXDialog.DialogTransition.CENTER);
            } else {
                // Перевірка аутентифікаційних даних
                String sql = "SELECT * FROM ADMININFO WHERE NID = ? AND PASSWORD = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, adminNID);
                preparedStatement.setString(2, adminPass);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Навігація до головної сторінки адміністратора
                    CommonTask.pageNavigation("/sample/yadmin/AdminPages/AdminMain.fxml", Main.stage, this.getClass(), "Admin Dashboard", 1000, 600);
                } else {
                    // Виведення повідомлення про невірний NID чи пароль
                    CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Failed!", "Incorrect NID or Password!", JFXDialog.DialogTransition.CENTER);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            // Закриття з'єднання з базою даних
            DBConnection.closeConnections();
        }
    }

    // Метод для повернення на головний екран
    public void BackToMain(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("/sample/sample.fxml", Main.stage, this.getClass(), "Hotel Management System", 600, 400);
    }

    // Ініціалізація
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Закриття вікна при натисканні на хрестик
        closeWindow.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }
}
