package model;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 
 * @author Sriram, Zhi Tan
 * Class which inherits (is-a) FHIRPerson. It does everything a FHIRPerson does
 * but also has more specific implementations and has more instance variables to
 * fit what a FHIRPatient in the context of our app has.
 *
 */
public class FHIRPatient extends FHIRPerson{
	public static final String URL_STRING_SECTION_FOR_PATIENT = "Patient/";
	private String country;
	private String state;
	private String city;
	private String birthday; 	
	private String gender;
	private FHIRCholesterol cholesterol = null;
	private FHIRBloodPressure bloodPressure = null;
	
	/**
	 * A constructor that initializes FHIRPatient and all its instance variables i.e.
	 * to default values
	 */	
	public FHIRPatient() {
		super("", "", "", "");


	}
	

	/**
	 * A constructor that initializes FHIRPatient according to the arguments/parameters passed to it.
	 * @param givenName the given name of the patient
	 * @param familyName the family name of the patient
	 * @param identifierSystem the identifierSystem that the patient uses (for e.g. passport)
	 * @param identifierValue the identifierValue that the patient uses (for e.g. passport number)
	 * @param country the country the patient is in
	 * @param state the state the patient is in
	 * @param city the city the patient is in
	 * @param birthday the birthday of the patient
	 * @param gender the gender of the patient
	 * @param cholestrolValue the latest FHIRObservation, in our current case: FHIRCholesterol of the patient 
	 */
	public FHIRPatient(String givenName, String familyName, String identifierSystem, String identifierValue, String country,
			String state, String city, String birthday, String gender, FHIRCholesterol cholestrolValue, FHIRBloodPressure bloodPressure) {
		super(givenName, familyName, identifierSystem, identifierValue);
		this.country = country;
		this.state = state;
		this.city = city;
		this.birthday = birthday;
		this.gender = gender;
		this.cholesterol = cholestrolValue;
		this.bloodPressure = bloodPressure;
	}
	
