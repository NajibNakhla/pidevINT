package Controles;

import java.net.URL;
import java.util.ResourceBundle;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.Workbook;
import jxl.write.*;
import Entities.Debt;
import Entities.DebtCategory;
import Services.DebtCategoryServicesDebts;
import Services.DebtServicesDebts;
import Tools.MyConnection;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AfficherDebt {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Debt, String> ColDateCreation;

    @FXML
    private TableColumn<Debt, String> ColDatePaiement;

    @FXML
    private TableColumn<Debt, Integer> ColID;

    @FXML
    private TableColumn<Debt, String> ColMontant;

    @FXML
    private TableColumn<Debt, String> ColMontantRestant;

    @FXML
    private TableColumn<Debt, String> ColTauxInteret;

    @FXML
    private TableColumn<Debt, String> ColType;

    @FXML
    private TableView<Debt> TableDebt;
    @FXML
    private ListView<DebtCategory> list;

    @FXML
    private Button SMS;

    @FXML
    private Button Excel;

    @FXML
    private TextField PhoneNumberText;

    // Twilio credentials
    private static final String ACCOUNT_SID = "ACdd868496e75931f00c532e7990efe34f";
    private static final String AUTH_TOKEN = "9b821bc23c73caea2d24178dd08b5275";
    private static final String VERIFY_SERVICE_SID = "VAd21743aa4d043f1fb08a37791fecad0e";
    private static final String YOUR_TWILIO_PHONE_NUMBER = "+1 716 404 8035";

    @FXML
    private Button accounts;

    @FXML
    private Button budget;

    @FXML
    private Button debts;
    @FXML
    private Button overview;
    @FXML
    private Button profile;

    @FXML
    private Button reports;

    @FXML
    private Button settings;

    @FXML
    private Button signout;

    @FXML
    private Button todo;

    @FXML
    private Button wishlist;

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) overview.getScene().getWindow(); // Access the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    @FXML
    void generateExcel(ActionEvent event) {
        try {
            // Ask the user for confirmation before proceeding
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to generate the Excel file?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Ask the user for the file save location and name
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Excel File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls"));
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    // Create a new Excel workbook
                    WritableWorkbook workbook = Workbook.createWorkbook(file);

                    // Create a new sheet in the workbook
                    WritableSheet sheet = workbook.createSheet("Debts", 0);

                    // Define column names as constants
                    final String[] COLUMN_NAMES = {"ID", "Amount", "Payment Date", "Amount To Pay", "Interest Rate", "Type", "Creation Date"};

                    // Add column headers to the sheet with modern 3D formatting
                    WritableFont headerFont = new WritableFont(WritableFont.createFont("Arial"), 12, WritableFont.BOLD);
                    WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
                    headerFormat.setBackground(Colour.LIGHT_BLUE);
                    headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_25_PERCENT);
                    headerFormat.setAlignment(Alignment.CENTRE);
                    headerFormat.setShrinkToFit(false); // Set shrink to fit to false
                    for (int i = 0; i < COLUMN_NAMES.length; i++) {
                        sheet.addCell(new jxl.write.Label(i, 0, COLUMN_NAMES[i], headerFormat));
                    }

                    // Get the data from the TableView
                    ObservableList<Debt> debts = TableDebt.getItems();

                    // Add data to the sheet with modern 3D formatting
                    WritableFont dataFont = new WritableFont(WritableFont.createFont("Arial"), 11);
                    WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
                    dataFormat.setBackground(Colour.WHITE);
                    dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_25_PERCENT);
                    dataFormat.setAlignment(Alignment.CENTRE);
                    dataFormat.setShrinkToFit(true);

                    // Calculate the total number of rows to be added
                    int totalRows = debts.size() + 1; // Add 1 for the header row

                    // Update the progress indicator as the data is added
                    for (int i = 0; i < totalRows; i++) {
                        // Check if i is less than the size of the debts list
                        if (i < debts.size()) {
                            Debt debt = debts.get(i);
                            sheet.addCell(new jxl.write.Label(0, i + 1, String.valueOf(debt.getIdDebt()), dataFormat));
                            sheet.addCell(new jxl.write.Label(1, i + 1, String.valueOf(debt.getAmount()), dataFormat));
                            sheet.addCell(new jxl.write.Label(2, i + 1, debt.getPaymentDate().toString(), dataFormat));
                            sheet.addCell(new jxl.write.Label(3, i + 1, String.valueOf(debt.getAmountToPay()), dataFormat));
                            sheet.addCell(new jxl.write.Label(4, i + 1, String.valueOf(debt.getInterestRate()), dataFormat));
                            sheet.addCell(new jxl.write.Label(5, i + 1, debt.getType(), dataFormat));
                            sheet.addCell(new jxl.write.Label(6, i + 1, debt.getCreationDate().toString(), dataFormat));
                        }
                    }

                    // Write the workbook to a file
                    workbook.write();
                    workbook.close();

                    // Show a success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Excel Generated");
                    alert.setHeaderText(null);
                    alert.setContentText("Excel file has been generated successfully.");
                    alert.showAndWait();
                }
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while generating the Excel file.");
            alert.showAndWait();
        }
    }

    @FXML
    void sendSMS(ActionEvent event) {
        try {
            // Initialize Twilio (should be done once in your application)
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Get the debts from the TableView
            ObservableList<Debt> debts = TableDebt.getItems();

            // Create a list of CompletableFutures for sending messages
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // Check each debt's payment date
            for (Debt debt : debts) {
                LocalDate paymentDate = debt.getPaymentDate().toLocalDate();

                // Calculate the remaining days
                long daysRemaining = ChronoUnit.DAYS.between(currentDate, paymentDate);

                // Create message
                String phoneNumber = "+216" + PhoneNumberText.getText(); // Assuming the phone number is in E.164 format
                String message;
                if (daysRemaining < 0) {
                    message = "Your debt with ID " + debt.getIdDebt() + " and amount " + debt.getAmount() + " is overdue!";
                } else if (daysRemaining <= 7) {
                    message = "Your debt with ID " + debt.getIdDebt() + " and amount " + debt.getAmount() + " is due in " + daysRemaining + " days!";
                } else {
                    continue; // Skip sending SMS for debts not due soon
                }

                // Send SMS using Twilio API asynchronously
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Message twilioMessage = Message.creator(
                                        new PhoneNumber(phoneNumber),
                                        new PhoneNumber(YOUR_TWILIO_PHONE_NUMBER),
                                        message)
                                .create();

                        // Display success message
                        System.out.println("Message sent successfully: " + twilioMessage.getSid());
                    } catch (ApiException e) {
                        // Handle TwilioRestException
                        System.out.println("Error sending message: " + e.getMessage());
                    }
                });

                // Add the future to the list
                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
        } catch (ApiException e) {
            // Handle TwilioRestException
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    @FXML
    void openAjouterCatDebt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterDebtCategorie.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            ShowDebts();
            ShowDebtCategories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openModifierCatDebt(ActionEvent event) {
        DebtCategory selectedDebtCategory = list.getSelectionModel().getSelectedItem();
        if (selectedDebtCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune catégorie de dette sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une catégorie de dette pour la modifier.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de modification");
        confirmAlert.setHeaderText("Voulez-vous vraiment modifier cette catégorie de dette?");
        confirmAlert.setContentText("Catégorie de dette: \nNom: " + selectedDebtCategory.getNameDebt());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierDebtCategorie.fxml"));
                Parent root = loader.load();

                ModifierDebtCategorie modifierDebtCategorieController = loader.getController();
                modifierDebtCategorieController.setDebtCategory(selectedDebtCategory);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
                DebtCategoryServicesDebts ds = new DebtCategoryServicesDebts();
                ds.deleteEntity(selectedDebtCategory);
                ShowDebts();
                ShowDebtCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void supprimerCatDebt(ActionEvent event) {
        // Récupérer la catégorie de dette sélectionnée
        DebtCategory selectedCategory = (DebtCategory) list.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune catégorie sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une catégorie de dette à supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Voulez-vous vraiment supprimer cette catégorie de dette?");
        confirmAlert.setContentText("Catégorie de dette: " + selectedCategory.toString());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer la catégorie de dette
            DebtCategoryServicesDebts dcs = new DebtCategoryServicesDebts();
            dcs.deleteEntity(selectedCategory);
            ShowDebts();
            ShowDebtCategories();
        }
    }

    @FXML
    void openAjouterDebt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterDebt.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            ShowDebts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openModifierDebt(ActionEvent event) {
        Debt selectedDebt = TableDebt.getSelectionModel().getSelectedItem();
        if (selectedDebt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune dette sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une dette pour la modifier.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de modification");
        confirmAlert.setHeaderText("Voulez-vous vraiment modifier cette dette?");
        confirmAlert.setContentText("Dette: " + selectedDebt.getIdDebt() + "\nMontant: " + selectedDebt.getAmount());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierDebt.fxml"));
                Parent root = loader.load();

                ModifierDebt modifierDebtController = loader.getController();
                modifierDebtController.setDebt(selectedDebt);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
                ShowDebts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void supprimerDebt(ActionEvent event) {
        Debt selectedDebt = TableDebt.getSelectionModel().getSelectedItem();
        if (selectedDebt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune dette sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une dette pour la supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Voulez-vous vraiment supprimer cette dette?");
        confirmAlert.setContentText("Dette: " + selectedDebt.getIdDebt() + "\nMontant: " + selectedDebt.getAmount());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DebtServicesDebts ds = new DebtServicesDebts();
            ds.deleteEntity(selectedDebt);
            ShowDebts();
        }
    }

    @FXML
    void initialize() {
        ShowDebts();
        ShowDebtCategories();
    }

    public ObservableList<Debt> getDebt(){
        ObservableList<Debt> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM Debt";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Debt debt = new Debt();
                debt.setIdDebt(rs.getInt("IdDebt"));
                debt.setAmount(rs.getDouble("Amount"));
                debt.setPaymentDate(rs.getDate("PaymentDate"));
                debt.setAmountToPay(rs.getDouble("AmountToPay"));
                debt.setInterestRate(rs.getFloat("InterestRate"));
                debt.setType(rs.getString("Type"));
                debt.setCreationDate(rs.getDate("CreationDate"));
                data.add(debt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public void ShowDebts(){
        ObservableList<Debt> list=getDebt();
        TableDebt.setItems(list);
        ColID.setCellValueFactory(new PropertyValueFactory<Debt,Integer>("IdDebt"));
        ColMontant.setCellValueFactory(new PropertyValueFactory<Debt,String>("Amount"));
        ColDatePaiement.setCellValueFactory(new PropertyValueFactory<Debt,String>("PaymentDate"));
        ColMontantRestant.setCellValueFactory(new PropertyValueFactory<Debt,String>("AmountToPay"));
        ColTauxInteret.setCellValueFactory(new PropertyValueFactory<Debt,String>("InterestRate"));
        ColType.setCellValueFactory(new PropertyValueFactory<Debt,String>("Type"));
        ColDateCreation.setCellValueFactory(new PropertyValueFactory<Debt,String>("CreationDate"));

    }

    public ObservableList<DebtCategory> getDebtCategories() {
        ObservableList<DebtCategory> data = FXCollections.observableArrayList();
        String requete = "SELECT * FROM DebtCategory";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                DebtCategory category = new DebtCategory();
                category.setNameDebt(rs.getString("NameDebt"));
                data.add(category);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public void ShowDebtCategories(){
        ObservableList<DebtCategory> categories = getDebtCategories();
        list.setItems(categories);
    }

    public void openSignOut(ActionEvent actionEvent) {
        switchScene("/views/Settings.fxml");
    }
    @FXML
    void openAccounts(ActionEvent event) {
        switchScene("/views/Dashboard_Account.fxml");
    }
    @FXML
    void openBudget(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }

    @FXML
    void openDebts(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }
    @FXML
    void openOverview(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }

    @FXML
    void openProfile(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }

    @FXML
    void openReports(ActionEvent event) {
        switchScene("/views/Reports.fxml");
    }

    @FXML
    void openSettings(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }
    @FXML
    void openTodo(ActionEvent event) {
        switchScene("/views/Settings.fxml");
    }

    @FXML
    void openWishlist(ActionEvent event) {
        switchScene("/views/WishlistItem.fxml");
    }
}