package student;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;
import ias.TestUtil;

public class MyGameTestPublic {

    @TempDir
    File tempDir;
    private Game game;
	private Deck deck1;
	private Deck deck2;

    @BeforeEach
    public void createGame() throws GameException {
        game = Factory.createGame("Test");
		deck1 = game.createDeck();
		deck2 = game.createDeck();
    }

    @Test
    public void defineCard_withUniqueNames_assertCardExist() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
        game.defineCard("Card 3");
    }

    @Test
    public void defineCard_withDuplicateName_throws() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
		game.defineCard("Card 3");
        assertThrows(GameException.class, () -> {
            game.defineCard("Card 2");
        });
    }

    @Test
    public void defineProperty_withValidNameAndType_doesNotThrow() throws GameException {
        game.defineProperty("type", "string");
        game.defineProperty("power", "integer");
    }
	
	@Test
    public void defineProperty_withSameName_throws() throws GameException {
		game.defineProperty("Farbe", "integer");
        assertThrows(GameException.class, () -> { game.defineProperty("Farbe", "string");});
    }
	
	@Test
    public void setProperty_StringwithInvalidTyp_throws() throws GameException {
		game.defineCard("Card 1");
		game.defineProperty("Farbe", "string");
        assertThrows(GameException.class, () -> { game.setProperty("Card 1", "Farbe", 3);});
    }
	
	@Test
    public void setProperty_IntegerwithInvalidTyp_throws() throws GameException {
		game.defineCard("Card 1");
		game.defineProperty("zahl", "integer");
        assertThrows(GameException.class, () -> { game.setProperty("Card 1", "zahl", "rot");});
    }
	
	@Test
    public void setProperty_withManyIntegerAndValidValue_doesNotThrow() throws GameException {
        game.defineCard("One");
        game.defineProperty("power", "integer");
		game.setProperty("One", "power", 10);
		game.defineCard("Two");
		game.defineCard("Three");
        game.setProperty("Two", "power", 20);
        game.setProperty("Three", "power", 30);
		
    }
	
	@Test
    public void setProperty_withManyStringAndValidValue_doesNotThrow() throws GameException {
        game.defineCard("One");
		game.defineCard("Two");
		game.defineCard("Three");
		game.defineProperty("farbe", "string");
		game.setProperty("One", "farbe", "rot");
        game.setProperty("Two", "farbe", "grün");
        game.setProperty("Three", "farbe", "gelb");
		
    }
	
	@Test
    public void defineRule_withDuplicateStringRule_throws() throws GameException {
        game.defineProperty("zahl", "integer");
        game.defineRule("zahl", ">");
        assertThrows(GameException.class, () -> {
            game.defineRule("zahl", "<");});
    }
	
	@Test
    public void setProperty_withOutCardAndProperty_throws() throws GameException {
        assertThrows(GameException.class, () -> { game.setProperty("Two", "farbe", "grün");});
    }
	
	@Test
    public void defineRule_withoutCardAndProperty_throws() throws GameException {
        assertThrows(GameException.class, () -> { game.defineRule("zahl", "<");});
    }
