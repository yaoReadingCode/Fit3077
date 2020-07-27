package observerpattern;
/**
 * 
 * @author sriram, Zhi Tan
 * 
 * Interface for concrete observers that want to observe an observable
 *
 */
public interface Observer {
	// All concrete Observer's must implement the update method to update the value 
	// of the observable that they are tracking
	public abstract void update();
}
