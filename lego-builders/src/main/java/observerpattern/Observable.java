package observerpattern;
import java.util.ArrayList;
import java.util.List;
/***
 * 
 * @author sriram, Zhi Tan
 * Abstract class that keeps track of all the observers observing a paticular observable,
 * and at the same provides a set of methods that it's concrete classes will inherit
 *
 */
public abstract class Observable {
	// maintains a list of Observers that it keeps tracks of for a particular 
	// type of observable that extends this class
	private List<Observer> observers  = new ArrayList<Observer>();
	
	/**
	 * Adding Observers to the list of observers
	 * @param observer the observer that needs to observe an observable
	 */
	public void attach(Observer observer) {
		this.observers.add(observer);
	}
	
	/**
	 * Removing observers from the list of observers
	 * @param observer the observer that no longer needs to observe an observable
	 */
	public void detach(Observer observer) {
		this.observers.remove(observer);
	}
	
	/**
	 * Notifies the list of observers that the observable has changed
	 */
	public void notifyDatasetChanged() {
		for (Observer observer: this.observers) {
			observer.update();
		}
	}
	
}
