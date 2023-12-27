package sample.yadmin.AdminPages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMain implements Initializable {

    // Об'єкт головного контейнера BorderPane та елемент іконки для згортання вікна
    public BorderPane borderpane;
    public FontAwesomeIconView minimizeWindow;

    // Метод ініціалізації сторінки
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Завантаження початкової сторінки
        windowLoad("AdminHome.fxml");

        // Обробник події для згортання вікна
        minimizeWindow.setOnMouseClicked(event -> {
            minimizeStageOfNode((Node) event.getSource());
        });
    }

    // Метод для згортання вікна
    private void minimizeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).setIconified(true);
    }

    // Метод для завантаження вмісту сторінки у вікно
    public void windowLoad(String URL) {
        try {
            // Завантаження FXML файлу та встановлення його в якість центрального вмісту BorderPane
            Pane window = FXMLLoader.load(getClass().getResource(URL));
            borderpane.setCenter(window);
        } catch (Exception err) {
            System.out.println("Problem : " + err);
        }
    }

    // Методи для переходу на різні сторінки
    public void AdminHome(ActionEvent actionEvent) {
        windowLoad("AdminHome.fxml");
    }

    public void AddEmployee(ActionEvent actionEvent) {
        windowLoad("AdminAddEmployee.fxml");
    }

    public void AdminEmployeeInfo(ActionEvent actionEvent) {
        windowLoad("AdminEmployeeInfo.fxml");
    }

    public void AdminCustomerInfo(ActionEvent actionEvent) {
        windowLoad("AdminCustomerInfo.fxml");
    }

    public void AdminEarnLog(ActionEvent actionEvent) {
        windowLoad("AdminEarningLog.fxml");
    }

    public void AdminTotalEarnings(ActionEvent actionEvent) {
        windowLoad("AdminTotalEarnings.fxml");
    }

    // Метод для закриття додатку
    public void closeApplication(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
