package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

/**
 * A Wrapper for FHIRPatient. Hides complexity of FHIRPatient and provides an easier access and obtaining of data from GUI side.
 * @author Sriram, ZhiHaoTan
 * 
 */
public class PatientWrapper {
	private FHIRPatient patient; // the patient to be wrapped
	
	/**
	 * Constructor for PatientWrapper
	 * @param properlyInitializedPatient the patient object to be wrapped
	 */
	public PatientWrapper(FHIRPatient properlyInitializedPatient) { 
		this.patient = properlyInitializedPatient;
	}

	/**
	 * Retrieve data from server, numberOfMostRecentObervationsToReturn observations of the type observationName
	 * @param observationName the name of the Observation to be returned
	 * @param numberOfMostRecentObservationsToReturn the number of most recent observations to be returned
	 * @return an ArrayList of size numberOfMostRecentObservationsToReturn with the elements being the Observation queried for. If not enough data
	 * present, the rest of the ArrayList shall be null. 
	 * The Boolean indicate if timeRecorded is same, for e.g. systolic and diastolic has same time recorded so only shown once.
	 * The Tuple<String, Observation> is for Observation and name of the Observation for e.g. "Total Cholesterol" and a totalCholesterol Observation
	 * We have an arraylist of Tuple<String, Observation> because a FHIRObservation can have multiple Observations for e.g. FHIRBloodPressure has systolic and diastolic
	 * ArrayList of these cause we want the n most recent Observations.
	 */
	public ArrayList<Tuple<ArrayList<Tuple<String, Observation>>, Boolean>> getObservations(String observationName, int numberOfMostRecentObservationsToReturn){
		ArrayList<Tuple<ArrayList<Tuple<String, Observation>>, Boolean>> retArrayList = new ArrayList<Tuple<ArrayList<Tuple<String, Observation>>, Boolean>>();
//		Tuple<ArrayList<Tuple<String, Observation>>, Boolean> tupleElement = new Tuple<ArrayList<Tuple<String, Observation>>, Boolean>(null, null);
		if (observationName.equals(ObservationTypes.BLOOD_PRESSURE.toString())){ // if BloodPressure is queried
			ArrayList<Tuple<String, Observation>> tempObservationConstructor = new ArrayList<Tuple<String, Observation>>();
			JSONObject intermediateJSONObj = new JSONObject();
			FHIRBloodPressure fhirBloodPressure = new FHIRBloodPressure();
			try {
				// get the related JSONObject
				intermediateJSONObj = fhirBloodPressure.getRelatedJSONObject(this.patient.getIdentifier(), numberOfMostRecentObservationsToReturn);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// populate the most recent 'numberOfMostRecentObservationsToReturn' observations 
			for (int i = 0; i < numberOfMostRecentObservationsToReturn; i ++) {
				
				fhirBloodPressure = new FHIRBloodPressure();
				tempObservationConstructor = new ArrayList<Tuple<String, Observation>>();
				if (!fhirBloodPressure.retrieveDataFromServerAndInit(intermediateJSONObj, i)) { // try to get data from server, if can't...
					// put into the tempObservationConstructor empty Observations
					tempObservationConstructor.add(new Tuple(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString(), new Observation()));
					tempObservationConstructor.add(new Tuple(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString(), new Observation()));
					retArrayList.add(new Tuple(tempObservationConstructor, true));
				}
				else {
					// get the diastolicBPObservation and systolicBPObservation and put into the tempObservationConstructor
					Observation diastolicBPObservation = fhirBloodPressure.getDiastolicBPObservation();
					Observation systolicBPObservation = fhirBloodPressure.getSystolicBPObservation(); // value, units, system, timerecorded
					tempObservationConstructor.add(new Tuple(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString(), diastolicBPObservation));
					tempObservationConstructor.add(new Tuple(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString(), systolicBPObservation));
					
					retArrayList.add(new Tuple(tempObservationConstructor, true));
				}
			}
		}
		else if (observationName.equals(ObservationTypes.CHOLESTEROL.toString())){
			ArrayList<Tuple<String, Observation>> tempObservationConstructor = new ArrayList<Tuple<String, Observation>>();
			JSONObject intermediateJSONObj = new JSONObject();
			FHIRCholesterol fhirCholesterol = new FHIRCholesterol();
			try {
				// get the related JSONObject
				intermediateJSONObj = fhirCholesterol.getRelatedJSONObject(this.patient.getIdentifier(), numberOfMostRecentObservationsToReturn);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < numberOfMostRecentObservationsToReturn; i ++) {
				fhirCholesterol = new FHIRCholesterol();
				if (!fhirCholesterol.retrieveDataFromServerAndInit(intermediateJSONObj, i)) { // try to get data from server, if cant..
					// put into the tempObservationConstructor an empty Observation
					tempObservationConstructor.add(new Tuple(ObservationTypes.CHOLESTEROL.toString(), new Observation()));
					retArrayList.add(new Tuple(tempObservationConstructor, false));
				}
				else {
					// put into the tempObservationConstructor the totalCholesterol Observation
					Observation cholesterolObservation = fhirCholesterol.getCholesterolObservation();
					tempObservationConstructor.add(new Tuple(ObservationTypes.CHOLESTEROL.toString(), cholesterolObservation));
					retArrayList.add(new Tuple(tempObservationConstructor, false));
				}
			}
		}
		return retArrayList;
		
	}
	
	/**
	 * Retrieve data from server, numberOfMostRecentObervationsToReturn observations of the type observationName
	 * @param observationName the name of the Observation to be returned
	 * @return the Observation(s) queried for. 
	 * The Boolean indicate if timeRecorded is same, for e.g. systolic and diastolic has same time recorded so only shown once.
	 * The Tuple<String, Observation> is for Observation and name of the Observation for e.g. "Total Cholesterol" and a totalCholesterol Observation
	 * We have an arraylist of Tuple<String, Observation> because a FHIRObservation can have multiple Observations for e.g. FHIRBloodPressure has systolic and diastolic
	 */
	public Tuple<ArrayList<Tuple<String, Observation>>, Boolean> getObservation(String observationName) { // USE THIS METHOD. loop through th	is arrayList and call "y = x.get(ObservationComponent.VALUE.toString())" and then y.getValue() and so on.
//		return this.getObservations(observationName, 1).get(0);
				ArrayList<Tuple<String, Observation>> retArrayList = new ArrayList<Tuple<String, Observation>>();
		
		if (observationName.equals(ObservationTypes.BLOOD_PRESSURE.toString())) { // if querying for Blood Pressure
			FHIRBloodPressure bloodPressureObservation = this.patient.getBloodPressure(); // get the BloodPressure() of the Patient
			if (bloodPressureObservation == null) {
				// put in empty Observations if don't have any
				retArrayList.add(new Tuple(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString(), new Observation()));
				retArrayList.add(new Tuple(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString(), new Observation()));
				
				return new Tuple(retArrayList, true);
			}
			// put in the diastolic and systolic blood pressure
			Observation diastolicBPObservation = bloodPressureObservation.getDiastolicBPObservation();
			Observation systolicBPObservation = bloodPressureObservation.getSystolicBPObservation(); // value, units, system, timerecorded
			
			retArrayList.add(new Tuple(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString(), diastolicBPObservation));
			retArrayList.add(new Tuple(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString(), systolicBPObservation));
			
			return new Tuple(retArrayList, true);
		}
		else if (observationName.equals(ObservationTypes.CHOLESTEROL.toString())){ // querying for total CHolesterol
			
			FHIRCholesterol cholesterolObservation = this.patient.getCholesterol(); // get the total Cholesterol of the patient
			if (cholesterolObservation == null) {
				// if total colesterol of patient is null, put in empty Observation
				retArrayList.add(new Tuple(ObservationTypes.CHOLESTEROL.toString(), new Observation())); 
				return new Tuple(retArrayList, false);
			}
			// put in the totalCholesterol of the patient into the Observation
			Observation cholesterolObservationRep = cholesterolObservation.getCholesterolObservation();
			retArrayList.add(new Tuple(ObservationTypes.CHOLESTEROL.toString(), cholesterolObservationRep));
			return new Tuple(retArrayList, false);
		}
		return null; // if the query String does not correspond to any Observations
	}


	/**
	 * Get the patient that the wrapper is wrapping
	 * @return the patient the wrapper is wrapping
	 */
	public FHIRPatient getPatient() {
		return patient;
	}

}
