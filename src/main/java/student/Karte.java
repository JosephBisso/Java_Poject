package student;

public class Karte  {
	
	private String name;
	private Eigenschaft[] property;
	private int anzEigenschaft = 0;
	private String value = "";

	public Karte(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setProperty(String propertyName, String value, String typ) {
		if (anzEigenschaft > 0) {
			Eigenschaft[] zwischenLage = new Eigenschaft[anzEigenschaft];
			for (int i = 0; i < anzEigenschaft; i++) {
				zwischenLage[i] = property[i];
			} 
			property = new Eigenschaft[++anzEigenschaft];
			for (int i = 0; i < anzEigenschaft - 1; i++) {
				property[i] = zwischenLage[i];
			}					
		}
		if (anzEigenschaft == 0) {
			property = new Eigenschaft[++anzEigenschaft];
		}
		
		property[anzEigenschaft - 1] = new Eigenschaft (propertyName, typ);
		this.value += value + ";";
	}
	
	public void setProperty(String propertyName, int value, String typ) {
		if (anzEigenschaft > 0) {
			Eigenschaft[] zwischenLage = new Eigenschaft[anzEigenschaft];
			for (int i = 0; i < anzEigenschaft; i++) {
				zwischenLage[i] = property[i];
			} 
			property = new Eigenschaft[++anzEigenschaft];
			for (int i = 0; i < anzEigenschaft - 1; i++) {
				property[i] = zwischenLage[i];
			}					
		}
		if (anzEigenschaft == 0) {
			property = new Eigenschaft[++anzEigenschaft];
		}
		
		property[anzEigenschaft - 1] = new Eigenschaft (propertyName, typ);
		this.value += Integer.toString(value) + ";";
	}
	
	
	public Eigenschaft[] getProperty() {
		return this.property;
	}
	
	public String[] getValue() {
		return value.split(";");
	}
	
	public Regel[] getRegel() {
		int counter = 0;
		if (property == null) {
			return null;
		} else {
			for (Eigenschaft eigenschaft : property) {
				if (eigenschaft.getRegel() == null) {
					return null;
				} else {
					for (Regel regel : eigenschaft.getRegel()) {
						counter++;
					}
				}
			}
		}
		Regel[] regelReturn = new Regel[counter];
		int m = 0;
		while(m < counter) {
			for (int i = 0; i < property.length; i++) {
				for(int j = 0; j < property[i].getRegel().length; j++) {
					regelReturn[m++] = property[i].getRegel()[j]; 
				}
			}
		}
		return regelReturn;
	}
}