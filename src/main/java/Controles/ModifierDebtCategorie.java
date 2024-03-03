package Controles;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Entities.DebtCategory;
import Services.DebtCategoryServicesDebts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierDebtCategorie {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField NameDebtTextField;

    @FXML
    void modifierCategorieDebt(ActionEvent event){
        // Get the values from the UI components

        String OldnameDebt = NameDebtTextField.getText();
        // Validate that all fields are filled
        if (NameDebtTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez saisir un nom de dette.");
            alert.show();
            return;
        }

        // Validate the length of the debt name
        if (NameDebtTextField.getText().length() > 50) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le nom de la dette ne peut pas dépasser 50 caractères.");
            alert.show();
            return;
        }

        // Get the values from the UI components
        String nameDebt = NameDebtTextField.getText();

        // Create a new DebtCategory object with the retrieved values
        DebtCategory debtCategory = new DebtCategory(nameDebt);
        debtCategory.setOldNameDebt(OldnameDebt);
        // Update the DebtCategory object in the database
        DebtCategoryServicesDebts ds = new DebtCategoryServicesDebts();
        try {
            ds.addEntity(debtCategory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Catégorie de dette mise à jour avec succès!!");
            alert.show();
            // Close the ModifierDebtCategorie window
            Stage stage = (Stage) NameDebtTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }



    public void setDebtCategory(DebtCategory debtCategory) {
        NameDebtTextField.setText(String.valueOf(debtCategory.getNameDebt()));
    }

    @FXML
    void initialize() {
        // Set up input validation for NameDebtTextField
        NameDebtTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                NameDebtTextField.setText(oldValue);
            }
        });
    }

}