	/**
	 * Retrieve the data for a patient with the specified identifier
	 * and populate the FHIRPatient object with the data retrieved.
	 * Return true if operation succeeded, otherwise return false;
	 * @param identifierOfPatient the identifier of a patient
	 * @return boolean indicating whether the operation succeeded
	 */
	public boolean retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPatient) throws IOException {
		// build the URL String for the page of getting the latest data of a certain Patient and get the JSONObject for the url
		JSONObject patientPageJSONObject = FHIRClient.getJSONObjectUsingGETRequest(FHIRClient.BASE_URL + FHIRPatient.URL_STRING_SECTION_FOR_PATIENT 
							+ "?identifier=" + URLEncoder.encode(identifierOfPatient.first + "|", "UTF-8") + identifierOfPatient.second + "&" + FHIRClient.JSON_FORMAT); //FHIR CLIENT NOT USED HERE, USED BY SUPERCLASS
			
			
			// if it was null, return false cause data retrieval has failed
			if (patientPageJSONObject == null) {
				return false;
			}

			
			JSONArray entry = patientPageJSONObject.getJSONArray("entry");
			
			
			this.setFamilyName(entry.getJSONObject(0).getJSONObject("resource").getJSONArray("name").getJSONObject(0).getString("family")); // get the family name of the patient and set it
			this.setGivenName(entry.getJSONObject(0).getJSONObject("resource").getJSONArray("name").getJSONObject(0).getJSONArray("given").getString(0)); // get the given name of the patient and set it
			this.gender = entry.getJSONObject(0).getJSONObject("resource").getString("gender"); // get the gender of the patient and set it
			this.birthday = entry.getJSONObject(0).getJSONObject("resource").getString("birthDate"); // get the birthDate of the patient and set it

			JSONArray address = entry.getJSONObject(0).getJSONObject("resource").getJSONArray("address"); // get the address of the patient and set it
			this.city = address.getJSONObject(0).getString("city"); // get the city of the patient and set it
			this.country = address.getJSONObject(0).getString("country"); // get the country of the patient and set it
			this.state = address.getJSONObject(0).getString("state"); // get the state of the patient and set it
			FHIRCholesterol latestCholesterolOfPatient = new FHIRCholesterol(); // create a new FHIRCholesterol object
			FHIRBloodPressure latestBloodPressureOfPatient = new FHIRBloodPressure();
			if (latestCholesterolOfPatient.retrieveDataFromServerAndInitWithLatest(identifierOfPatient)) { // retrieve the latest FHIRCholesterol data and populate the new FHIRCholesterol object
				this.setCholesterol(latestCholesterolOfPatient); // set the FHIRCholesterol of the patient
			}
			if (latestBloodPressureOfPatient.retrieveDataFromServerAndInitWithLatest(identifierOfPatient)) { // retrieve the latest FHIRBloodPressure data and populate the new FHIRBloodPressure object
				this.setBloodPressure(latestBloodPressureOfPatient); // set the FHIRBloodPressure of the patient
			}
		return true;
	}
	
	/**
	 * Get the blood pressure data of the patient
	 * @return the blood pressure data of the patient
	 */
	public FHIRBloodPressure getBloodPressure() {
		return bloodPressure;
	}


	/**
	 * Set the blood pressure of the patient
	 * @param bloodPressure the FHIRBloodPressure of the patient
	 */
	public void setBloodPressure(FHIRBloodPressure bloodPressure) {
		this.bloodPressure = bloodPressure;
	}


	/**
	 * Get the cholesterol data of the patient
	 * @return the cholesterol data of the patient
	 */
	public FHIRCholesterol getCholesterol() {
		return cholesterol;
	}

	/**
	 * Set the cholesterol value of the patient
	 * @param cholesterolValue the FHIRCholesterol of the patient
	 */
	public void setCholesterol(FHIRCholesterol cholesterolValue) {
		this.cholesterol = cholesterolValue;
	}

	/**
	 * Get the country of the patient
	 * @return the country of the patient
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Set the country of the patient
	 * @param country the country of the patient
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Get the state of the patient
	 * @return the state of the patient
	 */
	public String getState() {
		return state;
	}

	/**
	 * Set the state of the patient
	 * @param state the state the patient is in/from
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Get the city of the patient
	 * @return the city of the patient
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Set the city of the patient
	 * @param city the city the patient is in/from
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Get the birthday of the patient
	 * @return the birthdate string of the patient
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * Set the birthday of the patient
	 * @param birthday the birthday of the patient
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * Get the gender of the patient
	 * @return the gender of the patient
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Set the gender of the patient
	 * @param gender the gender of the patient
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Get the URL_STRING_SECTION_FOR_PATIENT
	 * @return the string URL_STRING_SECTION_FOR_PATIENT
	 */
	public static String getUrlStringSectionForPatient() {
		return URL_STRING_SECTION_FOR_PATIENT;
	}
	
	/**
	 * Given a resource id for a Patient, retrieve his/her identifier.
	 * @param anyResourceId the resource id for a patient
	 * @return the identifier of the patient in Tuple<String, String> format
	 */
	public Tuple<String, String> retrieveIdentifierFromServer(String anyResourceId){
		JSONObject jsonObject = super.getJSONObjectRelatedToResourceAndSetIdentifier(FHIRClient.BASE_URL + FHIRPatient.URL_STRING_SECTION_FOR_PATIENT + anyResourceId + "?" + FHIRClient.JSON_FORMAT); //FHIR CLIENT NOT USED HERE, USED BY SUPERCLASS

		if (jsonObject == null) {
			return null;
		}
		else {
			return this.getIdentifier();
		}
		
	}
	
	/**
	 * Retrieve the data for a patient given a patient's resource id.
	 * and populate the FHIRPatient object with the data retrieved.
	 * First look for the identifier from the resource id, then retrieve 
	 * data using this.retrieveDataFromServerAndInit(identifier)
	 * Return true if operation succeeded, otherwise return false;
	 * @param patientResourceId
	 * @return
	 * @throws IOException
	 */
	//https://stackoverflow.com/questions/628950/constructors-vs-factory-methods hence we use constructors
	public boolean retrieveDataFromServerAndInitWithLatest(String patientResourceId) throws IOException { 
		Tuple<String, String> identifier = this.retrieveIdentifierFromServer(patientResourceId);
			
		if (identifier == null) {
			return false;
		}			
		// additional way to get data of patient from server, delegate task to the official method which uses the identifier tuple
		return this.retrieveDataFromServerAndInitWithLatest(identifier);	
	}
	
	
	/**
	 * Create a FHIRPatient, init it and then return the FHIRPatient.
	 * Kind of like a factory method but we don't need a Factory class yet. So just an init for now.
	 * @param identifier identifier of patient, identifier is of form Tuple<String, String> because we care about the identifierSystem and the identifierValue
	 * @return
	 */
	// could be a factory but for now I am not going to need it? Create a patient by retrieving data from server with a certain identifier
	public static FHIRPatient createPatient(Tuple<String, String> identifier) {
		FHIRPatient patient = new FHIRPatient();
		try {
			if (!patient.retrieveDataFromServerAndInitWithLatest(identifier)) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return patient;
	}

	/**
	 * Auto generated .equals method which checks if an object is conceptually
	 * equal to the current object
	 * @param obj the other Object to compare with the current object
	 * @return boolean indicating whether this is equal to the object in comparizon
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // if same object address, then same object
			return true;
		if (obj == null) {// if the object compared to this object is null, return false
			return false;
		}
		if (getClass() != obj.getClass()) { // if objects not of the same class, they are different
			return false;
		}
		FHIRPatient other = (FHIRPatient) obj;
		if (birthday == null) { // if this.birthday null but other.birthday not null
			if (other.birthday != null) {
				return false;
			}
		} else if (!birthday.equals(other.birthday)) { // if this.birthday not same as other.birthday
			return false;
		}
		if (cholesterol == null) { // if this.cholesterol is null but other.cholesterol is not null
			if (other.cholesterol != null) {
				return false;
			}
		} else if (!cholesterol.equals(other.cholesterol)) { // if this.cholesterol not same as other.cholesterol
			return false;
		}
		if (city == null) { // if this.city is null but other.city is not null
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) { // this.city not equal other.city
			return false;
		}
		if (country == null) { // if this.country is null but other.country is not null
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) { // if this.country not equal other.country
			return false;
		}
		if (gender == null) { // if this.gender is null but other.gender not null
			if (other.gender != null) {
				return false;
			}
		} else if (!gender.equals(other.gender)) { // if this.gender not equal other.gender
			return false;
		}
		if (state == null) { // if this.state is null but other.state is not null
			if (other.state != null) {
				return false;
			}
		} else if (!state.equals(other.state)) { // if this.state not equal other.state
			return false;
		}
		return super.equals(obj) && true; // if all test passed, test if super.equals also return true, if yes, return true, otherwise false.
	}
	
	/**
	 * The string representation of a FHIRPatient
	 */
	public String toString() {
		return super.toString();
	}

}
