package ias;

public final class GameException extends Exception {
    public static final String ERROR = "Error! ";

    public GameException(String message) {
    	super(ERROR + message);
    }
}
