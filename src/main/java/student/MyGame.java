package student;

// Erlaubte Pakete: java.lang, java.io, java.nio, java.util und org.junit;
import java.io.PrintWriter;
import java.io.IOException;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;

public class MyGame implements Game, Deck {
	
	public String name;
	public Karte[] card;
	private int anzCard = 0;
	public Eigenschaft[] property;
	private int anzProperty = 0;
	public Regel[] rule;
	private int anzRule = 0;
	public String path;
	
	public  MyGame(String name) throws GameException {
 
    }
	
	public static Game loadGame(String path) throws GameException {
		return null;
    }
	
	//aus Interface Game
	
	public void defineCard(String name) throws GameException {
		if (anzCard > 0) {
			for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(name)) {
					throw new GameException("Diese Karte ist bereits definiert worden");
					
				}
			}
			
			Karte[] zwischenLage = new Karte[anzCard];
			for (int i = 0; i < anzCard; i++) {
				zwischenLage[i] = card[i];
			} 
			card = new Karte[++anzCard];
			for (int i = 0; i < anzCard - 1; i++) {
				card[i] = zwischenLage[i];
			}
		} else {
			card = new Karte[++anzCard];
		}
		card[anzCard - 1] = new Karte(name);
	}

    public void defineProperty(String name, String type) throws GameException {
		if (!type.equals("string") & !type.equals("integer")) {
			throw new GameException("Der eingegebe Eigenschaftentyp ist ungültig");
			
		}
		if (anzProperty > 0) {
			for (int i = 0; i < anzProperty; i++) {
				if (property[i].getName().equals(name)) {
					throw new GameException("Der Name der gewünschten Eigenschaft ist schon vergeben");
					
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
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName) && !property[i].getTyp().equals("string")) {
				throw new GameException("Der eingegebene Wert passt nicht zum Typ der eingegebenen Eigenschaft");

			}
		}
		boolean keineKarte = false;
		for (Karte karte : card) {
			if (karte.getName().equals(cardName)) {
				if (karte.getProperty() != null) {
					for (int k = 0; k < karte.getProperty().length; k++) {
						if (karte.getProperty()[k].getName().equals(propertyName)) {
							throw new GameException("Die eingegebene Eigenschaft ist bereits der eingegebenen Karte "
												+ "zugeordnet worden");
						
						}
						char[] werte = value.toCharArray();
						for (char wert : werte) {
							if (Character.isDigit(wert)) {
								throw new GameException("Der eingegebene Wert passt nicht zum Typ der eingegebenen Eigenschaft");
							}
						}
					}
				}
				karte.setProperty(propertyName, value, "string");
				return;
			} else {
				keineKarte = true;
			}
		}
		if (keineKarte) {
			throw new GameException("Die eingegebene Karte existiert nicht");
		}
		
		
	}
	
    public void setProperty(String cardName, String propertyName, int value) throws GameException {
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName) && !property[i].getTyp().equals("integer")) {
				throw new GameException("Der eingegebene Wert passt nicht zum Typ der eingegebenen Eigenschaft");

			}
		}
		boolean keineKarte = false;
		for (Karte karte : card) {
			if (karte.getName().equals(cardName)) {
				if (karte.getProperty() != null) {
					for (int k = 0; k < karte.getProperty().length; k++) {
						if (karte.getProperty()[k].getName().equals(propertyName)) {
							throw new GameException("Die eingegebene Eigenschaft ist bereits der eingegebenen Karte "
												+ "zugeordnet worden");
						
						}
					} 
				}
				karte.setProperty(propertyName, Integer.toString(value), "integer");
				return;
			} else {
				keineKarte = true;
			}
		}
		if (keineKarte) {
			throw new GameException("Die eingegebene Karte existiert nicht");
		}
		
	}

    public void defineRule(String propertyName, String operation) throws GameException {
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (property[i].getTyp().equals("integer")) {
					if (!operation.equals(">") & !operation.equals("<")) {
						throw new GameException("Die eingegebene Operation nicht nicht erlaubt");
						
					}
					if(property[i].getRegel() != null) {
						if (property[i].getRegel().length != 0 
								& !property[i].getRegel()[0].getOperation().equals(operation)) {
							throw new GameException("Die eingegebe Operation widerspricht die bereits existieren");
						}
					}
					if (anzRule > 0) {
						Regel[] zwischenLage = new Regel[anzRule];
						for (int j = 0; j < anzRule; j++) {
							zwischenLage[j] = rule[j];
						} 
						rule = new Regel[++anzRule];
						for (int j = 0; j < anzRule - 1; j++) {
							rule[j] = zwischenLage[j];
						}					
					}
					if (anzRule == 0) {
						rule = new Regel[++anzRule];
					}
		
					rule[anzRule - 1] = new Regel(propertyName, operation);
					property[i].setRegel(rule[anzRule -1]);
				} else {
					throw new GameException("Für diese Eigenschaft muss Gewinner und Verlierer angegeben werden");
				}
			}
		}
	}
	
    public void defineRule(String propertyName, String winningName, String losingName) throws GameException {
		if (winningName.equals(losingName)) {
			throw new GameException("Ein Karte kann sich nicht selbst schlagen");
		}

/*		
a:		if (anzRule >= 1 && anzCard > 1) {
			for (Karte karte : card) {
				if (karte.getRegel() == null) {
					break a;
				}
				for(Regel regel : karte.getRegel()) {
					if (!regel.istString()) {
						break a;
					} 
				}
			}
			throw new GameException("Es können auch nur fur einen Teil der Karten String-Regeln angelegt werden");
		}
*/		
 		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (!property[i].getTyp().equals("string")) {
					throw new GameException("Für die Eigenschaft muss eine Operationangegeben werden");
					
				}
				
				if (property[i].getRegel() != null) {
					for (int j = 0; j < property[i].getRegel().length; j++) {
						if (property[i].getRegel()[j].getWinningName().equals(winningName)
								& property[i].getRegel()[j].getLosingName().equals(losingName)) {
							throw new GameException("Die erfasste Regel existiert bereits");
						
						}
					}
				}
				if (anzRule > 0) {
						Regel[] zwischenLage = new Regel[anzRule];
						for (int j = 0; j < anzRule; j++) {
							zwischenLage[j] = rule[j];
						} 
						rule = new Regel[++anzRule];
						for (int j = 0; j < anzRule - 1; j++) {
							rule[j] = zwischenLage[j];
						}					
					}
					if (anzRule == 0) {
						rule = new Regel[++anzRule];
					}
		
				rule[anzRule - 1] = new Regel(propertyName, winningName, losingName);
				property[i].setRegel(rule[anzRule -1]);
			}	
		}
	}

    public String[] get(String type, String name) throws GameException {
		if (!type.equals("game") & !type.equals("card") & !type.equals("property") & !type.equals("rule")) {
			throw new GameException("Der eingegebene Datentyp ist nicht erlaubt");
			
		}
		if ((name.contains("*") & name.length() > 1) || name.length() == 0 ) {
			throw new GameException("Der angegebene Name ist nicht gültig");
			
		}
		
		String[] stringReturn;
		if (type.equals("card")) {
			if (name.equals("*")) {
				stringReturn = new String[anzCard];
				for (int i = 0; i < anzCard; i++) {
					stringReturn[i] = card[i].getName();
				}
				return stringReturn = new String[0];
			}
			for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(name)) {
					stringReturn = new String[1];
					stringReturn[0] = card[i].getName();
					return stringReturn = new String[0];
				}
				return stringReturn = new String[0];
			}
		} else if (type.equals("property")){
			if (name.equals("*")) {
				stringReturn = new String[anzProperty];
				for (int i = 0; i < anzProperty; i++) {
					stringReturn[i] = property[i].getName();
				}
				return stringReturn = new String[0];
			}
			for (int i = 0; i < anzProperty; i++) {
				if (property[i].getName().equals(name)) {
					stringReturn = new String[1];
					stringReturn[0] = property[i].getName();
					return stringReturn = new String[0];
				}
				return stringReturn = new String[0];
			}
		} else if (type.equals("rule")) {
			
			if (name.equals("*")) {
				stringReturn = new String[anzRule];
				int counter = 0;
				while(counter < anzRule) {
					for (int j = 0; j < anzProperty; j++) {
						for (int k = 0; k < property[j].getRegel().length; k++) {
							stringReturn[counter++] = property[j].printRegel()[k];
						}
					}
				}
				return stringReturn = new String[0];
			}
			
			String[] eigenschaftRegel;
			if (name.contains("<")) {
				eigenschaftRegel = name.split("<");
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						if (property[i].getRegel()[0].getOperation() == "<") {
							return property[i].printRegel();
						}
						return stringReturn = new String[0];
					}
					return stringReturn = new String[0];
				}
			}
			if (name.contains(":")) {
				eigenschaftRegel = new String[3];
				String [] zwischenString1 = name.split(":");
				String [] zwischenString2 = zwischenString1[1].split(">");
				eigenschaftRegel[0] = zwischenString1[0];
				eigenschaftRegel[1] = zwischenString2[0];
				eigenschaftRegel[2] = zwischenString2[1];
				
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						for (int j = 0; j < property[i].getRegel().length; j++) {
							if (property[i].getRegel()[j].getWinningName().equals(eigenschaftRegel[1]) 
									&& property[i].getRegel()[j].getLosingName().equals(eigenschaftRegel[2])) {
								return property[i].printRegel();
							}
							return stringReturn = new String[0];
						}
					}
					return stringReturn = new String[0];
				}
			} else {
				eigenschaftRegel = name.split(">");
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						if (property[i].getRegel()[0].getOperation() == ">") {
							return property[i].printRegel();
						}
						return stringReturn = new String[0];
					}
					return stringReturn = new String[0];
				}
			}
		} else if (type.equals("game")) {
			//Fehlt 
		}
		return stringReturn = new String[0];
	}

    public void saveToFile(String path) throws GameException {
		try (PrintWriter writer = new PrintWriter(path)) {
			writer.printf("Game: %s\n", name);
			for (int i = 0; i < anzCard; i++) {
				writer.printf("Card: %s\n", card[i].getName());
			}
			if (property != null) {
				for (int j = 0; j < property.length; j++) {
					writer.printf("Property: %s | %s\n", property[j].getName(), property[j].getTyp());
				}
			}
			for (int i = 0; i < anzCard; i++) {
				if (card[i].getProperty() != null) {	
					for (int j = 0; j < card[i].getProperty().length; j++) {
						writer.printf("CardProperty: %s | %s | %s\n", card[i].getName(), card[i].getProperty()[j].getName(), card[i].getValue()[j]);
					}
				}
			}
			
			if (rule != null) {
				for (int j = 0; j < anzRule; j++) {
					if(!rule[j].istString()) {
						writer.printf("GameRuleInteger: %s | %s\n", rule[j].getName(), rule[j].getOperation());
					}
				}
				for (int j = 0; j < anzRule; j++) {
					if(rule[j].istString()) {
						writer.printf("GameRuleString: %s | %s | %s\n", rule[j].getName(), rule[j].getWinningName(), rule[j].getLosingName());
					}
				}
			}
		
		} catch (IOException ioe) {
			throw new GameException(ioe.getMessage());
		}
	}
    public Deck createDeck() {
		return null;
	}
	
	//Aus Interface Deck
	
	public void addCard(String cardName) throws GameException {
		
	}

    public String[] getAllCards() {
		return null;
	}

    public String[] getMatchingCards(String propertyName, int value) throws GameException {
		return null;
	}
    public String[] getMatchingCards(String propertyName, String value) throws GameException {
		return null;
	}

    public String[] selectBeatingCards(String opponentCard) throws GameException {
		return null;
	}
	
	
}