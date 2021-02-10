package student;

private class Eigenschaft  {
	
	private String name;
	private String typ;
	private Regel[] regel;
	private anzRegel = 0;

	public Eigenschaft(String name, String typ) {
		this.name = name;
		this.typ = typ;
	}

	public string getName() {
		return this.name;
	}
	
	public string getTyp() {
		return this.typ;
	}
	
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
	
	public Regel[] getRegel() {
		return regel;
	}
	
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