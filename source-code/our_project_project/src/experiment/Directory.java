package experiment;

import java.util.ArrayList;

public class Directory {
	
	public String name;
	
	public ArrayList<Directory> children = null;
	
	public Directory(String name) {
		this.name = name;
		children = new ArrayList<Directory>();
	}
	
	public void add(Directory d) {
		children.add(d);
	
	}
	
	public Directory get(int index) {
		return children.get(index);
	}

}
  