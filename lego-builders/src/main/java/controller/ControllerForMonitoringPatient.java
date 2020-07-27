package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import model.FHIRPatient;
import model.FHIRPractitioner;
import observerpattern.Observer;
import view.PatientObservationsMonitorGUI;

/**
 * 
 * @author sriram, Zhi Tan
 * Controller class to communicate the frequqnecy rate that the practitioner enters
 * and makes the requests based on that
 *
 */
public class ControllerForMonitoringPatient implements Observer {
	private FHIRPractitioner practitionerSearched;
	private PatientObservationsMonitorGUI view;
	private int freq;
	private Timer timer;
	public ControllerForMonitoringPatient(FHIRPractitioner prac, PatientObservationsMonitorGUI view) {
		this.practitionerSearched = prac;
		this.view = view;
		this.view.addFrequencyButtonListener(new FrequencySetListener());
	}
	
	/**
	 * Private class to implement an actionListener for the frequency button 
	 * @author sriram, Zhi Tan
	 *
	 */
	class FrequencySetListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			
			
			// time before next call
			try {
				
				Double tempFreq=Double.parseDouble(view.getFrequency());
				// ensuring that we inly consider an integer for frequency
				freq = (int) Math.round(tempFreq); 
				
				
				// if the frequency is 0, then cancel updates
				if (freq==0) {
					timer.cancel();
				}
				else if (freq<0) {
					throw new IllegalArgumentException("Enter a valid positive integer number");
				}
				else {
					if (timer != null) {
						timer.cancel();
					}
					timer = new Timer();
					// creates a new DataRetrieverRefresher which is a TimerTask and calls the overriden run method
					// It is called every freq * 1000 (i.e., N seconds) seconds; where 0 is the delay, and freq * 1000 is how frequently we need to 
					// refresh our data
					timer.schedule(new DataRetrieverRefresher(), 0, freq * 1000);
					
				}
			}
			// throws IllegalArgumentException when the input is not a valid positive integer number  
			catch(Exception e1) {
				throw new IllegalArgumentException("Enter a valid positive integer number");
			}
		}
	}
	
	class DataRetrieverRefresher extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// creating a new thread to retrieve data
			new Thread(new Runnable(){
					public void run() {
				try {
					// retrieving the data from the server
					practitionerSearched.retrieveDataFromServerAndInitWithLatest(practitionerSearched.getIdentifier());
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}).start();
			
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		this.view.update();
	}


}
