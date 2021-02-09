package student;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.CRC32;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;

public class Main {

    /**
     * Main entry point of the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // DO NOT remove this check! This prevents you from accidentally failing the exam
        if (hasBeenTamperedWith()) {
            System.out.println("You have changed one of the following files: "
                    + "build.gradle, Deck.java, Factory.java, Game.java or GameException.java, TestUtil.java");
            System.out.println("You must revert these changes before submitting, otherwise you will fail the exam!");
            return;
        }

        System.out.println("Welcome to the card game interactive shell!");
        System.out.println("Type \"help\" for a list of available commands.");

        Game game;
        try {
            game = Factory.createGame("Dynamic Game");
            // game = Factory.loadGame("./src/test/resources/Basic.game");
        } catch (GameException e) {
            System.err.printf("Failed to create a game: %s%n", e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        Deck deck = null;
        while (true) {
            try {
                System.out.printf("> ");
                String command = scanner.nextLine();
                if (command.equals("help")) {
                    System.out.println("Available commands: definecard, defineproperty, "
                                     + "setpropertyinteger, setpropertystring, get, quit, "
                                     + "createdeck, deckaddcard, decklistcards, deckmatchinginteger, "
                                     + "deckmatchingstring, deckselectbeatingcards");
                } else if (command.equals("definecard")) {
                    System.out.printf("Name: ");
                    game.defineCard(scanner.nextLine());
                } else if (command.equals("defineproperty")) {
                    System.out.printf("Name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Type: ");
                    String propertyType = scanner.nextLine();
                    game.defineProperty(propertyName, propertyType);
                } else if (command.equals("setpropertyinteger")) {
                    System.out.printf("Card name: ");
                    String cardName = scanner.nextLine();
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Value: ");
                    int propertyValue = scanner.nextInt();
                    scanner.nextLine(); // Eat trailing new line.
                    game.setProperty(cardName, propertyName, propertyValue);
                } else if (command.equals("setpropertystring")) {
                    System.out.printf("Card name: ");
                    String cardName = scanner.nextLine();
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Value: ");
                    String propertyValue = scanner.nextLine();
                    game.setProperty(cardName, propertyName, propertyValue);
                } else if (command.equals("get")) {
                    System.out.printf("Get type: ");
                    String getType = scanner.nextLine();
                    System.out.printf("Filter name: ");
                    String getName = scanner.nextLine();
                    String[] resultArray = game.get(getType, getName);
                    for (String result : resultArray) {
                        System.out.println(result);
                    }
                } else if (command.equals("createdeck")) {
                    if (deck != null) {
                        System.out.println("Replacing previous deck instance.");
                    }
                    deck = game.createDeck();
                } else if (command.equals("deckaddcard")) {
                    if (deck != null) {
                        System.out.printf("Card name: ");
                        String cardName = scanner.nextLine();
                        deck.addCard(cardName);
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("decklistcards")) {
                    if (deck != null) {
                        String[] cards = deck.getAllCards();
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckmatchinginteger")) {
                    if (deck != null) {
                        System.out.printf("Property name: ");
                        String propertyName = scanner.nextLine();
                        System.out.printf("Value: ");
                        int propertyValue = scanner.nextInt();
                        scanner.nextLine(); // Eat trailing new line.
                        String[] cards = deck.getMatchingCards(propertyName, propertyValue);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckmatchingstring")) {
                    if (deck != null) {
                        System.out.printf("Property name: ");
                        String propertyName = scanner.nextLine();
                        System.out.printf("Value: ");
                        String propertyValue = scanner.nextLine();
                        String[] cards = deck.getMatchingCards(propertyName, propertyValue);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckselectbeatingcards")) {
                    if (deck != null) {
                        System.out.printf("Opponent card name: ");
                        String cardName = scanner.nextLine();
                        String[] cards = deck.selectBeatingCards(cardName);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("quit")) {
                    break;
                } else {
                    System.err.printf("Unknown command: %s%n", command);
                }

            } catch (GameException e) {
                System.err.printf("Game error: %s%n", e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Calculates a CRC32 checksum of a list of files. This method is only used
     * to verify that you have not changed important files. You do not need to
     * understand how it works, as it has nothing to do with the task of this exam.
     * @return True, if no critical file has been modified
     */
    private static boolean hasBeenTamperedWith() {
        String[] files =  {"./build.gradle",
                "./src/main/java/ias/Deck.java",
                "./src/main/java/ias/Factory.java",
                "./src/main/java/ias/Game.java",
                "./src/main/java/ias/GameException.java",
                "./src/test/java/ias/TestUtil.java"};

        long result = 0;
        for (String file : files) {
            try (InputStream is = Files.newInputStream(Paths.get(file))) {
                CRC32 crc = new CRC32();
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    crc.update(buffer, 0, bytesRead);
                }
                result ^= crc.getValue();
            } catch (IOException ex) {
                System.out.println("Error calculating the checksum of " + file);
            }
        }

        // DO NOT remove this check! This prevents you from accidentally failing the exam
        return result != 438897027L && result != 241251047L;
    }
}
