package Controles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import Entities.*;
import Services.WishlistItemServicesWishlist;
import Services.WishlistServicesWishlist;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AjouterWishlistItem {

    @FXML
    private ChoiceBox<String> category;

    @FXML
    private TextField monthlyBudget;

    @FXML
    private TextField nameItem;

    @FXML
    private TextField priceItem;

    @FXML
    private ChoiceBox<Priority> priority;

    @FXML
    private ChoiceBox<Status> statuts;

    @FXML
    private ChoiceBox<String> wishlistChoiceBox;

    @FXML
    private TableColumn<WishlistItem, String> nameTab;

    @FXML
    private TableColumn<WishlistItem, Double> priceTab;

    @FXML
    private TableColumn<WishlistItem, Priority> priorityTab;

    @FXML
    private TableColumn<WishlistItem, Double> progress;

    @FXML
    private TableColumn<WishlistItem, Status> statusTab;

    @FXML
    private TableColumn<WishlistItem, String> categoryTab;
    @FXML
    private TableColumn<WishlistItem, LocalDate> creationDateTab;

    @FXML
    private TableView<WishlistItem> tableWishlistDisplay;

    private WishlistItemServicesWishlist wishlistItemServices; // Declare instance variable

    private ObservableList<Wishlist> wishlists = FXCollections.observableArrayList(); // Initialize and reference to the data source


    @FXML
    void addWishlistItem(ActionEvent event) throws Exception {
        Status selectedStatus = statuts.getValue();
        Priority selectedPriority = priority.getValue();
        String selectedWishlistName = wishlistChoiceBox.getValue();
        String selectedCategory = category.getValue();
        int selectedCategoryId = wishlistItemServices.getCategoryID(selectedCategory);
        String selectedWishlist = wishlistChoiceBox.getValue();
        int selectedWishlistId = wishlistItemServices.getWishlistID(selectedWishlist);

        // Check if all necessary fields are filled
        if (nameItem.getText().isEmpty() || priceItem.getText().isEmpty() || selectedPriority == null || category.getValue() == null || category == null || wishlistChoiceBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all fields.");
            alert.show();
            return;
        }

        // Check if the price is a valid double
        double price;
        try {
            price = Double.parseDouble(priceItem.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid number for the price.");
            alert.show();
            return;
        }



        // Check if the item is already present in the database
        WishlistItem newItem = new WishlistItem(nameItem.getText(), price, selectedPriority, selectedStatus, selectedCategoryId, selectedWishlistId);
        if (wishlistItemServices.isItemUnique(newItem)) {

            // Create a new WishlistItem object with the selected values
            WishlistItem wishlistItem = new WishlistItem(nameItem.getText(),
                    price,
                    selectedPriority,
                    selectedStatus,
                    selectedCategoryId,
                    selectedWishlistId);

            wishlistItemServices.addEntity(wishlistItem);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wishlist Item added!");
            alert.show();

            // Clear input fields
            clearInputFields();
            // Update choice box value to keep selected wishlist visible
            wishlistChoiceBox.setValue(selectedWishlistName);

            // Update table with new wishlist items
            List<WishlistItem> wishlistItems = wishlistItemServices.getAllWishlistItems(selectedWishlistId);
            populateTable(wishlistItems);
        } else {
            // Item already exists in the database
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Item already exists in the wishlist.");
            alert.show();
        }
    }


    private void clearInputFields() {
        nameItem.clear();
        priceItem.clear();
        category.setValue(null);
        priority.setValue(null);
        statuts.setValue(null);
    }

    @FXML
    void deleteWishlistItem(ActionEvent event) throws Exception {
        // Get the selected item from the table
        WishlistItem selectedItem = tableWishlistDisplay.getSelectionModel().getSelectedItem();
        String selectedWishlist = wishlistChoiceBox.getValue();
        int selectedWishlistId = wishlistItemServices.getWishlistID(selectedWishlist);


        // Check if an item is selected
        if (selectedItem != null) {
            // Ask for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Wishlist Item");
            alert.setContentText("Are you sure you want to delete the selected Wishlist Item?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed deletion
                // Extract the ID of the selected item
                int itemId = selectedItem.getIdWishlistItem();

                // Delete the item from the database
                wishlistItemServices.deleteEntity(itemId);

                // Refresh the table to reflect the changes
                clearInputFields();
                // Update table with new wishlist items
                List<WishlistItem> wishlistItems = wishlistItemServices.getAllWishlistItems(selectedWishlistId);
                populateTable(wishlistItems);

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Wishlist Item deleted!");
                successAlert.show();
                wishlistChoiceBox.setValue(selectedWishlist);

            }
        } else {
            // Show error message if no item is selected
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please select an item to delete.");
            errorAlert.show();
        }
    }


    @FXML
    void updateWishlistItem(ActionEvent event) throws Exception {
        // Get the selected item from the table
        WishlistItem selectedItem = tableWishlistDisplay.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem == null) {
            // If no item is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an item to update.");
            alert.show();
            return;
        }

        // Retrieve the updated values from the input fields
        String updatedName = nameItem.getText();
        String priceText = priceItem.getText();
        Priority updatedPriority = priority.getValue();
        Status updatedStatus = statuts.getValue();
        String updatedCategoryName = category.getValue();
        int updatedCategoryId = wishlistItemServices.getCategoryID(updatedCategoryName);
        // Check if any field is empty
        if (updatedName.isEmpty() || priceText.isEmpty() || updatedPriority == null || updatedStatus == null || updatedCategoryName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all fields.");
            alert.show();
            return;
        }

        // Parse the price to double
        double updatedPrice;
        try {
            updatedPrice = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            // Show error message if price is not a valid number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Price must be a valid number.");
            alert.show();
            return;
        }

        // Ask for confirmation before updating
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("Are you sure you want to update this item?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed update
            // Update the selected item with the new values
            selectedItem.setNameWishlistItem(updatedName);
            selectedItem.setPrice(updatedPrice);
            selectedItem.setPriority(updatedPriority);
            selectedItem.setStatus(updatedStatus);
            selectedItem.setIdItemCategory(updatedCategoryId);

            // Use WishlistItemServices to update the item in the database
            wishlistItemServices.updateEntity(selectedItem, selectedItem.getIdWishlistItem());

            // Clear the fields after update
            clearFields();

            // Refresh the table view with updated data for the selected wishlist
            List<WishlistItem> updatedWishlistItems = wishlistItemServices.getAllWishlistItems(selectedItem.getIdWishlist());
            populateTable(updatedWishlistItems);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wishlist Item updated!");
            alert.show();
        }
    }


    private void clearFields() {
        // Clear text fields
        nameItem.clear();
        priceItem.clear();

        // Clear choice boxes
        priority.getSelectionModel().clearSelection();
        statuts.getSelectionModel().clearSelection();
        category.getSelectionModel().clearSelection();
    }


    @FXML
    void initialize() {
        wishlistItemServices = new WishlistItemServicesWishlist();
        // Populate priority choice box
        priority.getItems().addAll(Priority.values());
        // Populate status choice box
        statuts.getItems().addAll(Status.values());

        // Populate category choice box
        WishlistItemServicesWishlist wishlistItemServices = new WishlistItemServicesWishlist();
        category.getItems().add("Manage Categories");

        category.getItems().addAll(wishlistItemServices.getCategories());
        // Populate the wishlist choice box
        WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
        List<String> wishlistNames = wishlistServices.getAllWishlistNames();
        wishlistChoiceBox.getItems().add("Manage Wishlists");

        wishlistChoiceBox.getItems().addAll(wishlistNames);
        // Populate table with wishlist items for the selected wishlist



        // Set a listener to update the table whenever a wishlist is selected
        wishlistChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the ID of the selected wishlist
                int selectedWishlistId = wishlistServices.getWishlistIdByName(newValue);
                // Retrieve the wishlist items for the selected wishlist
                List<WishlistItem> wishlistItems = wishlistItemServices.getAllWishlistItems(selectedWishlistId);
                // Retrieve the monthly budget of the selected wishlist
                double monthlyBudgetValue = wishlistServices.getMonthlyBudget(selectedWishlistId);
                // Set the monthly budget text field with the retrieved value
                monthlyBudget.setText(String.valueOf(monthlyBudgetValue));
                // Populate the table with the retrieved wishlist items
                try {
                    populateTable(wishlistItems);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        tableWishlistDisplay.setOnMouseClicked(event -> {
            // Get the selected item from the table
            WishlistItem selectedItem = tableWishlistDisplay.getSelectionModel().getSelectedItem();
            // Populate the fields with the selected item's details
            populateFields(selectedItem);
        });

        // Add listener to wishlistChoiceBox
        wishlistChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Manage Wishlists".equals(newValue)) {
                // Show the manageWishlists window
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManageWishlists.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    ManageWishlists manageWishlists1 = loader.getController();
                    manageWishlists1.setFirstWindowController(this);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Reset choice box selection to prevent "Manage Wishlists" from being selected
                wishlistChoiceBox.getSelectionModel().clearSelection();
            }
        });

        // Add listener to wishlistChoiceBox
        category.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Manage Categories".equals(newValue)) {
                // Show the manageWishlists window
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManageCategory.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    // Get the controller of the second window
                    ManageCategory manageCategoryController = loader.getController();

