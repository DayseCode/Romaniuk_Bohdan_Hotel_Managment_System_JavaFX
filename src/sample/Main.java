package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample._BackEnd.TableView.ResizeHelper;

// Головний клас, який наслідується від Application
public class Main extends Application {

    // Змінні для збереження об'єкту Stage та координат
    public static Stage stage;
    public static double x, y;
    public static double xxx, yyy;

    // Метод, який викликається при запуску додатку
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ініціалізація об'єкту Stage
        stage = primaryStage;

        // Завантаження кореневого вузла з файлу FXML
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        // Встановлення стилю вікна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Встановлення заголовку вікна
        primaryStage.setTitle("Hotel Management System");

        // Встановлення сцени з кореневим вузлом та розміром вікна (600x400)
        primaryStage.setScene(new Scene(root, 600, 400));

        // Встановлення початкової позиції вікна
        primaryStage.setX(140);
        primaryStage.setY(130);

        // Збереження координат вікна
        x = primaryStage.getX();
        y = primaryStage.getY();

        // Обробка подій для переміщення вікна
        root.setOnMousePressed(event -> {
            xxx = event.getSceneX();
            yyy = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xxx);
            primaryStage.setY(event.getScreenY() - yyy);
            x = primaryStage.getX();
            y = primaryStage.getY();
        });

        // Відображення вікна
        primaryStage.show();
    }

    // Головний метод, який запускає додаток
    public static void main(String[] args) {
        launch(args);
    }
}
