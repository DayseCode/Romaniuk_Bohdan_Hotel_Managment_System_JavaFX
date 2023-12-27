package sample.yadmin.AdminPages;

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

public class AdminHome implements Initializable {

    // Змінна, яка вказує, чи вже виводилося привітання користувачу
    public static int welcomed = 0;
    public StackPane rootPane;
    public AnchorPane rootAnchorPane;

    // Метод для повернення до головного вікна програми
    public void BackToMain(ActionEvent actionEvent) throws IOException {
        CommonTask.pageNavigation("/sample/sample.fxml", Main.stage, this.getClass(), "Admin Dashboard", 600, 400);
    }

    // Метод для ініціалізації сторінки
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Перевірка, чи вже виводилося привітання
        if (welcomed == 0){
            // Виведення повідомлення про успішний вхід
            CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Success!", "Successfully Logged In!", JFXDialog.DialogTransition.CENTER);
            welcomed = 1;
        }
    }
}
