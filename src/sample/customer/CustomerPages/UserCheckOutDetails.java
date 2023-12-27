package sample.customer.CustomerPages;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.scenario.effect.ImageData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.Main;
import sample._BackEnd.CommonTask;
import sample._BackEnd.DBConnection;
import sample._BackEnd.TableView.CustomerCheckOutTable;
import sample._BackEnd.TableView.CustomerRoomTable;
import sample._BackEnd.TableView.ManagerRoomTable;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static sample.customer.Login.UserLogin.currentCustomerNID;

public class UserCheckOutDetails extends DBConnection implements Initializable {

    @FXML
    private TableView<CustomerCheckOutTable> UserCheckOutDetailsTable;
    public TableColumn<CustomerCheckOutTable, String> nidCol;
    public TableColumn<CustomerCheckOutTable, String> roomNoCol;
    public TableColumn<CustomerCheckOutTable, String> checkedInCol;
    public TableColumn<CustomerCheckOutTable, String> checkedOutCol;
    public TableColumn<CustomerCheckOutTable, String> priceDayCol;
    public TableColumn<CustomerCheckOutTable, String> totalPriceCol;
    public TableColumn slipCol;

    private ObservableList<CustomerCheckOutTable> TABLEROW = FXCollections.observableArrayList();
    @FXML
    private TextField UserCheckOutDetailsSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Встановлення фабрик і назв для кожного стовпця таблиці
        nidCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("nid"));
        roomNoCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("roomNo"));
        checkedInCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("checkedIndate"));
        checkedOutCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("checkedOutDate"));
        priceDayCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("priceDay"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<CustomerCheckOutTable, String>("totalPrice"));

        // Виклик методу для відображення даних у таблиці
        showcheckInOutDetails();

        // Виклик методу для додавання кнопки завантаження витягу до кожного рядка
        slipDownloadBtn();
    }


    public void showcheckInOutDetails(){
        // Очищення списку даних
        TABLEROW.clear();
        Connection connection = getConnections();
        try {
            if(!connection.isClosed()){
                // Отримання даних з бази даних і заповнення ObservableList
                String sql = "SELECT * FROM CHECKINOUTINFO WHERE NID = ? ORDER BY SI_NO DESC";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, currentCustomerNID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    String NID = resultSet.getString("NID");
                    String RoomNo = resultSet.getString("ROOMNO");
                    String CheckedInDate = resultSet.getString("CHECKEDIN");
                    String CheckedOutDate = resultSet.getString("CHECKEDOUT");
                    String PriceDay = resultSet.getString("PRICEDAY");
                    String TotalPrice = resultSet.getString("TOTALPRICE");

                    CustomerCheckOutTable roomTablee = new CustomerCheckOutTable(NID, RoomNo, CheckedInDate, CheckedOutDate, PriceDay, TotalPrice);

                    TABLEROW.add(roomTablee);
                }
                // Встановлення елементів у таблицю
                UserCheckOutDetailsTable.getItems().setAll(TABLEROW);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnections();
        }

        // Перетворення ObservableList в FilteredList (спочатку відображаються всі дані).
        FilteredList<CustomerCheckOutTable> filteredData = new FilteredList<>(TABLEROW, b -> true);
        // Додавання слухача для текстового поля пошуку
        UserCheckOutDetailsSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Налаштування предиката для фільтрації даних
            filteredData.setPredicate(search -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                return search.getCheckedIndate().toLowerCase().indexOf(searchKeyword) != -1 ||
                        search.getRoomNo().indexOf(searchKeyword) != -1 ||
                        search.getPriceDay().toLowerCase().indexOf(searchKeyword) != -1;
            });
        });
        // Перетворення FilteredList в SortedList.
        SortedList<CustomerCheckOutTable> sortedData = new SortedList<>(filteredData);
        // Прив'язка компаратора SortedList до компаратора таблиці
        sortedData.comparatorProperty().bind(UserCheckOutDetailsTable.comparatorProperty());
        // Додавання відсортованих і відфільтрованих даних до таблиці
        UserCheckOutDetailsTable.setItems(sortedData);
    }

    // Метод для додавання кнопки завантаження витягу до кожного рядка таблиці
    private void slipDownloadBtn() {
        Callback<TableColumn<CustomerCheckOutTable, String>, TableCell<CustomerCheckOutTable, String>> cellCallback =
                new Callback<TableColumn<CustomerCheckOutTable, String>, TableCell<CustomerCheckOutTable, String>>() {
                    @Override
                    public TableCell<CustomerCheckOutTable, String> call(TableColumn<CustomerCheckOutTable, String> param) {

                        TableCell<CustomerCheckOutTable, String> cell = new TableCell<CustomerCheckOutTable, String>() {

                            FontAwesomeIconView downloadIcon = new FontAwesomeIconView(FontAwesomeIcon.DOWNLOAD);

                            final HBox hBox = new HBox(downloadIcon);

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    downloadIcon.setStyle(
                                            "-fx-cursor: hand ;"
                                                    + "-glyph-size:20px;"
                                                    + "-fx-fill:#ffffff;"
                                    );

                                    downloadIcon.setOnMouseEntered((MouseEvent event) ->{
                                        downloadIcon.setStyle(
                                                "-fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:khaki;"
                                        );
                                    });

                                    downloadIcon.setOnMouseExited((MouseEvent event2) ->{
                                        downloadIcon.setStyle(
                                                "-fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        + "-fx-fill:white;"
                                        );
                                    });

                                    downloadIcon.setOnMouseClicked((MouseEvent event2) ->{
                                        downloadIcon.setStyle(
                                                "-fx-cursor: hand ;"
                                                        +
                                                        "-glyph-size:20px;"
                                                        +"-fx-fill:lightgreen;"
                                        );

                                        // Виклик методу для створення та завантаження PDF-файлу
                                        CustomerCheckOutTable customerCheckOutTable = getTableView().getItems().get(getIndex());
                                        try {
                                            generatePdfSlip(customerCheckOutTable);
                                        } catch (DocumentException | IOException e) {
                                            e.printStackTrace();
                                        }

                                    });

                                    hBox.setStyle("-fx-alignment:center");
                                    setGraphic(hBox);
                                }
                            }
                        };

                        return cell;
                    }
                };
        // Встановлення фабрики ячейки для стовпця з кнопкою
        slipCol.setCellFactory(cellCallback);
    }

    // Метод для генерації та завантаження PDF-витягу
    private void generatePdfSlip(CustomerCheckOutTable customerCheckOutTable) throws IOException, DocumentException {

        File currentDirFile = new File("");
        String pathFinder = currentDirFile.getAbsolutePath();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pathFinder+"/tempSlip.pdf"));
        document.open();

        // Формування текстового вмісту PDF
        String title = "Hotel Management System Slip\n\n";
        String nid = "Customer NID: "+customerCheckOutTable.getNid()+"\n";
        String roomNo = "Room No: "+customerCheckOutTable.getRoomNo()+"\n";
        String checkedIn = "Checked In Date: "+customerCheckOutTable.getCheckedIndate()+"\n";
        String checkedOut = "Checked Out Date: "+customerCheckOutTable.getCheckedOutDate()+"\n";
        String priceDay = "Price per day: "+customerCheckOutTable.getPriceDay()+" UAH\n";
        String totalBill = "Total Bill: "+customerCheckOutTable.getTotalPrice()+" UAH\n";
        String totalParagraph = title+nid+roomNo+checkedIn+checkedOut+priceDay+totalBill;

        Paragraph para = new Paragraph(totalParagraph);

        document.add(para);
        document.close();

        // Відкриття PDF-файлу за допомогою стандартної програми перегляду PDF
        File file = new File(pathFinder+"/tempSlip.pdf");
        if(file.exists()) {
            Desktop.getDesktop().open(file);
        } else {
            System.out.println("File Doesn't Exists");
        }
    }
}
