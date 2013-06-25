package glue;

import art.framework.utils.*;
import java.util.ArrayList;

import art.experiments.WERenums;

public class Directory {

	public String name;

	public ArrayList<Directory> children = null;

	public Directory(String name) {
		this.name = name;
		children = new ArrayList<Directory>();
	}

	public void addChild(String s) {
		children.add(new Directory(s));
	}

	public void addChild(Directory d) {
		children.add(d);
	}
	
	public void removeChildren() {
		children.clear();
	}

	
	public int leafCount() {
		int sum=0;
		if (!this.children.isEmpty()) {
			for (Directory child : this.children) {
				sum+= child.leafCount();
			}
		}
		else {
			return 1;
		}
		return sum;
	}

	public void add(String[] ss) {
		for (String s : ss)
			children.add(new Directory(s));
	}

	public ArrayList<Directory> getChildren() {
		return children;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.stringHelper(sb, 0);
		return sb.toString();
	}

	public String stringHelper(StringBuilder sb, int depth) {

		//StringBuilder sb = new StringBuilder();

		if (this.children.isEmpty()) {
			for (int i = 0; i < depth; i++) {
				sb.append(" ");
			}
			sb.append(this.name);

			sb.append("\n");
		}
		else {
			for (int i = 0; i < depth; i++) {
				sb.append(" ");
			}
			sb.append(this.name + "\n");
			for (Directory child : this.children) {
				//	System.out.println("child: "  + child.name);
				child.stringHelper(sb, depth + 2);
				//	System.out.println("childString: " + childString);
				//sb.append(childString);
			}
		}

		return sb.toString();

	}

	public void addChildToAllLeafs(Directory d) {

		if (!this.children.isEmpty()) {
			for (Directory child : this.children) {
				child.addChildToAllLeafs(d);
			}
		}
		else {
			addChild(d);
		}

	}
	
	public void addChildToAllLeafs(String s ) {

		if (!this.children.isEmpty()) {
			for (Directory child : this.children) {
				child.addChildToAllLeafs(s);
			}
		}
		else {
			addChild(new Directory(s));
		}

	}

	public void addChildToAllLeafs(String[] ss) {

		if (!children.isEmpty()) {
			for (Directory child : this.children) {
				child.addChildToAllLeafs(ss);
			}
		}
		else {
			for (String s : ss) {
				this.addChild(new Directory(s));
			}
		}

	}

	
	public String getDirName(String rootDir) {
		String newRootDir = rootDir + this.name + "/";
		return newRootDir;
	}

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
		Directory other = (Directory) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Directory get(int index) {
		return children.get(index);
	}

	public Directory get(String indexName) {
		//	System.out.println("children.size(): " + children.size());
		//for (int i=0; i < children.size(); i++) {
		//System.out.println(children.get(i).name);
		//}
		int index = children.indexOf(new Directory(indexName));
		//System.out.println("index:" + index);
		if (index != -1) {
			return get(index);
		}
		else {
			return null;
		}
	}

}
