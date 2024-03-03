package Controles;

import com.sun.javafx.charts.Legend;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import Entities.Transaction;
import Services.FinancialAnalysisService;
import Services.TransactionService;
import Services.WalletService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class ReportsController {

    public AnchorPane incomevexpenseContainer;
    public AnchorPane overviewStatContainer;
    public VBox statsVboxContainer;
    public Text secondChartTitle;
    private  String currencySymbol ="";
    public Text titleChart1;
    @FXML
    private AnchorPane MenuDashboardAnchor;

    @FXML
    private Button accounts;

    @FXML
    private Button budget;

    @FXML
    private Button debts;

    @FXML
    private VBox mainVBox;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

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
    private Text totalIncomeText;

    @FXML
    private Text totalExpensesText;

    @FXML
    private Text surplusDeficitText;
    @FXML
    private PieChart incomeExpensePieChart;

    @FXML
    private LineChart<String, Number> combinedBalanceChart;

    TransactionService transactionService = new TransactionService();
    WalletService walletService = new WalletService();



    private double totalIncome;
    private double totalExpenses;

    private FinancialAnalysisService analysisService = new FinancialAnalysisService();

    // Assuming you have a list of transactions
    private List<Transaction> transactions;

    public void initialize() {
        selectReports();
        combinedBalanceChart.setAnimated(false);
        updateCurrencySymbol(1);

        // Create a pause transition with a 3-second delay
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            updateFinancialSummary();

            updateChart();
            updateCombinedBalanceChart();
            System.out.println(currencySymbol);
        });

        // Start the pause transition
        pause.play();

        // Add smooth animation (optional)
        addSmoothAnimation();
    }

    private void addSmoothAnimation() {
        // Assuming combinedBalanceChart is your LineChart
        TranslateTransition smoothAnimation = new TranslateTransition(Duration.seconds(1), combinedBalanceChart);
        smoothAnimation.setFromY(100); // Initial Y position
        smoothAnimation.setToY(0);     // Final Y position (you can adjust this value)


         smoothAnimation.play();
    }



    private void selectReports() {
        reports.getStyleClass().add("button-selected");
    }

    private void updateFinancialSummary() {
        transactions=transactionService.getAllTransactions();
         totalIncome = analysisService.calculateTotalIncome(transactions);
         totalExpenses = analysisService.calculateTotalExpenses(transactions);

        totalIncomeText.setText("Total Income: " + decimalFormat.format(totalIncome)+ currencySymbol );
        totalExpensesText.setText("Total Expenses: "  + decimalFormat.format(totalExpenses) +currencySymbol );

        double surplusDeficit = analysisService.calculateSurplusOrDeficit(totalIncome, totalExpenses);
        String surplusDeficitString = (surplusDeficit >= 0) ? "Surplus: "+ decimalFormat.format(surplusDeficit) + currencySymbol : "Deficit: "+ Math.abs(surplusDeficit) +currencySymbol ;
        surplusDeficitText.setText(surplusDeficitString);
    }
    private void updateChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Income", totalIncome),
                new PieChart.Data("Expenses", totalExpenses)
        );

        incomeExpensePieChart.setData(pieChartData);
       setPieChartSliceColor(incomeExpensePieChart.getData().get(0), "green"); // Income
        setPieChartSliceColor(incomeExpensePieChart.getData().get(1), "red");   // Expenses

    }
    private void customizeLegendColors() {
        Legend legend = (Legend) incomeExpensePieChart.lookup(".chart-legend");
        if (legend != null) {
            ObservableList<Legend.LegendItem> legendItems = legend.getItems();
            if (legendItems.size() >= 2) {
                // Set color for Income legend item
                legendItems.get(0).getSymbol().setStyle("-fx-background-color: green;");

                // Set color for Expenses legend item
                legendItems.get(1).getSymbol().setStyle("-fx-background-color: red;");
            }
        }
    }
    private void setPieChartSliceColor(PieChart.Data slice, String color) {
        slice.getNode().setStyle("-fx-pie-color: " + color + ";");
    }
    @FXML
    void goToOverview(ActionEvent event) {

    }

    @FXML
    void openAccountsPage(ActionEvent event) {
        switchScene("/views/Dashboard_Account.fxml");
    }

    @FXML
    void openSettingsPage(ActionEvent event) {
       switchScene("/views/Dashboard_account.fxml");
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


    private void updateCombinedBalanceChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Transaction> transactions = transactionService.getAllTransactions(); // Assuming you have a method to get all transactions
        Map<LocalDate, Double> combinedBalanceMap = analysisService.calculateCombinedBalanceOverTime(transactions);

        // Sort dates by LocalDate
        List<LocalDate> dates = combinedBalanceMap.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (LocalDate date : dates) {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"));
            series.getData().add(new XYChart.Data<>(formattedDate, combinedBalanceMap.get(date)));
        }

        combinedBalanceChart.getData().add(series);
        setupXAxis();
    }


    private void setupXAxis() {
        CategoryAxis xAxis = (CategoryAxis) combinedBalanceChart.getXAxis();
        xAxis.setLabel("Date");
        // Rotate x-axis labels for better visibility
        xAxis.setTickLabelRotation(45);
        xAxis.setStartMargin(0.0);
        xAxis.setEndMargin(0.0);

    }








}
