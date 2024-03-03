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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class ModifierDebt {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker DateCreationDatePicker;

    @FXML
    private DatePicker DatePaiementDatePicker;

    @FXML
    private TextField IdTextField;

    @FXML
    private TextField MontantRestantTextField;

    @FXML
    private TextField MontantTextField;

    @FXML
    private TextField TauxInteretTextField;

    @FXML
    private ChoiceBox<String> TypeChoiceBox;

    @FXML
    void modifierDebt(ActionEvent event) {
        // Validate that all fields are filled
        if (MontantTextField.getText().isEmpty() ||MontantRestantTextField.getText().isEmpty() || TauxInteretTextField.getText().isEmpty() || DatePaiementDatePicker.getValue() == null || DateCreationDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }
        // Get the values from the UI components
        int id = Integer.parseInt(IdTextField.getText());
        double amount = Double.parseDouble(MontantTextField.getText());
        Date paymentDate = Date.valueOf(DatePaiementDatePicker.getValue());
        double amountToPay = Double.parseDouble(MontantRestantTextField.getText());
        float interestRate = Float.parseFloat(TauxInteretTextField.getText());
        String type = (String) TypeChoiceBox.getValue(); // Assurez-vous que TypeChoiceBox.getValue() renvoie une chaîne valide
        Date creationDate = Date.valueOf(DateCreationDatePicker.getValue());

        // Create a new Debt object with the retrieved values
        Debt debt = new Debt(id,amount, paymentDate, amountToPay, interestRate, type, creationDate);
        // Update the Debt object in the database
        DebtServicesDebts ds = new DebtServicesDebts();
        try {
            ds.updateEntity(debt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Debt mise à jour avec succès!!");
            alert.show();
            // Close the ModifierDebt window
            Stage stage = (Stage) MontantTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void setDebt(Debt debt) {
        IdTextField.setText(String.valueOf(debt.getIdDebt()));
        MontantTextField.setText(String.valueOf(debt.getAmount()));
        DatePaiementDatePicker.setValue(debt.getPaymentDate().toLocalDate());
        MontantRestantTextField.setText(String.valueOf(debt.getAmountToPay()));
        TauxInteretTextField.setText(String.valueOf(debt.getInterestRate()));
        TypeChoiceBox.setValue(debt.getType());
        DateCreationDatePicker.setValue(debt.getCreationDate().toLocalDate());
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
        // Set up input validation for MontantRestantTextField
        MontantRestantTextField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        MontantRestantTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                MontantRestantTextField.setText(oldValue);
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
