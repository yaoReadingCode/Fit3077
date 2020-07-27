package view;

import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.FHIRPatient;

/**
 * 
 * @author sriram, Zhi Tan
 * 
 * Class that builds GUI window for displaying additional patient information
 *
 */
public class PatientInfoGUI extends JFrame {
	private FHIRPatient chosenPatient;
	
	public PatientInfoGUI (FHIRPatient chosenPatient) {
		super("Patient's Additional Information Window");
		this.chosenPatient=chosenPatient;
		this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setSize(500, 150);
        this.setVisible(true);
        
        // creating labels for patient info
        JLabel birthdate= new JLabel();
        JLabel gender= new JLabel();
        JLabel city= new JLabel();
        JLabel state= new JLabel();
        JLabel country= new JLabel();
        JLabel nameLabel= new JLabel();
        
        
        //setting label info
        nameLabel.setText("Name: "+this.chosenPatient.getGivenName()+" "+this.chosenPatient.getFamilyName()+"; ");
        birthdate.setText("Birthdate: "+this.chosenPatient.getBirthday()+"; ");
        gender.setText("Gender: "+this.chosenPatient.getGender()+"; ");
        city.setText("City: "+this.chosenPatient.getCity()+"; ");
        state.setText("State: "+this.chosenPatient.getState()+"; ");
        country.setText("Country: "+this.chosenPatient.getCountry());
        
        //adding components to the frame
        this.add(nameLabel);
        this.add(birthdate);
        this.add(gender);
        this.add(city);
        this.add(state);
        this.add(country);
	}

}
