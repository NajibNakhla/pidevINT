package Controles;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Entities.Wishlist;
import Services.WishlistServicesWishlist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageWishlists {

    @FXML
    private TableColumn<Wishlist, Double> monthlyBudgetTab;

    @FXML
    private TableColumn<Wishlist, String> nameTab;
    @FXML
    private TableColumn<Wishlist, LocalDate> creationDateTab;

    @FXML
    private TableView<Wishlist> wishlistTab;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TextField nameWishlist;

    @FXML
    private TextField monthlyBudget;

    @FXML
    private ListView<Wishlist> wishlistsListView;

    private AjouterWishlistItem firstWindowController;

    // Method to set the reference to the controller of the first window
    public void setFirstWindowController(AjouterWishlistItem firstWindowController) {
        this.firstWindowController = firstWindowController;
    }

    @FXML
    void addWishlistButton(ActionEvent event) {
        if (nameWishlist.getText().isEmpty() || monthlyBudget.getText().isEmpty()) {
            // Alert user to fill in all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all fields.");
            alert.show();
            return;
        }
        double budget;
        try {
            budget = Double.parseDouble(monthlyBudget.getText());
        } catch (NumberFormatException e) {
            // Alert user that the monthly budget must be a valid number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid number for the monthly budget.");
            alert.show();
            return;
        }
        // Check if the wishlist name already exists
        String wishlistName = nameWishlist.getText();
        WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
        if (wishlistServices.isNameUnique(wishlistName)) {
            // Add wishlist to the data source
            Wishlist wishlist = new Wishlist(wishlistName, budget);
            wishlistServices.addEntity(wishlist);
            // Refresh the table to reflect the changes
            populateTable();
            // Clear the text fields after update
            clearTextFields();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Wishlist added!");
            successAlert.show();
            firstWindowController.refreshWishlistChoiceBox();

        } else {
            // Show alert indicating that the wishlist name already exists
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("This wishlist already exists!");
            errorAlert.show();
        }
    }

    @FXML
    void deleteWishlistButton(ActionEvent event) {
        // Get the selected wishlist from the table
        Wishlist selectedWishlist = wishlistTab.getSelectionModel().getSelectedItem();

        // Check if a wishlist is selected
        if (selectedWishlist != null) {
            // Ask for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Wishlist");
            alert.setContentText("Are you sure you want to delete the selected wishlist?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed deletion
                // Extract the ID of the selected wishlist
                int wishlistId = selectedWishlist.getIdWishlist();

                // Delete the wishlist from the database
                WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
                wishlistServices.deleteEntity(wishlistId);

                // Refresh the table to reflect the changes
                populateTable();
                clearTextFields();

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Wishlist deleted!");
                successAlert.show();
                firstWindowController.refreshWishlistChoiceBox();
            }
        } else {
            // Show error message if no wishlist is selected
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please select a wishlist to delete.");
            errorAlert.show();
        }
    }

    @FXML
    void editWishlistButton(ActionEvent event) {
        // Get the selected wishlist from the table
        Wishlist selectedWishlist = wishlistTab.getSelectionModel().getSelectedItem();

        // Check if a wishlist is selected
        if (selectedWishlist == null) {
            // If no wishlist is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a wishlist to update.");
            alert.show();
            return;
        }

        // Show confirmation alert before updating
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("Are you sure you want to update this wishlist?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Retrieve the updated values from the input fields
            String updatedName = nameWishlist.getText();
            double updatedBudget = Double.parseDouble(monthlyBudget.getText());

            // Update the selected wishlist with the new values
            selectedWishlist.setNameWishlist(updatedName);
            selectedWishlist.setMonthlyBudget(updatedBudget);

            // Use WishlistServices to update the wishlist in the database
            WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
            wishlistServices.updateEntity(selectedWishlist, selectedWishlist.getIdWishlist());
            // Refresh the table view with updated data
            populateTable();
            clearTextFields();

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Wishlist updated!");
            successAlert.show();
            firstWindowController.refreshWishlistChoiceBox();
        }
    }

    private void clearTextFields() {
        // Clear the text fields
        nameWishlist.clear();
        monthlyBudget.clear();
    }

    public void initialize() {
        populateTable();

        // Set a listener to update the text fields when a wishlist is selected in the table
        wishlistTab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected wishlist from the table
                Wishlist selectedWishlist = newValue;

                // Update the text fields with the selected wishlist's information
                nameWishlist.setText(selectedWishlist.getNameWishlist());
                monthlyBudget.setText(String.valueOf(selectedWishlist.getMonthlyBudget()));
            }
        });
    }
    private void populateTable() {
        WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
        List<Wishlist> wishlists = wishlistServices.getAllData();
        ObservableList<Wishlist> data = FXCollections.observableArrayList(wishlists);
        wishlistTab.setItems(data);
        nameTab.setCellValueFactory(new PropertyValueFactory<>("nameWishlist"));
        monthlyBudgetTab.setCellValueFactory(new PropertyValueFactory<>("monthlyBudget"));
        creationDateTab.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
    }


}
