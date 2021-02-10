package student;

private class Karte  {
	
	private String name;
	private Eigenschaft property;
	private string value;

	public void setName(String name) {
		this.name = name;
	}
	
	public string getName() {
		return this.name;
	}
	
	public void setProperty(String propertyName, String value, String typ) {
		property = new Eigenschaft (propertyName, typ);
		this.value = value;
	}
	public Eigenschaft getProperty() {
		return this.property;
	}
}