// Set the reference to the controller of the first window in the second window controller
                    manageCategoryController.setFirstWindowController(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Reset choice box selection to prevent "Manage Wishlists" from being selected
                category.getSelectionModel().clearSelection();
            }
        });




    }

    private void populateTable(List<WishlistItem> wishlistItems) throws Exception {
        // Clear existing table data
        tableWishlistDisplay.getItems().clear();
        // Add new wishlist items to the table
        ObservableList<WishlistItem> data = FXCollections.observableArrayList(wishlistItems);
        tableWishlistDisplay.setItems(data);

        // Set cell value factories for each column
        nameTab.setCellValueFactory(new PropertyValueFactory<>("nameWishlistItem"));

        nameTab.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);

                    // Add click functionality
                    setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PriceAPI.fxml"));
                            Parent root = loader.load();

// Get the PriceAPI controller
                            PriceAPI priceAPIController = loader.getController();

// Pass the selected name to the PriceAPI controller
                            priceAPIController.setSelectedName(item);

// Set the selected item in the PriceAPI controller
                            //priceAPIController.setItem(item);

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });


        priceTab.setCellValueFactory(new PropertyValueFactory<>("price"));
        priorityTab.setCellValueFactory(new PropertyValueFactory<>("priority"));
        //categoryTab.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        statusTab.setCellValueFactory(new PropertyValueFactory<>("status"));
        creationDateTab.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        // Set custom cell value factory for the category column
        categoryTab.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getIdItemCategory();
            WishlistItemServicesWishlist wishlistItemService = new WishlistItemServicesWishlist();
            String categoryName = wishlistItemService.getCategoryNameById(categoryId);
            return new SimpleStringProperty(categoryName);
        });
        // Create a progress bar column
        progress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progress.setCellFactory(ProgressBarTableCell.forTableColumn());


