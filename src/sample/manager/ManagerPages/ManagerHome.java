package sample.manager.ManagerPages;

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

public class ManagerHome implements Initializable {

    // Поля FXML для керування графічним інтерфейсом
    public AnchorPane rootAnchorPane;
    public StackPane rootPane;

    // Статична змінна для визначення, чи відбулося вже привітання
    public static int welcomed = 0;

    // Обробник натискання кнопки "Manager Info"
    public void ManagerInfo(ActionEvent actionEvent) throws IOException {
        // Виклик методу для переходу на сторінку "Manager Info"
        CommonTask.pageNavigation("ManagerInfo/ManagerInfo.fxml", null, this.getClass(), "User Home", 550, 400);
    }

    // Обробник натискання кнопки "Log Out"
    public void LogOut(ActionEvent actionEvent) throws IOException {
        // Виклик методу для виходу із облікового запису та повернення на стартову сторінку
        CommonTask.pageNavigation("/sample/sample.fxml", Main.stage, this.getClass(), "User Home", 600, 400);
        welcomed = 0;  // Скидання лічильника привітань
    }

    // Метод ініціалізації, який викликається при завантаженні сторінки
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Перевірка, чи привітання вже відбулося
        if (welcomed == 0) {
            // Виведення спливаючого повідомлення про успішний вхід при першому вході
            CommonTask.showJFXAlert(rootPane, rootAnchorPane, "information", "Login Success!", "Successfully Logged In!", JFXDialog.DialogTransition.CENTER);
            welcomed = 1;  // Встановлення прапорця привітання
        }
    }
}
