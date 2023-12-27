package sample.customer.CustomerPages;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample._BackEnd.CommonTask;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserHome implements Initializable {
    public StackPane rootPane;
    public AnchorPane rootAnchorPane;
    public static int welcomed = 0;

    // Метод для переходу на сторінку логіну при натисканні кнопки "Back"
    public void BackToLoginPage(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("/sample/sample.fxml", Main.stage, this.getClass(), "User Home", 600, 400);
        welcomed = 0;
    }

    // Метод для переходу на сторінку інформації про користувача
    public void UserInfo(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("/sample/customer/CustomerPages/CustomerInfo/UserInfo.fxml", null, this.getClass(), "User Home", 550, 400);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Показ повідомлення про успішний вхід тільки при першому відкритті сторінки
        if (welcomed == 0) {
            CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Success!", "Successfully Logged In!", JFXDialog.DialogTransition.CENTER);
            welcomed = 1;
        }
    }
}
