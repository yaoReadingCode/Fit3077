package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import model.FHIRPractitioner;
import view.PatientObservationsMonitorGUI;
import view.PractitionerLoginGUI;


/**
 * 
 * @author sriram, Zhi Tan
 * 
 * Controller class to communicate the prac ID to model,
 * and send the patient list to be shown on the PatientCholesterolSelectionGUI
 *
 */
public class ControllerForPractitionerLogin {
	//view 
	private PractitionerLoginGUI view;
	//model
	//private ArrayList<Patient> patientList;
	//name of the window  
	private static final String WINDOW_NAME="Patient Monitoring Window";
	private int arr[]=new int[]{ 1,2,3,4,5 }; 

	
	public ControllerForPractitionerLogin(PractitionerLoginGUI theView) {
		// TODO Auto-generated constructor stub

		this.view=theView;
		// To handle condition when the submit button is clicked
		this.view.addSubmitListener(new SubmitListener());
	}
	

	/**
	 * 
	 * @author Sriram, Zhi Tan
	 * 
	 */
	class SubmitListener implements ActionListener{
		// Implementing the action when the sumbit button is clicked
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String pracID="";
			try {
				// getting the prac ID
				pracID=view.getPracID();
				
				// retrieve practitioner's patients
				FHIRPractitioner practitioner = new FHIRPractitioner();
				practitioner.retrieveDataFromServerAndInitWithLatest(pracID);

				view.dispose();
				
				// GUI for patient cholesterol monitoring
				PatientObservationsMonitorGUI sndView = new PatientObservationsMonitorGUI(practitioner, WINDOW_NAME);
				
				ControllerForMonitoringPatient controllerForSndView = new ControllerForMonitoringPatient(practitioner, sndView);
				practitioner.addObserversForTheObservableHashMapOfPractitioner(controllerForSndView);
//				practitioner.addObserversForTheObservableHashMapOfPractitioner(sndView);
			}
			catch (Exception e1){
				System.out.println("Invalid Entry, please enter a number");
				e1.printStackTrace();
			}
			
		}
		
	}
}
