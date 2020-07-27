package model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import observerpattern.ObservableHashMap;
import observerpattern.Observer;

/**
 * 
 * @author Sriram, Zhi Tan
 * Class which inherits (is-a) FHIRPerson. It does everything a FHIRPerson does
 * but also has more specific implementations and has more instance variables to
 * fit what a FHIRPractitioner in the context of our app has.
 *
 */
public class FHIRPractitioner extends FHIRPerson {
	public static final String URL_STRING_SECTION_FOR_PRACTITIONER = "Practitioner/";	
	private ObservableHashMap<Tuple<String, String>, FHIRPatient> patients = new ObservableHashMap<Tuple<String, String>, FHIRPatient>();
	
	
	/**
	 * Retrieve the data for a practitioner with the resource id to a practitioner resource
	 * and populate the FHIRPractitioner object with the data retrieved.
	 * Returns true if operation succeeded, otherwise return false
	 * @param practitionerResourceID the resource id for a practitioner resource
	 * @return boolean indicating whether the operation succeeded
	 */
	// calls the retrievePractitionerDataFromServerAndInit() to initialize practitioner object and then use
	// the obtained practitioner's identifier to find all patients related to this practitioner through
	// retrievePatientsOfPractitionerDataFromServerAndInit
	public boolean retrieveDataFromServerAndInitWithLatest(String practitionerResourceID) {

		Boolean booleanFlag1 = this.retrievePractitionerDataFromServerAndInitWithLatest(practitionerResourceID); // get practitioner's basic details (RENAME)
		Boolean booleanFlag2 = false;
		try {
			booleanFlag2 = this.retrievePatientsOfPractitionerDataFromServerAndInitWithLatest(this.getIdentifier()); // get all the patients of the practitioner
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return (booleanFlag1 && booleanFlag2);
			
	}
	
	// get Practitioner's data like practitioner's name and etc. and init the practitioner object with these data
	private boolean retrievePractitionerDataFromServerAndInitWithLatest(String anyResourceId) {
		// set identifier of FHIRPractitioner and get the jsonObject related to the resource id.
		JSONObject jsonObject = super.getJSONObjectRelatedToResourceAndSetIdentifier(FHIRClient.BASE_URL + FHIRPractitioner.URL_STRING_SECTION_FOR_PRACTITIONER + anyResourceId + "?" + FHIRClient.JSON_FORMAT); 
		
		if (jsonObject == null) { // if it the jsonObject was null (failed to get)
			return false;
		}
		
		
		String familyName = jsonObject.getJSONArray("name").getJSONObject(0).getString("family"); // get the familyName of the Practitioner
		String givenName = jsonObject.getJSONArray("name").getJSONObject(0).getJSONArray("given").getString(0); // get the givenName of the Practitioner
		String pracIdentifierSystem = jsonObject.getJSONArray("identifier").getJSONObject(0).getString("system"); // get the identifier system the practitioner uses
		String pracIdentifierValue = jsonObject.getJSONArray("identifier").getJSONObject(0).getString("value"); // get the value relating to the identifier system of the practitioner
		this.setFamilyName(familyName); // set the familyName of the practitioner
		this.setGivenName(givenName); // set the givenName of the practitioner
		this.setIdentifier(pracIdentifierSystem, pracIdentifierValue); // set the identifier of the Practitioner
		
		return true;
	}
	
	/**
	 * A constructor that initializes FHIRPractitioner and all its instance variables
	 * to default values
	 */	
	public FHIRPractitioner() {
		super("", "", "", "");
	}

	/**
	 * A constructor that initializes FHIRPractitioner according to the arguments/parameters passed in
	 * @param givenName the given name of the practitioner
	 * @param familyName the family name of the practitioner
	 * @param identifierSystem the identifierSystem that the patient uses (for e.g. passport)
	 * @param identifierValue the identifierValue that the patient uses (for e.g. passport number)
	 */
	public FHIRPractitioner(String givenName, String familyName, String identifierSystem, String identifierValue) {
		super(givenName, familyName, identifierSystem, identifierValue);
	}
	
	/**
	 * Add observers for the ObservableHashMap of the practitioner.
	 * @param observer Any observers
	 */
	// Design decision: I have a ObservableHashMap, if you want to talk to it, you have to go through me.
	public void addObserversForTheObservableHashMapOfPractitioner(Observer observer) {
		patients.attach(observer);
	}
	
	/**
	 * Given a practitioner identifier, retrieve the data for the patients of practitioner
	 * @param pracIdentifier the identifier of the practitioner
	 * @return boolean indicating whether the operation succeeded.
	 * @throws IOException
	 */
	// patients are part of a practitioner's data. But getting patients is a big thing, it is not piled together with obtaining practitioner's data through retrievePractitionerDataFromServerAndInit
	private boolean retrievePatientsOfPractitionerDataFromServerAndInitWithLatest(Tuple<String, String> pracIdentifier) throws IOException {
		this.setIdentifier(pracIdentifier); // set the identifier of the FHIRPractitioner
		
		// Construct the urlString for page of getting the patient data of the practitioner
		String urlString=FHIRClient.BASE_URL + "Encounter?practitioner.identifier=" +
						URLEncoder.encode((this.getIdentifierSystem() + "|"), "UTF-8") + 
						this.getIdentifierValue() + "&" + FHIRClient.JSON_FORMAT;
		
		JSONObject jsonObject = FHIRClient.getJSONObjectUsingGETRequest(urlString); // get the JSONObject for the url above
		if (jsonObject == null) { // if it was null, return false cause data retrieval has failed
			return false;
		}

		int loopCounter=0; 
		boolean loopFlag=true;
		//  create a patientsHashMap the resource ids of the patients, alot of duplicates, use HashMap to reduce access to the server unnecessarily
		HashMap<String, String> patientsHashMap = new HashMap<String, String>();
		while (loopFlag) { // while last page not reached
			JSONArray entryID = jsonObject.getJSONArray("entry");
			int entryIDLength = entryID.length();
			
			
			while (loopCounter < entryIDLength) { // for each entry on the page, put it in the HashMap to remove duplicates
				JSONObject subject = entryID.getJSONObject(loopCounter).getJSONObject("resource").getJSONObject("subject");
				String subjectReference = subject.getString("reference");
				String subjectID = subjectReference.substring(8); // 8 character offset for Patient/
				patientsHashMap.put(subjectID, subjectID);
				loopCounter++;
				
			}
			loopCounter=0;
			JSONArray linkID= jsonObject.getJSONArray("link");
			if(linkID.getJSONObject(1).getString("relation").equalsIgnoreCase("next")) // if has next Page
			{
				urlString=linkID.getJSONObject(1).getString("url");
				try {
					jsonObject = FHIRClient.getJSONObjectUsingGETRequest(urlString);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			else { // no more next page
				loopFlag=false;
			}
		}
		// create another HashMap: backEndHashMap to retrieve only patients that are unique (different identifiers)
		HashMap<Tuple<String, String>, FHIRPatient> backEndHashMap = new HashMap<Tuple<String, String>, FHIRPatient>();
		for(String uniquePatientID: patientsHashMap.values()){
			FHIRPatient p = new FHIRPatient();
			Tuple<String, String> identifierOfP = p.retrieveIdentifierFromServer(uniquePatientID); // NOT FULL YET
			
			backEndHashMap.put(identifierOfP, p);
		}
		// for each of the unique patients, retrieve the data of the patient from the server and then put it into the patients under the Practitioner
		for(HashMap.Entry<Tuple<String, String>, FHIRPatient> entry : backEndHashMap.entrySet()) {
		    Tuple<String, String> key = entry.getKey();
		    FHIRPatient patient = entry.getValue();
		    
		    if (!patient.retrieveDataFromServerAndInitWithLatest(key)) {
		    	return false;
		    }
		}
		patients.putAllThenNotify(backEndHashMap); // only notify once after putting every thing.
		
		return true;

		
	}

	/**
	 * Get the patients of the practitioner, a copy is returned cause we don't want to permit mutations
	 * @return
	 */
	public ObservableHashMap<Tuple<String, String>, FHIRPatient> getPatients() {
		return new ObservableHashMap<Tuple<String, String>, FHIRPatient>(this.patients);
	}

	/**
	 * Retrieve data of the practitioner's patients only cause if identifier is given, it is assumed that the practitioner's basic data has been retrieved
	 * @param identifierOfPractitioner the practitioner's identifier.
	 * @return a boolean indicating whether the operation succeeded
	 */
	public boolean retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPractitioner) throws IOException {
		return retrievePatientsOfPractitionerDataFromServerAndInitWithLatest(identifierOfPractitioner);
	}
	
	/**
	 * The string representation of a FHIRPractitioner
	 */
	public String toString() {
		return super.toString();
	}


}
