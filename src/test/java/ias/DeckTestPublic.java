package ias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTestPublic {

    private Game game;
    private Deck deck;

    @BeforeEach
    public void createGame() throws GameException {
        this.game = Factory.createGame("Test");
        this.deck = game.createDeck();
    }

    @Test
    public void addCard_withNonexistingCard_throws() {
        assertThrows(GameException.class,
                () -> deck.addCard("Foobar"));
    }

    @Test
    public void getAllCards_withoutCards_returnsEmptyArray() {
        TestUtil.assertArrayEqualsUnordered(new String[] {}, deck.getAllCards());
    }

    @Test
    public void getAllCards_withMultipleCards_returnsAllCards() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
        game.defineCard("Card 3");
        deck.addCard("Card 1");
        deck.addCard("Card 2");
        deck.addCard("Card 3");
        TestUtil.assertArrayEqualsUnordered(new String[] {"Card 1", "Card 2", "Card 3"}, deck.getAllCards());
    }

    @Test
    public void getMatchingCards_withOneIntegerProperty_returnsCorrectCards() throws GameException {
        game.defineCard("Gemrazer");
        game.defineCard("Scavenging Ooze");
        game.defineCard("Lotus Cobra");

        game.defineProperty("power", "integer");
        game.setProperty("Gemrazer", "power", 4);
        game.setProperty("Scavenging Ooze", "power", 2);
        game.setProperty("Lotus Cobra", "power", 2);

        deck.addCard("Gemrazer");
        deck.addCard("Scavenging Ooze");
        deck.addCard("Lotus Cobra");

        TestUtil.assertArrayEqualsUnordered(new String[] {"Scavenging Ooze", "Lotus Cobra"},
                deck.getMatchingCards("power", 2));
    }

    @Test
    public void getMatchingCards_withStringArgumentForIntegerType_throws() throws GameException {
        game.defineProperty("power", "integer");
        game.defineProperty("toughness", "string");

        assertThrows(GameException.class,
                () -> deck.getMatchingCards("power", "asdf"));
    }

    @Test
    public void selectBeatingCards_withOneIntegerProperty_cardDoesNotBeatItself() throws GameException {
        game.defineCard("Card 1");
        game.defineProperty("power", "integer");
        game.defineRule("power", ">");
        deck.addCard("Card 1");
        TestUtil.assertArrayEqualsUnordered(new String[]{}, deck.selectBeatingCards("Card 1"));
    }

    @Test
    public void selectBeatingCards_withOneStringProperty_returnsBeatingCards() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
        game.defineCard("Card 3");
        game.defineCard("Card 4");
        game.defineCard("Card 5");

        game.defineProperty("color", "string");
        game.setProperty("Card 1", "color", "red");
        game.setProperty("Card 2", "color", "blue");
        game.setProperty("Card 3", "color", "green");
        game.setProperty("Card 4", "color", "black");
        game.setProperty("Card 5", "color", "black");

        game.defineRule("color", "red", "green");
        game.defineRule("color", "green", "black");
        game.defineRule("color", "blue", "red");
        game.defineRule("color", "black", "red");

        Deck deck = game.createDeck();
        deck.addCard("Card 1");
        deck.addCard("Card 2");
        deck.addCard("Card 3");
        deck.addCard("Card 4");
        deck.addCard("Card 5");

        TestUtil.assertArrayEqualsUnordered(new String[]{"Card 2", "Card 4", "Card 5"}, deck.selectBeatingCards("Card 1"));
    }
}
