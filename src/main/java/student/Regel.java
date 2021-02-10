package student;

public class Regel  {
	
	private String nameEigenschaft;
	private String operation;
	private String winningName;
	private String losingName;
	private boolean istString;

	public boolean istString() {
		return istString;
	}
	public String getName() {
		return nameEigenschaft;
	}	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setWinningName(String winningName) {
		this.winningName = winningName;
	}
	
	public String getWinningName() {
		return winningName;
	}
	
	public void setLosingName(String losingName) {
		this.losingName = losingName;
	}
	
	public String getLosingName() {
		return losingName;
	}
	
	public Regel(String propertyName, String operation) {
		nameEigenschaft = propertyName;
		this.operation = operation;
		istString = false;
	}
	
	public Regel(String propertyName, String winningName, String losingName) {
		nameEigenschaft = propertyName;
		this.winningName = winningName;
		this.losingName = losingName;
		istString = true;
	}
	
}