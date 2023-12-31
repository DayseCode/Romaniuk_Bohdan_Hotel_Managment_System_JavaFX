package sample.yadmin.AdminPages;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminAddEmployee {

    // Текстові поля для введення інформації про нового працівника
    public TextField EmployeeNameField;
    public TextField EmployeeEmailField;
    public TextField EmployeePhoneField;
    public TextField EmployeeAddressField;
    public TextField EmployeeNIDField;
    public TextField EmployeePassField;

    // Підключення до бази даних
    Connection connection = DBConnection.getConnections();

    // Обробник події для створення нового працівника
    public void CreateEmployee(ActionEvent actionEvent) throws SQLException {
        String employeeName = EmployeeNameField.getText();
        String employeeNID = EmployeeNIDField.getText();
        String employeePassword = EmployeePassField.getText();
        String employeeEmail = EmployeeEmailField.getText();
        String employeePhone = EmployeePhoneField.getText();
        String employeeAddress = EmployeeAddressField.getText();

        // Перевірка на заповненість обов'язкових полів
        if (employeeName.isEmpty() || employeeNID.isEmpty() || employeePassword.isEmpty() || employeeEmail.isEmpty() || employeeAddress.isEmpty() || employeePhone.isEmpty()) {
            CommonTask.showAlert(Alert.AlertType.WARNING, "Error", "Field can't be empty!");
        } else {
            // SQL-запит на вставку нового працівника
            String sql = "INSERT INTO EMPLOYEEINFO(NAME, NID, PASSWORD, EMAIL, PHONE, ADDRESS) VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employeeName);
            preparedStatement.setString(2, employeeNID);
            preparedStatement.setString(3, employeePassword);
            preparedStatement.setString(4, employeeEmail);
            preparedStatement.setString(5, employeePhone);
            preparedStatement.setString(6, employeeAddress);
            try {
                // Виконання запиту та виведення повідомлення про успішне створення
                preparedStatement.execute();
                CommonTask.showAlert(Alert.AlertType.INFORMATION, "Successful", "Employee created!");
            } catch (SQLException e) {
                // Виведення повідомлення про помилку, якщо обліковий запис з таким NID вже існує
                CommonTask.showAlert(Alert.AlertType.ERROR, "Error", "Account already exists with this NID!");
            } finally {
                // Закриття підключення до бази даних
                DBConnection.closeConnections();
            }
        }
    }
}
