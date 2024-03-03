package Controles;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Entities.Debt;
import Services.DebtServicesDebts;
import Tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.util.converter.*;

public class AjouterDebt {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker DateCreationDatePicker;

    @FXML
    private DatePicker DatePaiementDatePicker;

    @FXML
    private TextField MontantTextField;

    @FXML
    private TextField TauxInteretTextField;

    @FXML
    private ChoiceBox<String> TypeChoiceBox;

    @FXML
    void ajouterDebt(ActionEvent event) {
        // Validate that all fields are filled
        if (MontantTextField.getText().isEmpty() || TauxInteretTextField.getText().isEmpty() || DatePaiementDatePicker.getValue() == null || DateCreationDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }
        double amount = Double.parseDouble(MontantTextField.getText());
        Date paymentDate = Date.valueOf(DatePaiementDatePicker.getValue());
        float interestRate = Float.parseFloat(TauxInteretTextField.getText());
        String type = (String) TypeChoiceBox.getValue();// Assurez-vous que TypeChoiceBox.getValue() renvoie une chaîne valide
        Date creationDate = Date.valueOf(DateCreationDatePicker.getValue());

        Debt debt = new Debt(amount, paymentDate, interestRate, type, creationDate);
        DebtServicesDebts ds = new DebtServicesDebts();
        try {
            ds.addEntity(debt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Debt ajouter avec succes!!");
            alert.show();
            Stage stage = (Stage) MontantTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void initialize() {

        // Set up input validation for MontantTextField
        MontantTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        MontantTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                MontantTextField.setText(oldValue);
            }
        });
        // Set up input validation for TauxInteretTextField
        TauxInteretTextField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
        TauxInteretTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                TauxInteretTextField.setText(oldValue);
            }
        });
        // Set up input validation for DatePaiementDatePicker
        DatePaiementDatePicker.setConverter(new LocalDateStringConverter());
        DatePaiementDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                DatePaiementDatePicker.setValue(oldValue);
            }
        });

        // Set up input validation for DateCreationDatePicker
        DateCreationDatePicker.setConverter(new LocalDateStringConverter());
        DateCreationDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                DateCreationDatePicker.setValue(oldValue);
            }
        });

        // Fetch the values from the database
        List<String> types = new ArrayList<>();
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery("SELECT NameDebt FROM DebtCategory");
            while (rs.next()) {
                types.add(rs.getString("NameDebt"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Populate the TypeChoiceBox with the fetched values
        ObservableList<String> observableTypes = FXCollections.observableArrayList(types);
        TypeChoiceBox.setItems(observableTypes);
    }

}
