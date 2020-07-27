/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.category.DefaultCategoryDataset;

import model.FHIRPatient;
import model.FHIRPractitioner;
import model.Observation;
import model.ObservationTypes;
import model.PatientWrapper;
import model.Tuple;
import observerpattern.Observer;

/**
 * @author Sriram, Zhi Tan
 *  Class that builds GUI that is able to display multiple patient Observations. This extends the 
 *  abstract class, PatientSelectionGUI and builds on GUI which are more specific to 
 *  monitoring Observations. It also implements Observer because it is observing the 
 *  Patient's Observations.
 *
 */
public class PatientObservationsMonitorGUI extends PatientSelectionGUI implements Observer {

	// stores the average of the cholesterol values of the patients we are tracking.
    private Double average=0.0;
    
    // to store all check boxes in one place to remove if else blocks
    private ArrayList<JCheckBox> checkBoxList=new ArrayList<JCheckBox>(); 
    
    // the current 2 check box options
    private JCheckBox cholesterolSelection= new JCheckBox(ObservationTypes.CHOLESTEROL.toString());
    private JCheckBox bloodPressureSelection= new JCheckBox(ObservationTypes.BLOOD_PRESSURE.toString());
    
    //button to update table contents based on the selected Observations
    JButton updateTableButton = new JButton("Update Table");
    
    //button to monitor patients that have a high BP based on the entered systolic value 
    JButton monitorHighBPButton = new JButton("Monitor High BP");
    
