package it.polimi.db2.GMA.exceptions;

public class ContainOffensiveWordsException extends Exception {
    private static final long serialVersionUID = 1L;

    public ContainOffensiveWordsException(String message) {
        super(message);
    }
}