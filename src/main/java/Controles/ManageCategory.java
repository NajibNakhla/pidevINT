package Controles;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Entities.CategoryItem;
import Services.CategoryItemServicesWishlist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageCategory {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<CategoryItem, String> nameTab;

    @FXML
    private TableColumn<?, ?> usedTab;

    @FXML
    private TableView<CategoryItem> categoryTab;
    @FXML
    private TextField categoryName;

    private AjouterWishlistItem firstWindowController;

    // Method to set the reference to the controller of the first window
    public void setFirstWindowController(AjouterWishlistItem firstWindowController) {
        this.firstWindowController = firstWindowController;
    }

    @FXML
    void addCategoryButton(ActionEvent event) {
        if (categoryName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in the Name field.");
            alert.show();
            return;
        }
        String categoryNameValue = categoryName.getText();
        CategoryItemServicesWishlist categoryItemServices = new CategoryItemServicesWishlist();
        if (categoryItemServices.isCategoryNameUnique(categoryNameValue)) {
            CategoryItem categoryItem = new CategoryItem(categoryNameValue);
            categoryItemServices.addEntity(categoryItem);
            populateTable();
            clearTextFields();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Category added!");
            successAlert.show();
            // After successfully adding the category, call the method to update the choice box in the first window
            firstWindowController.updateCategoryChoiceBox();

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("This category already exists!");
            errorAlert.show();
        }
    }


    @FXML
    void deleteCategoryButton(ActionEvent event) {
        CategoryItem selectedCategory = categoryTab.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Category");
            alert.setContentText("Are you sure you want to delete the selected category?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int categoryId = selectedCategory.getIdItemCategory();
                CategoryItemServicesWishlist categoryItemServices = new CategoryItemServicesWishlist();
                categoryItemServices.deleteEntity(categoryId);
                populateTable();
                clearTextFields();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Category deleted!");
                successAlert.show();
                // After successfully adding the category, call the method to update the choice box in the first window
                firstWindowController.updateCategoryChoiceBox();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please select a Category to delete.");
            errorAlert.show();
        }
    }


    @FXML
    void ediCategoryButton(ActionEvent event) {
        CategoryItem selectedCategory = categoryTab.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a category to update.");
            alert.show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("Are you sure you want to update this category?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String updatedName = categoryName.getText();
            if (updatedName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a name for the category.");
                alert.show();
                return;
            }
            selectedCategory.setNameItemCategory(updatedName);
            CategoryItemServicesWishlist categoryItemServices = new CategoryItemServicesWishlist();
            categoryItemServices.updateEntity(selectedCategory, selectedCategory.getIdItemCategory());
            populateTable();
            clearTextFields();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("Category updated!");
            successAlert.show();
            // After successfully adding the category, call the method to update the choice box in the first window
            firstWindowController.updateCategoryChoiceBox();

        }
    }


    @FXML
    void initialize() {
        populateTable();

        // Set a listener to update the text fields when a wishlist is selected in the table
        categoryTab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected wishlist from the table
                CategoryItem selectedCategory = newValue;

                // Update the text fields with the selected wishlist's information
                categoryName.setText(selectedCategory.getNameItemCategory());
            }
        });

    }

    private void clearTextFields() {
        categoryName.clear();
    }

    private void populateTable() {
        CategoryItemServicesWishlist categoryItemServices = new CategoryItemServicesWishlist();
        List<CategoryItem> categories = categoryItemServices.getAllData();
        ObservableList<CategoryItem> data = FXCollections.observableArrayList(categories);
        categoryTab.setItems(data);
        nameTab.setCellValueFactory(new PropertyValueFactory<>("nameItemCategory"));
    }



}
