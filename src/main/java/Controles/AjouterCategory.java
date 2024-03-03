//////////
package Controles;
////

import Entities.Category;
import Entities.SubCategory;
import Services.CategoryService;
import Services.SubCategoryService;
import edu.esprit.pi.MainApplication;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.Node;
import javafx.application.Platform;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

import javafx.geometry.Insets;
import javafx.scene.control.*;


import java.util.List;
import java.util.Optional;

public class AjouterCategory {

    @FXML
    private ListView<Category> categoryListView;

    private ObservableList<Category> categories;

    private CategoryService categoryService;
    private SubCategoryService subCategoryService;

    @FXML
    public void initialize() {
        categoryService = new CategoryService();
        subCategoryService = new SubCategoryService();
        loadCategories();
    }

    private void loadCategories() {
        List<Category> categoryList = categoryService.getAllData();
        categories = FXCollections.observableArrayList(categoryList);
        categoryListView.setItems(categories);

        categoryListView.setCellFactory(param -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create buttons for delete and update actions
                    Button deleteButton = createStyledButton("", "icon_remove.png");
                    Button updateButton = createStyledButton("", "icon_edit.png");
                    Button addSubCategoryButton = createStyledButton("", "icon_add.png");
                    ToggleButton showSubcategoriesButton = ToggleImageButton("icon_expand.png");

                    // Set actions for buttons
                    deleteButton.setOnAction(event -> deleteCategory(item));
                    updateButton.setOnAction(event -> updateCategory(item));
                    addSubCategoryButton.setOnAction(event -> addSubCategory(item));

                    deleteButton.setTooltip(new Tooltip("Delete"));
                    updateButton.setTooltip(new Tooltip("Update"));
                    addSubCategoryButton.setTooltip(new Tooltip("Add Subcategory"));
                    showSubcategoriesButton.setTooltip(new Tooltip("Show Subcategories"));

                    Label nameLabel = new Label(item.getName() + "");
                    Label budgetLimitLabel = new Label("" + item.getBudgetLimit());

                    //
                    ImageView nameLabelImage = new ImageView(new Image(getClass().getResource("/images/icon_category.png").toString()));
                    ImageView budgetLimitLabelImage = new ImageView(new Image(getClass().getResource("/images/icon_budget.png").toString()));

                    nameLabelImage.setFitWidth(20);
                    nameLabelImage.setFitHeight(20);

                    budgetLimitLabelImage.setFitWidth(20);
                    budgetLimitLabelImage.setFitHeight(20);

                    HBox nameLabelBox = new HBox(nameLabelImage, nameLabel);
                    HBox budgetLimitLabelBox = new HBox(budgetLimitLabelImage, budgetLimitLabel);

                    nameLabelBox.setSpacing(5);
                    budgetLimitLabelBox.setSpacing(5);

                    VBox labelsBox = new VBox(nameLabelBox, budgetLimitLabelBox);
                    labelsBox.setSpacing(5);
                    //

                    ListView<SubCategory> subCategoryListView = new ListView<>();
                    subCategoryListView.setItems(FXCollections.observableArrayList(item.getSubCategories()));


                    subCategoryListView.setCellFactory(param -> new ListCell<SubCategory>() {
                        @Override
                        protected void updateItem(SubCategory subCategory, boolean empty) {
                            super.updateItem(subCategory, empty);
                            if (empty || subCategory == null) {
                                setText(null);
                            } else {
                                Label nomLabel = new Label("" + subCategory.getNom());
                                //Label mtAssignéLabel = new Label("Montant Assigné: " + subCategory.getMtAssigné());
                                //Label mtDépenséLabel = new Label("Montant Dépensé: " + subCategory.getMtDépensé());
                                Label informationLabel = new Label("You spent " + subCategory.getMtDépensé() + " from " + subCategory.getMtAssigné());

                                ProgressBar progressBar = new ProgressBar();
                                Label progressMessageLabel = new Label();
                                progressBar.setPrefWidth(200);
                                double remainingAmount = subCategory.getMtAssigné() - subCategory.getMtDépensé();

                                progressBar.setProgress(remainingAmount / subCategory.getMtAssigné());

                                if (remainingAmount > 0) {
                                    progressMessageLabel.setText("You still have " + remainingAmount + " to spend.");
                                    progressMessageLabel.setStyle("-fx-text-fill: green;");
                                    progressBar.setStyle("-fx-accent: green;");
                                } else if (remainingAmount == 0) {
                                    progressMessageLabel.setText("You fully spent your budget.");
                                    progressMessageLabel.setStyle("-fx-text-fill: orange;");
                                    progressBar.setStyle("-fx-accent: orange;");
                                } else {
                                    progressMessageLabel.setText("You overspent your budget with " + (-remainingAmount) + ".");
                                    progressMessageLabel.setStyle("-fx-text-fill: red;");
                                    progressBar.setStyle("-fx-accent: red;");
                                }

                                Button editButton = createStyledButton("Edit", "icon_plusmoney.png");
                                Button deleteButton = createStyledButton("Delete", "icon_removemoney.png");

                                editButton.setOnAction(event -> updateSubCategory(subCategory, item, subCategoryListView));
                                deleteButton.setOnAction(event -> deleteSubCategory(subCategory, item, subCategoryListView));


                                VBox editDeleteBox = new VBox(editButton, deleteButton);
                                editDeleteBox.setSpacing(5);

                                VBox vbox = new VBox(nomLabel, informationLabel, progressBar, progressMessageLabel, editDeleteBox);
                                vbox.setSpacing(5);


                                HBox hbox = new HBox(vbox, editDeleteBox);
                                hbox.setAlignment(Pos.CENTER_LEFT);
                                hbox.setSpacing(10);
                                setGraphic(hbox);
                            }
                        }
                    });


                    subCategoryListView.managedProperty().bind(showSubcategoriesButton.selectedProperty());
                    subCategoryListView.visibleProperty().bind(showSubcategoriesButton.selectedProperty());

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(5);

                    HBox hbox = new HBox(deleteButton, updateButton, addSubCategoryButton, showSubcategoriesButton);
                    hbox.setAlignment(Pos.CENTER_RIGHT);

                    gridPane.addRow(0, labelsBox, hbox);
                    gridPane.addRow(1, subCategoryListView);
                    GridPane.setColumnSpan(subCategoryListView, GridPane.REMAINING);

                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    gridPane.getColumnConstraints().addAll(new ColumnConstraints(), columnConstraints);

                    setGraphic(gridPane);
                }
            }
        });
        categoryListView.getStylesheets().add("data:text/css," + scrollbarCss);

    }

    String scrollbarCss =
            ".scroll-bar:vertical {" +
                    "-fx-background-color: #e0e0e0;" +
                    "-fx-background-radius: 5;" +
                    "}" +
                    ".scroll-bar:vertical .thumb {" +
                    "-fx-background-color: #22577A;" +
                    "-fx-background-radius: 5;" +
                    "}";


    private Button createStyledButton(String text, String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent;");
        ImageView imageView = new ImageView(new Image(getClass().getResource("/images/" +imagePath).toString()));
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        button.setGraphic(imageView);
        return button;
    }
    private ToggleButton ToggleImageButton(String imagePath) {
        ToggleButton button = new ToggleButton();

        Image image = new Image(getClass().getResource("/images/" +imagePath).toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);

        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");

        return button;
    }

