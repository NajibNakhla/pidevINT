package Entities;

import enums.AccountType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account {
    private int idAccount;
    private String nameAccount;
    private AccountType typeAccount;  // Use the AccountType enum

    private double balance;
    private String description;
    private int idWallet;

    private static double totalBalance = 0.0;

    private List<Transaction> transactions;
    // private List<Category> categories;

    public Account() {
        List<Transaction> transactions;
    }

    public Account(int idAccount, String nameAccount, AccountType typeAccount, double balance, String description, int idWallet) {
        this.idAccount = idAccount;
        this.nameAccount = nameAccount;
        this.typeAccount = typeAccount;
        this.balance = balance;
        this.description = description;
        this.idWallet = idWallet;
        transactions = new ArrayList<>();
    }

    public Account(String nameAccount, AccountType typeAccount, double balance, String description, int idWallet) {
        this.nameAccount = nameAccount;
        this.typeAccount = typeAccount;
        this.balance = balance;
        this.description = description;
        this.idWallet = idWallet;
        transactions = new ArrayList<>();
    }

    public Account(String nameAccount, AccountType typeAccount, double balance, String description) {
        this.nameAccount = nameAccount;
        this.typeAccount = typeAccount;
        this.balance = balance;
        this.description = description;
        totalBalance += balance;
        transactions = new ArrayList<>();
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public AccountType getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(AccountType typeAccount) {
        this.typeAccount = typeAccount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public static double getTotalBalance() {
        return totalBalance;
    }

    public static void setTotalBalance(double totalBalance) {
        Account.totalBalance = totalBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAccount=" + idAccount +
                ", nameAccount='" + nameAccount + '\'' +
                ", typeAccount=" + typeAccount +
                ", balance=" + balance +
                ", description='" + description + '\'' +
                ", idWallet=" + idWallet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return idAccount == account.idAccount && Double.compare(balance, account.balance) == 0 && idWallet == account.idWallet && Objects.equals(nameAccount, account.nameAccount) && typeAccount == account.typeAccount && Objects.equals(description, account.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, nameAccount, typeAccount, balance, description, idWallet);
    }




}
