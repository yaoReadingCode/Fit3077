package model;

/**
 * Enum class for the FHIRObservations and their respective Observation. e.g. FHIRBloodPressure and Systolic and Diastolic BP.
 * @author Sriram, ZhiHaoTan
 * 
 */
public enum ObservationTypes {
	CHOLESTEROL {
        public String toString() {
            return "Total Cholesterol";
        }
    }, 
	BLOOD_PRESSURE	{
        public String toString() {
            return "Blood Pressure";
        }
    },
    SYSTOLIC_BLOOD_PRESSURE {
        public String toString() {
            return "Systolic Blood Pressure";
        }
    },
    DIASTOLIC_BLOOD_PRESSURE {
        public String toString() {
            return "Diastolic Blood Pressure";
        }
    }

}
