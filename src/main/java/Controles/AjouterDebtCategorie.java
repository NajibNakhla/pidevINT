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

public class AjouterDebtCategorie {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField NameDebtTextField;

    @FXML
    void ajouterCategorieDebt(ActionEvent event) {
        String nameDebt = NameDebtTextField.getText();

        // Vérifiez si le champ de texte est vide
        if (nameDebt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez saisir un nom de dette.");
            alert.show();
            return;
        }

        DebtCategory debt = new DebtCategory(nameDebt);
        DebtCategoryServicesDebts ds = new DebtCategoryServicesDebts();
        try {
            ds.addEntity(debt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Debt ajouter avec succes!!");
            alert.show();
            Stage stage = (Stage) NameDebtTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }


    @FXML
    void initialize() {
    }

}
