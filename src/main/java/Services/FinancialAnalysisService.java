package Services;

import Entities.Transaction;
import enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FinancialAnalysisService {
    public double calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateTotalExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateSurplusOrDeficit(double totalIncome, double totalExpenses) {
        return totalIncome - totalExpenses;
    }



    public Map<LocalDate, Double> calculateCombinedBalanceOverTime(List<Transaction> transactions) {
        // Sort transactions by date
        transactions.sort((t1, t2) -> t1.getDate().compareTo(t2.getDate()));

        // Map to store the combined balance for each date
        Map<LocalDate, Double> combinedBalanceMap = new TreeMap<>();

        // Initialize balances
        double incomeBalance = 0.0;
        double expenseBalance = 0.0;

        // Iterate through transactions to calculate balance over time
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            // Update the balance based on transaction type
            if (transaction.getType() == TransactionType.INCOME) {
                incomeBalance += transaction.getAmount();
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                expenseBalance += transaction.getAmount();
            }

            // Calculate the net balance
            double netBalance = incomeBalance - expenseBalance;

            // Store the net balance for the date
            combinedBalanceMap.put(transactionDate, netBalance);
        }

        return combinedBalanceMap;
    }

}
