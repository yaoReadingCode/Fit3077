package observerpattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
/**
 * 
 * @author sriram, Zhi Tan
 *
 * @param <T> Generic Class T
 * @param <V> Generic Class V
 * 
 * Class that is an observable, so everytime data changes all observers are notified.
 * It delegates most of its tasks to a hashmap
 * 
 * Ref: Some Javadocs are obtained from the HashMap Javadocs (https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#values--)
 * 
 */
public class ObservableHashMap<T, V> extends Observable {
	// only 2 original methods: putAllThenNotify(ObservableHashMap) and putAllThenNotify(HashMap)
	// tasks delegated to HashMap. Only calling notifyDataSetChanged when appropriate
	private HashMap<T, V> underlyingHashMap; 

	
	public ObservableHashMap() {
		underlyingHashMap = new HashMap<T, V>();
	}
	
	public ObservableHashMap(ObservableHashMap<T, V> observableHashmap) {
		super();
		this.underlyingHashMap = observableHashmap.underlyingHashMap;
	}
	
	/**
	 * Clears the hashmap
	 */
	public void clear() {
		underlyingHashMap.clear();
	}

	/**
	 * returns a clone of the hashmap
	 */
	public Object clone() {
		return underlyingHashMap.clone();
	}

	/**
	 * Checks if the Hashmap contains the key
	 * @param key the key to be checked in the hashmap
	 * @return true if key is present, else false
	 */
	public boolean containsKey(Object key) {
		return underlyingHashMap.containsKey(key);
	}

	/**
	 * Checks if the Hashmap contains the value
	 * @param value the value to be checked in the hashmap
	 * @return true if value is present in the hashmap, else false
	 */
	public boolean containsValue(Object value) {
		return underlyingHashMap.containsValue(value);
	}

	/**
	 * Makes a set out of the entries in the hashmap
	 * @return a set of entries in the hashmap
	 */
	public Set<Entry<T, V>> entrySet() {
		return underlyingHashMap.entrySet();
	}

	/**
	 *  Compares the specified object with this map for equality
	 *  @return true if the specified object is equal to this map
	 */
	public boolean equals(Object arg0) {
		return underlyingHashMap.equals(arg0);
	}

	/**
	 * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception. 
	 * @param arg0 The action to be performed for each entry
	 */
	public void forEach(BiConsumer<? super T, ? super V> arg0) {
		underlyingHashMap.forEach(arg0);
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
	 */
	public V get(Object key) {
		return underlyingHashMap.get(key);
	}

	/**
	 * 
	 * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key
	 * @param arg0 the key whose associated value is to be returned
	 * @param arg1 the default mapping of the key
	 * @return the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key
	 */
	public V getOrDefault(Object arg0, V arg1) {
		return underlyingHashMap.getOrDefault(arg0, arg1);
	}
	
	/**
	 * Inside this method, notifyDatasetChanged is called after every put if and only if data was different. 
	 * Set every entry in the other hashmap into the current underlying hashmap.
	 * 
	 * @param other the other observable hashmap
	 */
	public void putAllThenNotify(ObservableHashMap<T, V> other) { 
		boolean hasChanges = false;
		for (Map.Entry<T, V> pair: other.entrySet()) {
			T key = pair.getKey();
			V value = pair.getValue();
			if (!value.equals(this.get(key))) {
				underlyingHashMap.put(pair.getKey(), pair.getValue());
				hasChanges = true;
			}
		}
		if (hasChanges) {
			this.notifyDatasetChanged();
		}
	}

	/**
	 * the hash code value for this map
	 * @return the hash code value for this map
	 *
	 */
	public int hashCode() {
		return underlyingHashMap.hashCode();
	}

	/**
	 * Returns true if this map contains no key-value mappings.
	 * @return true if this map contains no key-value mappings
	 */
	public boolean isEmpty() {
		return underlyingHashMap.isEmpty();
	}

	/**
	 * 
	 * @return a set view of the keys contained in this map
	 */
	public Set<T> keySet() {
		return underlyingHashMap.keySet();
	}

	/**
	 * Removes the entry for the specified key only if it is currently mapped to the specified value
	 * @param arg0 key with which the specified value is associated
	 * @param arg1 value expected to be associated with the specified key
	 * @return true if the value was removed
	 */
	public boolean remove(Object arg0, Object arg1) {
		this.notifyDatasetChanged();
		return underlyingHashMap.remove(arg0, arg1);
	}

	/**
	 * Replaces the entry for the specified key only if currently mapped to the specified value.
     *
	 * @param arg0 key with which the specified value is associated
	 * @param arg1 value expected to be associated with the specified key
	 * @param arg2 value to be associated with the specified key
	 * @return true if the value was replaced
	 */
	public boolean replace(T arg0, V arg1, V arg2) {
		this.notifyDatasetChanged();
		return underlyingHashMap.replace(arg0, arg1, arg2);
	}

	/**
	 * returns the size of the hashmap
	 * @return the size of the hashmap
	 */
	public int size() {
		return underlyingHashMap.size();
	}
	
	/**
	 * returns a Stringified version of the hashmap
	 */
	public String toString() {
		return underlyingHashMap.toString();
	}

	/**
	 * Returns a Collection view of the values contained in this map
	 * @return a collection view of the values contained in this map
	 */
	public Collection<V> values() {
		return underlyingHashMap.values();
	}
	
	/**
	 * 
	 * Inside this method, notifyDatasetChanged is called after every put if and only if data was different. 
	 * Set every entry in the other hashmap into the current underlying hashmap.
	 * 
	 * @param other the other hashmap
	 */
	public void putAllThenNotify(HashMap<T, V> other) {
		boolean hasChanges = false;
		for (Map.Entry<T, V> pair: other.entrySet()) {
			T key = pair.getKey();
			V value = pair.getValue();
			if (!value.equals(this.get(key))) {
				underlyingHashMap.put(pair.getKey(), pair.getValue());
				hasChanges = true;
			}
		}
		if (hasChanges) {
			this.notifyDatasetChanged();
		}
	}
	
	/**
	 * Associates the specified value with the specified key in this map. If the object to put in is different, notifyDatasetChanged is called. 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	public V put(T key, V value) {
		V retVal;
		if (value.equals(this.get(key))) {
			retVal = underlyingHashMap.put(key, value);
		}
		else {
			retVal = underlyingHashMap.put(key, value);
			this.notifyDatasetChanged();
		}
		return retVal;
		
	}

	/**
	 * 
	 * @param arg0 key with which the specified value is associated. After replacing, notifydatsetchanged is called.
	 * @param arg1 value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping for the key
	 */
	public V replace(T arg0, V arg1) {
		V retVal = underlyingHashMap.replace(arg0, arg1);
		this.notifyDatasetChanged();
		return retVal;
	}

	/**
	 * Removes the mapping for a key from this map if it is present. After removing, notifydatsetchanged is called.
	 * @param key key whose mapping is to be removed from the map
	 * @return the previous value associated with key, or null if there was no mapping for key
	 */
	public V remove(Object key) {
		V retVal = underlyingHashMap.remove(key);
		this.notifyDatasetChanged();
		return retVal;
	}
}
