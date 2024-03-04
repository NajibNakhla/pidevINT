package Controles;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Entities.Transaction;
import enums.TransactionType;
import Services.AccountService;
import Services.PayeeService;
import Services.TransactionService;

import java.time.LocalDate;
import java.util.List;

public class AddTransferTransactionDialogController {


    public TextArea descriptionField;
    public TextField amountField;
    public AnchorPane incomeDialog;
    private int accountId;
    @FXML
    private ComboBox<String> toAccountComboBox;
    @FXML
    private ComboBox<String> payeeComboBox;



    private Stage dialogStage;  // The stage of the dialog

    private boolean okClicked = false;  // Indicates whether the OK button was clicked

    @FXML
    private void initialize() {
        setupComboBoxItems();

    }
    private void setupComboBoxItems() {
        PayeeService payeeService = new PayeeService();
        AccountService accountService = new AccountService();

        List<String> payeeNames = payeeService.getPayeeNames();
        List<String> accountNames = accountService.getAccountsNames();

        // Remove the account with the current accountId
        accountNames.remove(accountService.getAccountNameById(this.accountId));

        payeeComboBox.getItems().setAll(payeeNames);
        toAccountComboBox.getItems().setAll(accountNames);
    }

    // This method can be called whenever you need to update the ComboBox items
    public void updateComboBoxItems() {
        setupComboBoxItems();
    }

    @FXML
    private void handleOK() {
        if (isValidInput()) {
            // Set the flag to indicate that the OK button was clicked
            okClicked = true;

            // Close the dialog stage
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog stage without saving data
        dialogStage.close();
    }

    private boolean isValidInput() {

        // Retrieve input values from the user interface (e.g., text fields, combo boxes)
        String description = descriptionField.getText();
        String amountText = amountField.getText();

        // Validate if the description is not empty
        if (description.isEmpty()) {
            showAlert("Please enter a description.");
            return false;
        }

        // Validate if the amount is a valid number
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Please enter a valid positive amount.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric amount.");
            return false;
        }
        // Validate if payeeComboBox has a selected item
        if (payeeComboBox.getSelectionModel().isEmpty()) {
            showAlert("Please select a payee.");
            return false;
        }

        // Validate if toAccountComboBox has a selected item
        if (toAccountComboBox.getSelectionModel().isEmpty()) {
            showAlert("Please select a destination account.");
            return false;
        }

        return true;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }



    public String getPayee() {
        return payeeComboBox.getValue();
    }
    public String getAccount() {
        return toAccountComboBox.getValue();
    }


    public String getDescription() {
        return descriptionField.getText();
    }

    public double getAmount() {
        // Add validation logic to ensure it's a valid double before parsing
        return Double.parseDouble(amountField.getText());
    }

    @FXML
    private void addTransferTransaction(ActionEvent event) {

        if (isValidInput()) {

            TransactionService transactionService = new TransactionService();
            PayeeService payeeService = new PayeeService();
            AccountService accountService = new AccountService();
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String selectedPayee = payeeComboBox.getValue();
            int idPayee = payeeService.getPayeeIdByName(selectedPayee);

            String selectedAccount = toAccountComboBox.getValue();
            int idToAccount = accountService.getAccountIdByName(selectedAccount);

            System.out.println(this.accountId);
            Transaction transaction = new Transaction(LocalDate.now(), TransactionType.TRANSFER, description, amount, this.accountId,idToAccount , 1, idPayee);
            transactionService.addTransactionService(transaction);

            //   double balance = accountService.getCurrentBalance(this.accountId);



            closeDialog();



        }
    }


    private void closeDialog() {
        // Get the current stage
        Stage stage = (Stage) descriptionField.getScene().getWindow();

        // Close the stage
        stage.close();
    }


    public void initializeData(int idAccount) {
        //  this.selectedAccountId = idAccount;
        // System.out.println("Received idAccount: " + idAccount);
    }


    public void initdata(int selectedAccountId) {
        this.accountId = accountId;
        System.out.println("Received idAccount: " + accountId);
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void cancelAction(ActionEvent actionEvent) {
        closeDialog();

    }
}

