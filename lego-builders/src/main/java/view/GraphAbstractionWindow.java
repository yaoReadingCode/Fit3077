package view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Abstract class that is used to serve as the basic template for classes that 
 * might draw new chart in the future, like, for example, pie chart etc.
 * @author sriram
 *
 */

public abstract class GraphAbstractionWindow extends JFrame{
	protected JPanel graphPanel;
	

	public GraphAbstractionWindow(String windowName) {
		// TODO Auto-generated constructor stub
		// setting window name
		super(windowName);
		//setting frame size 
		this.setSize(1600, 600);
		//setting frame property in relation what it'd do upon the user closing the window
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Setting panel properties
		graphPanel = new JPanel();
		graphPanel.setBackground(Color.LIGHT_GRAY);
		graphPanel.setBorder(new TitledBorder(null, windowName, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				
	}
	
	/**
	 * Helps to render the chart using the given data set
	 * @param dataSet set of data that needs to be plotted 
	 * @param title title of the chart
	 * @param xAxis x axis of the chart
	 * @param yAxis y axis of the chart
	 */
	public abstract void renderChart(DefaultCategoryDataset dataSet, String title,String xAxis, String yAxis);

}
