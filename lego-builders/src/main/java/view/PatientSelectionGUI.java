package view;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.fhir.ucum.Component;

import ch.qos.logback.classic.db.names.ColumnName;
import model.FHIRPatient;
import model.FHIRPractitioner;
import model.Tuple;
import observerpattern.ObservableHashMap;


//ref --> https://stackoverflow.com/questions/27455775/java-gui-multiple-selection-program
/***
 * 
 * @author Sriram, Zhi Tan
 * Abstract class that has a basic GUI for the list of patients for a practitioner, and the patients
 * whose observations he might want to monitor. This class allows for extensibility because when there
 * are different Observations, we could simply extend this basic GUI and add specific implementations 
 * for those observations in the concrete class.
 */
public abstract class PatientSelectionGUI extends JFrame {
	// left list represents all the patients that has had an encounter with
	// the practitioner
    protected JList leftlist; 
    // right list represents all the patients that the practitioner wants 
    // to monitor 
    protected JList rightlist;

    protected JButton addbutton;
    protected JButton removebutton;
    
    public Set objects;
    
    protected ObservableHashMap<Tuple<String, String>, FHIRPatient> patientList= new ObservableHashMap<Tuple<String, String>, FHIRPatient>(); 
    protected ArrayList<FHIRPatient> selectedPatientList=new ArrayList<FHIRPatient>(); 

    
    protected FHIRPractitioner practitioner;
    protected JLabel update_seconds;
    protected JTextField frequency;
    protected JButton submit;
    

    public PatientSelectionGUI(FHIRPractitioner practitioner, String windowName) {
        
    	super(windowName);
        this.setSize(1600, 1000);
        this.setVisible(true);
        
        this.practitioner = practitioner;      
        
        // storing Practitioner's patients in the class
        this.patientList = practitioner.getPatients();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.practitioner = practitioner;
        
        // layout type set to FlowLayout
        setLayout(new FlowLayout());

        // adding the practitioner's list of patients to the left list
        leftlist = new JList();
        Object[] temp = patientList.values().toArray();
        DefaultListModel leftListModel = new DefaultListModel();
        for (int i = 0; i < temp.length; i ++) {
        	leftListModel.addElement(temp[i]);
        }
        leftlist.setModel(leftListModel);
        
        // 10 maximum patients diplayed, but the list is scrollable
        leftlist.setVisibleRowCount(10);
        leftlist.setFixedCellWidth(650);
        
        // left list has multiple interval selection
        leftlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(leftlist));
        
        
        addbutton = new JButton("ADD");
        
        
        add(addbutton);
      

        rightlist = new JList();
        DefaultListModel rightListModel = new DefaultListModel();
        rightlist.setModel(rightListModel);
        
        rightlist.setFixedCellWidth(675);
        //right list has single selection only
        rightlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rightlist.setVisibleRowCount(10);
        removebutton = new JButton("REMOVE");
        

        add(new JScrollPane(rightlist));
        add(removebutton);
        
        // to provide user the functionlity to enter the frequency number for the updates
        // of his/her choice
        update_seconds = new JLabel();
        update_seconds.setText("Frequency of Update (s): ");
        frequency = new JTextField(7);
        submit = new JButton("Update Frequency");
        
        this.add(update_seconds);
        this.add(frequency);
        this.add(submit);

    }
    
    /***
     * Returns the Practitioner that has logged in
     * @return practitioner that's logged in
     */
    public FHIRPractitioner getPractitioner() {
		return practitioner;
	}
    
    /***
     * Sets the practitioner whose patients we want to monitor
     * @param practitioner: practitioner whose patients we want to monitor
     */
	public void setPractitioner(FHIRPractitioner practitioner) {
		this.practitioner = practitioner;
	}
	
	/***
	 * 
	 * @return List of patients
	 */
	public ObservableHashMap<Tuple<String, String>, FHIRPatient> getPatientList() {
		return patientList;
	}
	/**
	 * Sets the lits of patients of the practitioner
	 * @param patientList: List of practitioner patients
	 */
	public void setPatientList(ObservableHashMap<Tuple<String, String>, FHIRPatient> patientList) {
		this.patientList = patientList;
	}
	
	// calculates the average for a given Observation for the list of patients being monitored 
	public abstract void calcAverageCholesterol(Set objects);
	// sets the table properties associated with the table
    public abstract void setTableColourProperty();
  
}