/*	
	@Test
    public void defineRule_withOnlyStringRuleForEveryCard_throws() throws GameException {
		game.defineCard("One");
		game.defineCard("Two");
		game.defineCard("Three");
        game.defineProperty("farbe", "string");
		game.setProperty("One", "farbe", "rot");
		game.setProperty("Two", "farbe", "schwarz");
		game.setProperty("Three", "farbe", "blau");
		game.defineRule("farbe", "blau", "rot");
        assertThrows(GameException.class, () -> {
            game.defineRule("farbe", "schwarz", "rot");});
    }
*/	
	@Test
    public void get_withInvalidCard_giveNothing() throws GameException {
		game.defineCard("One");
        TestUtil.assertArrayEqualsUnordered(new String[] {}, game.get("card", "Two"));
    }
	
	@Test
    public void get_withNoCard_giveNothing() throws GameException {
        TestUtil.assertArrayEqualsUnordered(new String[] {}, game.get("card", "Two"));
    }
	
	@Test
    public void get_withFalseGameName_giveGame() throws GameException {
        TestUtil.assertArrayEqualsUnordered(new String[] {"Test"}, game.get("game", "*"));
		TestUtil.assertArrayEqualsUnordered(new String[] {"Test"}, game.get("game", ""));
		TestUtil.assertArrayEqualsUnordered(new String[] {"Test"}, game.get("game", "*adf4"));
    }
	
	@Test
    public void selectBeatingCards_withStringPropertyAndDraw_returnsNoBeatingCards() throws GameException {
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
        game.setProperty("Card 5", "color", "yellow");

        game.defineRule("color", "red", "green");
        game.defineRule("color", "green", "red");
		game.defineRule("color", "blue", "black");
        game.defineRule("color", "black", "blue");
		game.defineRule("color", "black", "yellow");
		game.defineRule("color", "yellow", "blue");

        Deck deck = game.createDeck();
        deck.addCard("Card 1");
        deck.addCard("Card 2");
        deck.addCard("Card 3");
        deck.addCard("Card 4");
        deck.addCard("Card 5");

        TestUtil.assertArrayEqualsUnordered(new String[]{}, deck.selectBeatingCards("Card 2"));
		TestUtil.assertArrayEqualsUnordered(new String[]{"Card 4"}, deck.selectBeatingCards("Card 5"));
		TestUtil.assertArrayEqualsUnordered(new String[]{}, deck.selectBeatingCards("Card 1"));
    }
	
/*	@Test
    public void saveToFile_withMultipleCardPropertiesWithoutValues_writesAllCardProperties() throws GameException, IOException {
        game.defineCard("Arbor Elf");
        game.defineCard("Realm Cloaked Giant");
        game.defineProperty("height", "integer");
        game.defineProperty("race", "string");
		game.defineRule("height", ">");
		game.defineRule("race", "rot", "gelb");
        game.setProperty("Arbor Elf", "race", "elf");
        game.setProperty("Realm Cloaked Giant", "race", "giant");
        game.setProperty("Arbor Elf", "height", 4);
        game.setProperty("Realm Cloaked Giant", "height", 9000);

        File targetFile = new File("C:\\Users\\bisso\\Downloads\\Test.game");
        game.saveToFile("C:\\Users\\bisso\\Downloads\\Test.game");
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.contains("CardProperty: Arbor Elf | race |"));
        assertTrue(file.contains("CardProperty: Arbor Elf | height |"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | race |"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | height |"));
    }
	
	@Test
    public void saveToFile_withTestGame_savedFileCanBeLoadedAgain() throws GameException {
        game = Factory.loadGame("C:\\Users\\bisso\\Downloads\\Test.game");
        File targetFile = new File("C:\\Users\\bisso\\Downloads\\SaveLoadTest.game");
        game.saveToFile("C:\\Users\\bisso\\Downloads\\SaveLoadTest.game");
        //This does not check for correctness, only consistency
        assertNotNull(Factory.loadGame(targetFile.getAbsolutePath()));
    }
*/	
	@Test
    public void loadGame_withBasicFile_throwsAfterDefine() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        game.defineCard("Four");
        game.defineCard("Five");
		
       assertThrows(GameException.class, () -> {
            game.defineCard("Two");
        });
    }


