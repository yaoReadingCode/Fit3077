package model;

import java.io.IOException;
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
 * it is a FHIRBundle and so would need to implement retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPatient).
 * FHIRBloodPressure is made up of two Observation(s). DIASTOLIC Blood Pressure and SYSTOLIC Blood Pressure
 *
 */
public class FHIRBloodPressure extends FHIRObservation {
	public static final String DIASTOLIC_BP_CODE = "8462-4";
	public static final String SYSTOLIC_BP_CODE = "8480-6";
	private static final int NUM_OBSERVATION = 2; // FHIRObservation has 2 Observation(s)
	private static final int SYSTOLIC_BLOOD_PRESSURE_POS = 0; // the position to find SYSTOLIC_BP
	private static final int DIASTOLIC_BLOOD_PRESSURE_POS = 1; // the position to find DIASTOLIC_BP
	// loinc code for BLOOD_PRESSURE
	public static final String CODE = "http://loinc.org|55284-4";
	
	/**
	 * Constructor of FHIRBloodPressure that initializes the ArrayList<Observation> with two default instances. 
	 * 1 for SYSTOLIC and 1 for DIASTOLIC
	 */
	public FHIRBloodPressure() {
		super(FHIRBloodPressure.NUM_OBSERVATION);
	}
	
	/**
	 * Set the Observation for Systolic Blood Pressure Observation
	 * @param value The value of the Systolic Blood Pressure Observation
	 * @param units The units used for the value
	 * @param system The system of measurement used
	 * @param timeRecorded The time the Observation was recorded
	 */
	public void setSystolicBPObservation(String value, String units, String system, String timeRecorded) {
		super.setObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS, value, units, system, timeRecorded);
	}
	
	/**
	 * Get the Systolic Blood Presure Observation (copy) so that the original isn't changed
	 * @return the Observation for Systolic Blood Pressure
	 */
	public Observation getSystolicBPObservation() {
		return new Observation(this.getObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS));
	}
	
	/**
	 * Get the value of the Systolic Blood Pressure Observation
	 * @return the value of the Systolic Blood Pressure Observation
	 */
	public String getSystolicBPObservationValue(){
		return this.getObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS).getValue();
	}
	
	/**
	 * Get the units used for the value of the Systolic Blood Pressure Observation
	 * @return the units for the value of the Systolic Blood Pressure Observation
	 */
	public String getSystolicBPObservationUnits() {
		return this.getObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS).getUnits();
	}
	
	/**
	 * Get the system of measurement used for recording the value of the Systolic Blood Pressure Observation
	 * @return the system of measurement used for recording the value of the Systolic Blood Pressure Observation
	 */
	public String getSystolicBPObservationSystem() {
		return this.getObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS).getSystem();
	}
	
	/**
	 * Get the time the Systolic Blood Pressure Observation was recorded
	 * @return the time the Systolic Blood Pressure Observation was recorded
	 */
	public String getSystolicBPObservationTimeRecorded() {
		return this.getObservation(FHIRBloodPressure.SYSTOLIC_BLOOD_PRESSURE_POS).getTimeRecorded();
	}
	
	/**
	 * Set the Observation for Diastolic Blood Pressure Observation
	 * @param value The value of the Diastolic Blood Pressure Observation
	 * @param units The units used for the value
	 * @param system The system of measurement used
	 * @param timeRecorded The time the Observation was recorded
	 */
	public Observation getDiastolicBPObservation() {
		return new Observation(this.getObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS));
	}
	
	/**
	 * Get the Diastolic Blood Presure Observation (copy) so that the original isn't changed
	 * @return the Observation for Diastolic Blood Pressure
	 */
	public void setDiastolicBPObservation(String value, String units, String system, String timeRecorded) {
		super.setObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS, value, units, system, timeRecorded);
	}
	
	/**
	 * Get the value of the Diastolic Blood Pressure Observation
	 * @return the value of the Diastolic Blood Pressure Observation
	 */
	public String getDiastolicBPObservationValue(){
		return this.getObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS).getValue();
	}
	
	/**
	 * Get the units used for the value of the Diastolic Blood Pressure Observation
	 * @return the units for the value of the Systolic Blood Pressure Observation
	 */
	public String getDiastolicBPObservationUnits() {
		return this.getObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS).getUnits();
	}
	
	/**
	 * Get the system of measurement used for recording the value of the Systolic Blood Pressure Observation
	 * @return the system of measurement used for recording the value of the Systolic Blood Pressure Observation
	 */
	public String getDiastolicBPObservationSystem() {
		return this.getObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS).getSystem();
	}	
	
	/**
	 * Get the time the Diastolic Blood Pressure Observation was recorded
	 * @return the time the Diastolic Blood Pressure Observation was recorded
	 */
	public String getDiastolicBPObservationTimeRecorded() {
		return this.getObservation(FHIRBloodPressure.DIASTOLIC_BLOOD_PRESSURE_POS).getTimeRecorded();
	}
	
	
	/**
	 * Retrieve the most recent Blood Pressure data for a patient with the specified identifier
	 * and populate the FHIRBloodPressure object with the data retrieved.
	 * Return true if operation succeeded, otherwise return false;
	 * @param identifierOfPatient the identifier of a patient
	 * @return a boolean indicating if the operation succeeded
	 */
	public boolean retrieveDataFromServerAndInitWithLatest(Tuple<String, String> identifierOfPatient) throws IOException {
		JSONObject bloodPressurePageJSONObject = getRelatedJSONObject(identifierOfPatient, 1);
		return retrieveDataFromServerAndInit(bloodPressurePageJSONObject, 0);
	}
	
	/**
	 * Retrieve the 'numberToReturn' most recent Blood Pressure data for a patient with the specified identifier
	 * Return the related JSONObject if operation succeeded, otherwise return null;
	 * @param identifierOfPatient the identifier of a patient
	 * @param numberToReturn the number of Observations to get, the count param of FHIR get request
	 * @return the JSONObject which contains the data for the numberToReturn most recent Observation(s)
	 */
	@Override
	public JSONObject getRelatedJSONObject(Tuple<String, String> identifierOfPatient, int numberToReturn)
			throws IOException {
		// Build the URL String for retrieving the BloodPressure Observation(s) of the patient
		String urlStrForBloodPressureOfPatient = FHIRClient.BASE_URL + FHIRObservation.URL_STRING_SECTION_FOR_OBSERVATION 
				+ "?patient.identifier=" + URLEncoder.encode(identifierOfPatient.first + "|", "UTF-8") + identifierOfPatient.second + "&" + "code=" + URLEncoder.encode(FHIRBloodPressure.CODE, "UTF-8") 
				+ "&" + FHIRClient.SORT_PARAMETER + FHIRClient.DESCENDING_DATE + "&" + FHIRClient.COUNT_PARAMETER + String.valueOf(numberToReturn) + "&" + FHIRClient.JSON_FORMAT;
		
		// return the JSONObject for the url above.
		return FHIRClient.getJSONObjectUsingGETRequest(urlStrForBloodPressureOfPatient); 
		
	}
	
	
	/**
	 * Given a JSONObject for blood pressure, retrieve the data at ith pos related to it. 
	 * The i == 0 is the most recent while i == 1 is the 2nd most recent.
	 * @param bloodPressurePageJSONObject the JSONObject containing the Observation data that is queried
	 * @param i the position of the Observation in the JSONObject
	 * @return a boolean indicating whether extraction of data succeeded or not.
	 */
	public boolean retrieveDataFromServerAndInit(JSONObject bloodPressurePageJSONObject, int i) {
		if (bloodPressurePageJSONObject == null) {
			return false;
		}

		// if the subject/patient of the specified identifier doesn't have Cholesterol value, return false.
		if (bloodPressurePageJSONObject.getInt("total") == 0) { // cholesterol page has "total"
			return false;
		}
		
		// go to the JSONObject (page) containing the data that needs to be retrieved.
		if (i >= bloodPressurePageJSONObject.getInt("total")) { // if the i to be retrieved is not on current page
			// if there is next page, go to next page
			if (bloodPressurePageJSONObject.getJSONArray("link").length() >= 2 && bloodPressurePageJSONObject.getJSONArray("link").getJSONObject(1).getString("relation").equalsIgnoreCase("next")) {
				String urlString = bloodPressurePageJSONObject.getJSONArray("link").getJSONObject(1).getString("url");
				i = i - bloodPressurePageJSONObject.getInt("total");
				try {
					return retrieveDataFromServerAndInit(FHIRClient.getJSONObjectUsingGETRequest(urlString), i); // recurse to the next page.
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else { // if data to be retrieved not on currentPage but there is no next page.
				return false;
			}
		}
		
		// Get the relevant components and timeRecorded of the BloodPressure Observation
		JSONObject bloodPressurePageResource = bloodPressurePageJSONObject.getJSONArray("entry").getJSONObject(i).getJSONObject("resource");
		JSONArray component = bloodPressurePageResource.getJSONArray("component");
		String timeRecorded = bloodPressurePageResource.getString("effectiveDateTime");
		String diastolicBPValue = "";
		String diastolicBPUnits = "";
		String diastolicBPSystem = "";
		
		String systolicBPValue = "";
		String systolicBPUnits = "";
		String systolicBPSystem = "";
		
		// Get the first and second component (Systolic and Diastolic Blood Pressure)
		JSONObject firstObject = component.getJSONObject(0);
		JSONObject secondObject = component.getJSONObject(1);
		
		// Get the JSONObject related to the value of the Systolic and Diastolic Observation
		JSONObject firstObjectValueQuantity = firstObject.getJSONObject("valueQuantity");
		JSONObject secondObjectValueQuantity = secondObject.getJSONObject("valueQuantity");
		
		// if the firstObject is Diastolic, the 2nd would be Systolic
		if (firstObject.getJSONObject("code").getJSONArray("coding").getJSONObject(0).getString("code").equals(FHIRBloodPressure.DIASTOLIC_BP_CODE)) {
			// Get the diastolic Observation's value, unit and system using the firstObject
			diastolicBPValue = (Double.toString(firstObjectValueQuantity.getDouble("value")));  // get the value for diastolic BP
			diastolicBPUnits = (firstObjectValueQuantity.getString("unit")); // get the units associated with the value
			diastolicBPSystem = (firstObjectValueQuantity.getString("system")); // get the system of measurement used and set them

			// Get the systolic Observation's value, unit and system using the secondObject
			systolicBPValue = (Double.toString(secondObjectValueQuantity.getDouble("value")));
			systolicBPUnits = (secondObjectValueQuantity.getString("unit")); // get the units associated with the value and set them
			systolicBPSystem = (secondObjectValueQuantity.getString("system")); // get the system of measurement used and set them

		}
		else { // if the firstObject is Systolic, the 2nd would be Diastolic.
			// Get the systolic Observation's value, unit and system using the firstObject
			systolicBPValue = (Double.toString(firstObjectValueQuantity.getDouble("value"))); // get the value for diastolic BP
			systolicBPUnits = (firstObjectValueQuantity.getString("unit")); // get the units associated with the value and set them
			systolicBPSystem = (firstObjectValueQuantity.getString("system")); // get the system of measurement used and set them

			// Get the diastolic Observation's value, unit and system using the secondObject
			diastolicBPValue = (Double.toString(secondObjectValueQuantity.getDouble("value")));  // get the value for diastolic BP
			diastolicBPUnits = (secondObjectValueQuantity.getString("unit")); // get the units associated with the value and set them
			diastolicBPSystem = (secondObjectValueQuantity.getString("system")); // get the system of measurement used and set them

		}
		this.setDiastolicBPObservation(diastolicBPValue, diastolicBPUnits, diastolicBPSystem, timeRecorded); // Set the Diastolic BP with the data retrieved above
		this.setSystolicBPObservation(systolicBPValue, systolicBPUnits, systolicBPSystem, timeRecorded); // Set the Systolic BP with the data retrieved above
		return true;

	}


	/**
	 * The String representation of a FHIRBloodPressure
	 */
	public String toString() {
		return "Diastolic Blood Pressure: [" + "value = " + this.getDiastolicBPObservationValue() + ", units = " + this.getDiastolicBPObservationUnits()
				+ ", timeRecorded = " + this.getDiastolicBPObservationTimeRecorded() + "]"
				+ "Systolic Blood Pressure: [" + "value = " + this.getSystolicBPObservationValue() + ", units = " + this.getSystolicBPObservationUnits()
				+ ", timeRecorded = " + this.getSystolicBPObservationTimeRecorded() + "]"
				;

	}
}
