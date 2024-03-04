package Controles;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Services.AccountService;
import Services.TransactionService;
import Services.WalletService;

import java.io.IOException;
import java.text.DecimalFormat;

public class AccountPaneController {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private AccountsController accountsController;

    private WalletService walletService = new WalletService();
    private String currencySymbol="";
    private int idWallet;

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }
// Other methods and fields...

    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }
    private boolean dialogClosedSuccessfully = false;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView typeIcon;

    @FXML
    private Label typeLabel;

    public void initialize(int idAccount, String name, String type, String balance, String description) {
        updateCurrencySymbol(idWallet);
        nameLabel.setText(name);
        typeLabel.setText(type);

        // Set the account type icon based on the type (customize this part)
        typeIcon.setImage(getTypeIcon(type));

        balanceLabel.setText( balance +currencySymbol );
        descriptionLabel.setText(description);

        anchorPane.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                handleDoubleClick(idAccount);
            }
        });
    }

    private void handleDoubleClick(int idAccount) {

        try {
            System.out.println("Double-clicked on account with id: " + idAccount);
            // Assuming accountPopMenuController is the instance of AccountPopMenuController

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountPopMenu.fxml"));
            Parent root = loader.load();

            // Retrieve the controller
            AccountPopMenu popMenuController = loader.getController();
           // popMenuController.setAccountsController(this);


            // Pass the reference to AccountsController
            popMenuController.setAccountsController(this.accountsController);

            // Set any necessary data in the pop-up controller
            popMenuController.setAccountId(idAccount);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Account Options");
            stage.setScene(new Scene(root));

            // Show the pop-up and wait for it to be closed
            stage.showAndWait();
            reloadAccounts();
            refreshData();
            if (popMenuController.isDialogClosedSuccessfully()) {
                reloadAccounts();
             refreshData();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isDialogClosedSuccessfully() {
        return dialogClosedSuccessfully;
    }



    private Image getTypeIcon(String type) {
        // Customize this method based on your account types and corresponding icons
        switch (type) {
            case "SAVINGS":
                System.out.println("Loading Savings icon");
                return new Image("/icons/save-money.png");
            case "CHECKING":
                System.out.println("Loading Checking  icon");
                return new Image("/icons/money.png");
            case "CASH":
                System.out.println("Loading Checking  icon");
                return new Image("/icons/money.png");    // Add more cases for other account types and their icons
            default:
                System.out.println("Unknown account type: " + type);
                return null; // Return a default icon or handle unrecognized types
        }
    }

    public String getAccountName() {
        return nameLabel.getText();
    }
    private void refreshData() {

        accountsController.loadAccounts();

        AccountService as =new AccountService();
        WalletService ws = new WalletService();
        TransactionService ts = new TransactionService();
        int numberOfAccounts = as.countAccountsForWallet(idWallet);
        double totalBalance = ws.getTotalBalanceData(idWallet);
        int numberOfTransactions = ts.countTransactions();
        accountsController.setData(numberOfAccounts,totalBalance,numberOfTransactions);


    }

    private void reloadAccounts() {
        // Check if accountsController is not null before reloading
        if (accountsController != null) {
            refreshData();
            accountsController.loadAccounts();
        }
    }


    private void updateCurrencySymbol(int idWallet) {
        String currency = walletService.getCurrency(idWallet);

        switch (currency) {
            case "USD":
                currencySymbol = "$";
                break;
            case "EUR":
                currencySymbol = "€";
                break;
            case "TND":
                currencySymbol = "DT";  // Change it based on the currency symbol for Tunisian Dinar
                break;
            // Add more cases for other currencies if needed
            default:
                currencySymbol = "";  // Default or handle differently for unknown currencies
        }
    }
}



