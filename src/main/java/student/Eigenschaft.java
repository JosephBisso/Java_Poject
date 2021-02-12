package student;

/** Klasse Eigenschaft.
*/
public class Eigenschaft  {
	
	private String name;
	private String typ;
	private Regel[] regel;
	private int anzRegel = 0;

/** @param name gewünscht.
*	@param typ gewünscht
*/
	public Eigenschaft(String name, String typ) {
		this.name = name;
		this.typ = typ;
	}

/** @return name aus Klasse.
*/
	public String getName() {
		return this.name;
	}

/** @return typ aus Klasse.
*/	
	public String getTyp() {
		return this.typ;
	}

/** @param rule gewünscht.
*/	
	public void setRegel(Regel rule) {
		if (anzRegel > 0) {
			Regel[] zwischenLage = new Regel[anzRegel];
			for (int i = 0; i < anzRegel; i++) {
				zwischenLage[i] = regel[i];
			} 
			regel = new Regel[++anzRegel];
			for (int i = 0; i < anzRegel - 1; i++) {
				regel[i] = zwischenLage[i];
			}					
		}
		if (anzRegel == 0) {
			regel = new Regel[++anzRegel];
		}
		
		regel[anzRegel - 1] = rule;
	}

/** @return regel aus KLasse.
*/		
	public Regel[] getRegel() {
		return regel;
	}
	
/** @return regel aus KLasse als String.
*/	
	public String[] printRegel() {
		String[] stringReturn;
		if (!regel[0].istString()) {
			stringReturn = new String[1];
			stringReturn[0] = name + regel[0].getOperation();
			return stringReturn;
		}
		
		stringReturn = new String[anzRegel];
		for (int i = 0; i < anzRegel; i++) {
			stringReturn[i] = name + ":" + regel[i].getWinningName() + ">" + regel[i].getLosingName();
		}
		return stringReturn;
	}	
	
}