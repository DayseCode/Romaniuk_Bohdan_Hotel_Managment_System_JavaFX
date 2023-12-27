package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import sample._BackEnd.CommonTask;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    // Елементи інтерфейсу
    public ImageView closeWindow;
    public JFXButton customerLoginBtn;
    public JFXButton managerLoginBtn;
    public JFXButton adminLoginBtn;
    public JFXButton btn;

    // Обробник події для кнопки "Customer Login"
    @FXML
    void Customer_Login(ActionEvent event) throws IOException {
        // Виклик методу для навігації на сторінку авторизації користувача
        CommonTask.pageNavigation("customer/Login/UserLogin.fxml",Main.stage,this.getClass(),"Customer Login", 600, 400);
    }

    // Обробник події для кнопки "Manager Login"
    @FXML
    void Manager_Login(ActionEvent event) throws IOException {
        // Виклик методу для навігації на сторінку авторизації менеджера
        CommonTask.pageNavigation("manager/Login/ManagerLogin.fxml",Main.stage,this.getClass(),"Manager Login", 600, 400);
    }

    // Обробник події для кнопки "Admin Login"
    @FXML
    void Admin_Login(ActionEvent event) throws IOException {
        // Виклик методу для навігації на сторінку авторизації адміністратора
        CommonTask.pageNavigation("yadmin/Login/AdminLogin.fxml", Main.stage,this.getClass(),"Admin Login", 600, 400);
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
