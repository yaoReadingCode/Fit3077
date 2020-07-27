package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Scrollbar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import model.FHIRPatient;
/**
 * Class that dispays text in a separate window. This window was originally created for the sake of diplaying the 
 * history of past 5Systolic BP values. But it now can display any text that the user wants to display
 * @author sriram
 *
 */
public class TextualObservationMonitor extends JFrame {
	JLabel textLabel= new JLabel();
	String text= "";
	
	public TextualObservationMonitor (String windowName) {
		//sets the window name of the frame
		super(windowName);
		//setting frame property in relation what it'd do upon the user closing the window
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// layout style 
		this.setLayout(new FlowLayout());
		//frame size 
        this.setSize(1500, 1000);
        
        //fitiing in a scroll pane in the frame
        JScrollPane pane = new JScrollPane(textLabel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setContentPane(pane);

	}
	
	/**
	 * Sets the text to be displayed
	 * @param patientObservationinfo Patient's observation data
	 * @param noDataMessage if there is nothing to display, display defualt message
	 */
	public void addPatientInfo(String patientObservationinfo, String noDataMessage) {
		text+=patientObservationinfo;
		if (text.equals("<html></html>")) {
			textLabel.setText(noDataMessage);
		}
		else {
			textLabel.setText(text);
		}
		
	}
	
	/**
	 * Clears the screen
	 */
	public void clearScreen() {
		text="";
		textLabel.setText("");
	}

}