package sample.yadmin.AdminPages.EditCustomerEmployee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;
import sample._BackEnd.TableView.AdminCustomerTable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerInfoEdit implements Initializable {

    public Button UserConfirm;

    // Поля для редагування інформації про користувача
    @FXML
    private TextField UserNameEdit;

    @FXML
    private TextField UserNidEdit;

    @FXML
    private TextField UserEmailEdit;

    @FXML
    private TextField UserPhoneEdit;

    @FXML
    private TextField UserPassEdit;

    @FXML
    private TextArea UserAddressEdit;

    // Обробник події для підтвердження редагування
    @FXML
    void UserConfirmEdit(ActionEvent event) throws IOException, SQLException {
        Connection connection = DBConnection.getConnections();
        String customerName = UserNameEdit.getText();
        String customerNID = UserNidEdit.getText();
        String customerPassword = UserPassEdit.getText();
        String customerEmail = UserEmailEdit.getText();
        String customerPhone = UserPhoneEdit.getText();
        String customerAddress = UserAddressEdit.getText();

        // Перевірка на заповненість обов'язкових полів
        if (customerName.isEmpty() || customerNID.isEmpty() || customerPassword.isEmpty() || customerEmail.isEmpty() || customerAddress.isEmpty()) {
            CommonTask.showAlert(Alert.AlertType.WARNING, "Error", "Field can't be empty!");
        } else {
            // SQL-запит на оновлення інформації про користувача
            String sql = "UPDATE CUSTOMERINFO SET NAME = ?, PASSWORD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE NID = ?";
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(sql);
            preparedStatementUpdate.setString(1, customerName);
            preparedStatementUpdate.setString(2, customerPassword);
            preparedStatementUpdate.setString(3, customerEmail);
            preparedStatementUpdate.setString(4, customerPhone);
            preparedStatementUpdate.setString(5, customerAddress);
            preparedStatementUpdate.setString(6, customerNID);
            try {
                // Виконання оновлення та закриття вікна редагування
                preparedStatementUpdate.execute();
                Stage stage = (Stage) UserConfirm.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                CommonTask.showAlert(Alert.AlertType.ERROR, "Error", "Maybe Sql error!");
            } finally {
                DBConnection.closeConnections();
            }
        }
    }

    // Ініціалізація контролера
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Встановлення інформації про користувача у вікно редагування
    public void setCustomerInfo(AdminCustomerTable adminCustomerTable) {
        UserNameEdit.setText(adminCustomerTable.getName());
        UserNidEdit.setText(adminCustomerTable.getNID());
        UserNidEdit.setDisable(true);
        UserEmailEdit.setText(adminCustomerTable.getEmail());
        UserPhoneEdit.setText(adminCustomerTable.getPhone());
        UserPassEdit.setText(adminCustomerTable.getPass());
        UserAddressEdit.setText(adminCustomerTable.getAddress());
    }

    // Обробник події для закриття вікна редагування
    public void BackBtn(ActionEvent event) throws IOException {
        Stage stage = (Stage) UserConfirm.getScene().getWindow();
        stage.close();
    }
}
