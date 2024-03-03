//
//package controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//import javafx.collections.ObservableList;
//import entities.Category;
//import services.CategoryService;
//
//public class AddCategoryPopupController {
//
//    @FXML
//    private TextField NomTf;
//
//    @FXML
//    private TextField BudgetLimitTf;
//
//    private ObservableList<Category> categoryList;
//    private CategoryService categoryService;
//
//    public void setCategoryList(ObservableList<Category> categoryList) {
//        this.categoryList = categoryList;
//    }
//
//    // Method to inject CategoryService
//    public void setCategoryService(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }
//
//    @FXML
//    void addCategory() {
//        String nom = NomTf.getText();
//        String budgetLimit = BudgetLimitTf.getText();
//
//        // Validate input and add category to the list
//        if (!nom.isEmpty() && !budgetLimit.isEmpty()) {
//            // Add category to the database using CategoryService
//            Category category = new Category(nom, Double.parseDouble(budgetLimit));
//
//            if(categoryService != null) {
//                categoryService.addEntity(category);
//                // Refresh category list
//                categoryList.add(category);
//                closePopup();
//            } else {
//                System.out.println("CategoryService is not initialized.");
//            }
//        } else {
//            // Show error message if input fields are empty
//            System.out.println("Please enter both category name and budget limit.");
//        }
//    }
//
//    private void closePopup() {
//        // Close the popup window
//        BudgetLimitTf.getScene().getWindow().hide();
//    }
//}
package edu.esprit.pi.controller;

import edu.esprit.pi.entities.Category;
import edu.esprit.pi.services.CategoryService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddCategoryPopupController {

    @FXML
    private TextField NomTf;

    @FXML
    private TextField BudgetLimitTf;

    private ObservableList<Category> categoryList;
    private CategoryService categoryService;

    public void setCategoryList(ObservableList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    // Method to inject CategoryService
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    void addCategory() {
        String nom = NomTf.getText();
        String budgetLimit = BudgetLimitTf.getText();


        if (!nom.isEmpty() && !budgetLimit.isEmpty()) {

            Category category = new Category(nom, Double.parseDouble(budgetLimit));

            if(categoryService != null) {
                try {
                    categoryService.addEntity(category);
                    categoryList.add(category);
                    closePopup();
                } catch (Exception e) {
                    showAlert("Error", "An error occurred while adding the category: " + e.getMessage());
                }
            } else {
                System.out.println("CategoryService is not initialized.");
            }
        } else {
            showAlert("Missing Information", "Please enter both category name and budget limit.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closePopup() {
        // Close the popup window
        BudgetLimitTf.getScene().getWindow().hide();
    }
}

