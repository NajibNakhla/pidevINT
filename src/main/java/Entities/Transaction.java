package Entities;
import enums.TransactionType;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private LocalDate date;
    private TransactionType type; // "expense", "income", "transfer"
    private String description;
    private double amount;
    private int fromAccount;
    private int toAccount;
    private int idCategory;
    private int idPayee;

    public Transaction(LocalDate date, TransactionType type, String description, double amount, int fromAccount, int toAccount, int idCategory, int idPayee) {
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.idCategory = idCategory;
        this.idPayee = idPayee;
    }

    public Transaction(int id, LocalDate date, TransactionType type, String description, double amount, int fromAccount, int toAccount) {
        this.id=id;
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.idCategory = idCategory;
        this.idPayee = idPayee;

    }

    public Transaction() {

    }

    public Transaction(int id, LocalDate date, TransactionType type, String description, double amount, int fromAccount, int toAccount, int idCategory, int idPayee) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.idCategory = idCategory;
        this.idPayee = idPayee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdPayee() {
        return idPayee;
    }

    public void setIdPayee(int idPayee) {
        this.idPayee = idPayee;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", idCategory=" + idCategory +
                ", idPayee=" + idPayee +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Double.compare(amount, that.amount) == 0 && fromAccount == that.fromAccount && toAccount == that.toAccount && idCategory == that.idCategory && idPayee == that.idPayee && Objects.equals(date, that.date) && type == that.type && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, type, description, amount, fromAccount, toAccount, idCategory, idPayee);
    }




// Constructors, getters, and setters
}