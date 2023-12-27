package sample.yadmin.AdminPages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;
import sample._BackEnd.TableView.AdminEmployeeTable;
import sample.yadmin.AdminPages.EditCustomerEmployee.EmployeeInfoEdit;

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

public class AdminEmployeeInfo extends DBConnection implements Initializable {
    public TableView<AdminEmployeeTable> employeeTable;
    public TableColumn<AdminEmployeeTable, String> nidCol;
    public TableColumn<AdminEmployeeTable, String> nameCol;
    public TableColumn<AdminEmployeeTable, String> emailCol;
    public TableColumn<AdminEmployeeTable, String> phoneCol;
    public TableColumn<AdminEmployeeTable, String> addressCol;
    public TableColumn<AdminEmployeeTable, String> passCol;
    public TableColumn actionCol;

    private ObservableList<AdminEmployeeTable> TABLEROW = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TABLEROW.clear();
        // Встановлення значень для відповідних стовпців у таблиці з використанням даних з класу AdminEmployeeTable
        nameCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("Name"));
        nidCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("NID"));
        emailCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("Email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("Phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("Address"));
        passCol.setCellValueFactory(new PropertyValueFactory<AdminEmployeeTable, String>("Pass"));
        showEmployeeTable();
        actionButtons();
    }

    // Метод для відображення таблиці з інформацією про працівників
    public void showEmployeeTable() {
        TABLEROW.clear();
        Connection connection = getConnections();
        try {
            if (!connection.isClosed()) {
                // SQL-запит для отримання даних з таблиці EMPLOYEEINFO та їх відображення в таблиці
                String sql = "SELECT * FROM EMPLOYEEINFO ORDER BY NID";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String NID = resultSet.getString("NID");
                    String NAME = resultSet.getString("NAME");
                    String EMAIL = resultSet.getString("EMAIL");
                    String PHONE = resultSet.getString("PHONE");
                    String ADDRESS = resultSet.getString("ADDRESS");
                    String PASS = resultSet.getString("PASSWORD");

                    AdminEmployeeTable empTable = new AdminEmployeeTable(NID, NAME, EMAIL, PHONE, ADDRESS, PASS);

                    TABLEROW.add(empTable);
                }
                employeeTable.getItems().setAll(TABLEROW);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // Метод для налаштування кнопок в рядках таблиці
    private void actionButtons() {
        Callback<TableColumn<AdminEmployeeTable, String>, TableCell<AdminEmployeeTable, String>> cellCallback =
                new Callback<TableColumn<AdminEmployeeTable, String>, TableCell<AdminEmployeeTable, String>>() {
                    @Override
                    public TableCell<AdminEmployeeTable, String> call(TableColumn<AdminEmployeeTable, String> param) {

                        TableCell<AdminEmployeeTable, String> cell = new TableCell<AdminEmployeeTable, String>() {

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);

                            public HBox hBox = new HBox(25, editIcon, deleteIcon);

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    deleteIcon.setStyle(
                                            " -fx-cursor: hand ;"
                                                    + "-glyph-size:20px;"
                                                    + "-fx-fill:#ffffff;"
                                    );

                                    deleteIcon.setOnMouseEntered((MouseEvent event) -> {
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:khaki;"
                                        );
                                    });

                                    deleteIcon.setOnMouseExited((MouseEvent event2) -> {
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:white;"
                                        );
                                    });

                                    deleteIcon.setOnMouseClicked((MouseEvent event2) -> {
                                        deleteIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:lightgreen;"
                                        );

                                        AdminEmployeeTable adminEmployeeTable = getTableView().getItems().get(getIndex());
                                        tableRowDelete(adminEmployeeTable);

                                    });

                                    editIcon.setStyle(
                                            " -fx-cursor: hand ;"
                                                    + "-glyph-size:20px;"
                                                    + "-fx-fill:#ffffff;"
                                    );

                                    editIcon.setOnMouseEntered((MouseEvent event) -> {
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:khaki;"
                                        );
                                    });

                                    editIcon.setOnMouseExited((MouseEvent event2) -> {
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:white;"
                                        );
                                    });

                                    editIcon.setOnMouseClicked((MouseEvent event2) -> {
                                        editIcon.setStyle(
                                                " -fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:lightgreen;"
                                        );

                                        AdminEmployeeTable adminEmployeeTable = getTableView().getItems().get(getIndex());
                                        editTableRowInfo(adminEmployeeTable);
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

    // Метод для відображення вікна редагування інформації про працівника
    private void editTableRowInfo(AdminEmployeeTable adminEmployeeTable) {
        Connection connection = getConnections();
        try {
            if (!connection.isClosed()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/yadmin/AdminPages/EditCustomerEmployee/EmployeeInfoEdit.fxml"));
                Parent viewContact = loader.load();
                Scene scene = new Scene(viewContact);
                // оновлення інформації
                EmployeeInfoEdit employeeInfoEdit = loader.getController();
                employeeInfoEdit.setEmployeeInfo(adminEmployeeTable);

                Stage window = new Stage();
                window.setScene(scene);
                window.initStyle(StageStyle.UNDECORATED);

                stagePosition(window, viewContact);

                window.showAndWait();
                showEmployeeTable();
            }

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    // Метод для визначення позиції вікна на екрані при його переміщенні
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

    // Метод для видалення рядка таблиці та відображення відповідного повідомлення
    public void tableRowDelete(AdminEmployeeTable adminEmployeeTable) {
        Connection connection = getConnections();
        try {
            if (!connection.isClosed()) {
                // SQL-запит для видалення працівника за його унікальним номером (NID)
                String sql = "DELETE FROM EmployeeInfo where NID=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, adminEmployeeTable.getNID());
                statement.execute();
                CommonTask.showAlert(Alert.AlertType.INFORMATION, "Delete Operation Successful", "Employee Named " + adminEmployeeTable.getName() + " is deleted from the database!");

                employeeTable.getItems().remove(adminEmployeeTable);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }
    }
}
