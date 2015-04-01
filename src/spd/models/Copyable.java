package spd.models;

public interface Copyable<T> {
	T getCopy();
	T createNewForCopy();
	void fromObject(T object);
}
