package Controles;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import Entities.Account;
import Services.AccountService;
import Services.TransactionService;
import Services.WalletService;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class AccountsController {
    public ScrollPane scrollPaneCon;
    public Button managePayeesButt;
    public Text allAccountsTitle;
    public Text accountsOverview;

    private int accountId;
    private int idWallet;
    private WalletService walletService = new WalletService();
    private String currencySymbol="";
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @FXML
    private AnchorPane AccountsAnchor;

    @FXML
    private Text TotalBalanceText;

    @FXML
    private Button accounts;

    @FXML
    private FlowPane accountsContainer;

    @FXML
    private VBox accountsContainer1;

    @FXML
    private Button addAccountButton;

    @FXML
    private VBox balanceContainer;

    @FXML
    private Button budget;

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    @FXML
    private Button debts;

    @FXML
    private HBox infoBox;

    @FXML
    private VBox mainVBox;

    @FXML
    private Text nbrAccounts;

    @FXML
    private Text nbrAccountsText;

    @FXML
    private Text nbrTransactionText;

    @FXML
    private Text nbrTransactions;

    @FXML
    private Button overview;

    @FXML
    private AnchorPane pageAcc;

    @FXML
    private ImageView pennywiseLOGO;

    @FXML
    private Button profile;

    @FXML
    private Button reports;

    @FXML
    private Button settings;

    @FXML
    private Button signout;

    @FXML
    private Button todo;

    @FXML
    private Text totalBalance;

    @FXML
    private VBox transactionsContainer;

    @FXML
    private Button wishlist;
    public void initialize() {
        System.out.println("AccountsController initialized");
        System.out.println(idWallet);
        System.out.println(idWallet);
        System.out.println(idWallet);

        loadAccounts();

        selectOverview();
        AccountService as =new AccountService();
        WalletService ws = new WalletService();
        TransactionService ts = new TransactionService();
        int numberOfAccounts = as.countAccountsForWallet(idWallet);
        double totalBalance = ws.getTotalBalanceData(idWallet);
        int numberOfTransactions = ts.countTransactions();


        setData(numberOfAccounts, totalBalance, numberOfTransactions);

    }
    private void selectOverview() {
        accounts.getStyleClass().add("button-selected");
    }
    public void loadAccounts() {
        AccountService accountService = new AccountService();

        List<Account> accounts  ;
        accounts = accountService.getAllAccounts(idWallet);
        accountsContainer.getChildren().clear();

        for (Account account : accounts) {
            addAccountPane(account.getIdAccount(),account.getNameAccount(), account.getTypeAccount().toString(), decimalFormat.format(account.getBalance()), account.getDescription());
        }




    }
    private void handleAddAccount() {
        // You can show a dialog or gather user input to create a new account
        // For simplicity, let's create a dummy account

    }

    private void addAccountPane(int idAccount,String name, String type, String balance, String description) {
        try {
            System.out.println("Adding account pane: " + name);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountPane.fxml"));
            AnchorPane accountPane = loader.load();

            // Get the controller associated with the loaded FXML
            AccountPaneController controller = loader.getController();
            controller.setIdWallet(idWallet);

            // Initialize the controller with account details
            controller.initialize(idAccount,name, type, balance, description);
            controller.setAccountsController(this);

            // Add the accountPane to the FlowPane
            accountsContainer.getChildren().add(accountPane);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addAccount(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddAccountForm.fxml"));

            // Load the parent root
            Parent root = loader.load();

            // Retrieve the controller
            AddAccountDIalog controller = loader.getController();
            controller.setIdWallet(idWallet);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Account");
            stage.setScene(new Scene(root));

            // Show the dialog and wait for it to be closed
            stage.showAndWait();
            if (controller.isDialogClosedSuccessfully()) {
                loadAccounts();
                AccountService as =new AccountService();
                WalletService ws = new WalletService();
                TransactionService ts = new TransactionService();
                int numberOfAccounts = as.countAccountsForWallet(idWallet);
                double totalBalance = ws.getTotalBalanceData(idWallet);
                int numberOfTransactions = ts.countTransactions();

                setData(numberOfAccounts, totalBalance, numberOfTransactions);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @FXML
    void openAccountsPage(ActionEvent event) {
         switchScene("/views/Dashboard_Accounts.fxml");
    }

    @FXML
    void openOverviewPage(ActionEvent event) {
        switchScene("/views/Dashboard.fxml");

    }
    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) overview.getScene().getWindow(); // Access the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    public void setData(int numberOfAccountsData, double totalBalanceData, int numberOfTransactionsData) {
        updateCurrencySymbol(idWallet);

        nbrAccounts.setText(Integer.toString(numberOfAccountsData));
        totalBalance.setText(decimalFormat.format(totalBalanceData) +currencySymbol);
        nbrTransactions.setText(Integer.toString(numberOfTransactionsData));
        // Set data for other Text nodes
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

    public void managePayees(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManagePayees.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            // Get the controller of the second window
            ManagePayees managePayees = loader.getController();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
