package sample._BackEnd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;
import sample._BackEnd.TableView.ResizeHelper;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class CommonTask extends Main {
    // Глобальні змінні для збереження стану вікна
    public static Stage newStage;
    public static double xx, yy;

    // Метод для навігації між сторінками
    public static void pageNavigation(String to, Stage stage, Class<?> classes, String title, int width, int height) throws IOException {
        // Зберігаємо попередні координати вікна, якщо вони є
        double xTemp = x;
        double yTemp = y;
        // Якщо вікно не існує, створюємо нове
        if (stage == null) {
            xTemp = x + (width/5.0);
            yTemp = y + (height/7.0);
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            newStage = stage;
        }
        newStage = stage;
        // Завантажуємо FXML файл та встановлюємо його в вікно
        Parent root = FXMLLoader.load(Objects.requireNonNull(classes.getResource(to)));
        stage.setTitle(title);
        stage.setX(xTemp);
        stage.setY(yTemp);
        stage.setScene(new Scene(root, width, height));

        // Обробники подій для переміщення вікна мишею
        root.setOnMousePressed(event -> {
            xx = event.getSceneX();
            yy = event.getSceneY();
        });
        Stage finalStage = stage;
        root.setOnMouseDragged(event -> {
            finalStage.setX(event.getScreenX() - xx);
            finalStage.setY(event.getScreenY() - yy);
            x = finalStage.getX();
            y = finalStage.getY();
        });
        x = finalStage.getX();
        y = finalStage.getY();
        stage.show();
    }

    // Метод для виведення сповіщення типу Alert
    public static void showAlert(Alert.AlertType type, String title, String header){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

    // Метод для виведення сповіщення типу JFXDialog
    public static void showJFXAlert(StackPane rootPane, AnchorPane rootAnchorPane, String type, String heading, String message, JFXDialog.DialogTransition transition){
        // Створення макету JFXDialog
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setMaxWidth(230);
        layout.setMaxHeight(100);

        // Створення кнопки ОК
        JFXButton okayBtn = new JFXButton("Okay");
        okayBtn.setStyle("-fx-background-color:#4A4A4A; -fx-text-fill: white; -fx-border-color: white");
        layout.getStylesheets().add("/sample/_BackEnd/dialogPane.css");
        JFXDialog dialog = new JFXDialog(rootPane, layout, transition);
        // Обробка події для закриття діалогу при кліку на кнопку
        okayBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
        });

        // Вибір значка відповідно до типу сповіщення
        if(type.toLowerCase(Locale.ROOT).equals("information")) {
            layout.setHeading(new Label("✅  "+heading));
        } else if(type.toLowerCase(Locale.ROOT).equals("warning")) {
            layout.setHeading(new Label("💢  "+heading));
        } else if(type.toLowerCase(Locale.ROOT).equals("error")) {
            layout.setHeading(new Label("❌ "+heading));
        } else {
            layout.setHeading(new Label("⚠ "+heading));
        }
        layout.setBody(new Label(""+message));
        layout.setActions(okayBtn);
        layout.setPadding(new Insets(0, 15, 13, 0));
        dialog.show();

        // Додаємо ефект розмиття заднього плану під час відкриття діалогу
        BoxBlur blur = new BoxBlur(5, 5, 4);
        rootAnchorPane.setEffect(blur);
        // Обробка події закриття діалогу для відновлення заднього плану
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            rootAnchorPane.setEffect(null);
        });
    }
}
