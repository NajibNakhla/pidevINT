package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Services.AccountService;

public class UpdateAccountDIalog {
      private int idAccount ;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField nameAccField;


    @FXML
    void cancelAction(ActionEvent event) {
        closeDialog();
    }



    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @FXML
    void updateAccount(ActionEvent event) {
        System.out.println(this.idAccount);
        if (isValidInput()) {
            // Get the updated values from the input fields
            String updatedName = nameAccField.getText().trim();
            String updatedDescription = descriptionField.getText().trim();
            double updatedBalance = Double.parseDouble(balanceField.getText().trim());

            AccountService as = new AccountService();
            as.updateAccount(this.idAccount,updatedName,updatedBalance,updatedDescription);

            showSuccessAlert();

            // Close the dialog
            closeDialog();
        }

    }

    private boolean isValidInput() {
        String name = nameAccField.getText().trim();
        String description = descriptionField.getText().trim();
        String balanceText = balanceField.getText().trim();

        if (name.isEmpty() || balanceText.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return false;
        }

        try {
            // Validate the balance field
            double balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                showAlert("Balance must be a non-negative number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid balance. Please enter a valid number.");
            return false;
        }

        // Additional validation if needed

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

}
