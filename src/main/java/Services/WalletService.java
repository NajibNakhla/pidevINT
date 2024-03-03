package Services;

import javafx.scene.control.Alert;
import Entities.Wallet;
import Interfaces.IWallet;

import java.sql.PreparedStatement;

import Tools.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletService implements IWallet<Wallet> {
    @Override
    public double getTotalBalanceData(int id) {
        String query = "SELECT totalbalance FROM wallet WHERE idWallet = ?";
        double totalBalance = 0;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    totalBalance = rs.getDouble("totalbalance");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalBalance;
    }

    public void updateTotalBalance(double newTotalBalance) {
        String updateQuery = "UPDATE wallet SET totalBalance = ? WHERE idWallet = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery)) {
            pst.setDouble(1, newTotalBalance);
            pst.setInt(2, 1);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Total balance updated successfully.");
            } else {
                System.out.println("Failed to update total balance.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating total balance: " + e.getMessage());
        }
    }

    public void updateWalletName(int walletId, String newName) {
        String updateWalletNameQuery = "UPDATE wallet SET name = ? WHERE idWallet = ?";

        try {

            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateWalletNameStatement = connection.prepareStatement(updateWalletNameQuery) ;

            updateWalletNameStatement.setString(1, newName);
            updateWalletNameStatement.setInt(2, walletId);

            updateWalletNameStatement.executeUpdate();

            System.out.println("Wallet name updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }


    public void updateWalletCurrencyAndBalance(int walletId, String newCurrency, CurrencyService currencyService) {
        String getOldCurrencyAndBalanceQuery = "SELECT currency, totalBalance FROM wallet WHERE idWallet = ?";
        String updateWalletCurrencyAndBalanceQuery = "UPDATE wallet SET currency = ?, totalBalance = ? WHERE idWallet = ?";

        try  {
             Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement getOldCurrencyAndBalanceStatement = connection.prepareStatement(getOldCurrencyAndBalanceQuery);
             PreparedStatement updateWalletCurrencyAndBalanceStatement = connection.prepareStatement(updateWalletCurrencyAndBalanceQuery);

            // Get the old currency and total balance of the wallet
            getOldCurrencyAndBalanceStatement.setInt(1, walletId);
            ResultSet resultSet = getOldCurrencyAndBalanceStatement.executeQuery();
            String oldCurrency = "";
            double oldTotalBalance = 0.0;

            if (resultSet.next()) {
                oldCurrency = resultSet.getString("currency");
                oldTotalBalance = resultSet.getDouble("totalBalance");
            }

            // Get the exchange rate between old currency and new currency
            double exchangeRate = currencyService.getExchangeRate(oldCurrency, newCurrency);

            // Update the wallet currency and balance
            updateWalletCurrencyAndBalanceStatement.setString(1, newCurrency);
            updateWalletCurrencyAndBalanceStatement.setDouble(2, oldTotalBalance * exchangeRate);
            updateWalletCurrencyAndBalanceStatement.setInt(3, walletId);

            updateWalletCurrencyAndBalanceStatement.executeUpdate();
            updateTransactionAndAccountTables(walletId, newCurrency, exchangeRate);
            updateDebtTable(walletId, newCurrency, exchangeRate);
            // Call this after updating wallet currency and balance
            updateWishlistTable(walletId, newCurrency, exchangeRate);
            updateWishlistItemTable(walletId, newCurrency, exchangeRate);
            updateSubcategoryTable(walletId, newCurrency, exchangeRate);



            System.out.println("Wallet currency and balance updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }
    private boolean updateTransactionAndAccountTables(int walletId, String newCurrency, double exchangeRate) {
        String updateAccountBalanceQuery = "UPDATE account SET balance = balance * ? WHERE idWallet = ?";
        String updateTransactionAmountQuery =
                "UPDATE transaction t " +
                        "JOIN account a ON t.fromAccount = a.idAccount OR t.toAccount = a.idAccount " +
                        "SET t.amount = t.amount * ? " +
                        "WHERE a.idWallet = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement updateAccountBalanceStatement = connection.prepareStatement(updateAccountBalanceQuery);
             PreparedStatement updateTransactionAmountStatement = connection.prepareStatement(updateTransactionAmountQuery);

            // Update account balances based on the new currency and exchange rate
            updateAccountBalanceStatement.setDouble(1, exchangeRate);
            updateAccountBalanceStatement.setInt(2, walletId);
            int accountUpdateCount = updateAccountBalanceStatement.executeUpdate();

            // Update transaction amounts based on the new currency and exchange rate
            updateTransactionAmountStatement.setDouble(1, exchangeRate);
            updateTransactionAmountStatement.setInt(2, walletId);
            int transactionUpdateCount = updateTransactionAmountStatement.executeUpdate();

            // Check if any update was successful
            if (accountUpdateCount > 0 || transactionUpdateCount > 0) {
                showSuccessAlert("Update successful!");
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return false;
        }
    }

    private void showSuccessAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public String getCurrency(int idWallet) {
        String query = "SELECT currency FROM wallet WHERE idWallet = ?";
        String currency = "";

        try{
            Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement pst = connection.prepareStatement(query);

            pst.setInt(1, idWallet);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    currency = rs.getString("currency");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currency;
    }

    public void updateTotalBalanceAfterUpdate(double amount, int idWallet) {
        try {
            String updateBalanceQuery = "UPDATE wallet SET totalBalance = totalBalance + ? WHERE idWallet = ?";
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateBalanceStatement = connection.prepareStatement(updateBalanceQuery);

            updateBalanceStatement.setDouble(1, amount);
            updateBalanceStatement.setInt(2, idWallet);

            updateBalanceStatement.executeUpdate();

            System.out.println("Wallet balance updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }



    private void updateDebtTable(int walletId, String newCurrency, double exchangeRate) {
        String updateDebtAmountQuery = "UPDATE debt SET amount = amount * ?, amountToPay = amountToPay * ? WHERE idWallet = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateDebtAmountStatement = connection.prepareStatement(updateDebtAmountQuery);

            // Update debt amounts based on the new currency and exchange rate
            updateDebtAmountStatement.setDouble(1, exchangeRate);
            updateDebtAmountStatement.setDouble(2, exchangeRate);
            updateDebtAmountStatement.setInt(3, walletId);

            updateDebtAmountStatement.executeUpdate();

            System.out.println("Debt table updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private void updateWishlistTable(int walletId, String newCurrency, double exchangeRate) {
        String updateWishlistBudgetQuery = "UPDATE wishlist SET monthlyBudget = monthlyBudget * ?, savedBudget = savedBudget * ? WHERE idWallet = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateWishlistBudgetStatement = connection.prepareStatement(updateWishlistBudgetQuery);

            // Update wishlist budgets based on the new currency and exchange rate
            updateWishlistBudgetStatement.setDouble(1, exchangeRate);
            updateWishlistBudgetStatement.setDouble(2, exchangeRate);
            updateWishlistBudgetStatement.setInt(3, walletId);

            updateWishlistBudgetStatement.executeUpdate();

            System.out.println("Wishlist table updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }


    private void updateWishlistItemTable(int walletId, String newCurrency, double exchangeRate) {
        String updateWishlistItemPriceQuery =
                "UPDATE wishlistitem wi  " +
                        "JOIN wishlist w ON wi.idWishlist = w.idWishlist  " +
                        "SET wi.price = wi.price * ? " +
                        "WHERE w.idWallet = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateWishlistItemPriceStatement = connection.prepareStatement(updateWishlistItemPriceQuery);

            // Update wishlistitem prices based on the new currency and exchange rate
            updateWishlistItemPriceStatement.setDouble(1, exchangeRate);
            updateWishlistItemPriceStatement.setInt(2, walletId);

            updateWishlistItemPriceStatement.executeUpdate();

            System.out.println("Wishlistitem table updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }


    private void updateSubcategoryTable(int walletId, String newCurrency, double exchangeRate) {
        String updateSubcategoryQuery = "UPDATE subcategory s " +
                "JOIN category c ON s.idCategory = c.idCategory " +
                "SET s.mTassigné = s.mTassigné * ?, s.mtDépensé = s.mtDépensé * ? " +
                "WHERE c.idWallet = ?";

        try {
            Connection connection = MyConnection.getInstance().getCnx();
            PreparedStatement updateSubcategoryStatement = connection.prepareStatement(updateSubcategoryQuery);

            // Update subcategory amounts based on the new currency and exchange rate
            updateSubcategoryStatement.setDouble(1, exchangeRate);
            updateSubcategoryStatement.setDouble(2, exchangeRate);
            updateSubcategoryStatement.setInt(3, walletId);

            updateSubcategoryStatement.executeUpdate();

            System.out.println("Subcategory table updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }









}

