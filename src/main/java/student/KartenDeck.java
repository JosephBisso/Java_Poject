package student;

import ias.Deck;
import ias.GameException;

/** Klasse KartenDeck für die Interface Deck.
*/
public class KartenDeck implements Deck {
	
	private MyGame myGame;
	private int anzCard = 0;
	private Karte[] card;
	private Eigenschaft[] property;
	private Regel[] rule;

/** @param myGame gewünscht.
*/
	public KartenDeck(MyGame myGame) {
		this.myGame = myGame;
	}

/** @return myGame aus Klasse.
*/	
	public MyGame getMyGame() {
		return this.myGame;
	}

/** @return property aus Klasse.
*/		
	public Eigenschaft[] property() {
		return this.property;
	} 

/** @return card aus Klasse.
*/	
	public Karte[] card() {
		return this.card;
	} 

/** @param cardName gewünscht.
*/	
	public void addCard(String cardName) throws GameException {
		boolean found = false;
		if (myGame.getCard() == null || myGame.getCard().length == 0) {
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
				card[anzCard - 1] = myGame.getCard()[i];
			}
		}
		if (!found) {
			throw new GameException("Die eingegebene Karte existiert nicht im Spiel.");
		}
	}

/** @return card aus Klasse als String.
*/
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

/** @param propertyName gewünscht.
*	@param value gewünscht
*	@return gewünscht card
*/
    public String[] getMatchingCards(String propertyName, int value) throws GameException {
		String stringReturn = "";
		boolean found = false,
				foundProperty = false;
		if (myGame.getProperty() == null || myGame.getProperty().length == 0) {
			return new String[0];
		}
				
		for (Eigenschaft eigenschaft : myGame.getProperty()) {
			if (eigenschaft.getName().equals(propertyName)) {
				foundProperty = true;
				if (!eigenschaft.getTyp().equals("integer")) {
					throw new GameException("Diese Eigenschaft wurde als Integer definiert");
				}
				for (Karte karte : card) {
					for (Eigenschaft kartenEigenschaft : karte.getProperty()) {
						for (String wert : karte.getValue()) {
							if (kartenEigenschaft.getTyp().equals("integer") 
								&& kartenEigenschaft.getName().equals(propertyName) 
										&& Integer.parseInt(wert) == value) {
								
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
	
/** @param propertyName gewünscht.
*	@param value gewünscht
*	@return gewünscht card
*/	
    public String[] getMatchingCards(String propertyName, String value) throws GameException {
		String stringReturn = "";
		boolean found = false,
				foundProperty = false;
		
		if (myGame.getProperty() == null || myGame.getProperty().length == 0) {
			return new String[0];
		}
		for (Eigenschaft eigenschaft : myGame.getProperty()) {
			if (eigenschaft.getName().equals(propertyName)) {
				foundProperty = true;
				if (!eigenschaft.getTyp().equals("string")) {
					throw new GameException("Diese Eigenschaft wurde als string definiert");
				}
				for (Karte karte : card) {
					for (Eigenschaft kartenEigenschaft : karte.getProperty()) {
						for (String wert : karte.getValue()) {
							if (kartenEigenschaft.getTyp().equals("string") 
								&& kartenEigenschaft.getName().equals(propertyName) 
										&& wert.equals(value)) {
								
								found = true;
								stringReturn += karte.getName() + ",";
							} 
						}
					}
				}
			}
		}
		if (!foundProperty) {
			throw new GameException("Diese Eigenschaft wurde nicht definiert");
		}
		if (!found) {
			return new String[0];
		}
		
		return stringReturn.split(",");
	}

/** @param opponentCard gewünscht.
*	@return gewünscht card
*/
    public String[] selectBeatingCards(String opponentCard) throws GameException {
		if (myGame.getRule() == null || myGame.getRule().length == 0) {
			return new String[0];
		}
		if (myGame.getProperty() == null || myGame.getProperty().length == 0) {
			return new String[0];
		}
		if (opponentCard.isEmpty()) {
			throw new GameException("Es ist keine zu schlagende karte eigegebene worden");
		}
		if (myGame.getCard() == null || myGame.getCard().length == 0) {
			throw new GameException("Es sind kein Karte im Spiel definiert worden");
		}
		int counter = 0;
		Karte gegnerKarte = new Karte("DummiName");
		for (Karte karte : myGame.getCard()) {
			if (karte.getName().equals(opponentCard)) {
				gegnerKarte = karte;
			}
		}
		String stringReturn = "";
		String winner = "";
		String losingWert = "";
		for (Regel regel : myGame.getRule()) {
			for (String wert : gegnerKarte.getValue()) {
				if (regel.istString() && regel.getLosingName().equals(wert)) {
					counter++;
					losingWert = wert;
					winner = regel.getWinningName();
				}
				if (!regel.istString() && !regel.getOperation().equals(">")) {
					counter++;
				}
			}
			for (Regel regel2 : myGame.getRule()) {
				if (regel2.istString() && regel2.getWinningName().equals(losingWert) 
					&& regel2.getLosingName().equals(winner)) {
					counter--;
				}
			}
			if (counter > 0) {
				String a = ""; //Kurzer Name Wegen StyleCheck
				String b = stringReturn; //Kurzer Name Wegen StyleCheck
				for (Eigenschaft eigenschaft : myGame.getProperty()) {
					if (eigenschaft.getName().equals(regel.getName())) {
						for (Karte karte : card) {
							a = karte.getName();
							for (Eigenschaft kartenEigenschaft : karte.getProperty()) {
								for (String wert : karte.getValue()) {
                                    if (kartenEigenschaft.getTyp().equals("string") 
										&& kartenEigenschaft.getName()
										.equals(regel.getName()) 
										&& wert.equals(winner)
										&& !b
                                        .contains(a)) {
                                        b
                                            +=
                                            a
                                            +
                                            ",";
									}
									if (kartenEigenschaft.getTyp()
										.equals("integer") && kartenEigenschaft
										.getName().equals(regel.getName())) {
												
										b += a + ",";
									}
								}
							}
						}
					}
				}
				stringReturn = b;
			}
		}
		if (stringReturn.contains(opponentCard)) {
			stringReturn = stringReturn.replaceAll(opponentCard, "");
		}
		if (stringReturn.isEmpty()) {
			return new String[0];
		}
		return stringReturn.split(",");
	}

}