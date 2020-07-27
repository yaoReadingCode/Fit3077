package model;

/**
 * 
 * @author sriram, Zhi Tan
 *
 * @param <Type1> Generic Type 1
 * @param <Type2> Generic Type 2
 * 
 * This class is a Tuple
 */

public class Tuple <Type1, Type2>{
	public final Type1 first;
	public final Type2 second;
	public Tuple(Type1 first, Type2 second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * returns the hash value representation of the tuple object. This is necessary to be used with the hashmap.
	 * @return the hash value representation of the tuple object
	 */
	@Override
	public int hashCode() { // supports hashCode() so that it can be used in HashMaps
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	
	/**
	 * Auto generated .equals method which checks if an object is conceptually
	 * equal to the current object
	 * @param obj the other Object to compare with the current object
	 * @return boolean indicating whether this is equal to the object in comparizon
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // if same object address, same object
			return true;
		if (obj == null) // if the object compared to this object is null, return false
			return false;
		if (getClass() != obj.getClass()) // if objects not of the same class, different
			return false;
		Tuple<Type1, Type2>other = (Tuple<Type1, Type2>) obj;
		if (first == null) {  // ensuring that first is null before entering further
			if (other.first != null)  // ensuring that other tuple's first value is not null before entering further
				return false; // returns false meaning they are not equal
		} else if (!first.equals(other.first))
			return false; // returns false if the first values dont match
		if (second == null) { // ensuring that second is null before entering further
			if (other.second != null) // ensuring that other tuple's second value is not null before entering further
				return false;  // returns false meaning they are not equal
		} else if (!second.equals(other.second))
			return false; // returns false if the second values dont match
		return true;// return true otherwise meaninf they are equal
	}
	
	
	@Override
	public String toString() { 
		return first.toString()+"    "+second.toString();
	}
}
