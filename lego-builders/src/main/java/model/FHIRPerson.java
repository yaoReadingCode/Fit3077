package model;


import java.io.IOException;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Sriram, Zhi Tan
 * Class which serves as a superclass for FHIRPatient and FHIRPractitioner
 * Every person has a givenName, familyName and identifier.
 */
public abstract class FHIRPerson implements FHIRBundle {
	private String givenName;
	private String familyName;
	private Tuple<String, String> identifier; // form of Tuple<String, String> because the identifier must know the system and the value
	
	/**
	 * A constructor that initializes FHIRPerson according to the arguments/parameters passed to it
	 * @param givenName the givenName of the person
	 * @param familyName the familyName of the person
	 * @param identifierSystem the identifierSystem the person uses
	 * @param identifierVal the identifierValue that relates to the identifierSystem used
	 */
	public FHIRPerson(String givenName, String familyName, String identifierSystem, String identifierVal) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.identifier = new Tuple<String, String>(identifierSystem, identifierVal);
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
		if (obj == null) { // if the object compared to this object is null 
			return false;
		}
		if (getClass() != obj.getClass()) { // if objects not of the same class, different
			return false;
		}
		FHIRPerson other = (FHIRPerson) obj;
		if (familyName == null) { // if familyName of this object is null bt other isn't null, different
			if (other.familyName != null) {
				return false;
			}
		} else if (!familyName.equals(other.familyName)) { // if other object's familyName not same as currentObject's familyName
			return false;
		}
		if (givenName == null) { // if givenName of this object is null but other isn't, different
			if (other.givenName != null) {
				return false;
			}
		} else if (!givenName.equals(other.givenName)) { // if other object's givenName not same as current object's givenName
			return false;
		}
		if (identifier == null) { // if identifier of this object is null but other isn;t, different
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) { // if other object's identifier not same as current object's identifier
			return false;
		}
		return true;
	}
	/**
	 * A constructor that initializes FHIRPerson according to the arguments/parameters passed to it
	 * @param givenName the givenName of the person
	 * @param familyName the familyName of the person
	 * @param identifier the Tuple<String, String> version of identifier. (identifierSystem, identifierValue)
	 */
	public FHIRPerson(String givenName, String familyName, Tuple<String, String> identifier) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.identifier = identifier;
	}
	
	/**
	 * Set the given name of the person
	 * @param givenName the given name of the person
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	/**
	 * Get the givenName of the person
	 * @return given name of the person
	 */
	public String getGivenName() {
		return this.givenName;
	}
	
	/**
	 * Set the familyName of the person
	 * @param familyName the family name of the person
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	/**
	 * Get the familyName of the person
	 * @return the family name of the person
	 */
	public String getFamilyName() {
		return this.familyName;
	}
	
	/**
	 * Set the identifier of the patient
	 * @param identifier the identifier tuple, (identifierSystem, identifierValue)
	 */
	public void setIdentifier(Tuple<String, String> identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Set the identifier of the patient
	 * @param identifierSystem the identifier system that the person uses
	 * @param identifierVal the value related to the identifier system that the person has
	 */
	public void setIdentifier(String identifierSystem, String identifierVal) {
		this.identifier = new Tuple<String, String>(identifierSystem, identifierVal);
	}
	
	/**
	 * Get the identifierSystem the person uses
	 * @return the identifier system the person uses
	 */
	public String getIdentifierSystem() {
		return this.identifier.first;
	}
	
	/**
	 * Get the identifierValue that the person uses
	 * @return the identifier value relating to the system that is used of te patient
	 */
	public String getIdentifierValue() {
		return this.identifier.second;
	}
	
	/**
	 * Get the String representation of the person's identifier
	 */
	public String identifierToString() {
		return this.getIdentifierSystem() + "|" + this.getIdentifierValue();
	}
	
	/**
	 * Get the identifier of the person
	 * @return the identifier of the person
	 */
	public Tuple<String, String> getIdentifier() { 
		return this.identifier;
	}

	/**
	 * Given a urlString to a resource id to a patient/practitioner, get the identifier of the patient/practitioner and set the identifier of the person
	 * and return the JSONObject relating to that url
	 * @param urlString url to a patient/practitioner's resource id
	 * @return JSONObject the corresponding JSONObject of the url provided
	 */
	protected JSONObject getJSONObjectRelatedToResourceAndSetIdentifier(String urlString) {
		JSONObject jsonObject;

		try {
			jsonObject = FHIRClient.getJSONObjectUsingGETRequest(urlString); // get the JSON Object
		} catch (IOException e) {
			return null;
		} 

		// set the identifier of the FHIRPerson.
		JSONArray identifierID=jsonObject.getJSONArray("identifier");
		this.setIdentifier(identifierID.getJSONObject(0).getString("system"), identifierID.getJSONObject(0).getString("value"));
		
		return jsonObject;
	}

	/**
	 * The string representation of a FHIRPerson
	 */
	public String toString() {
		return "Name = " + this.givenName + " " + this.familyName + ", Identifier = " + this.getIdentifierValue() ;
	}
}
