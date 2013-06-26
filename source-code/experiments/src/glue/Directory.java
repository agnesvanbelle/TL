package glue;

import art.framework.utils.*;
import java.util.ArrayList;

import art.experiments.WERenums;

public class Directory<N> {

	public N name;

	public ArrayList<Directory<N>> children = null;

	public Directory(N name) {
		this.name = name;
		children = new ArrayList<Directory<N>>();
	}
	
	public N name () {
		return this.name;
	}

	public void addChild(N s) {
		children.add(new Directory<N>(s));
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

	public void add(N[] ss) {
		for (N s : ss)
			children.add(new Directory<N>(s));
	}

	public ArrayList<Directory<N>> getChildren() {
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

	public void addChildToAllLeafs(Directory<N> d) {

		if (!this.children.isEmpty()) {
			for (Directory<N> child : this.children) {
				child.addChildToAllLeafs(d);
			}
		}
		else {
			addChild((N)d);
		}

	}
	
	public void addChildToAllLeafs(N s ) {

		if (!this.children.isEmpty()) {
			for (Directory<N> child : this.children) {
				child.addChildToAllLeafs(s);
			}
		}
		else {
			addChild((N) s);
		}

	}

	public void addChildToAllLeafs(N[] ss) {

		if (!children.isEmpty()) {
			for (Directory<N> child : this.children) {
				child.addChildToAllLeafs(ss);
			}
		}
		else {
			for (N s : ss) {
				this.addChild((N)s);
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
		Directory<?> other = (Directory<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Directory<N> get(int index) {
		return children.get(index);
	}

	public Directory<?> get(N indexName) {
		//	System.out.println("children.size(): " + children.size());
		//for (int i=0; i < children.size(); i++) {
		//System.out.println(children.get(i).name);
		//}
		int index = children.indexOf(new Directory<N>(indexName));
		//System.out.println("index:" + index);
		if (index != -1) {
			return get(index);
		}
		else {
			return null;
		}
	}

}
