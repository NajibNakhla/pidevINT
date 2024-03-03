package Controles;


import javafx.event.Event;
import javafx.event.EventType;

public class TransactionDeletedEvent extends Event {
    public static final EventType<TransactionDeletedEvent> TRANSACTION_DELETED =
            new EventType<>(Event.ANY, "TRANSACTION_DELETED");

    private final int deletedTransactionId;

    public TransactionDeletedEvent(int deletedTransactionId) {
        super(TRANSACTION_DELETED);
        this.deletedTransactionId = deletedTransactionId;
    }

    public int getDeletedTransactionId() {
        return deletedTransactionId;
    }
}
