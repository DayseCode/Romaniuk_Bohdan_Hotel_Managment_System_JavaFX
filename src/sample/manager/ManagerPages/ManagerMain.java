package sample.manager.ManagerPages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerMain implements Initializable {
    public BorderPane borderpane;
    public FontAwesomeIconView minimizeWindow;
    public AnchorPane managerMainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Завантаження початкової сторінки
        windowLoad("ManagerHome.fxml");

        // Обробник події для згортання вікна при натисканні на відповідний елемент
        minimizeWindow.setOnMouseClicked(event -> {
            minimizeStageOfNode((Node) event.getSource());
        });
    }

    // Метод для згортання вікна
    private void minimizeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).setIconified(true);
    }

    // Метод для завантаження вмісту сторінки
    public void windowLoad(String URL) {
        try {
            // Завантаження і встановлення сторінки за вказаним URL
            Pane window = FXMLLoader.load(getClass().getResource(URL));
            borderpane.setCenter(window);
        } catch (Exception err) {
            System.out.println("Problem : " + err);
        }
    }

    // Обробники подій для кнопок навігації

    public void ManageRooms(ActionEvent actionEvent) {
        windowLoad("ManagerManageRooms.fxml");
    }

    public void ManagerHome(ActionEvent actionEvent) {
        windowLoad("ManagerHome.fxml");
    }

    public void ManagerCheckIn(ActionEvent actionEvent) {
        windowLoad("ManagerCheckIn.fxml");
    }

    public void ManagerCheckOut(ActionEvent actionEvent) {
        windowLoad("ManagerCheckOut.fxml");
    }

    public void CheckOutDetails(ActionEvent actionEvent) {
        windowLoad("ManagerCheckOutDetails.fxml");
    }

    // Обробник події для закриття програми
    public void closeApplication(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
