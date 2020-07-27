package model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Sriram, Zhi Tan
 * Class which inherits (is-a) FHIRObservation. It does everything a FHIRObservation
 * does but has a specific loinc CODE that it uses. As it is a FHIRObservation,
 * it is a FHIRBundle and so would need to implement retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPatient)
 *
 */
public class FHIRCholesterol extends FHIRObservation {
	//fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?patient.identifier=https%3A%2F%2Fgithub.com%2Fsynthetichealth%2Fsynthea%7Ca1efa37b-39ed-4b6d-ac48-d171cc7d1d41&code=http%3A%2F%2Floinc.org%7C2093-3&_sort=-date&_count=1&_format=json
	private static final int NUM_OBSERVATION = 1;
	private static final int CHOLESTEROL_POS = 0;
	
	/**
	 * Constructor of FHIRCholesterol that initializes with a default instance for TotalCholesterol Observation
	 */
	public FHIRCholesterol() {
		super(FHIRCholesterol.NUM_OBSERVATION);
	}
	
	// loinc code for Total Cholesterol
	public static final String CODE = "http://loinc.org|2093-3";

//	public void setCholesterol(ArrayList<String> cholesterolObservation) {
//		super.setObservation(FHIRCholesterol.CHOLESTEROL_POS, cholesterolObservation);
//	}
	
	/**
	 * Set the Observation for TotalCholesterol Observation
	 * @param value The value of the TotalCholesterol Observation
	 * @param units The units use for the value
	 * @param system The system of measurement used
	 * @param timeRecorded The time the Observation was recorded
	 */
	public void setCholesterol(String value, String units, String system, String timeRecorded) {
		super.setObservation(FHIRCholesterol.CHOLESTEROL_POS, value, units, system, timeRecorded);
	}
	
	/**
	 * Get the TotalCholesterol Observation (copy) so that the original isn't changed
	 * @return the Observation for TotalCholesterol 
	 */
	public String getCholesterolValue(){
		return this.getObservation(FHIRCholesterol.CHOLESTEROL_POS).getValue();
	}
	
	/**
	 * Get the units for the value of the TotalCholesterol Observation
	 * @return the units for the value of the TotalCholesterol Observation
	 */
	public String getCholesterolUnits() {
		return this.getObservation(FHIRCholesterol.CHOLESTEROL_POS).getUnits();
	}
	
	/**
	 * Get the system of measurement used for recording the value of the TotalCholesterol Observation
	 * @return the system of measurement used for recording the value of TotalCholesterol Observation
	 */
	public String getCholesterolSystem() {
		return this.getObservation(FHIRCholesterol.CHOLESTEROL_POS).getSystem();
	}
	
	/**
	 * Get the time the TotalCholesterol Observation was recorded
	 * @return the time the TotalCholesterol Observation was recorded
	 */
	public String getCholesterolTimeRecorded() {
		return this.getObservation(FHIRCholesterol.CHOLESTEROL_POS).getTimeRecorded();
	}
	
