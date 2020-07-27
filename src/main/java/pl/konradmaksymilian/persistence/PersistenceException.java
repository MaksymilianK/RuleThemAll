package pl.konradmaksymilian.persistence;

public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(Throwable t) {
        super(t);
    }
}
