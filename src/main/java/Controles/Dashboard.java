package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Services.AccountService;
import Services.WalletService;

import java.io.IOException;

public class Dashboard {


    public VBox DashboardMenu;
    public AnchorPane MenuDashboardAnchor;
    @FXML
    private Button accounts;

    @FXML
    private Button budget;

    @FXML
    private Button debts;

    @FXML
    private VBox mainVBox;

    @FXML
    private Button overview;

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
    private Button wishlist;

    @FXML
    private Text nbrAccounts;

    @FXML
    private Text nbrTransactions;


    @FXML
    private Text totalBalance;
    @FXML
    private Text TotalBalanceText;
    @FXML
    private Text nbrAccountsText;

    @FXML
    private Text nbrTransactionText;


    public void setData(int numberOfAccountsData, double totalBalanceData, int numberOfTransactionsData) {

        nbrAccounts.setText(Integer.toString(numberOfAccountsData));
        totalBalance.setText(Double.toString(totalBalanceData));
        nbrTransactions.setText(Integer.toString(numberOfTransactionsData));
        // Set data for other Text nodes
    }
    @FXML
    public void initialize() {
        // Set the "Overview" button as the default selected button
        selectOverview();
        AccountService as =new AccountService();
        WalletService ws = new WalletService();
        int numberOfAccounts = as.countAccountsForWallet(1);
        double totalBalance = ws.getTotalBalanceData(1);
        int numberOfTransactions = 1 ;//dataService.getNumberOfTransactions();

        setData(numberOfAccounts, totalBalance, numberOfTransactions);
        accounts.setOnAction(event -> openAccountsPage());
    }

    private void selectOverview() {
        overview.getStyleClass().add("button-selected");
    }
    @FXML
    private void openAccountsPage() {
        try {
            // Load the FXML file for the Accounts page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard_Account.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage
            Stage stage = (Stage) accounts.getScene().getWindow();

            // Set the scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    public void goToSettings(ActionEvent actionEvent) {
        switchScene("/views/Settings.fxml");
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

}