	/**
	 * Retrieve the most recent Cholesterol data for a patient with the specified identifier
	 * and populate the FHIRCholesterol object with the data retrieved.
	 * Return true if operation succeeded, otherwise return false;
	 * @param identifierOfPatient the identifier of a patient
	 * 
	 */
	public boolean retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPatient) throws IOException {
		JSONObject totalCholesterolPageJSONObject = getRelatedJSONObject(identifierOfPatient, 1);
		return retrieveDataFromServerAndInit(totalCholesterolPageJSONObject, 0);
	}
	
	/**
	 * Retrieve the 'numberToReturn' most recent TotalCholesterol data for a patient with the specified identifier
	 * Return the related JSONObject if operation succeeded, otherwise return null;
	 * @param identifierOfPatient the identifier of a patient
	 * @param numberToReturn the number of Observations to get, the count param of FHIR get request
	 * @return the JSONObject which contains the data for the numberToReturn most recent Observation(s)
	 */
	@Override
	public JSONObject getRelatedJSONObject(Tuple<String, String> identifierOfPatient, int numberToReturn) throws IOException {
		// TODO Auto-generated method stub
		String urlStrForLatestCholesterolOfPatient = FHIRClient.BASE_URL + FHIRObservation.URL_STRING_SECTION_FOR_OBSERVATION 
				+ "?patient.identifier=" + URLEncoder.encode(identifierOfPatient.first + "|", "UTF-8") + identifierOfPatient.second + "&" + "code=" + URLEncoder.encode(FHIRCholesterol.CODE, "UTF-8") 
				+ "&" + FHIRClient.SORT_PARAMETER + FHIRClient.DESCENDING_DATE + "&" + FHIRClient.COUNT_PARAMETER + String.valueOf(numberToReturn) + "&" + FHIRClient.JSON_FORMAT;
	
		return FHIRClient.getJSONObjectUsingGETRequest(urlStrForLatestCholesterolOfPatient);
//		return null;
	}
	
	/**
	 * Given a JSONObject for total cholesterol, retrieve the data at ith pos related to it. 
	 * The i == 0 is the most recent while i == 1 is the 2nd most recent.
	 * @param cholesterolPageJSONObject the JSONObject containing the Observation data that is queried
	 * @param i the position of the Observation in the JSONObject
	 * @return a boolean indicating whether extraction of data succeeded or not.
	 */
	public boolean retrieveDataFromServerAndInit(JSONObject cholesterolPageJSONObject, int i) { // i is pos, 0 is for most recent, 1 for 2nd most recent
		// if it was null, return false cause data retrieval has failed
		if (cholesterolPageJSONObject == null) {
			return false;
		}

		// if the subject/patient of the specified identifier doesn't have Cholesterol value, return false.
		if (cholesterolPageJSONObject.getInt("total") == 0) { // cholesterol page has "total"
			return false;
		}
		
		// go to the JSONObject (page) containing the data that needs to be retrieved.
		if (i >= cholesterolPageJSONObject.getInt("total")) {
			// if there is next page, go to next page
			if (cholesterolPageJSONObject.getJSONArray("link").length() >= 2 && cholesterolPageJSONObject.getJSONArray("link").getJSONObject(1).getString("relation").equalsIgnoreCase("next")) {
				String urlString = cholesterolPageJSONObject.getJSONArray("link").getJSONObject(1).getString("url");
				i = i - cholesterolPageJSONObject.getInt("total");
				try {
					return retrieveDataFromServerAndInit(FHIRClient.getJSONObjectUsingGETRequest(urlString), i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else { // if data to be retrieved not on currentPage but there is no next page.
				return false;
			}
		}

		
		// Get the relevant components and timeRecorded of the TotalCholesterol Observation
		JSONObject cholesterolPageResource = cholesterolPageJSONObject.getJSONArray("entry").getJSONObject(i).getJSONObject("resource");
		JSONObject valueQuantity = cholesterolPageResource.getJSONObject("valueQuantity");
		String value = (Double.toString(valueQuantity.getDouble("value"))); //get the value of the total cholesterol and set them
		String units = (valueQuantity.getString("unit")); // get the units associated with the value and set them
		String system = ("system"); // get the system of measurement used and set them
		String timeRecorded = (cholesterolPageResource.getString("effectiveDateTime")); // get the effective date time of the total cholesterol observation was taken and set them
		this.setCholesterol(value, units, system, timeRecorded);
		return true;
				
	}

	/**
	 * Get the Total Cholesterol Observation (copy) so that the original isn't changed
	 * @return the Observation for Total Cholesterol
	 */
	public Observation getCholesterolObservation() {
		return new Observation(this.getObservation(FHIRCholesterol.CHOLESTEROL_POS));
	}

	
	/**
	 * The String representation of a FHIRCholesterol
	 */
	public String toString() {
		return "Cholesterol: [" + "value = " + this.getCholesterolValue() + ", units = " + this.getCholesterolUnits() + ", system = " + this.getCholesterolSystem()
				+ ", timeRecorded = " + this.getCholesterolTimeRecorded() + "]";
	}
	
}