/**    @Test
    public void setProperty_withIntegerAndValidValue_doesNotThrow() throws GameException {
        game.defineCard("One");
        game.defineProperty("power", "integer");
        game.setProperty("One", "power", 10);
    }

    @Test
    public void setProperty_withSameCardAndSamePropertyMultipleTimes_throws() throws GameException {
        game.defineCard("One");
        game.defineProperty("power", "integer");
        game.setProperty("One", "power", 10);
        assertThrows(GameException.class, () -> {
            game.setProperty("One", "power", 10);});
    }

    @Test
    public void defineRule_withOneIntegerRule_doesNotThrow() throws GameException {
        game.defineProperty("power", "integer");
        game.defineRule("power", ">");
    }

    @Test
    public void defineRule_withMultipleDifferentStringRules_doesNotThrow() throws GameException {
        game.defineProperty("type", "string");
        game.defineRule("type", "paper", "rock");
        game.defineRule("type", "rock", "scissors");
        game.defineRule("type", "scissors", "paper");
    }

    @Test
    public void defineRule_withDuplicateStringRule_throws() throws GameException {
        game.defineProperty("type", "string");
        game.defineRule("type", "rock", "scissors");
        assertThrows(GameException.class, () -> {
            game.defineRule("type", "rock", "scissors");});
    }

    @Test
    public void defineRule_withInvalidIntegerOperator_throws() throws GameException {
        game.defineProperty("power", "integer");
        assertThrows(GameException.class, () -> {
            game.defineRule("power", "!");});
    }

    @Test
    public void defineRule_withIntegerRuleAndStringProperty_throws() throws GameException {
        game.defineProperty("type", "string");
        assertThrows(GameException.class, () -> {
            game.defineRule("type", ">");});
    }

    @Test
    public void get_withGameAndAnything_returnsGameName() throws GameException {
        TestUtil.assertArrayEqualsUnordered(new String[] {"Test"}, game.get("game", "ignored"));
    }

    @Test
    public void get_withCardAndName_returnsGivenCard() throws GameException {
        game.defineCard("One");
        game.defineCard("Two");
        TestUtil.assertArrayEqualsUnordered(new String[] {"One"}, game.get("card", "One"));
    }

    @Test
    public void get_withPropertyAndWildcard_returnsAllProperties() throws GameException {
        game.defineProperty("power", "integer");
        game.defineProperty("type", "string");
        TestUtil.assertArrayEqualsUnordered(new String[] {"power", "type"}, game.get("property", "*"));
    }

    @Test
    public void get_withRuleAndName_returnsGivenStringRule() throws GameException {
        game.defineProperty("power", "integer");
        game.defineProperty("language", "string");
        game.defineRule("power", ">");
        game.defineRule("language", "Java", "JavaScript");
        game.defineRule("language", "CPP", "C");
        TestUtil.assertArrayEqualsUnordered(new String[] {"language:Java>JavaScript"},
                game.get("rule", "language:Java>JavaScript"));
    }

    @Test
    public void saveToFile_withValidPath_savesToGivenPath() throws GameException {
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        assertTrue(targetFile.exists());
    }

    @Test
    public void saveToFile_withGameName_containsGivenName() throws GameException, IOException {
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.startsWith("Game: Test"));
    }

    @Test
    public void saveToFile_withMultipleCards_writesAllCards() throws GameException, IOException {
        game.defineCard("Lurrus");
        game.defineCard("Yorion");
        game.defineCard("Kaheera");

        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.contains("Card: Lurrus"));
        assertTrue(file.contains("Card: Yorion"));
        assertTrue(file.contains("Card: Kaheera"));
    }

    @Test
    public void saveToFile_withMultipleCardProperties_writesAllCardProperties() throws GameException, IOException {
        game.defineCard("Arbor Elf");
        game.defineCard("Realm Cloaked Giant");
        game.defineProperty("height", "integer");
        game.defineProperty("race", "string");
        game.setProperty("Arbor Elf", "race", "elf");
        game.setProperty("Realm Cloaked Giant", "race", "giant");
        game.setProperty("Arbor Elf", "height", 4);
        game.setProperty("Realm Cloaked Giant", "height", 9000);

        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.contains("CardProperty: Arbor Elf | race | elf"));
        assertTrue(file.contains("CardProperty: Arbor Elf | height | 4"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | race | giant"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | height | 9000"));
    }

    @Test
    public void saveToFile_withBasicGame_savedFileCanBeLoadedAgain() throws GameException {
        game = Factory.loadGame("src/test/resources/Basic.game");
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        //This does not check for correctness, only consistency
        assertNotNull(Factory.loadGame(targetFile.getAbsolutePath()));
    }
	*/
}
