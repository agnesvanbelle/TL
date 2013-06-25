package experiment;

import java.util.ArrayList;

public class Directory {
	
	public String name;
	
	public ArrayList<Directory> children = null;
	
	public Directory() {
		children = new ArrayList<Directory>();
	}

}
