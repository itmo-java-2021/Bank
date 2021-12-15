public class TransactionsException extends Exception {
    public TransactionsException() {
    }

    public TransactionsException(String message) {
        super(message);
    }

    public TransactionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionsException(Throwable cause) {
        super(cause);
    }

    public TransactionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
