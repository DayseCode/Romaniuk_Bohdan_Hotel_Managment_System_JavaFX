package sample.customer.CustomerPages;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static sample._BackEnd.DBConnection.connection;
import static sample.customer.Login.UserLogin.currentCustomerNID;

public class UserCheckIn implements Initializable {

    public Label roomCapacityField;
    public Label roomTypeField;
    public Label roomPriceField;
    public AnchorPane userCheckInPane;
    public JFXComboBox userRoomChoicebox;
    public StackPane rootPane;
    @FXML
    private Label UserNameField;
    @FXML
    private Label UserNIDField;
    @FXML
    private Label UserEmailField;
    @FXML
    private Label UserPhoneField;
    @FXML
    private Label UserAddressField;
    @FXML
    public DatePicker UserCheckIndate;

    @FXML
    public void UserCheckInSubmitBtn(ActionEvent event) throws SQLException {
        // Отримання даних з полів форми
        String name = UserNameField.getText();
        String NID = UserNIDField.getText();
        String Email = UserEmailField.getText();
        String Phone = UserPhoneField.getText();
        String Address = UserAddressField.getText();
        String RoomNo = userRoomChoicebox.getValue() + "";
        LocalDate CheckInDate = UserCheckIndate.getValue();
        String roomCapacity = roomCapacityField.getText() + "";
        String roomType = roomTypeField.getText() + "";
        String roomPrice = roomPriceField.getText() + "";

        // Перевірка, чи вибрана дата і чи вона не більше, ніж за 30 днів в майбутньому
        if (CheckInDate == null || CheckInDate.isAfter(LocalDate.now().plusDays(30))) {
            CommonTask.showJFXAlert(rootPane, userCheckInPane, "warning", "Warning!", "Invalid Check-In Date! Please select a date within the next 30 days.", JFXDialog.DialogTransition.CENTER);
            return;
        }

        Connection connection = DBConnection.getConnections();
        if (roomType.equals("") || roomPrice.equals("") || roomCapacity.equals("")) {
            CommonTask.showJFXAlert(rootPane, userCheckInPane, "warning", "Warning!", "Field Can't be Empty!", JFXDialog.DialogTransition.CENTER);
        } else {
            // Виконання операцій з базою даних для оформлення заселення
            String sql = "INSERT INTO CHECKINOUTINFO (NAME, NID, EMAIL, PHONE, ADDRESS, ROOMNO, CHECKEDIN, ROOMTYPE, CAPACITY, PRICEDAY) VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, NID);
            preparedStatement.setString(3, Email);
            preparedStatement.setString(4, Phone);
            preparedStatement.setString(5, Address);
            preparedStatement.setString(6, RoomNo);
            preparedStatement.setString(7, CheckInDate.toString());
            preparedStatement.setString(8, roomType);
            preparedStatement.setString(9, roomCapacity);
            preparedStatement.setString(10, roomPrice);

            try {
                preparedStatement.execute();
                String sql1 = "UPDATE ROOMINFO SET STATUS = 'Booked' WHERE ROOM_NO = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                preparedStatement1.setString(1, RoomNo);
                preparedStatement1.execute();
                CommonTask.showJFXAlert(rootPane, userCheckInPane, "information", "Successful!", "Check In Successful!", JFXDialog.DialogTransition.CENTER);
            } catch (SQLException e) {
                CommonTask.showJFXAlert(rootPane, userCheckInPane, "information", "Error!", "SQL Exception Happened!", JFXDialog.DialogTransition.CENTER);
            } finally {
                DBConnection.closeConnections();
            }
        }
        updateChoiceBox();
        clearTextFields();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateChoiceBox();
        setAndShowCustomerInfo();
        userRoomChoicebox.setOnAction(this::setRoomInfo);
    }

    // Метод для встановлення інформації про номер кімнати при виборі з ComboBox
    public void setRoomInfo(Event event) {
        String roomNo = userRoomChoicebox.getValue()+"";
        Connection connection = DBConnection.getConnections();
        try {
            if(!connection.isClosed()){
                String sql = "SELECT * FROM ROOMINFO WHERE ROOM_NO = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, roomNo);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    String roomCapacity = resultSet.getString("CAPACITY");
                    String roomType = resultSet.getString("TYPE");
                    String roomPriceDay = resultSet.getString("PRICE_DAY");

                    roomCapacityField.setText(roomCapacity);
                    roomPriceField.setText(roomPriceDay);
                    roomTypeField.setText(roomType);
                } else {
                    CommonTask.showAlert(Alert.AlertType.ERROR, "ERROR", "Can't get/set Info!");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeConnections();
        }
    }

    // Метод для оновлення вмісту ComboBox з доступними номерами кімнат
    public void updateChoiceBox(){
        List<String> rooms = new ArrayList<String>();
        Connection connection = DBConnection.getConnections();
        try{
            if(!connection.isClosed()) {
                String sql = "SELECT * FROM ROOMINFO WHERE STATUS = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "Available");
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    rooms.add(resultSet.getString("ROOM_NO"));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            DBConnection.closeConnections();
        }
        userRoomChoicebox.getItems().setAll(rooms);
        userRoomChoicebox.setValue(null);
    }

    // Метод для встановлення та відображення інформації про користувача
    public void setAndShowCustomerInfo(){
        Connection connection = DBConnection.getConnections();
        try {
            if(!connection.isClosed()){
                String sql = "SELECT * FROM CUSTOMERINFO WHERE NID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, currentCustomerNID);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    String customerName = resultSet.getString("NAME");
                    String customerNID = resultSet.getString("NID");
                    String customerEmail = resultSet.getString("EMAIL");
                    String customerPhone = resultSet.getString("PHONE");
                    String customerAddress = resultSet.getString("ADDRESS");

                    UserNameField.setText(customerName);
                    UserNIDField.setText(customerNID);
                    UserEmailField.setText(customerEmail);
                    UserPhoneField.setText(customerPhone);
                    UserAddressField.setText(customerAddress);
                } else {
                    CommonTask.showAlert(Alert.AlertType.ERROR, "ERROR", "SQL connection Error!");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeConnections();
        }
    }

    // Метод для очищення текстових полів форми
    private void clearTextFields() {
        roomTypeField.setText("");
        roomCapacityField.setText("");
        roomPriceField.setText("");
        UserCheckIndate.getEditor().clear();
    }
}
