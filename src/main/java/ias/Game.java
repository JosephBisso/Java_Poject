package ias;

public interface Game {
    public void defineCard(String name) throws GameException;

    public void defineProperty(String name, String type) throws GameException;
    public void setProperty(String cardName, String propertyName, String value) throws GameException;
    public void setProperty(String cardName, String propertyName, int value) throws GameException;

    public void defineRule(String propertyName, String operation) throws GameException;
    public void defineRule(String propertyName, String winningName, String losingName) throws GameException;

    public String[] get(String type, String name) throws GameException;

    public void saveToFile(String path) throws GameException;
    public Deck createDeck();
}