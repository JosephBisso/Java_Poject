package student;

// Erlaubte Pakete: java.lang, java.io, java.nio, java.util und org.junit;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;

public class MyGame implements Game{
	
	public String name;
	public Karte[] card;
	private int anzCard = 0;
	public Eigenschaft[] property;
	private int anzProperty = 0;
	public Regel[] rule;
	private int anzRule = 0;
	public String path;
	public KartenDeck[] deck;
	private int anzDeck = 0;

	
	public Karte[] getCard() {
		return card;
	}
	
	public Eigenschaft[] getProperty() {
		return property;
	}
	
	public Regel[] getRule() {
		return rule;
	}
	
	public MyGame(String name) throws GameException {
		this.name = name;
    }
	
	public static Game loadGame(String path) throws GameException {
		File file = new File(path);
		MyGame myGame = new MyGame("");
		int counter = 0;
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.contains(":")) {
					throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
				}
				String[] sLine = line.split("\\: ");
				if (sLine[1].isEmpty()) {
					throw new GameException("Es gibt keine Angabe in der Leine " + counter);
				}
				if (counter++ == 0) {
					if (sLine[0].equals("Game")) {
						myGame = new MyGame(sLine[1]);
					} else {
						throw new GameException("Es fehlt die Definition (Name) des Spiels");
					}
				} else {
					if (sLine[0].equals("Game")) {
						throw new GameException("Es befinden sich 2 Spielennammen in der Datei");
					}
				} 
				if (sLine[0].equals("Card")) {
					myGame.defineCard(sLine[1]);
				}
				if (sLine[0].equals("Property")) {
					if (!sLine[1].contains("|")) {
						throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
					}
					String[] sProperty = sLine[1].split(" \\| ");
					myGame.defineProperty(sProperty[0], sProperty[1]);
				}
				if (sLine[0].equals("CardProperty")) {
					if (!sLine[1].contains("|")) {
						throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
					}
					String[] sCardProperty = sLine[1].split(" \\| ");
					boolean found = false;
					for (Karte karte : myGame.card) {
						if (karte == null) {
							throw new GameException("Das Spiel besitzt noch keine einzige Karte");
						}
						if (karte.getName().equals(sCardProperty[0])) {
							for (Eigenschaft eigenschaft : myGame.property) {
								if (eigenschaft == null) {
									throw new GameException("Diese Eigenschaft wurde noch nicht definiert");
								}
								if (sCardProperty.length != 3) {
									throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
								}
								if (sCardProperty[2].isEmpty()) {
									throw new GameException("Es fehlt ein Wert für diese Karteneigenschaft");
								}
								if (eigenschaft.getName().equals(sCardProperty[1])) {
									try {
										myGame.setProperty(sCardProperty[0], sCardProperty[1], Integer.parseInt(sCardProperty[2]));
									} catch (NumberFormatException e) {
										myGame.setProperty(sCardProperty[0], sCardProperty[1], sCardProperty[2]);
									} finally {
										found = true;
									}
								}
							}
						}
					}
					if (!found) {
						throw new GameException("Die Karte dieser Eigenschaft wurde noch nicht definiert");
					}
				}
				if (sLine[0].equals("GameRuleInteger")) {
					if (!sLine[1].contains("|")) {
						throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
					}
					String[] sCardRule = sLine[1].split(" \\| ");
					boolean found = false;
					for (Eigenschaft eigenschaft : myGame.property) {
						if (eigenschaft.getName().equals(sCardRule[0])) {
							if (!eigenschaft.getTyp().equals("integer")) {
								throw new GameException("Die Eigenschaft dieser Regel wurde nicht als Integer-Rgel definiert");
							}
							if (!sCardRule[1].equals(">") && !sCardRule[1].equals("<")) {
								throw new GameException("Der Wert dieser Regel ist für Integer-Regel nicht gültig");
							}
							myGame.defineRule(sCardRule[0], sCardRule[1]);
							found = true;
						}
					}
					if (!found) {
						throw new GameException("Diese Eigenschaft wurde noch nicht definiert");
					}
				}
				if (sLine[0].equals("GameRuleString")) {
					if (!sLine[1].contains("|")) {
						throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
					}
					String[] sCardRule = sLine[1].split(" \\| ");
					boolean found = false;
					for (Eigenschaft eigenschaft : myGame.property) {
						if (eigenschaft.getName().equals(sCardRule[0])) {
							if (!eigenschaft.getTyp().equals("string")) {
								throw new GameException("Die Eigenschaft dieser Regel wurde nicht als String-Regel definiert");
							}
							if (sCardRule.length != 3) {
									throw new GameException("Es gibt keine gültige Angabe in der Leine " + counter);
								}
							if (sCardRule[1].isEmpty() || sCardRule[2].isEmpty()) {
								throw new GameException("Es müssen für diese Regel sowohl ein winning- als auch ein losingName eigegeben werden");
							}
							if (sCardRule[1].equals(">") && sCardRule[2].equals("<")) {
								throw new GameException("Der Wert dieser Regel ist für String-Regel nicht gültig");
							}
							myGame.defineRule(sCardRule[0], sCardRule[1], sCardRule[2]);
							found = true;
						}
					}
					if (!found) {
						throw new GameException("Diese Eigenschaft wurde noch nicht definiert");
					}
				}
			}
			return ((Game)myGame);
			
		} catch (FileNotFoundException e) {
			throw new GameException("Die Datei konnte nicht geöffnet werden");
		}
    }
	
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
		if (!type.equals("string") && !type.equals("integer")) {
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
		boolean found = false;
		if (property == null) {
			throw new GameException("Es kann keine Eigenschaft gesetzt werden, ohne zuvor eine Eigenschaft zu definieren");
		}
		for (Eigenschaft eigenschaft : property) {
			if (eigenschaft.getName().equals(propertyName)) {
				found = true;
			}
		}
		if (!found) {
			throw new GameException("Die Eigenschaft wurde noch nicht gefunden");
		}
		
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
		boolean found = false;
		if (property == null) {
			throw new GameException("Es kann keine Regel angelegt werde, ohne zuvor eine Eigenschaft zu definieren");
		}
		for (Eigenschaft eigenschaft : property) {
			if (eigenschaft.getName().equals(propertyName)) {
				found = true;
			}
		}
		if (!found) {
			throw new GameException("Die Eigenschaft für diese Regel wurde nicht gefunden");
		}
		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (property[i].getTyp().equals("integer")) {
					if (!operation.equals(">") && !operation.equals("<")) {
						throw new GameException("Die eingegebene Operation nicht nicht erlaubt");
						
					}
					if(property[i].getRegel() != null) {
						if (property[i].getRegel().length != 0 
								&& !property[i].getRegel()[0].getOperation().equals(operation)) {
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
		
		if (card != null) {
			int counterCard = 0,
				counterStringCard = 0;
			for (Karte karte : card) {
				counterCard++;
				if (karte.getProperty() != null) {
					for (Eigenschaft eigenschaft : karte.getProperty()) {
						if (eigenschaft.getRegel() != null) {
							for (Regel regel : eigenschaft.getRegel()) {
								if (regel.istString()) {
									counterStringCard--;
								}
							}
						}
					}
				}
			}
			if(counterCard > 2 && counterCard == counterStringCard) {
			throw new GameException("Es können auch nur fur einen Teil der Karten String-Regeln angelegt werden");
			}
		}
		
 		for (int i = 0; i < anzProperty; i++) {
			if (property[i].getName().equals(propertyName)) {
				if (!property[i].getTyp().equals("string")) {
					throw new GameException("Für die Eigenschaft muss eine Operationangegeben werden");
					
				}
				
				if (property[i].getRegel() != null) {
					for (int j = 0; j < property[i].getRegel().length; j++) {
						if (property[i].getRegel()[j].getWinningName().equals(winningName)
								&& property[i].getRegel()[j].getLosingName().equals(losingName)) {
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
		if (!(type.equals("game") || type.equals("card") || type.equals("property") || type.equals("rule"))) {
			throw new GameException("Der eingegebene Datentyp ist nicht erlaubt");
			
		}
		if (!type.equals("game") && ((name.contains("*") && name.length() > 1) || name.length() == 0 )) {
			throw new GameException("Der angegebene Name ist nicht gültig");
			
		}
		
		String[] stringReturn;
		String getReturn = "";
		if (type.equals("card")) {
			if (name.contains("*")) {
				stringReturn = new String[anzCard];
				for (Karte karte : card) {
					getReturn += karte.getName() + ",";
				}
				return getReturn.split(",");
			}
			for (int i = 0; i < anzCard; i++) {
				if (card[i].getName().equals(name)) {
					stringReturn = new String[1];
					stringReturn[0] = card[i].getName();
					return stringReturn;
				}
			}
			return stringReturn = new String[0];
		} else if (type.equals("property")){
			if (name.contains("*")) {
				stringReturn = new String[anzProperty];
				for (int i = 0; i < anzProperty; i++) {
					stringReturn[i] = property[i].getName();
				}
				return stringReturn;
			}
			for (int i = 0; i < anzProperty; i++) {
				if (property[i].getName().equals(name)) {
					stringReturn = new String[1];
					stringReturn[0] = property[i].getName();
					return stringReturn;
				}
			}
			return stringReturn = new String[0];
		} else if (type.equals("rule")) {
			if (name.contains("*")) {
				stringReturn = new String[anzRule];
				int counter = 0;
				while(counter < anzRule) {
					for (int j = 0; j < anzProperty; j++) {
						for (int k = 0; k < property[j].getRegel().length; k++) {
							stringReturn[counter++] = property[j].printRegel()[k];
						}
					}
				}
				return stringReturn;
			}
			
			String[] eigenschaftRegel;
			if (name.contains("<")) {
				eigenschaftRegel = name.split("<");
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						if (property[i].getRegel()[0].getOperation() == "<") {
							return property[i].getRegel()[0].printRegel();
						}
					}
				}
				return stringReturn = new String[0];
			}
			if (name.contains(":")) {
				eigenschaftRegel = new String[3];
				String [] zwischenString1 = name.split("\\:");
				String [] zwischenString2 = zwischenString1[1].split(">");
				eigenschaftRegel[0] = zwischenString1[0];
				eigenschaftRegel[1] = zwischenString2[0];
				eigenschaftRegel[2] = zwischenString2[1];
				
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						for (int j = 0; j < property[i].getRegel().length; j++) {
							if (property[i].getRegel()[j].getWinningName().equals(eigenschaftRegel[1]) 
									&& property[i].getRegel()[j].getLosingName().equals(eigenschaftRegel[2])) {
								return property[i].getRegel()[j].printRegel();
							}
						}
					}
				}
				return stringReturn = new String[0];
			} else {
				eigenschaftRegel = name.split(">");
				for (int i = 0; i < anzProperty; i++) {
					if (property[i].getName().equals(eigenschaftRegel[0])) {
						if (property[i].getRegel()[0].getOperation() == ">") {
							return property[i].getRegel()[0].printRegel();
						}
					}
				}
				return stringReturn = new String[0];
			}
		} else if (type.equals("game")) {
			stringReturn = new String[1];
			stringReturn[0] = this.name;
			return stringReturn;
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
		if (anzDeck > 0) {
			KartenDeck[] zwischenLage = new KartenDeck[anzDeck];
			for (int i = 0; i < anzDeck; i++) {
				zwischenLage[i] = deck[i];
			} 
			deck = new KartenDeck[++anzDeck];
			for (int i = 0; i < anzDeck - 1; i++) {
				deck[i] = zwischenLage[i];
			}
		} else {
			deck = new KartenDeck[++anzDeck];
		}
		
		deck[anzDeck -1] = new KartenDeck(this);
		return deck[anzDeck - 1];
	}

}