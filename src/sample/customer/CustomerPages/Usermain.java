package sample.customer.CustomerPages;

import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample._BackEnd.CommonTask;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static sample._BackEnd.CommonTask.newStage;

public class Usermain implements Initializable {

    public BorderPane borderpane;
    public Button goHomeID;
    public Button roomDetailsID;
    public Button checkInID;
    public Button checkInOutID;
    public FontAwesomeIconView closeWindow;
    public FontAwesomeIconView minimizeWindow;
    public AnchorPane userMainPane;
    public StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Виклик методу для завантаження початкової сторінки
        windowLoadStackPane("UserHome.fxml");

        // Обробник події для кнопки закриття вікна
        closeWindow.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Обробник події для кнопки згортання вікна
        minimizeWindow.setOnMouseClicked(event -> {
            minimizeStageOfNode((Node) event.getSource());
        });

    }

    // Метод для мінімізації вікна
    private void minimizeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).setIconified(true);
    }

    // Метод для завантаження сторінки в область BorderPane
    public void windowLoad(String URL)
    {
        try
        {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(URL));
            borderpane.setCenter(pane);
        }
        catch(Exception err)
        {
            System.out.println("Problem : " + err);
        }
    }

    // Метод для завантаження сторінки в область StackPane
    public void windowLoadStackPane(String URL)
    {
        try
        {
            StackPane pane = FXMLLoader.load(getClass().getResource(URL));
            borderpane.setCenter(pane);
        }
        catch(Exception err)
        {
            System.out.println("Problem : " + err);
        }
    }

    // Обробник події для кнопки "Go Home"
    public void GoHome(ActionEvent actionEvent) {
        windowLoadStackPane("UserHome.fxml");
    }

    // Обробник події для кнопки "Room Details"
    public void GoRoomDetails(ActionEvent actionEvent) {
        windowLoad("UserRoomDetails.fxml");
    }

    // Обробник події для кнопки "Check In"
    public void GoCheckIn(ActionEvent actionEvent) {
        windowLoadStackPane("UserCheckIn.fxml");
    }

    // Обробник події для кнопки "Check Out Details"
    public void GoCheckDetails(ActionEvent actionEvent) {
        windowLoad("UserCheckOutDetails.fxml");
    }
}
