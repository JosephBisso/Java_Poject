package ias;

public interface Deck {
    public void addCard(String cardName) throws GameException;

    public String[] getAllCards();

    public String[] getMatchingCards(String propertyName, int value) throws GameException;
    public String[] getMatchingCards(String propertyName, String value) throws GameException;

    public String[] selectBeatingCards(String opponentCard) throws GameException;
}