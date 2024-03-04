package Services;

import Entities.Account;
import Entities.Transaction;
import enums.AccountType;
import enums.TransactionType;
import Interfaces.IAccount;
import Tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountService implements IAccount<Account> {
    private int walletDefaultID=1;
    // putting Wallet as the default one since were working on a main wallet with id =1 ;
    @Override
    public void addEntity(Account account) {
        String requete = "INSERT INTO account (nameAccount,typeAccount,balance,description,idWallet) VALUES (?,?,?,?,?)";
        String updateWalletQuery = "UPDATE wallet SET totalBalance = totalBalance + ? WHERE idWallet = 1";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            PreparedStatement pst2 = MyConnection.getInstance().getCnx().prepareStatement(updateWalletQuery);
            pst.setString(1, account.getNameAccount());
            pst.setString(2, String.valueOf(account.getTypeAccount()));
            pst.setString(3, String.valueOf(account.getBalance()));
            pst.setString(4, account.getDescription());
            pst.setString(5, String.valueOf(walletDefaultID));
            pst2.setDouble(1,account.getBalance());
            pst.executeUpdate();
            pst2.executeUpdate();


            System.out.println("Account added successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }
     // user can't update the whole account , he can only update its name , description , balance , because type must not be touched
     // also to update balance , user puts new balance but system must do the necessary transactions to fit the new balance
     // this service should be optimized after finishing the transactions services .
     @Override

     public void updateEntity(String newNameAcc, double newBalance, String newDescription, int id) {
         // Fetch the current balance of the account before the update
         double currentBalance = getAccountBalance(id);

         String updateQuery = "UPDATE account SET nameAccount = ?, balance = ?, description = ? WHERE idAccount = ?";
         try {
             PreparedStatement updateStatement = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
             updateStatement.setString(1, newNameAcc);
             updateStatement.setString(2, String.valueOf(newBalance));
             updateStatement.setString(3, newDescription);
             updateStatement.setString(4, String.valueOf(id));

             int updatedRows = updateStatement.executeUpdate();
             System.out.println("Account updated successfully");

             // Fetch the new idWallet after the update
             int newWalletId = getWalletIdForAccount(id);
             updateWalletBalance(newWalletId, currentBalance, newBalance);

             // Perform additional logic based on currentBalance, newBalance, and newWalletId if needed
         } catch (SQLException e) {
             System.out.println("Error updating account: " + e.getMessage());
         }
     }
    private void updateWalletBalance(int walletId, double oldBalance, double newBalance) {
        String updateWalletQuery = "UPDATE wallet SET totalBalance = totalBalance - ? + ? WHERE idWallet = ?";
        try {
            PreparedStatement updateWalletStatement = MyConnection.getInstance().getCnx().prepareStatement(updateWalletQuery);
            updateWalletStatement.setDouble(1, oldBalance);
            updateWalletStatement.setDouble(2, newBalance);
            updateWalletStatement.setInt(3, walletId);

            int updatedWalletRows = updateWalletStatement.executeUpdate();
            System.out.println("Wallet balance updated successfully");
        } catch (SQLException e) {
            System.out.println("Error updating wallet balance: " + e.getMessage());
        }
    }

    // Helper method to fetch the idWallet for an account
    private int getWalletIdForAccount(int accountId) {
        String walletIdQuery = "SELECT idWallet FROM account WHERE idAccount = ?";
        try {
            PreparedStatement walletIdStatement = MyConnection.getInstance().getCnx().prepareStatement(walletIdQuery);
            walletIdStatement.setInt(1, accountId);
            ResultSet rs = walletIdStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("idWallet");
            } else {
                // Handle the case where the account with the given id is not found
                System.out.println("Account not found");
                return 0; // or throw an exception
            }
        } catch (SQLException e) {
            System.out.println("Error fetching walletId for account: " + e.getMessage());
            return 0;
        }
    }


    // when deleting an account , transactions of that account must be deleted too .  show number of transactions deleted

    public void deleteEntityByID(int id) {
        String getBalanceQuery = "SELECT balance, idWallet FROM account WHERE idAccount = ?";
        String transactionDeleteQuery = "DELETE FROM transaction WHERE fromAccount = ?";
        String accountDeleteQuery = "DELETE FROM account WHERE idAccount = ?";
        String updateWalletQuery = "UPDATE wallet SET totalBalance = totalBalance - ? WHERE idWallet = ?";

        Connection connection = MyConnection.getInstance().getCnx();
        try {
            // Begin transaction
            connection.setAutoCommit(false);

            PreparedStatement getBalanceStatement = connection.prepareStatement(getBalanceQuery);
            PreparedStatement transactionDeleteStatement = connection.prepareStatement(transactionDeleteQuery);
            PreparedStatement accountDeleteStatement = connection.prepareStatement(accountDeleteQuery);
            PreparedStatement updateWalletStatement = connection.prepareStatement(updateWalletQuery);

            // Get the balance and wallet ID of the deleted account
            getBalanceStatement.setInt(1, id);
            ResultSet resultSet = getBalanceStatement.executeQuery();

            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                int walletId = resultSet.getInt("idWallet");

                // Deleting Account and its transactions
                transactionDeleteStatement.setInt(1, id);
                int transactionDeleteResult = transactionDeleteStatement.executeUpdate();
                System.out.println("Number of transactions deleted: " + transactionDeleteResult);

                // Deleting account
                accountDeleteStatement.setInt(1, id);
                int accountDeleteResult = accountDeleteStatement.executeUpdate();
                System.out.println("Number of accounts deleted: " + accountDeleteResult);

                // Update the total balance in the wallet table
                updateWalletStatement.setDouble(1, balance);
                updateWalletStatement.setInt(2, walletId);
                updateWalletStatement.executeUpdate();

                // Commit the transaction
                connection.commit();
                System.out.println("Account and associated transactions deleted successfully, wallet updated.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    @Override
    public void deleteEntity(Account account) {

    }
    public double getAccountBalance(int accountId) {
        String query = "SELECT balance FROM account WHERE idAccount = ?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, accountId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                // Handle the case where the account with the given id is not found
                System.out.println("Account not found");
                return 0.0; // or throw an exception
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println("Error fetching account balance: " + e.getMessage());
            return 0.0;
        }
    }

    public int countAccountsForWallet(int walletId) {
        String query = "SELECT COUNT(*) FROM account WHERE idWallet = ?";
        int count = 0;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, walletId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return count;
    }



    @Override
    public List<Account> getAllData() {
        return null;
    }
    // to work on after finishing transaction services so we can get the lists of transactions as well
    @Override
    public List<Account> getAllTransactions(int id) {
       /* List<Payee> data = new ArrayList<>();
        String requete = "SELECT *FROM transactions where idAccount = ? " ;
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, String.valueOf(id));
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {
                Transaction t = new Transaction();
                t.s(rs.getInt(1));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));
                t.setNamePayee(rs.getString("namePayee"));


                data.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

        */
        return null;
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






    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();

        String query = "SELECT * FROM account";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
             ResultSet resultSet = pst.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                AccountType type = AccountType.valueOf(resultSet.getString(3));
                Double balance =resultSet.getDouble(4);
                String description =resultSet.getString(5);
                int idWallet = resultSet.getInt(6);

                // Retrieve other attributes...

                Account account = new Account(id, name,type,balance,description,idWallet);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return accounts;
    }


    public double getCurrentBalance(int accountId) {
        String query = "SELECT balance FROM account WHERE idAccount = ?";
        double currentBalance = 0.0;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, accountId);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    currentBalance = resultSet.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return currentBalance;
    }




    public int getAccountIdByName(String selectedAccount) {
        String requete = "SELECT idAccount FROM account WHERE nameAccount = ?";
        int accountId = -1;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, selectedAccount);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    accountId = resultSet.getInt("idAccount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }



        return accountId;
    }




    public List<String> getAccountsNames() {
        List<String> accountNames = new ArrayList<>();
        String query = "SELECT nameAccount FROM account";

        try {
        Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement pst = connection.prepareStatement(query);
             ResultSet resultSet = pst.executeQuery() ;

            while (resultSet.next()) {
                String accountName = resultSet.getString("nameAccount");
                accountNames.add(accountName);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return accountNames;
    }




    public void updateAccount(int accountId, String newName, double newBalance, String newDescription) {
        String getOldBalanceQuery = "SELECT balance FROM account WHERE idAccount = ?";
        String updateAccountQuery = "UPDATE account SET nameAccount = ?, balance = ?, description = ? WHERE idAccount = ?";
        TransactionService ts = new TransactionService();

        try{
           Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement getOldBalanceStatement = connection.prepareStatement(getOldBalanceQuery);
             PreparedStatement updateAccountStatement = connection.prepareStatement(updateAccountQuery);

            // Get the old balance of the account
            getOldBalanceStatement.setInt(1, accountId);
            ResultSet resultSet = getOldBalanceStatement.executeQuery();
            double oldBalance = 0;

            if (resultSet.next()) {
                oldBalance = resultSet.getDouble("balance");
            }

            // Update the account
            updateAccountStatement.setString(1, newName);
            updateAccountStatement.setDouble(2, newBalance);
            updateAccountStatement.setString(3, newDescription);
            updateAccountStatement.setInt(4, accountId);
            updateAccountStatement.executeUpdate();

            // Check if the balance has changed and create a transaction accordingly
            if (newBalance > oldBalance) {
                double incomeAmount = newBalance - oldBalance;
                Transaction incomeTransaction = new Transaction(
                        LocalDate.now(),
                        TransactionType.INCOME,
                        "Balance Update - Income",
                        incomeAmount,
                        accountId,
                        accountId, // Assuming 0 for the 'toAccount' in case of income
                        1, // You may need to adjust these values based on your data model
                        1
                );
                ts.addTransactionToDatabase(incomeTransaction);
                WalletService walletService = new WalletService();
                walletService.updateTotalBalanceAfterUpdate(incomeAmount, 1);
            } else if (newBalance < oldBalance) {
                double expenseAmount = oldBalance - newBalance;
                Transaction expenseTransaction = new Transaction(
                        LocalDate.now(),
                        TransactionType.EXPENSE,
                        "Balance Update - Expense",
                        expenseAmount,
                        accountId,
                        accountId, // Assuming 0 for the 'toAccount' in case of expense
                        2, // You may need to adjust these values based on your data model
                        1
                );
                ts.addTransactionToDatabase(expenseTransaction);
                WalletService walletService = new WalletService();
                walletService.updateTotalBalanceAfterUpdate(-expenseAmount, 1);
            }

            System.out.println("Account updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }




    public List<String> getCategoryNames() {
        String requete = "SELECT *FROM subcategory" ;
        List<String> categoryNames = new ArrayList<>();

        try  {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery(requete);

            while (rs.next()) {
                String categoryName = rs.getString("name");
                categoryNames.add(categoryName);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return categoryNames;
    }


    public int getCategoryIdByName(String categoryName) {
        String requete = "SELECT idSubCategory FROM subcategory WHERE name = ?";
        int categoryId = -1;

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, categoryName);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("idSubCategory");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return categoryId;
    }


    public boolean isPayeeNameUnique(String accountName) {
        String query = "SELECT COUNT(*) FROM account WHERE nameAccount = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, accountName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, the name is unique
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if an exception occurs
    }


    public String getAccountNameById(int accountId) {
        String accountName = null;

        // Example query (adjust based on your actual schema)
        String query = "SELECT nameAccount FROM account WHERE idAccount = ?";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, accountId);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    accountName = resultSet.getString("nameAccount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return accountName;
    }

    // ... (other methods)
}




