package art.framework.example.parser;


public abstract class Predicate implements Comparable<Predicate> {

	protected String variableName;

	public Predicate(String variableName) {
		this.variableName = variableName;
	}

	public int compareTo(Predicate rel) {
		return this.toString().compareTo(rel.toString());
	}

	public String getName() {
		return variableName;
	}

	public String getVariableName() {
		return variableName;
	}
	
	public abstract String[] getVars();


}
