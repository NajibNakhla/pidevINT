package Services;

import javafx.scene.control.Alert;
import Entities.Transaction;
import Entities.TransactionDetails;
import enums.TransactionType;
import Interfaces.ITransaction;
import Tools.MyConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService implements ITransaction<Transaction> {

    @Override
    public void addTransactionService(Transaction transaction) {
        switch (transaction.getType()) {
            case INCOME:
                addIncomeTransaction(transaction);
                break;

            case EXPENSE:
                addExpenseTransaction(transaction);
                break;

            case TRANSFER:
                addTransferTransaction(transaction);
                break;

            default:
                // Handle unsupported transaction types or throw an exception
                throw new IllegalArgumentException("Unsupported transaction type: " + transaction.getType());
        }


    }


    private void addExpenseTransaction(Transaction transaction) {
        WalletService walletService = new WalletService();
        AccountService accountService = new AccountService();
        double fromAccountBalance = accountService.getAccountBalance(transaction.getFromAccount());
        if (fromAccountBalance >= transaction.getAmount()) {
            // Add the expense transaction to the database
            addTransactionToDatabase(transaction);

            // Update the account balance for the expense
            updateAccountBalance(transaction.getFromAccount(), accountService.getAccountBalance(transaction.getFromAccount()) - transaction.getAmount());

            // Update the wallet's total balance
            int idWallet = accountService.getWalletIdForAccount(transaction.getFromAccount());
            double newTotalBalanceExpense = walletService.getTotalBalanceData(idWallet);
            walletService.updateTotalBalance(newTotalBalanceExpense - transaction.getAmount(),idWallet);
            updateMtDepense(transaction.getIdCategory(), transaction.getAmount());

        } else {
            showInsufficientFundsAlert();
            System.out.println("Insufficient funds for the expense transaction");

        }
    }


    public double getAccountBalance(int accountId) {
        double balance = 0.0;
        String query = "SELECT balance FROM account WHERE idAccount = ?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, accountId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return balance;
    }

    public void updateAccountBalance(int accountId, double newBalance) {
        String query = "UPDATE account SET balance = ? WHERE idAccount = ?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setDouble(1, newBalance);
            pst.setInt(2, accountId);

            pst.executeUpdate();
            System.out.println("Account balance updated successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIncomeTransaction(Transaction transaction) {
        // Assuming you have a method to get the current balance of the "to" account
        double toAccountBalance = getAccountBalance(transaction.getToAccount());

        // Update the balance of the "to" account
        double newToAccountBalance = toAccountBalance + transaction.getAmount();
        updateAccountBalance(transaction.getToAccount(), newToAccountBalance);

        updateWalletTotalBalance(transaction.getAmount(), transaction.getToAccount());

        // Insert the income transaction into the database
        addTransactionToDatabase(transaction);
    }


    private void addTransferTransaction(Transaction transaction) {
        // Logic for adding a transfer transaction

        // Update the account balances
        double fromAccountBalance = getAccountBalance(transaction.getFromAccount());
        double toAccountBalance = getAccountBalance(transaction.getToAccount());

        // Check if there are sufficient funds in the fromAccount
        if (fromAccountBalance >= transaction.getAmount()) {
            // Subtract the amount from the fromAccount
            updateAccountBalance(transaction.getFromAccount(), fromAccountBalance - transaction.getAmount());

            // Add the amount to the toAccount
            updateAccountBalance(transaction.getToAccount(), toAccountBalance + transaction.getAmount());
            // update the wallet TotalBalance


            // Add the transfer transaction to the database
            addTransactionToDatabase(transaction);
        } else {
            showInsufficientFundsAlert();
            System.out.println("Insufficient funds for the transfer");
            // Handle insufficient funds scenario (throw an exception, display a message, etc.)
        }
    }

    private void showInsufficientFundsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Insufficient Funds");
        alert.setHeaderText(null);
        alert.setContentText("Insufficient funds for the transaction.");

        alert.showAndWait();
    }

    // Placeholder method for adding the transaction to the database
    public void addTransactionToDatabase(Transaction transaction) {
        String query = "INSERT INTO transaction (date, type, description, amount, fromAccount, toAccount, idCategory, idPayee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setDate(1, Date.valueOf(transaction.getDate()));
            pst.setString(2, String.valueOf(transaction.getType()));
            pst.setString(3, transaction.getDescription());
            pst.setDouble(4, transaction.getAmount());
            pst.setInt(5, transaction.getFromAccount());
            pst.setInt(6, transaction.getToAccount());
            pst.setInt(7, transaction.getIdCategory());
            pst.setInt(8, transaction.getIdPayee());

            pst.executeUpdate();
            System.out.println(" transaction added successfully to database");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateWalletTotalBalance(double incomeAmount, int accountId) {
        // Retrieve the current totalBalance from the Wallet

        WalletService walletService = new WalletService();
        AccountService accountService = new AccountService();
        int idWallet = accountService.getWalletIdForAccount(accountId);
        double currentTotalBalance = walletService.getTotalBalanceData(idWallet); // You need to implement this method

        // Calculate the new totalBalance after adding the income
        double newTotalBalance = currentTotalBalance + incomeAmount;



        // Update the totalBalance in the Wallet
        walletService.updateTotalBalance(newTotalBalance,idWallet); // You need to implement this method
    }


    public void updateEntity() {

    }

    @Override
    public void deleteEntity(Transaction transaction) {

    }

    @Override
    public void deleteEntityByID(int id) {

    }

    @Override
    public List<Transaction> getAllData() {
        return null;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transaction";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);


            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                TransactionType transactionType = TransactionType.valueOf(resultSet.getString("type"));
                // Assuming you have a constructor in your Transaction class
                Transaction transaction = new Transaction(
                        resultSet.getInt("idTransaction"),
                        resultSet.getDate("date").toLocalDate(),
                        transactionType,
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("fromAccount"),
                        resultSet.getInt("toAccount"),
                        resultSet.getInt("idCategory"),
                        resultSet.getInt("idPayee")

                );
                transactions.add(transaction);
            }


        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return transactions;
    }


    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            // Assuming you have a transactions table with columns like id, date, type, description, amount, fromAccount, toAccount, etc.
            String query = "SELECT * FROM transaction WHERE fromAccount = ? OR toAccount = ?";
            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
                pst.setInt(1, accountId);
                pst.setInt(2, accountId);

                try (ResultSet resultSet = pst.executeQuery()) {
                    while (resultSet.next()) {
                        TransactionType transactionType = TransactionType.valueOf(resultSet.getString("type"));
                        // Assuming you have a constructor in your Transaction class
                        Transaction transaction = new Transaction(
                                resultSet.getInt("idTransaction"),
                                resultSet.getDate("date").toLocalDate(),
                                transactionType,
                                resultSet.getString("description"),
                                resultSet.getDouble("amount"),
                                resultSet.getInt("fromAccount"),
                                resultSet.getInt("toAccount")
                                // Add more attributes as needed
                        );
                        transactions.add(transaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return transactions;
    }


    //added this method so the user can see the details of the transaction by getting all the data thanks to the foreign keys
    public List<TransactionDetails> getTransactionsDetailsForAccount(int accountId) {
        // Your join SQL query to fetch transactions with details
        String joinQuery = "SELECT t.*, aFrom.nameAccount AS fromAccountName, aTo.nameAccount AS toAccountName, " +
                "c.name AS categoryName, p.namePayee AS payeeName " +
                "FROM transaction t " +
                "LEFT JOIN account aFrom ON t.fromAccount = aFrom.idAccount " +
                "LEFT JOIN account aTo ON t.toAccount = aTo.idAccount " +
                "LEFT JOIN subcategory c ON t.idCategory = c.idSubCategory " +
                "LEFT JOIN payee p ON t.idPayee = p.idPayee " +
                "WHERE t.fromAccount = ? OR t.toAccount = ?";

        List<TransactionDetails> transactionDetailsList = new ArrayList<>();

        try (PreparedStatement preparedStatement = MyConnection.getInstance().getCnx().prepareStatement(joinQuery)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TransactionDetails details = new TransactionDetails();
                details.setIdTransaction(resultSet.getInt("idTransaction"));
                details.setDate(resultSet.getDate("date").toLocalDate());
                details.setType(TransactionType.valueOf(resultSet.getString("type")));
                details.setDescription(resultSet.getString("description"));
                details.setAmount(resultSet.getDouble("amount"));
                details.setFromAccount(resultSet.getInt("fromAccount"));
                details.setToAccount(resultSet.getInt("toAccount"));
                details.setIdCategory(resultSet.getInt("idCategory"));
                details.setIdPayee(resultSet.getInt("idPayee"));
                details.setFromAccountName(resultSet.getString("fromAccountName"));
                details.setToAccountName(resultSet.getString("toAccountName"));
                details.setCategoryName(resultSet.getString("categoryName"));
                details.setPayeeName(resultSet.getString("payeeName"));

                transactionDetailsList.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return transactionDetailsList;
    }

    public void deleteTransactionById(int id) {
        Transaction transaction = getTransactionById(id);

        if (transaction != null) {
            switch (transaction.getType()) {
                case INCOME:
                    handleIncomeTransactionDeletion(transaction);
                    break;
                case TRANSFER:
                    handleTransferTransactionDeletion(transaction);
                    break;
                // Add cases for other transaction types if needed

                default:
                    // No special handling needed for other transaction types
                    break;
            }

            // Perform common actions for all transaction types
            deleteTransactionFromDatabase(id);
        } else {
            System.out.println("Transaction not found.");
        }
    }

    public Transaction getTransactionById(int id) {
        String query = "SELECT * FROM transaction WHERE idTransaction = ?";
        Transaction transaction = null;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    // Assuming you have a constructor for Transaction
                    transaction = new Transaction(
                            resultSet.getInt("idTransaction"),
                            resultSet.getDate("date").toLocalDate(),
                            TransactionType.valueOf(resultSet.getString("type")),
                            resultSet.getString("description"),
                            resultSet.getDouble("amount"),
                            resultSet.getInt("fromAccount"),
                            resultSet.getInt("toAccount"),
                            resultSet.getInt("idCategory"),
                            resultSet.getInt("idPayee")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return transaction;
    }

    private void handleIncomeTransactionDeletion(Transaction transaction) {
        // Fetch the current balance of the fromAccount before the deletion
        double fromAccountBalance = getAccountBalance(transaction.getFromAccount());

        // Update the fromAccount balance by subtracting the amount of the deleted income
        double newFromAccountBalance = fromAccountBalance - transaction.getAmount();
        updateAccountBalance(transaction.getFromAccount(), newFromAccountBalance);

        WalletService walletService = new WalletService();
        AccountService accountService = new AccountService();
        int idWallet = accountService.getWalletIdForAccount(transaction.getFromAccount());
        // Optionally, update the wallet total balance if needed
        double walletTotalBalance = walletService.getTotalBalanceData(idWallet);
        double newWalletTotalBalance = walletTotalBalance - transaction.getAmount();

        // Debugging statements
        System.out.println("Wallet Total Balance Before: " + walletTotalBalance);
        System.out.println("Transaction Amount: " + transaction.getAmount());
        System.out.println("New Wallet Total Balance: " + newWalletTotalBalance);

        walletService.updateTotalBalance(newWalletTotalBalance,idWallet);

        // Delete the transaction from the database
        deleteTransactionFromDatabase(transaction.getId());

        System.out.println("Income transaction deleted successfully");
    }

    private void handleTransferTransactionDeletion(Transaction transaction) {
        double fromAccountBalance = getAccountBalance(transaction.getFromAccount());
        double toAccountBalance = getAccountBalance(transaction.getToAccount());

        // Update the fromAccount balance by adding back the transferred amount
        double newFromAccountBalance = fromAccountBalance + transaction.getAmount();
        updateAccountBalance(transaction.getFromAccount(), newFromAccountBalance);

        // Update the toAccount balance by subtracting the received amount
        double newToAccountBalance = toAccountBalance - transaction.getAmount();
        updateAccountBalance(transaction.getToAccount(), newToAccountBalance);

        // Delete the transaction from the database
        deleteTransactionFromDatabase(transaction.getId());

        System.out.println("Transfer transaction deleted successfully");
    }

    private void deleteTransactionFromDatabase(int id) {
        String deleteQuery = "DELETE FROM transaction WHERE idTransaction = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(deleteQuery)) {
            pst.setInt(1, id);
            pst.executeUpdate();

            System.out.println("Transaction deleted from the database.");
        } catch (SQLException e) {
            System.out.println("Error deleting transaction from the database: " + e.getMessage());
        }

    }


    public int countTransactions() {
        int transactionCount = 0;
        String query = "SELECT COUNT(*) AS transactionCount FROM transaction";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                transactionCount = resultSet.getInt("transactionCount");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return transactionCount;
    }


    public double getMtDepense(int subCatID) {
        // Implement logic to get the current value of mtDépensé for the given category ID
        // You can use your database connection and SQL query for this
        double currentMtDepense = 0.0; // Replace with actual logic

        // Example query (adjust based on your actual schema)
        String query = "SELECT mtDépensé FROM SubCategory WHERE idSubCategory = ?";

        try {
        PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, subCatID);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    currentMtDepense = resultSet.getDouble("mtDépensé");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return currentMtDepense;
    }

    public void updateMtDepense(int categoryId, double expenseAmount) {
        // Get the current value of mtDépensé
        double currentMtDepense = getMtDepense(categoryId);

        // Update mtDépensé by adding the expense amount
        double newMtDepense = currentMtDepense + expenseAmount;

        // Execute an SQL query to update mtDépensé in the database
        String updateQuery = "UPDATE SubCategory SET mtDépensé = ? WHERE idSubCategory = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery)) {
            pst.setDouble(1, newMtDepense);
            pst.setInt(2, categoryId);

            pst.executeUpdate();

            System.out.println("mtDépensé updated successfully for category ID: " + categoryId);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }


}



/* public List<Transaction> getTransactionsByType(String type) {
        // Implement logic to filter transactions by type
        return transactions.stream()
                .filter(transaction -> type.equalsIgnoreCase(transaction.getType()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        // Implement logic to filter transactions by category
        return transactions.stream()
                .filter(transaction -> category.equalsIgnoreCase(transaction.getCategory()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByPayee(String payee) {
        // Implement logic to filter transactions by payee
        return transactions.stream()
                .filter(transaction -> payee.equalsIgnoreCase(transaction.getPayee()))
                .collect(Collectors.toList());
    }


 */