// Calculate progress for each wishlist item
        WishlistItemServicesWishlist wishlistItemService = new WishlistItemServicesWishlist();
        for (WishlistItem item : data) {
            if (item.getStatus().equals(Status.IN_PROGRESS)) {
                double progress = wishlistItemService.calculateProgressForItemInProgress(item.getIdWishlistItem());
                item.setProgress(progress / 100.0);
            }
        }


// Define colors
        String red = "#FF6347";
        String orange = "#FFA500";
        String green = "#32CD32";

        progress.setCellFactory(column -> new TableCell<>() {
            private final StackPane stackPane = new StackPane();
            private final ProgressBar progressBar = new ProgressBar();
            private final Label progressLabel = new Label();

            {
                // Set the width of the progress bar
                progressBar.setPrefWidth(100);
                progressBar.setPrefHeight(25); // Adjust height as needed
                // Apply the custom style
                progressBar.getStyleClass().add("custom-progress-bar");
                progressLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: grey;");

                stackPane.getChildren().addAll(progressBar, progressLabel);


                // Set the text to display the progress value as a percentage
                progressBar.setTooltip(new Tooltip());
                progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
                    double value = newValue.doubleValue();
                    // Set the progress bar color based on the progress value
                    if (value < 0.3) {
                        setProgressBarColor(progressBar, red); // Red color
                    } else if (value < 0.7) {
                        setProgressBarColor(progressBar, orange); // Orange color
                    } else {
                        setProgressBarColor(progressBar, green); // Green color
                    }
                    // Update the tooltip to display the progress value as a percentage
                    progressBar.getTooltip().setText(String.format("%.0f%%", value * 100));

                    // Update the label text with the progress value
                    progressLabel.setText(String.format("%.0f%%", value * 100));
                });
                // Add hover functionality to display estimate purchase date
                stackPane.setOnMouseEntered(event -> {
                    WishlistItem item = (WishlistItem) getTableRow().getItem();
                    if (item != null) {
                        LocalDate purchaseDate = wishlistItemService.estimatePurchaseDate(item.getIdWishlistItem());
                        if (purchaseDate != null) {
                            progressBar.getTooltip().setText("Estimated Purchase Date: " + purchaseDate);
                        }
                    }else if(item.getStatus()!=Status.IN_PROGRESS){ //to modify
                        progressBar.getTooltip().setText("Not started yet!");
                    }
                });

                stackPane.setOnMouseExited(event -> {
                    progressBar.getTooltip().setText(""); // Clear tooltip when mouse exits
                });
            }



            @Override
            protected void updateItem(Double progress, boolean empty) {
                super.updateItem(progress, empty);

                if (empty || progress == null) {
                    setGraphic(null);
                } else {
                    progressBar.setProgress(progress);
                    setGraphic(stackPane);
                }
            }

            private void setProgressBarColor(ProgressBar progressBar, String color) {
                String css = String.format("-fx-accent: %s;", color);
                progressBar.setStyle(css);
            }
        });

    }



    private void populateFields(WishlistItem selectedItem) {
        nameItem.setText(selectedItem.getNameWishlistItem());
        priceItem.setText(String.valueOf(selectedItem.getPrice()));
        // Set the selected item's priority in the priority choice box
        priority.setValue(Priority.valueOf(selectedItem.getPriority().toString()));
        // Set the selected item's status in the status choice box
        statuts.setValue(Status.valueOf(selectedItem.getStatus().toString()));
        // Set the selected item's category in the category choice box
        String categoryName = wishlistItemServices.getCategoryNameById(selectedItem.getIdItemCategory());
        category.setValue(categoryName);
    }
    // Method to refresh the wishlist display
    public void refreshWishlistChoiceBox() {
        // Update the choice box with the new wishlist
        wishlistChoiceBox.getItems().clear(); // Clear existing items
        // Reload wishlist items from data source
        WishlistServicesWishlist wishlistServices = new WishlistServicesWishlist();
        List<String> wishlistNames = wishlistServices.getAllWishlistNames();
        wishlistChoiceBox.getItems().add("Manage Wishlists");
        wishlistChoiceBox.getItems().addAll(wishlistNames);
    }

    public void updateCategoryChoiceBox() {
        // Retrieve the updated list of categories
        List<String> categories = wishlistItemServices.getCategories();

        // Clear the existing options in the choice box
        category.getItems().clear();

        // Add "Manage Categories" as the first option
        category.getItems().add("Manage Categories");

        // Add all the retrieved categories to the choice box
        category.getItems().addAll(categories);
    }
}
