package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Entities.Account;
import enums.AccountType;
import Services.AccountService;


public class AddAccountDIalog {

    private boolean dialogClosedSuccessfully = false;
    @FXML
    private TextField balanceField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameAccField;

    private int idWallet;

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    @FXML
    private ComboBox<String> typeComboBox;
    private boolean addClicked = false;

    @FXML
    private void initialize() {

        typeComboBox.getItems().addAll("CASH","SAVINGS","CHECKING");  // Add your actual payee names
    }

    @FXML
    void addAccount(ActionEvent event) {
        if (isValidInput()) {
            AccountService accountService = new AccountService();

            if (accountService.isPayeeNameUnique(nameAccField.getText())) {

                Account account = new Account(nameAccField.getText(), AccountType.valueOf(typeComboBox.getValue()), Double.valueOf(balanceField.getText()), descriptionField.getText(), idWallet);
                accountService.addEntity(account);
                addClicked = true;
                showSuccessAlert();
                dialogClosedSuccessfully = true;
                closeDialog();
            }else{
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("This account already exists!");
                errorAlert.show();
            }
        }
    }

    @FXML
    void cancelAction(ActionEvent event) {
        closeDialog();
    }

    boolean isAddClicked() {
        return addClicked;
    }
    private boolean isValidInput() {
        String name = nameAccField.getText().trim();
        String type = typeComboBox.getValue();
        String balanceText = balanceField.getText().trim();

        if (name.isEmpty() || type == null || balanceText.isEmpty()) {
            showAlert("Please fill in all fields.");
            return false;
        }



        try {
            double balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                showAlert("Balance must be a non-negative number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid balance. Please enter a valid number.");
            return false;
        }

        return true;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameAccField.getScene().getWindow();
        stage.close();
    }
    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Account added successfully!");
        alert.showAndWait();
    }

    public boolean isDialogClosedSuccessfully() {
        return dialogClosedSuccessfully;
    }
}
