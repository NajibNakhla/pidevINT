package Controles;

import Entities.CategoryItem;
import Entities.Payee;
import Services.CategoryItemServicesWishlist;
import Services.PayeeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Optional;

public class ManagePayees {
    @FXML
    public TextField PayeeName;
    @FXML
    public TableView payeeTab;
    @FXML
    public TableColumn nameTab;
    public AnchorPane payeesContainer;
    public Label titlePayees;

    @FXML
    public void addPayeeButton(ActionEvent actionEvent) {
        if (PayeeName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in the Name field.");
            alert.show();
            return;
        }
        String payeeNameValue = PayeeName.getText();
        PayeeService payeeService = new PayeeService();
        if (payeeService.isPayeeNameUnique(payeeNameValue)) {

            Payee payee = new Payee(payeeNameValue);
            payeeService.addEntity(payee);
            populateTable();
            clearTextFields();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Payee added!");
            successAlert.show();


        }  else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("This payee already exists!");
            errorAlert.show();
        }
    }
    @FXML
    public void editPayeeButton(ActionEvent actionEvent) {
        Payee selectedpayee = (Payee) payeeTab.getSelectionModel().getSelectedItem();
        if (selectedpayee == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a payee to update.");
            alert.show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("Are you sure you want to update this category?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String updatedName = PayeeName.getText();
            if (updatedName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a name for the payee.");
                alert.show();
                return;
            }
            selectedpayee.setNamePayee(updatedName);
            PayeeService payeeService = new PayeeService();
            payeeService.updateEntity(selectedpayee, selectedpayee.getidPayee());
            populateTable();
            clearTextFields();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Payee updated!");
            successAlert.show();
            // After successfully adding the category, call the method to update the choice box in the first window


        }

    }
    @FXML
    public void deletePayeeButton(ActionEvent actionEvent) {
        Payee selectedpayee = (Payee) payeeTab.getSelectionModel().getSelectedItem();

        if (selectedpayee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Payee");
            alert.setContentText("Are you sure you want to delete the selected payee?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int payeeId = selectedpayee.getidPayee();
                PayeeService payeeService = new PayeeService();
                payeeService.deleteEntity(payeeId);
                populateTable();
                clearTextFields();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Payee deleted!");
                successAlert.show();
                // After successfully adding the category, call the method to update the choice box in the first window

            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please select a Payee to delete.");
            errorAlert.show();
        }
    }
    @FXML
    void initialize() {
        populateTable();

        // Set a listener to update the text fields when a wishlist is selected in the table
        payeeTab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected wishlist from the table
                Payee selectedPayee = (Payee) newValue;

                // Update the text fields with the selected wishlist's information
                PayeeName.setText(selectedPayee.getNamePayee());
            }
        });

    }


    private void clearTextFields() {
        PayeeName.clear();
    }

    private void populateTable() {
        PayeeService payeeService = new PayeeService();
        List<Payee> payees = payeeService.getAllData();
        ObservableList<Payee> data = FXCollections.observableArrayList(payees);
        payeeTab.setItems(data);
        nameTab.setCellValueFactory(new PropertyValueFactory<>("namePayee"));
    }
}
