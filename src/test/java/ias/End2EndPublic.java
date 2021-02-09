package ias;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class End2EndPublic {

    /*
        These tests should always pass if all other tests passed as well.
        They intentionally reward solving the assignment completely and not only partially.
     */

    @Test
    public void withBasicFile_loadsAllCardsAndRules() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        assertEquals(3, game.get("card", "*").length);
        TestUtil.assertArrayEqualsUnordered(new String[] {"Two"}, game.get("card", "Two"));
        assertEquals(1, game.get("property", "*").length);
        TestUtil.assertArrayEqualsUnordered(new String[] {"color"}, game.get("property", "color"));
        assertEquals(1, game.get("rule", "*").length);
        TestUtil.assertArrayEqualsUnordered(new String[] {"color:red>black"}, game.get("rule", "*"));
    }

    @Test
    public void withSSPFile_findsBeatingCardForStone() throws GameException {
        Game game = Factory.loadGame("src/test/resources/SSP.game");
        Deck deck = game.createDeck();
        deck.addCard("Schere");
        deck.addCard("Stein");
        deck.addCard("Papier");
        TestUtil.assertArrayEqualsUnordered(new String[]{"Papier"}, deck.selectBeatingCards("Stein"));
    }
}
