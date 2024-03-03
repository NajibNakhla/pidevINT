package enums;

public enum TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER;

    public static TransactionType fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
