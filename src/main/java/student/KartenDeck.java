package student;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ias.Deck;
import ias.Factory;
import ias.Game;
import ias.GameException;

public class KartenDeck implements Deck {
	
	public MyGame myGame;
	private int anzCard = 0;
	public Karte[] card;
	public Eigenschaft[] property;
	public Regel[] rule;
	
	public KartenDeck(MyGame myGame) {
		this.myGame = myGame;
	}
	
	public void addCard(String cardName) throws GameException {
		boolean found = false;
		if (myGame.getCard() == null) {
			throw new GameException("Das Spiel enthält keine im Deck verwendbare Karten");
		}
		for (int i = 0; i < myGame.getCard().length; i++) {
			if (myGame.getCard()[i].getName().equals(cardName)) {
				found = true;
				
				if (anzCard > 0) {
					Karte[] zwischenLage = new Karte[anzCard];
					for (int j = 0; j < anzCard; j++) {
						zwischenLage[j] = card[j];
					} 
					card = new Karte[++anzCard];
					for (int j = 0; j < anzCard - 1; j++) {
					card[j] = zwischenLage[j];
					}
				} else {
					card = new Karte[++anzCard];
				}
				card[anzCard - 1] = myGame.getCard()[i] ;
			}
		}
		if(!found) {
			throw new GameException("Die eingegebene Karte existiert nicht im Spiel.");
		}
	}

    public String[] getAllCards() {
		if (anzCard == 0) {
			return new String[0];
		}
		String[] stringReturn = new String[anzCard];
		for (int i = 0; i < anzCard; i++) {
			stringReturn[i] = card[i].getName();
		}
		return stringReturn;
	}

    public String[] getMatchingCards(String propertyName, int value) throws GameException {
		String stringReturn = "";
		boolean found = false,
				foundProperty = false;
				
		for (Eigenschaft eigenschaft : myGame.getProperty()) {
			if (eigenschaft.getName().equals(propertyName)) {
				foundProperty = true;
				if(!eigenschaft.getTyp().equals("integer")) {
					throw new GameException("Diese Eigenschaft wurde als Integer definiert");
				}
				for (Karte karte : card) {
					for (Eigenschaft kartenEigenschaft : karte.getProperty()) {
						for (String wert : karte.getValue()) {
							if (kartenEigenschaft.getTyp().equals("integer") 
									&& kartenEigenschaft.getName().equals(propertyName) && Integer.parseInt(wert) == value) {
								
								found = true;
								stringReturn += karte.getName() + ",";
							} 
						}
					}
				}
			}
		}
		if (!foundProperty) {
			return new String[0];
		}
		if (!found) {
			return new String[0];
		}
		
		return stringReturn.split(",");
	}
    public String[] getMatchingCards(String propertyName, String value) throws GameException {
		String stringReturn = "";
		boolean found = false,
				foundProperty = false;
				
		for (Eigenschaft eigenschaft : myGame.getProperty()) {
			if (eigenschaft.getName().equals(propertyName)) {
				foundProperty = true;
				if(!eigenschaft.getTyp().equals("string")) {
					throw new GameException("Diese Eigenschaft wurde als string definiert");
				}
				for (Karte karte : card) {
					for (Eigenschaft kartenEigenschaft : karte.getProperty()) {
						for (String wert : karte.getValue()) {
							if (kartenEigenschaft.getTyp().equals("string") 
									&& kartenEigenschaft.getName().equals(propertyName) && wert.equals(value)) {
								
								found = true;
								stringReturn += karte.getName() + ",";
							} 
						}
					}
				}
			}
		}
		if (!foundProperty) {
			return new String[0];
		}
		if (!found) {
			return new String[0];
		}
		
		return stringReturn.split(",");
	}

    public String[] selectBeatingCards(String opponentCard) throws GameException {
		if (myGame.getRule() == null) {
			throw new GameException("Es sind noch keine Regeln vorhanden");
		}
		String stringReturn = "";
		boolean found = false;
		for (Regel regel : myGame.getRule()) {
			int counter = 0;
			if (regel.istString() && regel.getLosingName().equals(opponentCard)) {
				counter++;
				for (Regel rules : myGame.getRule()) {
					if(rules.getWinningName().equals(opponentCard) && rules.getLosingName().equals(regel.getWinningName())) {
						counter--;
					}
				}
				if (counter > 0) {
					stringReturn += regel.getName();
				}
			}
			if (!regel.istString() && regel.getOperation().equals("<")) {
				counter++;
				for (Regel rules : myGame.getRule()) {
					if(!rules.istString() && rules.getOperation().equals(">")) {
						counter--;
					}
					if (counter > 0) {
						stringReturn += rules.getName();
					}
				}
			}
		}
		if (stringReturn.contains(opponentCard)) {
			stringReturn = stringReturn.replaceAll(opponentCard, "");
			if (stringReturn.equals("")) {
				return new String[0];
			}
		}
		
		String beatingCards = "";
		for (Karte karte : card) {
			if (stringReturn.contains(karte.getName())) {
				beatingCards += karte.getName();
			}
		}
		
		if (beatingCards.equals("")) {
			return new String[0];
		}
		
		return beatingCards.split(",");
	}

}