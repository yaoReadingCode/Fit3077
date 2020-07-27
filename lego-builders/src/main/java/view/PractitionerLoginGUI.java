package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 * 
 * @author sriram, Zhi Tan
 * 
 * Class that builds the GUI for practitioner Login Window
 *
 */
public class PractitionerLoginGUI extends JFrame {

    private JPanel panel;
    private JLabel pracIDLabel;
    private JTextField pracID;
    private JButton submit;
    
    /**
     * 
     */

    public PractitionerLoginGUI() {
        
        // Practitioner ID label
        pracIDLabel = new JLabel();
        pracIDLabel.setText("Practitioner ID :");
        
        // Submit button
        pracID = new JTextField();
        submit = new JButton("SUBMIT");
        
        //panel layout
        panel = new JPanel(new GridLayout(2, 1));
        
        //adding components to panel
        panel.add(pracIDLabel);
        panel.add(pracID);
        panel.add(submit);
        
        //setting close settings for the Frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(panel, BorderLayout.CENTER);
        setTitle("Practitioner Login");
        setSize(300, 100);
        setVisible(true);

    }
    
    
    /**
     * Action listener for the submit button
     * @param listenerForSubmitButton ActionListener for the add button
     */
    public void addSubmitListener(ActionListener listenerForSubmitButton) {
    	submit.addActionListener(listenerForSubmitButton);
    }


    /**
     * Getter mothod for PracID
     * @return practitioner ID
     */
    public String getPracID() {
    	return pracID.getText();
    }

	

}