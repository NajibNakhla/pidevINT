package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import Services.AccountService;
import Services.TransactionService;
import Services.WalletService;

import java.io.IOException;

public class AccountPopMenu {
    private int accountId;
    private boolean dialogClosedSuccessfully = false;
    private AccountsController accountsController;


    // Other methods and fields...

    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @FXML
    private Button closePopButt;

    @FXML
    private Button deleteButtonPop;

    @FXML
    private Button editButtonPop;

    @FXML
    private Line lineV1;

    @FXML
    private Line lineV2;

    @FXML
    private AnchorPane popTransactionMenu;

    @FXML
    void closePOP(ActionEvent event) {
        Stage stage = (Stage) closePopButt.getScene().getWindow();

        stage.close();
        refreshData();

        reloadAccounts();
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        AccountService accountService = new AccountService();
        System.out.println(this.accountId);
        accountService.deleteEntityByID(this.accountId);

        reloadAccounts();

        closePOP(event);
    }

    @FXML
    void editAccount(ActionEvent event) {
        // Assuming you have a method to open the edit account FXML and controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/updateAccountForm.fxml"));
        Parent root;
        try {
            root = loader.load();
            UpdateAccountDIalog editAccountController = loader.getController();

            // Pass the account ID to the controller
            editAccountController.setIdAccount(accountId);

            // Show the edit account stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Account");
            stage.showAndWait();
            refreshData();
            reloadAccounts();
// Show and wait for the user to close the edit account stage

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        reloadAccounts();
    }

    private void refreshData() {

        accountsController.loadAccounts();
        AccountService as =new AccountService();
        WalletService ws = new WalletService();
        TransactionService ts = new TransactionService();
        int numberOfAccounts = as.countAccountsForWallet(1);
        double totalBalance = ws.getTotalBalanceData(1);
        int numberOfTransactions = ts.countTransactions();

        accountsController.setData(numberOfAccounts, totalBalance, numberOfTransactions);
    }

    private void reloadAccounts() {
        // Check if accountsController is not null before reloading
        if (accountsController != null) {
            refreshData();
            accountsController.loadAccounts();
        }
    }

    public boolean isDialogClosedSuccessfully() {
        return dialogClosedSuccessfully;
    }

}
