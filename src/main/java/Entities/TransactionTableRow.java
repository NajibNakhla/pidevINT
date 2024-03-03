package Entities;

import java.time.LocalDate;

public class TransactionTableRow {
    private int idTransaction;
    private LocalDate date;
    private String type ;
    private String description;
    private Double amount ;
    private String fromAccountName;
    private String toAccountName;
    private String categoryName;
    private String payeeName;

    public TransactionTableRow(int idTransaction,LocalDate date, String type, String description, Double amount, String fromAccountName, String toAccountName, String categoryName, String payeeName) {
        this.idTransaction=idTransaction;
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.categoryName = categoryName;
        this.payeeName = payeeName;
    }


    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFromAccountName() {
        return fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }


    public Transaction getTransaction() {
        Transaction transaction = new Transaction();
        return transaction;
    }
}
