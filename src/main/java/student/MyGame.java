package student;

// Erlaubte Pakete: java.lang, java.io, java.nio, java.util und org.junit;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;

public class MyGame implements Game, Deck {
	
	public String name;
	public Karte[] card;
	private static int anzCard = 0;
	public Eigenschaft[] property;
	private static int anzProperty = 0;
	public Regel[] rule;
	private static int anzRule = 0;
	public path;
	
	public static MyGame(String name) throws GameException {
 
    }
	
	public static Game loadGame(String path) throws GameException {

    }
	
	//aus Interface Game
	
	public void defineCard(String name) throws GameException {
		if (anzCard > 0) {
			for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(name)) {
					throw new GameException("Diese Karte ist bereits definiert worden");
					return;
				}
			}
			
			Karte[] zwischenLage = new Kart[anzCard];
			for (int i = 0; i < anzCard; i++) {
				zwischenLage[i] = card[i];
			} 
			card = new Karte[++anzCard];
			for (int i = 0; i < anzCard - 1; i++) {
				card[i] = zwischenLage[i];
			}
		}
		if (anzCard == 0) {
			card = new Karte[++anzCard];
		}
		
		card[anzCard - 1].setName(name);
	}

    public void defineProperty(String name, String type) throws GameException {
		if (!type.equals("string") | !type.equals("integer")) {
			throw new GameException("Der eingegebe Eigenschaftentyp ist ungültig");
			return;
		}
		if (anzProperty > 0) {
			for (int i = 0; i < anzProperty; i++) {
				if (property[i].getName().equals(name)) {
					throw new GameException("Der Name der gewünschten Eigenschaft ist schon vergeben");
					return;
				}
			}
			Eigenschaft[] zwischenLage = new Eigenschaft[anzProperty];
			for (int i = 0; i < anzProperty; i++) {
				zwischenLage[i] = property[i];
			} 
			property = new Eigenschaft[++anzProperty];
			for (int i = 0; i < anzProperty - 1; i++) {
				property[i] = zwischenLage[i];
			}
		}
		if (anzProperty == 0) {
			property = new Eigenschaft[++anzProperty];
		}
		
		property[anzProperty - 1] = new Eigenschaft(name, type);
	}
    public void setProperty(String cardName, String propertyName, String value) throws GameException {
		for (int i = 0; i < anzCard; i++) {
			if (card[i].getName().equals("cardName")) {
				if (card[i].getProperty().getName().equals(propertyName)) {
					throw new GameException("Die eingegebene Eigenschaft ist bereits der eingegebenen Karte "
											+ "zugeordnet worden");
					return;
				}
			}
		}
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (!property.getTyp().equals("string")) {
					throw new GameException("Der eingegebene Wert passt nicht zum Typ der eingegebenen Eigenschaft");
					return;
				}
			}
		}
		for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(cardName)) {
					card[i].setProperty(propertyName, value, "string");
				} else {
					throw new GameException("Die eingegebene Karte existoert nicht");
					return;
				}
				
		}
	
		
	}
    public void setProperty(String cardName, String propertyName, int value) throws GameException {
		for (int i = 0; i < anzCard; i++) {
			if (card[i].getName().equals("cardName")) {
				if (card[i].getProperty().getName().equals(propertyName)) {
					throw new GameException("Die eingegebene Eigenschaft ist bereits der eingegebenen Karte "
											+ "zugeordnet worden");
					return;
				}
			}
		}
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (!property.getTyp().equals("integer")) {
					throw new GameException("Der eingegebene Wert passt nicht zum Typ der eingegebenen Eigenschaft");
					return;
				}
			}
		}
		for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(cardName)) {
					card[i].setProperty(propertyName, value, "integer");
				} else {
					throw new GameException("Die eingegebene Karte existoert nicht");
					return;
				}
				
		}
	}

    public void defineRule(String propertyName, String operation) throws GameException {
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (property[i].getTyp().equals("integer")) {
					if (!operation.equals(">") | !operation.equals("<")) {
						throw new GameException("Die eingegebene Operation nicht nicht erlaubt");
						return;
					}
					property[i].setRegel(operation);
				}
			}
		}
	}
    public void defineRule(String propertyName, String winningName, String losingName) throws GameException {
		if (winningName.equals(losingName)) {
			throw new GameException("Ein Karte kann sich nicht selbst schlagen");
		}
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (!property[i].getTyp().equals("string")) {
					throw new GameException("Die eingegebene Operation nicht nicht erlaubt");
					return;
				}
				property[i].setRegel(winningName, losingName);
			}	
		}
	}

    public String[] get(String type, String name) throws GameException;

    public void saveToFile(String path) throws GameException;
    public Deck createDeck();
	
	//Aus Interface Deck
	
	public void addCard(String cardName) throws GameException;

    public String[] getAllCards();

    public String[] getMatchingCards(String propertyName, int value) throws GameException;
    public String[] getMatchingCards(String propertyName, String value) throws GameException;

    public String[] selectBeatingCards(String opponentCard) throws GameException;
	
	
}