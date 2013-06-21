package art.framework.utils;


public abstract class AbstractPredicate implements Comparable<AbstractPredicate> {

	@Override
	public int compareTo(AbstractPredicate rel) {
		return this.toString().compareTo(rel.toString());
	}
}
