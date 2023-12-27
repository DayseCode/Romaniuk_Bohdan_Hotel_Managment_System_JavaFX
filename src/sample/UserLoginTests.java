package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import sample._BackEnd.CommonTask;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;
import static org.testfx.util.WaitForAsyncUtils.waitFor;


public class UserLoginTests extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CommonTask.pageNavigation("customer/Login/UserLogin.fxml",Main.stage,this.getClass(),"Customer Login", 600, 400);

    }

    @Test
    public void testTextField() {
        // Пошук текстового поля за його ID
        JFXTextField textField = lookup("#customerNIDField").query();

        // Введення тексту в текстове поле
        clickOn(textField).write("YourText");

        // Перевірка, чи має текстове поле введений текст
        assertEquals("YourText", textField.getText());
    }

    @Test
    public void testPasswordField() {
        // Пошук парольного поля за його ID
        JFXPasswordField passwordField = lookup("#customerPassField").query();

        // Введення тексту в парольне поле
        clickOn(passwordField).write("YourPassword");

        // Перевірка, чи має парольне поле введений текст
        assertEquals("YourPassword", passwordField.getText());
    }

    @Test
    public void testButtonClick() {
        // Пошук кнопки за її ID
        JFXButton loginButton = lookup("#loginButtonId").query();
        JFXButton backButton = lookup("#backButtonId").query();

        // Клік на кнопку
        clickOn(loginButton);
        clickOn(backButton);
    }



    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }
}
