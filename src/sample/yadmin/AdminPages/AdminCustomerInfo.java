package sample.yadmin.AdminPages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;
import sample._BackEnd.TableView.AdminCustomerTable;
import sample.yadmin.AdminPages.EditCustomerEmployee.CustomerInfoEdit;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static sample.Main.xxx;
import static sample.Main.yyy;

public class AdminCustomerInfo extends DBConnection implements Initializable{
    public TableView<AdminCustomerTable> customerTable;
    public TableColumn<AdminCustomerTable, String> nidCol;
    public TableColumn<AdminCustomerTable, String> nameCol;
    public TableColumn<AdminCustomerTable, String> emailCol;
    public TableColumn<AdminCustomerTable, String> phoneCol;
    public TableColumn<AdminCustomerTable, String> addressCol;
    public TableColumn<AdminCustomerTable, String> passCol;
    public TableColumn actionCol;  // Стовпець для кнопок дій

    private ObservableList<AdminCustomerTable> TABLEROW = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TABLEROW.clear();
        // Встановлення значень для відповідних стовпців у таблиці з використанням даних з класу AdminCustomerTable
        nameCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("Name"));
        nidCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("NID"));
        emailCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("Email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("Phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("Address"));
        passCol.setCellValueFactory(new PropertyValueFactory<AdminCustomerTable, String>("Pass"));
        showCustomerTable();
        actionButtons();
    }

    // Метод для відображення даних у таблиці
    public void showCustomerTable(){
        TABLEROW.clear();
        Connection connection = getConnections();
        try {
            if(!connection.isClosed()){
                // SQL-запит для отримання даних з таблиці CUSTOMERINFO та їх відображення в таблиці
                String sql = "SELECT * FROM CUSTOMERINFO ORDER BY NID";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    String NID = resultSet.getString("NID");
                    String NAME = resultSet.getString("NAME");
                    String EMAIL = resultSet.getString("EMAIL");
                    String PHONE = resultSet.getString("PHONE");
                    String ADDRESS = resultSet.getString("ADDRESS");
                    String PASS = resultSet.getString("PASSWORD");

                    AdminCustomerTable custTable = new AdminCustomerTable(NID, NAME, EMAIL, PHONE, ADDRESS, PASS);

                    TABLEROW.add(custTable);
                }
                customerTable.getItems().setAll(TABLEROW);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // Метод для налаштування кнопок дій в кожному рядку таблиці
    private void actionButtons() {
        Callback<TableColumn<AdminCustomerTable, String>, TableCell<AdminCustomerTable, String>> cellCallback =
                new Callback<TableColumn<AdminCustomerTable, String>, TableCell<AdminCustomerTable, String>>() {
                    @Override
                    public TableCell<AdminCustomerTable, String> call(TableColumn<AdminCustomerTable, String> param) {

                        TableCell<AdminCustomerTable, String> cell = new TableCell<AdminCustomerTable, String>() {

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);

                            public HBox hBox = new HBox(25, editIcon, deleteIcon);

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{

                                    deleteIcon.setStyle(
                                            " -fx-cursor: hand ;"
                                                    + "-glyph-size:20px;"
                                                    + "-fx-fill:#ffffff;"
                                    );

                                    deleteIcon.setOnMouseEntered((MouseEvent event) ->{
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:khaki;"
                                        );
                                    });

                                    deleteIcon.setOnMouseExited((MouseEvent event2) ->{
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:white;"
                                        );
                                    });

                                    deleteIcon.setOnMouseClicked((MouseEvent event2) ->{
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:lightgreen;"
                                        );

                                        // Видалення вибраного рядка з таблиці та бази даних
                                        AdminCustomerTable adminCustomerTable = getTableView().getItems().get(getIndex());
                                        tableRowDelete(adminCustomerTable);
                                    });

                                    editIcon.setStyle(
                                            " -fx-cursor: hand ;"
                                                    + "-glyph-size:20px;"
                                                    + "-fx-fill:#ffffff;"
                                    );

                                    editIcon.setOnMouseEntered((MouseEvent event) ->{
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:khaki;"
                                        );
                                    });

                                    editIcon.setOnMouseExited((MouseEvent event2) ->{
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:white;"
                                        );
                                    });

                                    editIcon.setOnMouseClicked((MouseEvent event2) ->{
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:lightgreen;"
                                        );
                                        // Редагування інформації вибраного рядка
                                        AdminCustomerTable adminCustomerTable = getTableView().getItems().get(getIndex());
                                        editTableRowInfo(adminCustomerTable);
                                    });

                                    hBox.setStyle("-fx-alignment:center");
                                    setGraphic(hBox);
                                }
                            }
                        };

                        return cell;
                    }
                };
        actionCol.setCellFactory(cellCallback);
    }

    // Метод для редагування інформації вибраного рядка
    private void editTableRowInfo(AdminCustomerTable adminCustomerTable) {
        Connection connection = getConnections();
        try {
            if (!connection.isClosed()) {
                // Відкриття нового вікна для редагування інформації про клієнта
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/yadmin/AdminPages/EditCustomerEmployee/CustomerInfoEdit.fxml"));
                Parent viewContact = loader.load();
                Scene scene = new Scene(viewContact);
                // Передача інформації для редагування в контролер відповідного FXML-файлу
                CustomerInfoEdit customerInfoEdit = loader.getController();
                customerInfoEdit.setCustomerInfo(adminCustomerTable);
                Stage window = new Stage();
                window.setScene(scene);
                window.initStyle(StageStyle.UNDECORATED);
                // Встановлення позиції вікна та додавання можливості перетягування
                stagePosition(window, viewContact);
                window.showAndWait();
                showCustomerTable();
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // Метод для встановлення позиції та можливості перетягування вікна
    private void stagePosition(Stage primaryStage, Parent root) {
        AtomicReference<Double> x = new AtomicReference<>(primaryStage.getX());
        AtomicReference<Double> y = new AtomicReference<>(primaryStage.getY());
        root.setOnMousePressed(event -> {
            xxx = event.getSceneX();
            yyy = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xxx);
            primaryStage.setY(event.getScreenY() - yyy);
            x.set(primaryStage.getX());
            y.set(primaryStage.getY());
        });
    }

    // Метод для видалення вибраного рядка з бази даних та таблиці
    public void tableRowDelete(AdminCustomerTable adminCustomerTable) {
        Connection connection = getConnections();
        try {
            if (!connection.isClosed()) {
                // SQL-запит для видалення клієнта за його NID
                String sql = "DELETE FROM CustomerInfo where NID=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, adminCustomerTable.getNID());
                statement.execute();
                // Відображення повідомлення про успішне видалення
                CommonTask.showAlert(Alert.AlertType.INFORMATION, "Delete Operation Successful", "Customer Named " + adminCustomerTable.getName() + " is deleted from database!");
                // Видалення рядка з таблиці
                customerTable.getItems().remove(adminCustomerTable);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }
}
