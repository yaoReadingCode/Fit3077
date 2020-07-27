package model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONObject;



/**
 * 
 * @author Sriram, Zhi Tan
 * Class which serves as a superclass for any FHIRObservation type objects,
 * for e.g., it is the superclass of FHIRCholesterol. It has everything common to
 * FHIRObservations such as the value, units, system and the timeRecorded of an Observation object
 *
 */
public abstract class FHIRObservation implements FHIRBundle {
    //fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?patient.identifier=https%3A%2F%2Fgithub.com%2Fsynthetichealth%2Fsynthea%7Ca1efa37b-39ed-4b6d-ac48-d171cc7d1d41&code=http%3A%2F%2Floinc.org%7C2093-3&_sort=-date&_count=1&_format=json
	public static final String URL_STRING_SECTION_FOR_OBSERVATION = "Observation/";
	protected ArrayList<Observation> observations; // A FHIRObservation can have multiple Observation for e.g. FHIRBloodPressure can have Systolic and Diastolic Blood Pressure
	
	/**
	 * A constructor that initializes FHIRObservation and all its observations i.e.
	 * value, units, system and timeRecorded of Observation to take on default values.
	 * 
	 * @param size the number of Observation belonging to this FHIRObservation. For e.g. 2 in the case of FHIRBloodPressure because we have both
	 * systolic and diastolic BP.
	 */
	protected FHIRObservation(int size) {
		this.observations = new ArrayList<Observation>();
		for (int i = 0; i < size; i++) {
			this.observations.add(new Observation());
		}
	}
	
	/**
	 * Set the Observation at certain index to take on the value, units, system and timeRecorded specified.
	 * For e.g. if we want to set diastolic BP which is located at index 1, we call this method with index param as 1 and the appropriate value, units
	 * system and time recorded.
	 * @param index the location of the Observation to be set. e.g. Systolic at 0, Diastolic at 1
	 * @param value the value of the Observation
	 * @param units the units for the value
	 * @param system the system of measurement used
	 * @param timeRecorded the time the Observation was recorded
	 */
	protected void setObservation(int index, String value, String units, String system, String timeRecorded) {
		this.observations.get(index).setValue(value);
		this.observations.get(index).setUnits(units);
		this.observations.get(index).setSystem(system);
		this.observations.get(index).setTimeRecorded(timeRecorded);
	}
	
	/**
	 * Get the Observation at a certain index
	 * @param index the index the Observation is located
	 * @return the copy of the Observation found
	 */
	protected Observation getObservation(int index){
		Observation obs = this.observations.get(index);
		Observation retObs = new Observation(); // create a copy
		retObs.setObservation(obs.getValue(), obs.getUnits(), obs.getSystem(), obs.getTimeRecorded()); 
		return retObs;  		
	}


	/**
	 * A method to check if the other obj is the same as this one
	 * 
	 * @param obj the other object to check if equals to this
	 * @return boolean indicating whether other object equals to this.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // if same object address, return true
			return true;
		if (obj == null) // if other obj is null, return false
			return false;
		if (getClass() != obj.getClass()) // if both this and other obj not same class, return false
			return false;
		FHIRObservation other = (FHIRObservation) obj; // cast other obj to the class of this obj
		if (observations == null) { // if this.observations is null
			if (other.observations != null) // if other.obsevations is not null, return false
				return false;
		} else if (!observations.equals(other.observations)) // if this.observations is not null, check if this.observations.equals(other.observations)), if not return false
			return false;
		return true; // return true if all tests passed
	}
	
	/**
	 * An abstract method so that all the subclasses will implement their method to getRelatedJSONObject.
	 * @param identifierOfPatient the patient's identifier, identifier system and identifier value
	 * @param numberToReturn the number of Observations to return (count param of FHIR get requests)
	 * @return the JSONObject containing the Observations queried
	 * @throws IOException
	 */
	public abstract JSONObject getRelatedJSONObject(Tuple<String, String> identifierOfPatient, int numberToReturn) throws IOException;

	/**
	 * An abstract method so that all subclasses will implement their method of 
	 * @param jsonObj the JSONObject obtained from getRelatedJSONObject
	 * @param i the position of the Observation to be returned. For e.g. i == 0 means the most recent Observation while i == 1 means the 2nd most recent and so on
	 * @return boolean indicating whether the retrieval failed or succeeded.
	 * @throws IOException
	 */
	public abstract boolean retrieveDataFromServerAndInit(JSONObject jsonObj, int i) throws IOException;
}
