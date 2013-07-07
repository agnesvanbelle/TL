package data;

public class NameContainer
{
	private static int ID_NEXT = 0;
	
	public final int    ID;
	public final String name; // Equal names <-> Equal NameContainers.
	
	public NameContainer metacontainer; // Parent container (for meta-features and such).
	
	NameContainer()
	{
		this.ID   = ID_NEXT++;
		this.name = "entity-" + ID; // Entities with no name get a generic one.
		
		metacontainer = null;
	}
	
	NameContainer(String name)
	{
		this.ID   = ID_NEXT++;
		this.name = name;
		
		metacontainer = null;
	}
	
	// Methods generated automatically by Eclipse:

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameContainer other = (NameContainer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
urn false;
		NameContainer other = (NameContainer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
