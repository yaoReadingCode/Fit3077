package model;

import java.util.ArrayList;

/**
 * 
 * @author Sriram, Zhi Tan
 * Class which serves as the actual Observation. An Observation has a value, its units, the system of measurement and the time recorded.
 * FHIRObservation uses this class because a FHIRObservation can have multiple Observations
 * For e.g. FHIRBloodPressure has 2 Observation(s). 1 for systolic and 1 for diastolic
 *
 */
public class Observation { // ArrayList..to take into account x to y values, but YAGNI, dont take tat into account yet
	private String value;
	private String units;
	private String system;
	private String timeRecorded;
	
	/**
	 * A constructor that initializes Observation and all its instance variables i.e. value, units, system and timeRecorded
	 * to default values
	 */	
	public Observation() {
		this.value = "";
		this.units = "";
		this.system = "";
		this.timeRecorded = "";
	}

	/**
	 * Check if the current Observation is conceptually empty.
	 * @return true if Observation is empty, otherwise false.
	 */
	public boolean isEmpty() {
		return this.value.equals("");
	}
	
	/**
	 * Constructor to create a copy of other Observation
	 * @param other the other Observation to be copied
	 */
	public Observation(Observation other) {
		this.value = other.value;
		this.units = other.units;
		this.system = other.system;
		this.timeRecorded = other.timeRecorded;
	}
	
	/**
	 * Set the Observation
	 * @param value
	 * @param units
	 * @param system
	 * @param timeRecorded
	 */
	public void setObservation(String value, String units, String system, String timeRecorded) {
		this.value = value;
		this.units = units;
		this.system = system;
		this.timeRecorded = timeRecorded;
	}

	/**
	 * Get the value of the Observation
	 * @return the value of the Observation
	 */
	public String getValue() {
		return value;
	}


	/**
	 * Check if the other obj equals this
	 * @param obj the other object
	 * @return a boolean indicating if obj is the same as this
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // check if this has same address as other, if yes return true
			return true;
		if (obj == null) // if other obj is null, return false
			return false;
		if (getClass() != obj.getClass()) // if this's class is not obj's class, return alse
			return false;
		Observation other = (Observation) obj;
		if (system == null) { // if this.system is null
			if (other.system != null) // if other.system is not null then return false
				return false;
		} else if (!system.equals(other.system)) // if this.system not equals other.system, return false
			return false;
		if (timeRecorded == null) { // if this.timeRecorded is null
			if (other.timeRecorded != null) // if other.timeRecorded is not null then return false
				return false;
		} else if (!timeRecorded.equals(other.timeRecorded)) // if this.timeRecorded not equals other.timeRecorded, return false
			return false;
		if (units == null) { // if this.units is null
			if (other.units != null) // if other.units is not null then return false
				return false;
		} else if (!units.equals(other.units)) // if this.units not equals other.units, then return false
			return false;
		if (value == null) { // if this.value is null
			if (other.value != null) // if other.value is not null then return false
				return false;
		} else if (!value.equals(other.value)) // if this.value not equals other.value then return false
			return false;
		return true;
	}

	/**
	 * Setter for value of this observation
	 * @param value of this observation
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Getter for units
	 * @return the units for the value of this observation that was used
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Setter for units
	 * @param units the units for the value of this observation that was used
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * Getter for system of measurement
	 * @return the system of measurement of this observation
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * Setter for System of measurement
	 * @param system the system of measurement
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * Getter for timeRecorded of this observation
	 * @return the timeRecorded for this observation
	 */
	public String getTimeRecorded() {
		return timeRecorded;
	}

	/**
	 * Setter for timeRecorded
	 * @param timeRecorded the timeRecorded for this observation
	 */
	public void setTimeRecorded(String timeRecorded) {
		this.timeRecorded = timeRecorded;
	}
	
	public String toString() {
		return "[" + "value = " + this.getValue() + ", units = " + this.getUnits() + ", system = " + this.getSystem()
				+ ", timeRecorded = " + this.getTimeRecorded() + "]";

	}
	
	
}
