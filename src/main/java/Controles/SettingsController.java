package Controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Services.CurrencyService;
import Services.WalletService;

import java.io.IOException;
import java.util.List;

public class SettingsController {

    public AnchorPane exchangerateContainer;
    public Text exchangerateTitle;
    public AnchorPane walletSettingsContainer;
    public ComboBox<String> walletCurrencyCombox;
    public TextField newNameText;
    public Text WalletSettingsTitle;
    @FXML
    private AnchorPane MenuDashboardAnchor;

    @FXML
    private Button accounts;

    @FXML
    private Button budget;

    @FXML
    private Button debts;

    @FXML
    private Text exchangeRateTxt;

    @FXML
    private ComboBox<String> fromCurrencyCB;

    @FXML
    private ImageView fromCurrencyCountry;

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
    private ComboBox<String> toCurrencyCB;

    @FXML
    private ImageView toCurrencyCountry;

    @FXML
    private Button todo;

    @FXML
    private Button wishlist;

    @FXML
    void openAccountsPage(ActionEvent event) {
        switchScene("/views/Dashboard_Accounts");

    }
    @FXML
  public void initialize() {
        selectSettings();
        exchangerateContainer.getStyleClass().add("exchangerate-container");
        exchangerateTitle.getStyleClass().add("exchangerate-title");
        populateComboBox();
        setupComboBoxListeners();
        setupTextFieldListener();

        setupWalletCurrencyListener();



    }

    private void populateComboBox() {

        List<String> currencies = CurrencyService.getPredefinedCurrencyCodes();
        fromCurrencyCB.getItems().addAll(currencies);
        toCurrencyCB.getItems().addAll(currencies);
        walletCurrencyCombox.getItems().addAll(currencies);
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

    public void goToOverview(ActionEvent actionEvent) {
        switchScene("views/dashboard.fxml");
    }


    private void setupComboBoxListeners() {
        fromCurrencyCB.setOnAction(event -> updateExchangeRate());
        toCurrencyCB.setOnAction(event -> updateExchangeRate());
    }
    private void updateExchangeRate() {
        CurrencyService currencyService = new CurrencyService();
        String fromCurrency = fromCurrencyCB.getValue();
        String toCurrency = toCurrencyCB.getValue();

        if (fromCurrency != null && toCurrency != null && !fromCurrency.equals(toCurrency)) {
            double exchangeRate = currencyService.getExchangeRate(fromCurrency, toCurrency);
            exchangeRateTxt.setText("Exchange Rate: 1 " + fromCurrency + " = " + exchangeRate + " " + toCurrency);
            setCurrencyIcon(fromCurrency, fromCurrencyCountry);
            setCurrencyIcon(toCurrency, toCurrencyCountry);

        } else {
            exchangeRateTxt.setText(""); // Clear the text if either ComboBox is not selected or both are the same
            fromCurrencyCountry.setImage(null);
            toCurrencyCountry.setImage(null);
        }
    }

    private void setCurrencyIcon(String currencyCode, ImageView iconImageView) {
        // Assuming you have an image file named usd_icon.png in the resources directory
        String iconPath = "/icons/" + currencyCode.toLowerCase() + "_icon.png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        iconImageView.setImage(icon);
    }


    private void selectSettings() {
        settings.getStyleClass().add("button-selected");
    }
    private void setupTextFieldListener() {
        newNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the text change, for example, call your changeWalletName method
            WalletService walletService = new WalletService();
            walletService.updateWalletName(1,newValue);


        });
    }


    private void setupWalletCurrencyListener() {
        walletCurrencyCombox.setOnAction(event -> updateWalletCurrency());
    }

    private void updateWalletCurrency() {
        String selectedCurrency = walletCurrencyCombox.getValue();
        if (selectedCurrency != null) {
            WalletService walletService = new WalletService();
            CurrencyService currencyService = new CurrencyService();
            walletService.updateWalletCurrencyAndBalance(1,selectedCurrency,currencyService);
        }
    }


    public void openReportsPage(ActionEvent actionEvent) {
        switchScene("/views/Reports.fxml");
    }
}
