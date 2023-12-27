package sample.customer.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import sample.Main;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserSignup implements Initializable {

    public TextField CustomerNameField;
    public TextField CustomerNIDField;
    public TextField CustomerPassField;
    public TextField CustomerEmailField;
    public TextField CustomerPhoneField;
    public TextArea CustomerAddressField;
    public ImageView closeWindow;
    Connection connection = DBConnection.getConnections();

    // Обробник події для кнопки реєстрації користувача
    @FXML
    void UserSignUp(ActionEvent event) throws IOException, SQLException {
        String customerName = CustomerNameField.getText();
        String customerNID = CustomerNIDField.getText();
        String customerPassword = CustomerPassField.getText();
        String customerEmail = CustomerEmailField.getText();
        String customerPhone = CustomerPhoneField.getText();
        String customerAddress = CustomerAddressField.getText();
        if (customerName.isEmpty() || customerNID.isEmpty()  || customerPassword.isEmpty() || customerEmail.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()) {
            // Виведення сповіщення про невірне заповнення полів
            CommonTask.showAlert(Alert.AlertType.WARNING, "Error", "Field can't be empty!");
        } else {
            // Вставка нового користувача в базу даних
            String sql = "INSERT INTO CUSTOMERINFO(NAME, NID, PASSWORD, EMAIL, PHONE, ADDRESS) VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, customerNID);
            preparedStatement.setString(3, customerPassword);
            preparedStatement.setString(4, customerEmail);
            preparedStatement.setString(5, customerPhone);
            preparedStatement.setString(6, customerAddress);
            try{
                preparedStatement.execute();
                // Виведення сповіщення про успішну реєстрацію
                CommonTask.showAlert(Alert.AlertType.INFORMATION, "Successful", "Sign-up Successful!");
                // Перехід на сторінку входу користувача
                CommonTask.pageNavigation("UserLogin.fxml", Main.stage,this.getClass(),"Customer Login", 600, 400);
            } catch (SQLException e){
                // Виведення сповіщення про помилку (наприклад, користувач з таким NID вже існує)
                CommonTask.showAlert(Alert.AlertType.ERROR, "Error", "Account already exists with this NID!");
            } finally {
                DBConnection.closeConnections();
            }
        }
    }

    // Обробник події для кнопки повернення на сторінку входу користувача
    public void BackToUserLogin(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("UserLogin.fxml", Main.stage,this.getClass(),"User Home", 600, 400);
    }

    private static final String IDLE_BUTTON_STYLE = "-fx-scale-x: 1; -fx-scale-y: 1; -fx-opacity: 0.8";
    private static final String HOVERED_BUTTON_STYLE = "-fx-scale-x: 1.2; -fx-scale-y: 1.2; -fx-opacity: 1";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Обробник події для кнопки закриття вікна
        closeWindow.setOnMouseClicked(event -> {
            System.exit(0);
        });
        // Зміна стилів при наведенні курсору на кнопку закриття вікна
        closeWindow.setOnMouseEntered(e -> closeWindow.setStyle(HOVERED_BUTTON_STYLE));
        closeWindow.setOnMouseExited(e -> closeWindow.setStyle(IDLE_BUTTON_STYLE));
    }
}
