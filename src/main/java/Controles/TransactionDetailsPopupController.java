package Controles;

import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Entities.Transaction;
import Services.TransactionService;

import java.util.function.Consumer;
import javafx.event.Event;

public class TransactionDetailsPopupController {

    public AnchorPane popTransactionMenu;

    public void setDashboardController(Dashboard_Accounts dashboardController) {
        this.dashboardController = dashboardController;
    }

    private Dashboard_Accounts dashboardController;
    private Consumer<Boolean> onCloseCallback;
    public Button editButtonPop;
    public Button deleteButtonPop;
    public Line lineV2;
    public Line lineV1;
    private Stage popupStage;
    private int selectedTransactionId;

    public void setSelectedTransactionId(int selectedTransactionId) {
        this.selectedTransactionId = selectedTransactionId;
    }

    public void initialize() {
        // Initialize other components

        // Set the stage
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setScene(new Scene(popTransactionMenu));
        popTransactionMenu.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Bounds boundsInScreen = popTransactionMenu.localToScreen(popTransactionMenu.getBoundsInLocal());

            double x = event.getScreenX();
            double y = event.getScreenY();

            if (!boundsInScreen.contains(x, y)) {
                closePopup();
            }
        });
    }


    public void editTransaction(ActionEvent actionEvent) {
        // Implement your edit logic here
    }

    public void deleteTransaction(ActionEvent actionEvent) {

        System.out.println("trying to delete this Transaction with ID :  " +this.selectedTransactionId );
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Are you sure you want to delete this transaction?");
        alert.setContentText("This action cannot be undone.");

        // Capture the user's choice
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // If the user clicks OK, proceed with deletion
        if (result == ButtonType.OK) {
            TransactionService transactionService = new TransactionService();
            transactionService.deleteTransactionById(this.selectedTransactionId);
            fireEvent(new TransactionDeletedEvent(this.selectedTransactionId));


            dashboardController.refreshTable();

            hidePopup();

        }

    }

    private void fireEvent(Event event) {
        if (dashboardController != null) {
            dashboardController.handleTransactionDeletedEvent(event);
        }
    }


    public void showPopup(double x, double y) {
        popupStage.setX(x);
        popupStage.setY(y);

        if (!popupStage.isShowing()) {

            popupStage.show();
        }

        popupStage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            double popupX = popupStage.getX();
            double popupY = popupStage.getY();
            double popupWidth = popupStage.getWidth();
            double popupHeight = popupStage.getHeight();

            double clickX = event.getScreenX();
            double clickY = event.getScreenY();

            // Check if the click is outside the popup bounds
            if (clickX < popupX || clickX > popupX + popupWidth || clickY < popupY || clickY > popupY + popupHeight) {
                popupStage.hide();
            }
        });


    }




    public void hidePopup() {
        popupStage.hide();
    }

    public Stage getPopupStage() {
        return popupStage;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void setTransaction(Transaction selectedTransaction) {
    }
    public void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }

    public void closePOP(ActionEvent actionEvent) {
        if (popupStage != null) {
            popupStage.close();
        }

    }

    public void initializeData(int selectedTransactionId) {
        this.selectedTransactionId = selectedTransactionId;

        // Now you can use this.selectedTransactionId wherever you need it in your controller
        System.out.println("Selected Transaction ID: " + this.selectedTransactionId);
    }

    public int getSelectedTransactionId() {
        return selectedTransactionId;
    }


    public void setOnCloseCallback(Consumer<Boolean> onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    // Other methods...


    private void handleCloseButtonAction(ActionEvent event) {
        // Handle the close button action
        // ...

        // Notify the callback that the popup is closed
        if (onCloseCallback != null) {
            onCloseCallback.accept(true);
        }
    }
}