//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setStyle("-fx-background-color: #22577a; -fx-text-fill: white; -fx-font-weight: bold;");
//        return button;
//    }

    @FXML
    public void openAddCategoryPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("AddCategoryPopup.fxml"));
            Parent root = loader.load();

            AddCategoryPopupController controller = loader.getController();
            controller.setCategoryList(categories);
            controller.setCategoryService(categoryService);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResource("/images/icon_add.png").toString()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            stage.showAndWait();

            loadCategories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCategory(Category category) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Category");
        confirmation.setHeaderText("Are you sure you want to delete this category?");
        confirmation.setContentText("Category: " + category.getName());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            categoryService.deleteEntityById(category.getId());
            categories.remove(category);
        }
    }


    private void updateCategory(Category category) {
        Dialog<Pair<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Update Category");
        dialog.setHeaderText("Enter the new name and budget limit for the category:");

        // Set the button types (OK and Cancel)
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the name and budget limit fields
        TextField nameField = new TextField(category.getName());
        TextField budgetLimitField = new TextField(Double.toString(category.getBudgetLimit()));
        // Create a grid pane and add the fields to it
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Budget Limit:"), 0, 1);
        grid.add(budgetLimitField, 1, 1);

        // Enable/disable the update button depending on whether the name and budget limit are valid
        Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
        updateButton.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || !isDouble(budgetLimitField.getText()));
        });
        budgetLimitField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || !isDouble(newValue));
        });

        // Set the grid pane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default
        Platform.runLater(nameField::requestFocus);

        // Convert the result to a pair (name, budget limit) when the update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Pair<>(nameField.getText(), Double.parseDouble(budgetLimitField.getText()));
            }
            return null;
        });

        // Show the dialog and update the category if the user clicks OK
        Optional<Pair<String, Double>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            categoryService.updateEntity(category.getId(), pair.getKey(), pair.getValue());
            category.setName(pair.getKey());
            category.setBudgetLimit(pair.getValue());
            categoryListView.refresh();
        });
    }

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void addSubCategory(Category category) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Subcategory");
        dialog.setHeaderText("Enter the details for the subcategory:");

        // Create input fields for subcategory name, montant assigné, and montant dépensé
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField subCategoryNameField = new TextField();
        TextField montantAssignéField = new TextField();
        TextField montantDépenséField = new TextField();
        subCategoryNameField.setPrefWidth(700);
        montantAssignéField.setPrefWidth(700);
        montantDépenséField.setPrefWidth(700);

        grid.add(new Label("Subcategory Name:"), 0, 0);
        grid.add(subCategoryNameField, 1, 0);
        grid.add(new Label("Montant Assigné:"), 0, 1);
        grid.add(montantAssignéField, 1, 1);
        grid.add(new Label("Montant Dépensé:"), 0, 2);
        grid.add(montantDépenséField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a string representation of the subcategory when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String subCategoryName = subCategoryNameField.getText();
                double montantAssigné = Double.parseDouble(montantAssignéField.getText());
                double montantDépensé = Double.parseDouble(montantDépenséField.getText());

                // Return a string representation of the subcategory
                return subCategoryName + " - Montant Assigné: " + montantAssigné + " - Montant Dépensé: " + montantDépensé;
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(subCategoryString -> {
            // Extract the subcategory details from the string representation
            String[] parts = subCategoryString.split(" - ");
            String subCategoryName = parts[0];
            double montantAssigné = Double.parseDouble(parts[1].split(": ")[1]);
            double montantDépensé = Double.parseDouble(parts[2].split(": ")[1]);

            // Create the subcategory object and add it
            SubCategory subCategory = new SubCategory(subCategoryName, montantAssigné, montantDépensé, category.getId());
            subCategoryService.addEntity(subCategory);
            category.addSubCategory(subCategory);
            categoryListView.refresh();
        });
    }

    private void deleteSubCategory(SubCategory subCategory, Category category, ListView<SubCategory> subCategoryListView) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete SubCategory");
        confirmation.setHeaderText("Are you sure you want to delete this SubCategory?");
        confirmation.setContentText("SubCategory: " + subCategory.getNom());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            subCategoryService.deleteEntityById(subCategory.getId());
            // Update the Category's SubCategory list
            category.setSubCategories(categoryService.getSubCategoriesForCategory(category.getId()));
            // Refresh the subCategoryListView
            subCategoryListView.setItems(FXCollections.observableArrayList(category.getSubCategories()));
        }
    }


    private void updateSubCategory(SubCategory subCategory, Category category, ListView<SubCategory> subCategoryListView) {
        Dialog<Pair<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Update SubCategory");
        dialog.setHeaderText("Enter the Montant dépensé");

        // Set the button types (OK and Cancel)
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the name and budget depense fields
        TextField budgetDepenseField = new TextField(Double.toString(subCategory.getMtDépensé()));

        // Create a grid pane and add the fields to it
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Montant dépensé:"), 0, 0);
        grid.add(budgetDepenseField, 1, 0);

        // Enable/disable the update button depending on whether the name and budget depense are valid
        Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
        updateButton.setDisable(true);
        budgetDepenseField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || !isDouble(newValue));
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default
        Platform.runLater(budgetDepenseField::requestFocus);

        // Set the result converter for the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Pair<>("Montant dépensé", Double.parseDouble(budgetDepenseField.getText()));
            }
            return null;
        });

        // Show the dialog and update the subCategory if the user clicks OK
        Optional<Pair<String, Double>> result = dialog.showAndWait();
        result.ifPresent(montantDepense -> {
            // Perform the update here
            subCategoryService.updateMontantDepense(subCategory.getId(), montantDepense.getValue());
            // Update the Category's SubCategory list
            category.setSubCategories(categoryService.getSubCategoriesForCategory(category.getId()));
            // Refresh the subCategoryListView
            subCategoryListView.setItems(FXCollections.observableArrayList(category.getSubCategories()));
        });
    }

    @FXML
    public void openStatPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("statCategory.fxml"));
            Parent root = loader.load();

            StatController controller = loader.getController();
            controller.initialize(); // Initialize the chart

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResource("/images/icon_stat.png").toString()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
