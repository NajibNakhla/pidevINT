package Controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Entities.*;
import enums.TransactionType;
import Services.AccountService;
import Services.TransactionService;
import javafx.scene.control.cell.PropertyValueFactory;
import Services.WalletService;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Dashboard_Accounts {
    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    private int idWallet=2;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private WalletService walletService = new WalletService();
    private String currencySymbol="";
    private AtomicInteger selectedAccountIdForTransactions = new AtomicInteger(0);

    private ObservableList<TransactionTableRow> transactionData = FXCollections.observableArrayList();
    private int selectedAccountId;
    public AnchorPane TableContainer;
    public TableColumn dateColumn;
    public TableColumn typeColumn;
    public TableColumn descriptionColumn;
    public TableColumn amountColumn;
    public TableColumn fromAccountColumn;
    public TableColumn toAccountColumn;
    public TableColumn categoryNameColumn;
    public TableColumn payeeNameColumn;
    public AnchorPane accInterface;
    public TextField searchBar;
    public Button viewButton;
    public Button addTransactionButton;
    public Button fileImportButton;


    @FXML
    private MenuButton MenuOfAccounts;

    @FXML
    private Button accountOperations;

    @FXML
    private Button accounts;

    @FXML
    private Text balanceOfAccount;

    @FXML
    private Button budget;

    @FXML
    private Button debts;

    @FXML
    private ImageView iconOfAccount;

    @FXML
    private VBox mainVBox;

    @FXML
    private Button overview;

    @FXML
    private ImageView penEdit;

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
    private Text typeOfAccount;

    @FXML
    private Button wishlist;
    @FXML
    private TableView<TransactionTableRow> transactionTable;
    private FilteredList<TransactionTableRow> filteredData;


    @FXML
    private Line firstLine;

    @FXML
    private Line firstLine1;
    @FXML
    private AnchorPane tableContainer;

    private TransactionDetailsPopupController popupController = new TransactionDetailsPopupController();



    @FXML
    void openAccountsPage(ActionEvent event) {

    }


    @FXML
    public void initialize() {
        // Set the "Overview" button as the default selected button
        selectOverview();

        initializeAccounts();
        initializeColumns();
        initializeFiltering();

        initializePopupController();
        updateCurrencySymbol(1);




        transactionTable.setRowFactory(tv -> {
            TableRow<TransactionTableRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    TransactionTableRow rowData = row.getItem();
                    showTransactionPopup(rowData.getTransaction());
                }
            });
            return row;
        });



    }





    private void showTransactionPopup(Transaction selectedTransaction) {
        if (popupController == null) {
            initializePopupController();
        }

        // Assuming you have a method like setTransaction in your popup controller
        popupController.setTransaction(selectedTransaction);

        int selectedTransactionId = getSelectedTransactionId();

        // Print the selected transaction ID
        System.out.println("Selected Transaction ID: " + selectedTransactionId);
        popupController.setSelectedTransactionId(selectedTransactionId);


        // Calculate X and Y coordinates
        Bounds bounds = transactionTable.localToScreen(transactionTable.getBoundsInLocal());
        double x = bounds.getMinX();
        double y = bounds.getMaxY();

        // Call the method to show the popup
        popupController.showPopup(600, 600);

        // Set the stage always on top
        Stage popupStage = popupController.getPopupStage();
        if (popupStage != null) {
            popupStage.setAlwaysOnTop(true);
            popupStage.setOnHidden(event -> {
                // Refresh the table when the popup is closed
                updateTransactionTable(selectedAccountId);
                updateBalance();
            });
        }
        popupController.setOnCloseCallback(closed -> {
            if (closed) {

                popupStage.close();
                updateTransactionTable(selectedAccountId);
                refreshTable();
            }
        });
    }

    private void initializePopupController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransactionDetailsPopup.fxml"));
            Parent popupRoot = loader.load();
            popupController = loader.getController();
            popupController.setDashboardController(this);
            // Get the selected transaction ID
            int selectedTransactionId = getSelectedTransactionId(); // Replace this with the actual method to get the ID

            // Initialize the data in the popup controller
            popupController.initializeData(selectedTransactionId);


            // Create the stage only if it hasn't been created yet
            if (popupController.getPopupStage() == null) {
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initStyle(StageStyle.UNDECORATED);
                popupStage.setScene(new Scene(popupRoot));
                popupController.setPopupStage(popupStage);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private int getSelectedTransactionId() {
        TransactionTableRow selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        // Assuming Transaction class has a method getId(), replace it with the actual method
        return (selectedTransaction != null) ? selectedTransaction.getIdTransaction() : -1;
    }



    private void hideTransactionPopup() {
        // Call the method to hide the popup
        popupController.hidePopup();
    }


  /*  private Bounds getRowBounds(TransactionTableRow row) {
        // Assuming your TableView is in a VBox, you might need to adjust based on your layout
        Bounds tableBounds = tableContainer.localToScene(tableContainer.getBoundsInLocal());
        Bounds rowBounds = row.localToScene(row.getBoundsInLocal());

        // Convert the row bounds to the coordinate system of the table
        double minX = rowBounds.getMinX() - tableBounds.getMinX();
        double minY = rowBounds.getMinY() - tableBounds.getMinY();
        double width = rowBounds.getWidth();
        double height = rowBounds.getHeight();

        return new BoundingBox(minX, minY, width, height);
    } */

    private void initializeAccounts() {
        transactionTable.getColumns().clear();
        AccountService accountService = new AccountService();
        List<Account> accounts = accountService.getAllAccounts(idWallet);


        // Clear existing items (optional, depending on your use case)
        MenuOfAccounts.getItems().clear();

        // Create MenuItems based on accounts and add them to the MenuButton
        for (Account account : accounts) {
            MenuItem accountItem = new MenuItem(account.getNameAccount());

            accountItem.setOnAction(event -> {
                updateCurrencySymbol(1);

                selectedAccountId = account.getIdAccount();
                System.out.println("selected ID" +selectedAccountId);
                switchAccount(
                    account.getIdAccount(),
                    account.getNameAccount(),
                    account.getTypeAccount().toString(),
                         decimalFormat.format(account.getBalance()) + currencySymbol ,
                    account.getDescription(),
                    account.getIdWallet()
                       // decimalFormat.format(account.getBalance()) + currencySymbol

                );

                    // Update the transactionTable with data for the selected account
                    updateTransactionTable(account.getIdAccount());
                    updateBalance();


             });
            MenuOfAccounts.getItems().add(accountItem);
        }
    }


    private void updateTransactionTable(int accountId) {
        System.out.println("Updating transaction table for account ID: " + accountId);

        TransactionService transactionService = new TransactionService();
        List<TransactionDetails> transactionsDetails = transactionService.getTransactionsDetailsForAccount(accountId);

        // Debugging: Print the number of columns in the TableView
        System.out.println("Number of Columns: " + transactionTable.getColumns().size());
        // Clear existing items in the table (optional, depending on your use case)
        transactionData.clear();





        // Add transactions to the table
        for (TransactionDetails transactionDetail : transactionsDetails) {
            transactionData.add(new TransactionTableRow(
                    transactionDetail.getIdTransaction(),
                    transactionDetail.getDate(),
                    transactionDetail.getType().toString(),
                    transactionDetail.getDescription(),
                    transactionDetail.getAmount(),
                    transactionDetail.getFromAccountName(),
                    transactionDetail.getToAccountName(),
                    transactionDetail.getCategoryName(),
                    transactionDetail.getPayeeName()
                    // Add other data fields as needed
            ));
        }

        transactionTable.refresh();
    }
    private void switchAccount(int id,String nameAccount, String typeAccount, String balance,String Description,int idWallet) {
        showAlert("Switched to " + nameAccount + " account.");
        MenuOfAccounts.setText(nameAccount);
        balanceOfAccount.setText(balance);
        typeOfAccount.setText(typeAccount);
        Image imageURL = setTypeImage(typeAccount);
        iconOfAccount.setImage(imageURL);
        System.out.println("testing switch");
        searchBar.clear();

        // Update the transactionTable with data for the selected account
        updateTransactionTable(id);

        // Clear and reapply the filter based on the new data
        searchBar.clear();
        filterData(searchBar.getText());
    }

    private Image setTypeImage(String typeAccount) {
        String imagePath;

        switch (typeAccount) {
            case "CASH":
                imagePath = "/icons/money.png";
                break;
            case "SAVINGS":
                imagePath = "/icons/save-money.png";
                break;
            case "CREDIT CARD":
                imagePath = "/icons/credit-card_blue.png";
                break;
            // Add more cases for other account types as needed
            default:
                imagePath = "/icons/money.png"; // Default icon if type is not recognized
                break;
        }

        return new Image(getClass().getResourceAsStream(imagePath));
    }

    private void selectOverview() {
        accounts.getStyleClass().add("button-selected");
    }



    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void initializeColumns() {


        TableColumn<TransactionTableRow, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(120.0);

        TableColumn<TransactionTableRow, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(75);

        TableColumn<TransactionTableRow, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(170);



        TableColumn<TransactionTableRow, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setPrefWidth(100);

        TableColumn<TransactionTableRow, String> fromAccountColumn = new TableColumn<>("FromAccount");
        fromAccountColumn.setCellValueFactory(new PropertyValueFactory<>("fromAccountName"));
        fromAccountColumn.setPrefWidth(120);

        TableColumn<TransactionTableRow, String> toAccountColumn = new TableColumn<>("ToAccount");
        toAccountColumn.setCellValueFactory(new PropertyValueFactory<>("toAccountName"));
        toAccountColumn.setPrefWidth(120);


        TableColumn<TransactionTableRow, String> categoryNameColumn = new TableColumn<>("Category");
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryNameColumn.setPrefWidth(120);

        TableColumn<TransactionTableRow, String> payeeNameColumn = new TableColumn<>("Payee");
        payeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("payeeName"));
        payeeNameColumn.setPrefWidth(120);


        transactionTable.getColumns().addAll(dateColumn, typeColumn,descriptionColumn,amountColumn,fromAccountColumn,toAccountColumn,categoryNameColumn,payeeNameColumn);


        transactionTable.setItems(transactionData);

    }


    private void filterData(String filterText) {
        filteredData.setPredicate(transaction -> {
            // If filter text is empty, display all items.
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            // Compare each column value to the filter text.
            String lowerCaseFilter = filterText.toLowerCase();

            // Customize this part based on your column types
            if (transaction.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches date column.
            } else if (transaction.getType().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches type column.
            } else if (transaction.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches description column.
            }  else if (transaction.getAmount().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }  else if (transaction.getFromAccountName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }  else if (transaction.getToAccountName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }  else if (transaction.getCategoryName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }  else if (transaction.getPayeeName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return false; // Does not match


        });
    }


    private void initializeFiltering() {
        filteredData = new FilteredList<>(transactionData, p -> true);

        // Bind the filter predicate to the textProperty of the search bar
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });

        // Wrap the filtered list in a SortedList.
        SortedList<TransactionTableRow> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(transactionTable.comparatorProperty());

        // Add the sorted (and filtered) data to the table.
        transactionTable.setItems(sortedData);
    }


    private TransactionType showTransactionTypeDialog(int accountId) {
        List<String> choices = Arrays.asList("INCOME", "TRANSFER", "EXPENSE");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Select Transaction Type");
        dialog.setHeaderText("Choose the type of transaction:");
        dialog.setContentText("Transaction Type:");

        Optional<String> result = dialog.showAndWait();
        return result.map(TransactionType::valueOf).orElse(null);
    }

    public void addTransaction(ActionEvent actionEvent) {
        TransactionType transactionType = showTransactionTypeDialog(selectedAccountId);

        if (transactionType != null) {
            // Handle the selected transaction type
            switch (transactionType) {
                case INCOME:
                    // Handle income transaction
                    showAddIncomeTransactionDialog(selectedAccountId);
                    System.out.println("selected ID when pressing" +selectedAccountId);
                    break;
                case TRANSFER:
                    showAddTransferTransactionDialog(selectedAccountId);
                    System.out.println("selected ID when pressing" +selectedAccountId);
                    // showAddTransferTransactionDialog();
                    break;
                case EXPENSE:
                    showAddExpenseTransactionDialog(selectedAccountId);
                    System.out.println("selected ID when pressing" +selectedAccountId);
                    break;
            }
        }
    }

    private void showAddIncomeTransactionDialog(int idAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddIncomeTransactionDialog.fxml"));

            // Load the parent root
            Parent root = loader.load();

            // Retrieve the controller
            AddIncomeTransactionDialogController controller = loader.getController();

            // Set the account id
            controller.setAccountId(idAccount);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Income Transaction");
            stage.setScene(new Scene(root));

            // Show the dialog and wait for it to be closed
            stage.showAndWait();

            // You can handle results or actions after the dialog is closed
            // For example, refresh the transaction table

            updateTransactionTable( selectedAccountId);
            // Update the balance
            updateBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showAddTransferTransactionDialog(int idAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddTransferTransactionDialog.fxml"));

            // Load the parent root
            Parent root = loader.load();

            // Retrieve the controller
            AddTransferTransactionDialogController controller = loader.getController();

            // Set the account id
            controller.setAccountId(idAccount);
            controller.updateComboBoxItems();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Expense Transaction");
            stage.setScene(new Scene(root));

            // Show the dialog and wait for it to be closed
            stage.showAndWait();

            // You can handle results or actions after the dialog is closed
            // For example, refresh the transaction table

            updateTransactionTable( selectedAccountId);
            // Update the balance
            updateBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showAddExpenseTransactionDialog(int idAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddExpenseTransactionDialog.fxml"));

            // Load the parent root
            Parent root = loader.load();

            // Retrieve the controller
            AddExpenseTransactionDialogController controller = loader.getController();

            // Set the account id
            controller.setAccountId(idAccount);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Transfer Transaction");
            stage.setScene(new Scene(root));

            // Show the dialog and wait for it to be closed
            stage.showAndWait();

            // You can handle results or actions after the dialog is closed
            // For example, refresh the transaction table

            updateTransactionTable( selectedAccountId);
            // Update the balance
            updateBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void updateBalance() {
        AccountService accountService = new AccountService();
        updateCurrencySymbol(1);
        String updatedBalance = decimalFormat.format(accountService.getAccountBalance(selectedAccountId)) +  currencySymbol ;
        balanceOfAccount.setText(updatedBalance);
    }


    public void openOverviewPage(ActionEvent actionEvent) {

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


    private void showPopupForSelectedTransaction() {
        int selectedIndex = transactionTable.getSelectionModel().getSelectedIndex();
        double x = tableContainer.getLayoutX();
        double y = tableContainer.getLayoutY() + transactionTable.getLayoutY() + selectedIndex * 30;

        // Call the showPopup method from the TransactionDetailsPopupController
        popupController.showPopup(x, y);
    }
    public void refreshTable() {
        System.out.println("Refreshing the transaction table in Dashboard_Accounts");
        this.transactionTable.refresh();
    }


    public void handleTransactionDeletedEvent(Event event) {
        // Handle the event, e.g., refresh the table or perform other actions
        System.out.println("Transaction deleted event received in Dashboard_Accounts");
        refreshTable();
    }

    public void allAccountsPage(ActionEvent actionEvent) {
        try {
            AccountService accountService =new AccountService();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AllAccounts.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) overview.getScene().getWindow(); // Access the current stage
            stage.setScene(new Scene(root));

            // Get the controller and manually call initialize
            AccountsController controller = loader.getController();
            controller.setIdWallet(this.idWallet);
            controller.initialize();


        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }


    }

    public void goToSettings(ActionEvent actionEvent) {
        switchScene("/views/Settings.fxml");
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


    public void exportToCsv(ActionEvent actionEvent) {

        ObservableList<TransactionTableRow> transactions = transactionTable.getItems();
        if (!transactions.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                String filePath = file.getAbsolutePath();
                CSVExporter.exportToCSV(transactions, filePath);
            }
        }
    }

    public void openReportsPage(ActionEvent actionEvent) {
        switchScene("/views/Reports.fxml");
    }
}





