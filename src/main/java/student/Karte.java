package student;

public class Karte  {
	
	private String name;
	private Eigenschaft[] property;
	private int anzEigenschaft = 0;
	private String value;

	public void setName(String name) {
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
		this.value += value + ",";
	}
	public Eigenschaft[] getProperty() {
		return this.property;
	}
	
	public String[] getValue() {
		return value.split(",");
	}
}