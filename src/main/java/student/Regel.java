package student;

/** Klasse Regel.
*/
public class Regel  {
	
	private String nameEigenschaft;
	private String operation;
	private String winningName;
	private String losingName;
	private boolean istString;

/** Prüft ob die Regel ein String oder ein Integer Regel ist.
*	@return true falls string Regel
*/
	public boolean istString() {
		return istString;
	}
	
/** gibt Name der Eigenschaft.
*	@return Name der Eigenschaft
*/
	public String getName() {
		return nameEigenschaft;
	}	
	
/** setzt die Operation.
*	@param operation ist die zu setzende Operation
*/
	public void setOperation(String operation) {
		this.operation = operation;
	}

/** gibt die Operation zurück.
*	@return die Operation
*/	
	public String getOperation() {
		return operation;
	}

/** @param winningName der eigenen Klasse.
*/		
	public void setWinningName(String winningName) {
		this.winningName = winningName;
	}

/** @return winningName der eigenen Klasse.
*/	
	public String getWinningName() {
		return winningName;
	}

/** @param losingName der eigenen Klasse.
*/	
	public void setLosingName(String losingName) {
		this.losingName = losingName;
	}

/** @return losingName der eigenen Klasse.
*/	
	public String getLosingName() {
		return losingName;
	}

/** @param propertyName der Eigenschaft.
*	@param operation gewünscht
*/	
	public Regel(String propertyName, String operation) {
		nameEigenschaft = propertyName;
		this.operation = operation;
		istString = false;
	}

/** @param propertyName der Eigenschaft.
*	@param winningName gewünscht
*	@param losingName gewünscht
*/		
	public Regel(String propertyName, String winningName, String losingName) {
		nameEigenschaft = propertyName;
		this.winningName = winningName;
		this.losingName = losingName;
		istString = true;
	}

/** @return die regel als Sprint.
*/	
	public String[] printRegel() {
		String[] stringReturn = new String[1];
		if (!istString) {
			stringReturn[0] = nameEigenschaft + operation;
			return stringReturn;
		}
		
		stringReturn[0] = nameEigenschaft + ":" + winningName + ">" + losingName;
		return stringReturn;
	}	
}