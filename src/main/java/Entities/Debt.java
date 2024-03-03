package Entities;

import java.sql.Date;

public class Debt {
    private int idDebt;
    private double Amount;
    private Date PaymentDate;
    private double amountToPay;
    private float InterestRate;
    private String Type;
    private Date CreationDate;

    public Debt() {
    }

    public Debt(double amount, Date paymentDate, float interestRate, String type, Date creationDate) {
        this.idDebt = idDebt;
        Amount = amount;
        PaymentDate = paymentDate;
        this.amountToPay = amount;
        InterestRate = interestRate;
        Type = type;
        CreationDate = creationDate;
    }
    public Debt(int idDebt, double amount, Date paymentDate, double amountToPay, float interestRate, String type, Date creationDate) {
        this.idDebt = idDebt;
        Amount = amount;
        PaymentDate = paymentDate;
        this.amountToPay = amountToPay;
        InterestRate = interestRate;
        Type = type;
        CreationDate = creationDate;
    }

    public Debt(double amount, Date paymentDate, double amountToPay, float interestRate, String type, Date creationDate) {
        Amount = amount;
        PaymentDate = paymentDate;
        this.amountToPay = amountToPay;
        InterestRate = interestRate;
        Type = type;
        CreationDate = creationDate;
    }

    public int getIdDebt() {
        return idDebt;
    }

    public void setIdDebt(int idDebt) {
        this.idDebt = idDebt;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public Date getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        PaymentDate = paymentDate;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public float getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(float interestRate) {
        InterestRate = interestRate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "idDebt=" + idDebt +
                ", Amount=" + Amount +
                ", PaymentDate=" + PaymentDate +
                ", amountToPay=" + amountToPay +
                ", InterestRate=" + InterestRate +
                ", Type='" + Type + '\'' +
                ", CreationDate=" + CreationDate +
                '}';
    }
}
