package Entities;

import enums.TransactionType;

import java.time.LocalDate;
import java.util.Objects;

public class TransactionDetails {

    private int idTransaction ;
    private LocalDate date;
    private TransactionType type ;
    private String description;
    private Double amount ;
    private int fromAccount;
    private int toAccount;
    private int idCategory;
    private int idPayee;

    private String fromAccountName;
    private String toAccountName;
    private String categoryName;
    private String payeeName;

    public TransactionDetails() {
    }

    public TransactionDetails(int idTransaction, LocalDate date, TransactionType type, String description, Double amount, int fromAccount, int toAccount, int idCategory, int idPayee, String fromAccountName, String toAccountName, String categoryName, String payeeName) {
        this.idTransaction = idTransaction;
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.idCategory = idCategory;
        this.idPayee = idPayee;
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.categoryName = categoryName;
        this.payeeName = payeeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDetails that = (TransactionDetails) o;
        return idTransaction == that.idTransaction && fromAccount == that.fromAccount && toAccount == that.toAccount && idCategory == that.idCategory && idPayee == that.idPayee && Objects.equals(date, that.date) && type == that.type && Objects.equals(description, that.description) && Objects.equals(amount, that.amount) && Objects.equals(fromAccountName, that.fromAccountName) && Objects.equals(toAccountName, that.toAccountName) && Objects.equals(categoryName, that.categoryName) && Objects.equals(payeeName, that.payeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaction, date, type, description, amount, fromAccount, toAccount, idCategory, idPayee, fromAccountName, toAccountName, categoryName, payeeName);
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "idTransaction=" + idTransaction +
                ", date=" + date +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", idCategory=" + idCategory +
                ", idPayee=" + idPayee +
                ", fromAccountName='" + fromAccountName + '\'' +
                ", toAccountName='" + toAccountName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", payeeName='" + payeeName + '\'' +
                '}';
    }


}
