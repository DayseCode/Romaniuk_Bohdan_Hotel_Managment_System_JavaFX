package sample.customer.CustomerPages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;
import sample._BackEnd.TableView.CustomerRoomTable;
import sample._BackEnd.TableView.ManagerRoomTable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserRoomDetails extends DBConnection implements Initializable {

    @FXML
    private TextField UserSearchRoomStatus;

    @FXML
    private Button UserRoomSearchBtn;

    @FXML
    public TableView<CustomerRoomTable> roomTable;
    public TableColumn<CustomerRoomTable, String> roomNoCol;
    public TableColumn<CustomerRoomTable, String> roomTypeCol;
    public TableColumn<CustomerRoomTable, String> roomCapacityCol;
    public TableColumn<CustomerRoomTable, String> price_DayCol;
    public TableColumn<CustomerRoomTable, String> roomStatusCol;
    private ObservableList<CustomerRoomTable> TABLEROW = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Встановлення властивостей стовпців таблиці
        roomNoCol.setCellValueFactory(new PropertyValueFactory<CustomerRoomTable, String>("ROOMNO"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<CustomerRoomTable, String>("TYPE"));
        roomCapacityCol.setCellValueFactory(new PropertyValueFactory<CustomerRoomTable, String>("CAPACITY"));
        price_DayCol.setCellValueFactory(new PropertyValueFactory<CustomerRoomTable, String>("PRICEDAY"));
        roomStatusCol.setCellValueFactory(new PropertyValueFactory<CustomerRoomTable, String>("STATUS"));
        // Завантаження таблиці номерів кімнат при ініціалізації
        showRoomTable();
    }

    // Метод для відображення таблиці номерів кімнат
    public void showRoomTable(){
        TABLEROW.clear();
        Connection connection = getConnections();
        try {
            if(!connection.isClosed()){
                String sql = "SELECT * FROM ROOMINFO ORDER BY STATUS";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    String ROOMNO = resultSet.getString("ROOM_NO");
                    String TYPE = resultSet.getString("TYPE");
                    String CAPACITY = resultSet.getString("CAPACITY");
                    String PRICEDAY = resultSet.getString("PRICE_DAY");
                    String STATUS = resultSet.getString("STATUS");

                    CustomerRoomTable roomTablee = new CustomerRoomTable(ROOMNO, TYPE, CAPACITY, PRICEDAY, STATUS);
                    TABLEROW.add(roomTablee);
                }
                roomTable.getItems().setAll(TABLEROW);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }

        // Створення FilteredList для фільтрації таблиці за значенням в полі пошуку
        FilteredList<CustomerRoomTable> filteredData = new FilteredList<>(TABLEROW, b -> true);
        UserSearchRoomStatus.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(search -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Порівняння значень усіх стовпців таблиці з полем пошуку
                String searchKeyword = newValue.toLowerCase();
                if (search.getROOMNO().toLowerCase().indexOf(searchKeyword) != -1 ) {
                    return true; // Фільтр за номером кімнати
                } else if (search.getTYPE().toLowerCase().indexOf(searchKeyword) != -1 ) {
                    return true; // Фільтр за типом кімнати
                } else if (search.getCAPACITY().toLowerCase().indexOf(searchKeyword) != -1 ) {
                    return true; // Фільтр за вмістимістю кімнати
                } else if (search.getPRICEDAY().toLowerCase().indexOf(searchKeyword) != -1 ) {
                    return true; // Фільтр за ціною за день
                } else if(search.getSTATUS().toLowerCase().indexOf(searchKeyword) != -1){
                    return true; // Фільтр за статусом кімнати
                } else {
                    return false;
                }
            });
        });

        // Створення SortedList для сортування відфільтрованих даних
        SortedList<CustomerRoomTable> sortedData = new SortedList<>(filteredData);

        // Прив'язка компаратора SortedList до компаратора таблиці
        sortedData.comparatorProperty().bind(roomTable.comparatorProperty());

        // Додавання відсортованих (і відфільтрованих) даних в таблицю
        roomTable.setItems(sortedData);
    }
}
