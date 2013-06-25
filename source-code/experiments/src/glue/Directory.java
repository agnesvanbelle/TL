package glue;

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
	
	public ArrayList<Directory> getChildren() {
		return children;
	}
	
	
	public Directory get(int index) {
		return children.get(index);
	}
	
	public Directory get(String indexName) {
		int index = children.indexOf(indexName);
		return get(index);
	}

}
  
