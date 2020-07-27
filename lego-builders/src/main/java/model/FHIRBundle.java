package model;

import java.io.IOException;

// retrieve data for a person be it Observation of a person or the person's info itself.
/**
 * 
 * @author Sriram, Zhi Tan
 * Interface that promises to implement the method: 
 * retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPerson) because it is very common
 *
 */
public interface FHIRBundle {
	public boolean retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPerson) throws IOException;
}