    //default column of the table
    private String columnNames[] = {"Name"};
    // The 0 argument is number rows
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames,0); 
    private JTable table = new JTable(tableModel);
    
    //Bar chart to monitor patient cholesterol
    private BarChart cholesterolChart= new BarChart("Total Cholesterol Graph");
    //Line chart to monitor patient's Systolic BP
    private LineChart systolicChart= new LineChart("Systolic Blood Pressure Graph");
    private TextualObservationMonitor systolicMonitor= new TextualObservationMonitor("Systolic BP Monitor Window");
    
    // A default value given to Systolic Pressure
    private JTextField systolicValue=new JTextField("1200");
    // A default value given to Diastolic Pressure
    private JTextField diastolicValue= new JTextField("780");
  
    // label for systolic BP value
    private JLabel systolicLabel;
    // label for diastolic BP value
    private JLabel diastolicLabel;
    
    //List to keep track of all the patient wrappers that will be used to monitor observations based on the check boxes ticked 
    private ArrayList<PatientWrapper> patientWrapperList=new ArrayList();
    //List to keep track of all the patient wrappers that have high systolic BP based on the practitioner's entered values
    private ArrayList<PatientWrapper> highSystolicPatientWrapper =new ArrayList(); 
    
    /***
     * 
     * @param practitioner: the current Practitioner
     * @param windowName: The name of the window
     */
    
    public PatientObservationsMonitorGUI(FHIRPractitioner practitioner, String windowName) {
    	// passing in the practitioner and the window name
    	super(practitioner, windowName);
    	// adds the check
    	this.addCheckBox();
    	// defualting the cholesterol chart to not be visible
    	cholesterolChart.setVisible(false);
    	// un checking the check box to begin with
		cholesterolSelection.setSelected(false);
		// un checking the check box to begin with
		bloodPressureSelection.setSelected(false);

		// Use a set here to eliminate redundancy
		objects = new LinkedHashSet();
		
		
		//action listener for the add button
		addbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
                //make sure you preserve the previously selected list items
                int size = rightlist.getModel().getSize();
                tableModel.setRowCount(0);
                
                // adding previous items from the right list
                for (int i = 0; i < size; i++) {
                    objects.add(rightlist.getModel().getElementAt(i));
                }
                
                // adding currently selected elements in the left list
                List<FHIRPatient> listOfItem=leftlist.getSelectedValuesList();
                for (int i = 0; i < listOfItem.size(); i++) {
                	objects.add(listOfItem.get(i));
                }
                
                
                
                //setting the right list elements based on the non
                //redudant patients in the objects variable
                DefaultListModel listModel =  (DefaultListModel)(rightlist.getModel());
        		listModel.removeAllElements();
        	    Object[] temp = objects.toArray();
        	    
    	        for (int i = 0; i < temp.length; i ++) {
    	        	listModel.addElement(temp[i]);
    	        }
       
                //update Table, chart(s), textual Mointoring windows 
                update();

           }
            
        });
		
		// To provide an option to view additional information about the patient
        table.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e){
				int row=table.rowAtPoint(e.getPoint());
	            // extracting the patient of that row selection
	            FHIRPatient chosenPatient=selectedPatientList.get(row);
	            new PatientInfoGUI(chosenPatient);  
	        }
		});
        
       //action listener for the remove button
        removebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // iterate over item objects and calculate the total
            	objects = new LinkedHashSet();
                int size = rightlist.getModel().getSize();
                              
                //getting index of the currently selected patient in the right list
                int selectedIndex = rightlist.getSelectedIndex();
                if (selectedIndex != -1) {
                	for (int i = 0; i < size; i++) {
                		// storing all patients from the right list into the objects set
                		// excluding the selected index
                		if (!(selectedIndex == i)) {
                			objects.add(rightlist.getModel().getElementAt(i));
                		}
                    }
                	
                }
                DefaultListModel listModel =  (DefaultListModel)(rightlist.getModel());
        		listModel.removeAllElements();
        	    Object[] temp = objects.toArray();
        	    
    	        for (int i = 0; i < temp.length; i ++) {
    	        	listModel.addElement(temp[i]);
    	        }
       
                tableModel.setRowCount(0);
              //update Table, chart(s), textual Monitoring windows 
                update();
             
            }
        });
        
        // adding the check box for cholesterol 
        add(cholesterolSelection);
        // adding the check box for blood pressure  
        add(bloodPressureSelection);
        
        /**
         * Updates the table contents when the button is clicked 
         */
        updateTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// updates the table
		    	update();
		    	setTableColourProperty();
			}
		});
        add(updateTableButton);
        
        /**
         * Shows the Cholesterol Bar Chart when the button is clicked 
         */
        JButton showCholesterolChartButton = new JButton("Show Cholesterol Chart");
        showCholesterolChartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cholesterolChart.setVisible(true);
				// fill in the data for the chart
				populateCholesterolChart(new ArrayList<FHIRPatient>(objects));

			}
		});
        
        /**
         * Shows the Systolic BP Line Chart when the button is clicked 
         */
        JButton showSystolicChartButton = new JButton("Show Systolic BP Chart(s)");
        showSystolicChartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systolicChart.getGraphPanel().removeAll();
				// fill in the data for the chart
				populateSystolicChart();
			}
		});
        
        
		

        // scroll pane for table
        JScrollPane pane = new JScrollPane(table);  
        Dimension listSize = new Dimension(1600, 500);
    	pane.setSize(listSize);
    	pane.setMaximumSize(listSize);
    	pane.setPreferredSize(listSize);
        add(pane);
        add(showCholesterolChartButton);
        
        // adding label for Systolic Pressure Label and Text Field for it 
        systolicLabel=new JLabel("Systolic Pressure Value");
        add(systolicLabel);
        add(systolicValue);
        
        // adding label for Diastolic Pressure Label and Text Field for it 
        diastolicLabel=new JLabel("Diastolic Pressure Value");
        add(diastolicLabel);
        add(diastolicValue);
        
        /**
         * Updates the Systolic Pressure Value when the enter button is clicked and the table colour 
         * is updated at the same time
         */
        systolicValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
            	setTableColourProperty();
			}
		});
        
        /**
         * Updates the Diastolic Pressure Value when the enter button is clicked and the table colour 
         * is updated at the same time
         */
        diastolicValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
            	setTableColourProperty();
			}
		});
        
        /***
         * displays  window to dislpay patients' history for a particular observation
         */
        monitorHighBPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// updates highSystolicPatientWrapper list with the patients with high Systolic BP
				checkHighSystolicPatients();
				// diplays the text therafter
				displaySystolicPatients();
			}
		});
        add(monitorHighBPButton);
        add(showSystolicChartButton);
	}
	/**
	 * Action Listener for add Frequency button
	 * @param listenerForFrequencyButton
	 */
    public void addFrequencyButtonListener(ActionListener listenerForFrequencyButton) {
    	submit.addActionListener(listenerForFrequencyButton);
    }
	
    
    
    /**
     * 
     * @return the current frequency that the doctor has entered
     */
	public String getFrequency() {
		return frequency.getText();
	}
	
	/**
	 * Populates the data set for cholesterol bar chart
	 * @param monitoredPatients patients whose chol. values need to be diplayed on the chart
	 */
	public void populateCholesterolChart(ArrayList<FHIRPatient> monitoredPatients) {
		DefaultCategoryDataset dataSet= new DefaultCategoryDataset();
		for (int i=0;i<monitoredPatients.size();i++) {
			// initialising the values 
			if (monitoredPatients.get(i).getCholesterol()==null) {
	       		 continue;
	        	}
			// name of the patient 
        	String name=monitoredPatients.get(i).getGivenName()+" "+monitoredPatients.get(i).getFamilyName();
        	// cholesterol value of the patient 
        	Double cholesterolValue=Double.parseDouble(monitoredPatients.get(i).getCholesterol().getCholesterolValue());
        	// setting the dat set values
        	dataSet.setValue(cholesterolValue, "Total Cholesterol mg/dl", name);
		}
        //	renders the chart
		cholesterolChart.renderChart(dataSet,"Total Cholesterol mg/dl", "Patient Name(s)", "");
		
	}

	/**
	 * Populates the data set for Systolic BP Line chart
	 */
	public void populateSystolicChart() {
		// updates highSystolicPatientWrapper list with the patients with high Systolic BP
		checkHighSystolicPatients();
		// make chart visible
		systolicChart.setVisible(true);
		
		// going through each patient that has high sys BP
		for (int i=0;i<highSystolicPatientWrapper.size();i++) {
			// extracting the name of the patient 
			String name=highSystolicPatientWrapper.get(i).getPatient().getGivenName()+" "+highSystolicPatientWrapper.get(i).getPatient().getFamilyName();
			DefaultCategoryDataset dataSet= new DefaultCategoryDataset();

			//fetch the data of the latest 5 BP observations for that patient
			ArrayList<Tuple<ArrayList<Tuple<String, Observation>>, Boolean>> monitorPatient= highSystolicPatientWrapper.get(i).getObservations(ObservationTypes.BLOOD_PRESSURE.toString(), 5);
			//reversing the order of the patient observations from previous observations to the most recent Observation 
			Collections.reverse(monitorPatient);
			// keeping track of the number of observations for the x axis 
			int count=1;
			for (int j =0;j<monitorPatient.size();j++) {
				// extracting the Observation 
				for(int k=0;k< monitorPatient.get(j).first.size();k++) {
					// ensuring that the value isn't null
					if (!(monitorPatient.get(j).first.get(k).second.isEmpty())) {
						// ensuring that we only extract the value which is systolic blood pressure 
						if (monitorPatient.get(j).first.get(k).first.equals(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString())) {
							dataSet.setValue(Double.parseDouble(monitorPatient.get(j).first.get(k).second.getValue()), "Systolic BP Value", Integer.toString(count));
							count++;
							
						}
						
					}
					
				}
				
			}
			// updates the UI
			systolicChart.getGraphPanel().updateUI();
			// render the chart 
			systolicChart.renderChart(dataSet,name,"","");
			
		}
	}
	
	
	/**
	 * Update table with the current list of patients to be monitored
	 * @param objects
	 */
	private void updateTableWithSelectedListPatients(Set objects){
		tableModel.setColumnCount(0);
		// add name column in the beginning
		tableModel.addColumn("Name");
	
		selectedPatientList = new ArrayList<FHIRPatient>(objects);
		// clearing the previous contents of the container 
		patientWrapperList.clear();
		
		// Creating a list of wrapper patients based on the selected patients
		for (int i=0;i<selectedPatientList.size();i++) {
    		patientWrapperList.add(new PatientWrapper(selectedPatientList.get(i)));
    	}
    	
		// This sequence of loops is used to update the table with the column names 
		// based on the Observation Chekcboxes that are ticked by the user
    	// going through each checkbox list items
    	for (int i=0;i<checkBoxList.size();i++) {
    		// checking if the check box is selected
			if (checkBoxList.get(i).isSelected()) {
				// going through each patient in the wrapper list 
				for (int j=0;j<patientWrapperList.size();j++) {
					// check if the patient has an observation for the checkbox that's selected
					if (patientWrapperList.get(j).getObservation(checkBoxList.get(i).getText())!=null) {
						// getting the Observation based on the checkbox String 
						Tuple <ArrayList<Tuple<String, Observation>>, Boolean> passedTuple=patientWrapperList.get(j).getObservation(checkBoxList.get(i).getText());
						// Extracying a List of Tuples that contains the Observation
						ArrayList <Tuple<String, Observation>> passedList= passedTuple.first;
						// extracting the boolean value for time 
						Boolean passedBool=passedTuple.second;
						for (int k=0;k< passedList.size();k++) {
							// adds the column name 
							tableModel.addColumn(passedList.get(k).first);
							// if the boolean value is false, then in the arraylist there is only one time field which needs to be added in the table 
								if (passedBool==false) {
									tableModel.addColumn("Time");
								}
							
					    }
						// if the boolean value is true, then in the arraylist there is 2 redundant time fields which need to be added in the table only once 
						if (passedBool==true) {
							tableModel.addColumn("Time");
						}
						break;
					}
				}
			}
    	}
    	
	   selectedPatientList = new ArrayList<FHIRPatient>(objects);
	   
	   // storing each patient data as a row in the arraylist. 
	   ArrayList<String> tableRows = new ArrayList<String>();
	   String time="";
	   
	   // previous section was adding the columns in the table based on the observation selected by the practitioner
	   // but Now we begin populating the data in the table
	   Boolean hasTimeBeenAdded=false;
	   // going through each patient in the wrapper list 
	   for (int j=0;j<patientWrapperList.size();j++) {
		   // adding the patient name in the beginning because that is the first column
		   tableRows.add(patientWrapperList.get(j).getPatient().getGivenName()+" "+patientWrapperList.get(j).getPatient().getFamilyName());
		   // going through each checkbox list items
		   for (int i=0;i<checkBoxList.size();i++) {
			   // check if that particular check box is selected 
			   if (checkBoxList.get(i).isSelected()) {
				   // check if the patient has an observation for the checkbox that's selected
					if (patientWrapperList.get(j).getObservation(checkBoxList.get(i).getText())!=null) {
						// getting the Obseravtion based on the checkbox String 
						Tuple <ArrayList<Tuple<String, Observation>>, Boolean> passedTuple=patientWrapperList.get(j).getObservation(checkBoxList.get(i).getText());
						// Extracying a List of Tuples that contains the Observation
						ArrayList <Tuple<String, Observation>> passedList= passedTuple.first;
						// extracting the boolean value for time 
						Boolean passedBool=passedTuple.second;
						for (int k =0;k< passedList.size();k++) {
							// ensuring that the Observation value isnt empty
							if (!passedList.get(k).second.isEmpty()) {
								// adding the observation value and the units to the list 
								tableRows.add(passedList.get(k).second.getValue()+" "+passedList.get(k).second.getUnits());
								// if the boolean value is false, then in the arraylist there is only one time field which needs to be added in the table 
								if (passedBool==false) {
									tableRows.add(passedList.get(k).second.getTimeRecorded());
									hasTimeBeenAdded=true;
								}
								time=passedList.get(k).second.getTimeRecorded();
							}
							// if it's empty add  - for value and time
							else {
								tableRows.add("-                  ");
								time="-                  ";
								
							}
						}
					}
					// if the boolean value is true, then in the arraylist there is 2 redundant time fields which need to be added in the table only once 	
					if (!(hasTimeBeenAdded)) {
						tableRows.add(time);
					}
				}	
			   hasTimeBeenAdded=false;
			}	
			   // adding this to the table now by converting the arraylist into array because the tabel model accepts only array
			   String[] array = tableRows.toArray(new String[0]);
			   tableModel.addRow(array);
			   tableRows = new ArrayList<String>();	
			   hasTimeBeenAdded=false;
		}

	}
	
	
	/***
	 * 
	 * @author sriram, Zhi Tan
	 * 
	 * private class to support cell colour properties depending on specific
	 * Patient Observations conditions for the table
	 *
	 */
	private class CellColorRenderer extends DefaultTableCellRenderer {
		//source---> https://stackoverflow.com/questions/17732005/trying-to-color-specific-cell-in-jtable-gettablecellrenderercomponent-overide
        Color originalColor = null;
        Double comparisonValue;
        Color passedColor;
        
        /**
         * 
         * @param comparisonValue the value that the observation needs to be compared against
         * @param passedColor the color that the cell needs to be highlighted in
         */
        public CellColorRenderer(Double comparisonValue, Color passedColor) {
        	this.comparisonValue= comparisonValue;
            this.passedColor= passedColor;
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // extracting the original color
            if (originalColor == null) {
                originalColor = getForeground();
            }
            if (value == null) {
                renderer.setText("");
            } else {
                renderer.setText(value.toString());
            }
            
            // splitting the observation string into value and unit 
            String[] splitted = value.toString().split("\\s+");
            String observationString = splitted[0];

        
            // if the patient's observation is greater than the comparison value, highloght the value in the passed Color
            if (!(observationString.substring(0, 1).equalsIgnoreCase("-")) && Double.parseDouble(observationString)>comparisonValue) {
                renderer.setForeground(passedColor);
            }
            else {
            	// represent it in black
                renderer.setForeground(Color.BLACK); 
            }

            return renderer;
        }
    }
	
	
	
	/***
	 * Setting table property for cell colors
	 */
	@Override
	public void setTableColourProperty() {
		CellColorRenderer cholesterolCondition = new CellColorRenderer(average,Color.RED);
		CellColorRenderer systolicCondition = new CellColorRenderer(Double.parseDouble(systolicValue.getText()),Color.MAGENTA);
		CellColorRenderer diastolicCondition = new CellColorRenderer(Double.parseDouble(diastolicValue.getText()),Color.MAGENTA);
   	 	
		// iterating through each table column and adding cell color properties depending on the columns
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			// Setting the cell properties for the column systolic pressure
			if (tableModel.getColumnName(i).equals(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString())) {
            	table.getColumn(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString()).setCellRenderer(systolicCondition);
            }
			// Setting the cell properties for the column diastolic pressure
			if (tableModel.getColumnName(i).equals(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString())) {
            	table.getColumn(ObservationTypes.DIASTOLIC_BLOOD_PRESSURE.toString()).setCellRenderer(diastolicCondition);
            }
			// Setting the cell properties for the column total cholesterol
            if (tableModel.getColumnName(i).equals(ObservationTypes.CHOLESTEROL.toString())) {
            	table.getColumn(ObservationTypes.CHOLESTEROL.toString()).setCellRenderer(cholesterolCondition);
            }
        }  
   }

	
	/**
	 * Implementing Observer's update method to update values that might have changed. Updates the overall view if something's been changed.
	 */
	public void update() {
		// TODO Auto-generated method stub
		this.patientList = practitioner.getPatients();
		
		// setting left list because there might be changes from N seconds update
		DefaultListModel leftListModel =  (DefaultListModel)(leftlist.getModel());
		leftListModel.removeAllElements();
	    Object[] leftListArray = patientList.values().toArray();
	    
        for (int i = 0; i < leftListArray.length; i ++) {
        	leftListModel.addElement(leftListArray[i]);
        }

        int size = rightlist.getModel().getSize();

        tableModel.setRowCount(0);
        
        objects.clear();
        
        for (int i = 0; i < size; i++) {
        	FHIRPatient p = (FHIRPatient) rightlist.getModel().getElementAt(i);
        	Tuple<String, String> id = p.getIdentifier();
        	FHIRPatient newP = this.patientList.get(id);
        	objects.add(newP);
        }
        // setting the new right list with new patients if there are any
        DefaultListModel rightListModel =  (DefaultListModel)(rightlist.getModel());
		rightListModel.removeAllElements();
        Object[] rightListArray = objects.toArray();
        for (int i = 0; i < rightListArray.length; i ++) {
        	rightListModel.addElement(rightListArray[i]);
	    }
        
        this.updateTableWithSelectedListPatients(objects);
        //calculate average based on the current number of patients
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (tableModel.getColumnName(i).equals(ObservationTypes.CHOLESTEROL.toString())) {
            	 calcAverageCholesterol(objects);
            }            
        }
        
        // if the user is currently viewing the cholesterol bar chart
        if (cholesterolChart.isDisplayable()) {
        	cholesterolChart.setVisible(true);
        	// update the chart with the new data
        	populateCholesterolChart(new ArrayList<FHIRPatient>(objects));
        	
        }
        
        // if the user is currently viewing the Systolic line chart
        if (systolicChart.isDisplayable()) {
        	systolicChart.getGraphPanel().removeAll();
        	// populate the chart
        	populateSystolicChart();
        	// because populateSystolicChart() already uses the  checkHighSystolicPatients() updates the list we don't have to query it again
        	// hence it was nested with this if condition
        	if (systolicMonitor.isDisplayable()) {
        		 // display all the textual history 
        		 displaySystolicPatients();
           }
        	
        }
        // If the systolic chart window is not open but the systolicMonitor Window is open, we can now use checkHighSystolicPatients()
        // separately and update the list of high Cholesterol Patients. So, in this case we are ensuring that the code is not redundant.
        else if (systolicMonitor.isDisplayable()) {
        	 checkHighSystolicPatients();
        	// display all the textual history 
     		 displaySystolicPatients();
        }
       
	}
	
	@Override
	public void calcAverageCholesterol(Set objects) {
		//list of patients that the practitioner wants to monitor
    	selectedPatientList = new ArrayList<FHIRPatient>(objects);
        int observableSize = 0;
        
        Double sum=0.0;
        
        for (int i=0;i<selectedPatientList.size();i++) {
        	// initialising the values 
        	Double cholesterolDoubleValue=0.0;
        	String name=selectedPatientList.get(i).getGivenName()+" "+selectedPatientList.get(i).getFamilyName();
        	String cholesterolValue="-                   ";
        	String date="-                      ";
        	
        	// if the values aren't null, proceed to extract the data for cholesterol
        	if (!(selectedPatientList.get(i).getCholesterol()==null)) {
	        	String cholesterolUnitString = selectedPatientList.get(i).getCholesterol().getCholesterolUnits();
	        	cholesterolValue=selectedPatientList.get(i).getCholesterol().getCholesterolValue()+" "+cholesterolUnitString;
	       		date=selectedPatientList.get(i).getCholesterol().getCholesterolTimeRecorded();
	       		String cholesterolString=cholesterolValue.substring(0,cholesterolValue.length()-cholesterolUnitString.length());
	       		cholesterolDoubleValue=Double.parseDouble(cholesterolString);
	       		observableSize+=1;
        	}
        	sum+=cholesterolDoubleValue;
    	}
        // calculating the average
        average=sum/observableSize;
        // setting the table properties for the table
     	setTableColourProperty();
    	
    }
	/**
	 * Adds all the Observations that can be monitored by the system in a list of checkboxes
	 */
	private void addCheckBox() {
    	checkBoxList.add(cholesterolSelection);
    	checkBoxList.add(bloodPressureSelection);
    }
	
	/**
	 * This method is used to add all patients that have high Systolic BP values 
	 * into the highSystolicPatientWrapper list that tracks patients with high BP
	 */
	public void checkHighSystolicPatients() {
		// resetting the list
		highSystolicPatientWrapper.clear();
		// going through each patient in the patientWrapperList
		for (int i=0;i<patientWrapperList.size();i++) {
			// ensuring that the Observation relating to Sytolic Blood pressure isn't empty
			if (patientWrapperList.get(i).getObservation(ObservationTypes.BLOOD_PRESSURE.toString())!=null) {
				// extracting the Observation Tuple that is linked to Blood Pressure 
				Tuple <ArrayList<Tuple<String, Observation>>, Boolean> passedTuple=patientWrapperList.get(i).getObservation(ObservationTypes.BLOOD_PRESSURE.toString());
				// extracting the arraylist that contains the Vakues related to the Blood pressure 
				ArrayList <Tuple<String, Observation>> passedList= passedTuple.first;
				for (int k =0;k< passedList.size();k++) {
					// ensure that the value is not empty
					if (!(passedList.get(k).second.isEmpty())) {
						// ensure that the Observation that we are wanting to extrcat is systlic Blood pressure
						if (passedList.get(k).first.equals(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString())){
							// if the value is greater than the value specified by the practitioner, then add the patient to the list of highBPPatients which ofcourse is highSystolicPatientWrapper
							if (Double.parseDouble(passedList.get(k).second.getValue())> Double.parseDouble(systolicValue.getText())){
								highSystolicPatientWrapper.add(patientWrapperList.get(i));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Generating the string to be displayed in the window to track patients' history of blood pressures 
	 */
	public void displaySystolicPatients() {
		// opeening html tag
		String outputString="<html>";
		systolicMonitor.clearScreen();
		// going through all the patients that have a high BP
		for (int i=0;i<highSystolicPatientWrapper.size();i++) {
			//printing out the name of each patient 
			outputString+=highSystolicPatientWrapper.get(i).getPatient().getGivenName()+" "+highSystolicPatientWrapper.get(i).getPatient().getFamilyName()+": ";
			//fetch the data of the latest 5 BP observations for that patient
			ArrayList<Tuple<ArrayList<Tuple<String, Observation>>, Boolean>> monitorPatient= highSystolicPatientWrapper.get(i).getObservations(ObservationTypes.BLOOD_PRESSURE.toString(), 5);
			//reversing the order of the patient observations from previous observations to the most recent Observation 
			Collections.reverse(monitorPatient);
			//going through each of the instances of a BP recording
			for (int j =0;j<monitorPatient.size();j++) {
				// going through each of the BPs in that 'j' instance
				for(int k=0;k< monitorPatient.get(j).first.size();k++) {
					// ensuring that the value isn't null
					if (!(monitorPatient.get(j).first.get(k).second.isEmpty())) {
						// ensuring that we only extract the value which is systolic blood pressure 
						if (monitorPatient.get(j).first.get(k).first.equals(ObservationTypes.SYSTOLIC_BLOOD_PRESSURE.toString())) {
							// adding the value and the time recorded data to the string to be diplayed on the text based monitor
							outputString+=monitorPatient.get(j).first.get(k).second.getValue() + " (" + monitorPatient.get(j).first.get(k).second.getTimeRecorded()+")";
							// append "," after every observation as long it is not the last observation value
							if (j<monitorPatient.size()-1) {
								outputString+=", ";
							}
						}
						
					}
					
				}
				
			}
			// formatting the string so that it identifies a line break
			outputString+="<br><br>";
		}
		// closing html tag
		outputString+="</html>";
		// adding patient info to the textual monitor 
		systolicMonitor.addPatientInfo(outputString,"None of the Selected Patients have a high BP");
		systolicMonitor.setVisible(true);

	}

}
