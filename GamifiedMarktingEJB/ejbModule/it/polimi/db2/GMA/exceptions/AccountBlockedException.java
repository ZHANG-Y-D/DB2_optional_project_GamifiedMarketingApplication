package it.polimi.db2.GMA.exceptions;

public class AccountBlockedException extends Exception {
    private static final long serialVersionUID = 1L;

    public AccountBlockedException(String message) {
        super(message);
    }
}